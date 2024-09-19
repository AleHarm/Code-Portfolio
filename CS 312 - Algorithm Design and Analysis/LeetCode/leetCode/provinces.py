from typing import List


class Solution:
    def findCircleNum(self, matrix: List[List[int]]) -> int:
        notVisited = [i for i in range(len(matrix))]
        q = set()
        numProvinces = 0

        while len(notVisited) > 0:
            q.add(notVisited[0])

            while len(q) > 0:
                fromCity = q.pop()
                notVisited.remove(fromCity)

                for toCity in range(len(matrix)):
                    if fromCity == toCity:
                        continue

                    if toCity in notVisited and matrix[fromCity][toCity] == 1:
                        q.add(toCity)


            numProvinces += 1

        return numProvinces






sol = Solution()


# matrix = [[1,1,0],[1,1,0],[0,0,1]]
# print(sol.findCircleNum(matrix))
matrix = [[1,1,1],[1,1,1],[1,1,1]]
print(sol.findCircleNum(matrix))