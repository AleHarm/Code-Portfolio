import math
from typing import List

class Solution:
    def minimumTotal(self, triangle: List[List[int]]) -> int:
        paths = self.makePathsTable(triangle)
        self.dpItUp(paths, triangle)
        return self.findLowestCost(paths)

    def makePathsTable(self, triangle: List[List[int]]):
        maxSize = len(triangle)
        paths = [[math.inf for _ in range(maxSize)] for _ in range(maxSize)]

        paths[0][0] = triangle[0][0]

        for index in range(1, maxSize):
            # Always left
            paths[index][0] = triangle[index][0] + paths[index - 1][0]
            #Always right
            paths[0][index] = triangle[index][len(triangle[index]) - 1] + paths[0][index - 1]

        return paths

    def dpItUp(self, paths, triangle):

        maxSize = len(triangle)

        for i in range(1, maxSize - 1):
            for j in range (1, maxSize - i):
                if paths[i - 1][j] < paths[i][j - 1]:
                    previousPath = paths[i - 1][j]
                else:
                    previousPath = paths[i][j - 1]
                currentPositionInTriangleCost = triangle[i + j][j]
                paths[i][j] = previousPath + currentPositionInTriangleCost

    def findLowestCost(self, paths):
        lowestCost = math.inf
        i = len(paths) - 1
        j = 0

        while j < len(paths):
            currentCost = paths[i][j]
            if currentCost < lowestCost:
                lowestCost = currentCost
            j += 1
            i -= 1

        return lowestCost



sol = Solution()

triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
print(sol.minimumTotal(triangle))