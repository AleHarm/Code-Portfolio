from typing import List


class Garden:
    def __init__(self):
        self.neighbors = []
        self.flower = -1


class Solution:
    def gardenNoAdj(self, n: int, paths: List[List[int]]) -> List[int]:

        graph = self.makeGraph(paths, n)


        for garden in graph:
            flowers = [f for f in range(4)]
            for neighbor in garden.neighbors:
                neighborFlower = graph[neighbor].flower
                if neighborFlower != -1 and neighborFlower in flowers:
                    flowers.remove(graph[neighbor].flower)

            garden.flower = flowers[0]

        return [(graph[i].flower + 1) for i in range(n)]

    def makeGraph(self, paths, n):

        graph = [Garden() for _ in range(n)]
        for edge in paths:
            g1 = edge[0] - 1
            g2 = edge[1] - 1
            graph[g1].neighbors.append(g2)
            graph[g2].neighbors.append(g1)

        return graph



sol = Solution()

paths = [[1,2],[2,3],[3,1]]
print(sol.gardenNoAdj(3, paths))
paths = [[4,1],[4,2],[4,3],[2,5],[1,2],[1,5]]
print(sol.gardenNoAdj(6, paths))

