from builtins import int
from typing import List


class Edge:
    def __init__(self, distance, point1, point2):
        self.distance = distance
        self.point1 = point1
        self.point2 = point2

class Solution:

    def minCostConnectPoints(self, points: List[List[int]]) -> int:
        edges, connectedPoints = self.createEdgesAndPointsInLists(points)
        totalCost = 0

        while len(connectedPoints) > 1:
            edge = edges.pop(0)
            edgesConnected, point1Index, point2Index = self.arePointsConnected(edge, connectedPoints)
            if not edgesConnected:
                connectedPoints[point1Index].extend(connectedPoints[point2Index])
                connectedPoints.pop(point2Index)
                totalCost += edge.distance
        return totalCost


    def arePointsConnected(self, edge, connectedPoints):
        point1 = edge.point1
        point2 = edge.point2
        point1Index = None
        point2Index = None
        
        for index in range(len(connectedPoints)):
            pointGroup = connectedPoints[index]
            if point1 in pointGroup:
                point1Index = index
                if point2Index != None:
                    break
            if point2 in pointGroup:
                point2Index = index
                if point1Index != None:
                    break

        return point1Index == point2Index, point1Index, point2Index


    def createEdgesAndPointsInLists(self, points: List[List[int]]):
        edges = set()
        pointsInLists = []
        pointsSize = len(points)
        for i in range(pointsSize):
            pointsInLists.append([points[i]])
            for j in range(i + 1, pointsSize):
                edges.add(Edge(self.getManhattanDistance(points[i], points[j]), points[i], points[j]))

        return sorted(edges, key=lambda x: x.distance), pointsInLists

    def getManhattanDistance(self, fromPoint, toPoint):

        return abs((fromPoint[0] - toPoint[0])) + abs((fromPoint[1] - toPoint[1]))

sol = Solution()



points = [[2,-3],[-17,-8],[13,8],[-17,-15]]
print(sol.minCostConnectPoints(points))
points = [[0,0],[2,2],[3,10],[5,2],[7,0]]
print(sol.minCostConnectPoints(points))
points = [[0,0]]
print(sol.minCostConnectPoints(points))
points = [[-14,-14],[-18,5],[18,-10],[18,18],[10,-2]]
print(sol.minCostConnectPoints(points))