#!/usr/bin/python3
import copy
import math

import TSPClasses
from which_pyqt import PYQT_VER

if PYQT_VER == 'PYQT5':
	from PyQt5.QtCore import QLineF, QPointF
elif PYQT_VER == 'PYQT4':
	from PyQt4.QtCore import QLineF, QPointF
elif PYQT_VER == 'PYQT6':
	from PyQt6.QtCore import QLineF, QPointF
else:
	raise Exception('Unsupported Version of PyQt: {}'.format(PYQT_VER))

import time
import numpy as np
from TSPClasses import *
import heapq
import itertools


class TSPSolver:
	def __init__(self, gui_view):
		self._scenario = None

	def setupWithScenario(self, scenario):
		self._scenario = scenario

	''' <summary>
		This is the entry point for the default solver
		which just finds a valid random tour.  Note this could be used to find your
		initial BSSF.
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of solution,
		time spent to find solution, number of permutations tried during search, the
		solution found, and three null values for fields not used for this
		algorithm</returns>
	'''

	def defaultRandomTour(self, time_allowance=60.0):
		results = {}
		cities = self._scenario.getCities()
		ncities = len(cities)
		foundTour = False
		count = 0
		bssf = None
		start_time = time.time()
		while not foundTour and time.time() - start_time < time_allowance:
			# create a random permutation
			perm = np.random.permutation(ncities)
			route = []
			# Now build the route using the random permutation
			for i in range(ncities):
				route.append(cities[perm[i]])
			bssf = TSPSolution(route)
			count += 1
			if bssf.cost < np.inf:
				# Found a valid route
				foundTour = True
		end_time = time.time()
		results['cost'] = bssf.cost if foundTour else math.inf
		results['time'] = end_time - start_time
		results['count'] = count
		results['soln'] = bssf
		results['max'] = None
		results['total'] = None
		results['pruned'] = None
		return results

	''' <summary>
		This is the entry point for the greedy solver, which you must implement for
		the group project (but it is probably a good idea to just do it for the branch-and
		bound project as a way to get your feet wet).  Note this could be used to find your
		initial BSSF.
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of best solution,
		time spent to find best solution, total number of solutions found, the best
		solution found, and three null values for fields not used for this
		algorithm</returns>
	'''

	def greedy(self, time_allowance=60.0):
		results = {}
		cities = copy.deepcopy(self._scenario.getCities())
		ncities = len(cities)
		foundTour = False
		count = 1
		bssf = None
		startCityIndex = random.randint(0, ncities)
		currentCity = cities.pop(startCityIndex)
		route = []
		route.append(currentCity)
		start_time = time.time()
		while not foundTour and time.time() - start_time < time_allowance:
			shortestPathIndex = None
			shortestPathLength = math.inf
			for index in range(ncities - len(route)):
				currentCost = currentCity.costTo(cities[index])
				if currentCost < shortestPathLength:
					shortestPathIndex = index
					shortestPathLength = currentCost

			if shortestPathIndex is not None:
				route.append(cities[shortestPathIndex])
				currentCity = cities.pop(shortestPathIndex)
			else:
				break

			bssf = TSPSolution(route)
			if len(route) == ncities and bssf.cost < math.inf:
				foundTour = True

		end_time = time.time()
		results['cost'] = bssf.cost if foundTour else math.inf
		results['time'] = end_time - start_time
		results['count'] = count
		results['soln'] = bssf
		results['max'] = None
		results['total'] = None
		results['pruned'] = None
		return results

	''' <summary>
		This is the entry point for the branch-and-bound algorithm that you will implement
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of best solution,
		time spent to find best solution, total number solutions found during search (does
		not include the initial BSSF), the best solution found, and three more ints:
		max queue size, total number of states created, and number of pruned states.</returns>
	'''

	def branchAndBound(self, time_allowance=60.0):
		numStatesCreated = 0
		numPruned = 0
		numSolutionsTested = 0
		maxPQSize = 0
		cities = self._scenario.getCities()
		ncities = len(cities)
		foundTour = False
		try:
			bssf = self.greedy()['soln']
		except:
			bssf = self.defaultRandomTour()['soln']
		initialRCMatrix = self.createAndPopulateRCMatrix(cities)
		initialState = State(0, initialRCMatrix, self.reduceMatrix(initialRCMatrix, 0), 0, [cities[0]])
		pq = PQ()
		pq.push(initialState)
		start_time = time.time()

		while not pq.is_empty() and time.time() - start_time < time_allowance:
			parentState = pq.pop()
			fromIndex = parentState.getFromIndex()
			parentRCMatrix = parentState.getRCMatrix()
			parentDepth = parentState.getDepth()
			parentLB = parentState.getLB()
			parentRoute = parentState.getRoute()

			#  Expand
			for toIndex in range(len(parentRCMatrix)):
				if toIndex == fromIndex or self.isCityInRoute(parentRoute, toIndex):
					continue
				newState = State(toIndex, self.mediumDepthCopy(parentRCMatrix), parentLB, parentDepth + 1, parentRoute.copy())
				numStatesCreated += 1
				newState.setLB(self.setCrosshairsToInfinity(fromIndex, toIndex, newState.getRCMatrix(), newState.getLB()))
				newState.setLB(self.reduceMatrix(newState.getRCMatrix(), newState.getLB()))
				newState.appendToRoute(cities[toIndex])

				if newState.getLB() < bssf.cost:
					if newState.getRouteLength() == ncities:
						bssf = TSPSolution(newState.getRoute())
						numSolutionsTested += 1
						foundTour = True
					else:
						pq.push(newState)
				else:
					numPruned += 1

			if pq.get_size() > maxPQSize:
				maxPQSize = pq.get_size()

		end_time = time.time()
		results = {'cost': bssf.cost if foundTour else math.inf, 'time': end_time - start_time,
				   'count': numSolutionsTested, 'soln': bssf, 'max': maxPQSize, 'total': numStatesCreated, 'pruned': numPruned + pq.get_size()}
		return results

	def createAndPopulateRCMatrix(self, cities):

		numberOfCities = len(cities)
		rcMatrix = [[math.inf for _ in range(numberOfCities)] for _ in range(numberOfCities)]

		for fromIndex in range(numberOfCities):
			fromCity = cities[fromIndex]
			for toIndex in range(numberOfCities):
				toCity = cities[toIndex]
				rcMatrix[fromIndex][toIndex] = fromCity.costTo(toCity)

		return rcMatrix

	def reduceMatrix(self, matrix, LB):
		matrixSize = len(matrix)
		changed = True

		while changed:
			changed = False
			LB, changed = self.reduceMatrixRows(matrix, matrixSize, LB, changed)
			LB, changed = self.reduceMatrixCols(matrix, matrixSize, LB, changed)
		return LB

	def reduceMatrixRows(self, matrix, matrixSize, LB, changed):
		for row in range(matrixSize):
			lowestVal = math.inf
			for col in range(matrixSize):
				currentVal = matrix[row][col]
				if currentVal <= lowestVal and currentVal != -1:
					lowestVal = currentVal

			if lowestVal > 0 and lowestVal != math.inf:
				LB += lowestVal
				changed = True
				# Reduce all values in that row
				for col in range(matrixSize):
					if matrix[row][col] != -1:
						matrix[row][col] = matrix[row][col] - lowestVal
		return LB, changed

	def reduceMatrixCols(self, matrix, matrixSize, LB, changed):

		for col in range(matrixSize):
			lowestVal = math.inf
			for row in range(matrixSize):
				currentVal = matrix[row][col]
				if currentVal <= lowestVal and currentVal != -1:
					lowestVal = currentVal

			if lowestVal > 0 and lowestVal != math.inf:
				LB += lowestVal
				changed = True
				# Reduce all values in that row
				for row in range(matrixSize):
					if matrix[row][col] != -1:
						matrix[row][col] = matrix[row][col] - lowestVal
		return LB, changed

	def setCrosshairsToInfinity(self, fromIndex, toIndex, rcMatrix, LB):
		LB += rcMatrix[fromIndex][toIndex]

		#  Set row and column in crosshair pattern to visited
		for i in range(len(rcMatrix)):
			rcMatrix[fromIndex][i] = math.inf
			rcMatrix[i][toIndex] = math.inf

		return LB

	def isCityInRoute(self, route, index):
		for city in route:
			if city.getIndex() == index:
				return True

		return False

	#  copy.deepcopy() is super slow, and was making my time complexity a lot worse,
	#  so this function copies matrices without taking five-ever
	def mediumDepthCopy(self, matrix):
		copiedMatrix = []
		for row in range(len(matrix)):
			copiedMatrix.append(matrix[row].copy())

		return copiedMatrix

	''' <summary>
		This is the entry point for the algorithm you'll write for your group project.
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of best solution,
		time spent to find best solution, total number of solutions found during search, the
		best solution found.  You may use the other three field however you like.
		algorithm</returns>
	'''

	def fancy(self, time_allowance=60.0):
		pass
