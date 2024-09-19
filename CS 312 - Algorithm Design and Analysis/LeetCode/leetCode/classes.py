from collections import deque
from typing import List


class Solution:
    def canFinish(self, numCourses: int, prerequisites: List[List[int]]) -> bool:

        graph = self.createGraph(numCourses, prerequisites)
        inDegreeList = self.createInDegreeList(graph, numCourses)
        q = deque()
        for index in range(len(inDegreeList)):
            if inDegreeList[index] == 0:
                q.append(index)

        visitedIndex = 0
        visited = [None for _ in range(numCourses)]

        while len(q) != 0:
            currentCourseIndex = q.pop()
            visited[visitedIndex] = currentCourseIndex
            visitedIndex += 1
            for childIndex in graph[currentCourseIndex]:
                inDegreeList[childIndex] -= 1
                if inDegreeList[childIndex] == 0:
                    q.append(childIndex)

        if visited[len(visited) - 1] == None:
            return False
        else:
            return True

    def createInDegreeList(self, graph, numCourses): # numCourses * len(preReqs)
        inDegreeList = [0 for _ in range(numCourses)]

        for index in range(numCourses):
            for preReq in graph[index]:
                inDegreeList[preReq] += 1

        return inDegreeList

    def createGraph(self, numCourses, prerequisites):
        graph = {i: [] for i in range(numCourses)}

        for preReq in prerequisites:
            graph[preReq[1]].append(preReq[0])

        return graph

sol = Solution()

classes = [[1,0]]
print(sol.canFinish(2, classes))
