#!/usr/bin/python3
import math
import random

from CS312Graph import *
import time


class NetworkRoutingSolver:

    shortestPath = [[]]

    def __init__( self):
        pass

    def initializeNetwork( self, network ):
        assert( type(network) == CS312Graph )
        self.network = network

    def getShortestPath( self, destIndex ):
        self.dest = destIndex

        def findDestinationNodeIndex():
            for index in range(len(self.shortestPath)):
                if self.shortestPath[index][0] == destIndex:
                    return index

        path_edges = []
        total_cost = 0
        currentIndex = findDestinationNodeIndex()
        if currentIndex == None:
            return {'cost': math.inf, 'path': []}
        backPointer = self.shortestPath[currentIndex][1]

        while True:
            if backPointer == None:
                break

            while self.shortestPath[currentIndex][1] != backPointer:
                currentIndex -= 1

            nodesIndex = self.shortestPath[currentIndex][0]

            if self.network.nodes[backPointer].neighbors[0].dest.node_id == nodesIndex:
                edge = self.network.nodes[backPointer].neighbors[0]

            elif self.network.nodes[backPointer].neighbors[1].dest.node_id == nodesIndex:
                edge = self.network.nodes[backPointer].neighbors[1]
            else:
                edge = self.network.nodes[backPointer].neighbors[2]

            path_edges.append((edge.src.loc, edge.dest.loc, '{:.0f}'.format(edge.length)))
            total_cost += edge.length

            while self.shortestPath[currentIndex][0] != backPointer:
                currentIndex -= 1
            backPointer = self.shortestPath[currentIndex][1]

        return {'cost': total_cost, 'path': path_edges}


    def computeShortestPaths( self, srcIndex, use_heap ):
        self.source = srcIndex
        t1 = time.time()

        if not use_heap:
            self.shortestPath = self.dijkstrasArray(srcIndex)
        else:
            self.shortestPath = self.dijkstrasHeap(srcIndex)

        t2 = time.time()
        return (t2-t1)


    def dijkstrasArray(self, srcIndex):

        def makeQueue(srcIndex):
            pQueue = [[UNREACHABLE, None] for _ in range(len(self.network.nodes))]
            pQueue[srcIndex][0] = 0
            return pQueue

        def decreaseKey(pQueue, index, distance, lastNode):
            pQueue[index] = [distance, lastNode]

        def deleteMin(pQueue):

            minValue = UNREACHABLE
            minIndex = 0
            for index in range(len(pQueue)):

                if ((minValue == UNREACHABLE or pQueue[index][0] < minValue)
                        and pQueue[index][0] != UNREACHABLE
                        and pQueue[index][0] != VISITED):
                    minIndex = index
                    minValue = pQueue[index][0]

            pQueue[minIndex][0] = VISITED
            return minIndex, minValue

        VISITED = -2
        UNREACHABLE = -1
        shortestPath = []
        pQueue = makeQueue(srcIndex)

        while True:
            minIndex, minValue = deleteMin(pQueue)
            if minValue == UNREACHABLE:
                break

            shortestPath.append([minIndex, pQueue[minIndex][1]])
            if len(shortestPath) == len(self.network.nodes):
                break

            neighbors = self.network.nodes[minIndex].neighbors
            for index in range(3):

                neighborID = neighbors[index].dest.node_id
                neighborDistance = neighbors[index].length
                if (pQueue[neighborID][0] != VISITED
                        and ((pQueue[neighborID][0] == UNREACHABLE) or (neighborDistance + minValue) < pQueue[neighborID][0])): # Replaced index with neighborID in (neighborDistance + minValue) < pQueue[neighborID][0]))
                    decreaseKey(pQueue, neighborID, neighborDistance + minValue, minIndex)

        return shortestPath


    def dijkstrasHeap(self, srcIndex):

        def makeQueueAndPointerArray(srcIndex):
            pq = [[None, UNREACHABLE, None] for _ in range(len(self.network.nodes))]  #NodeNumber, currentCost, backpointer
            pointerArray = [UNREACHABLE for _ in range(len(self.network.nodes) + 1)]
            pq[0] = [srcIndex, 0, None]
            pointerArray[srcIndex] = 0
            pointerArray[len(pointerArray) - 1] = 1  # Last index keeps track of where the next Null spot is

            return pq, pointerArray

        def insert(value: [int, int, int | None]):

            nextNullSpot = pointerArray[len(pointerArray) - 1]

            pq[nextNullSpot] = value
            pointerArray[value[0]] = nextNullSpot
            pointerArray[len(pointerArray) - 1] += 1
            bubbleUp(nextNullSpot)

        def deleteMin():
            lowestItemIndex = pointerArray[len(pointerArray) - 1] - 1
            smallestNode = pq[0]
            pointerArray[smallestNode[0]] = VISITED
            pq[0] = pq[lowestItemIndex]
            if lowestItemIndex != 0:
                pointerArray[pq[0][0]] = 0
            pq[lowestItemIndex] = [None, -1, None]
            pointerArray[len(pointerArray) - 1] = lowestItemIndex

            siftDown()

            return smallestNode

        def decreaseKey(newNodeValue):
            pqIndex = pointerArray[newNodeValue[0]]
            pq[pqIndex] = newNodeValue
            bubbleUp(pqIndex)

        def siftDown():

            parentIndex = 0

            while True:
                child1Index = (2 * parentIndex) + 1
                child2Index = (2 * parentIndex) + 2
                outOfBoundsIndex = pointerArray[len(pointerArray) - 1]

                # Both children out of bounds
                if child1Index >= outOfBoundsIndex and child2Index >= outOfBoundsIndex:
                    break

                # child2 out of bounds
                elif child2Index >= outOfBoundsIndex:
                    if pq[parentIndex][1] < pq[child1Index][1]:
                        break
                    else:
                        swapIndex = child1Index

                # Neither child out of bounds
                else:
                    if pq[parentIndex][1] < pq[child1Index][1] and pq[parentIndex][1] < pq[child2Index][1]:
                        break
                    elif pq[child1Index][1] < pq[child2Index][1]:
                        swapIndex = child1Index
                    else:
                        swapIndex = child2Index

                parent = pq[parentIndex]
                child = pq[swapIndex]
                pq[parentIndex] = child
                pq[swapIndex] = parent
                pointerArray[pq[parentIndex][0]] = parentIndex
                pointerArray[pq[swapIndex][0]] = swapIndex
                parentIndex = swapIndex


        def bubbleUp(newNodeLocation):

            currentParentIndex = math.floor((newNodeLocation - 1) / 2)

            while pq[newNodeLocation][1] < pq[currentParentIndex][1]:
                childValue = pq[newNodeLocation]
                parentValue = pq[currentParentIndex]
                pq[newNodeLocation] = parentValue
                pq[currentParentIndex] = childValue

                pointerArray[parentValue[0]] = newNodeLocation
                pointerArray[childValue[0]] = currentParentIndex

                newNodeLocation = currentParentIndex
                currentParentIndex = math.floor(newNodeLocation / 2)


        UNREACHABLE = -1
        VISITED = -2
        pq, pointerArray = makeQueueAndPointerArray(srcIndex)
        shortestPath = []

        while pointerArray[len(pointerArray) - 1] != 0:
            nextNode = deleteMin()
            shortestPath.append([nextNode[0], nextNode[2]])
            neighbors = self.network.nodes[nextNode[0]].neighbors

            for index in range(3):

                neighborID = neighbors[index].dest.node_id
                neighborDistance = neighbors[index].length
                if pointerArray[neighborID] == VISITED:
                    continue
                elif pointerArray[neighborID] == UNREACHABLE:
                    insert([neighborID, neighborDistance + nextNode[1], nextNode[0]])

                elif (neighborDistance + nextNode[1]) < pq[pointerArray[neighborID]][1]:
                        decreaseKey([neighborID, neighborDistance + nextNode[1], nextNode[0]])

        return shortestPath

