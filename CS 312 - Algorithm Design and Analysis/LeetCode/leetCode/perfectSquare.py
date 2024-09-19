import math
import sys
from math import sqrt


class Solution:
    def numSquares(self, n: int) -> int:
        MAX_INT = sys.maxsize
        squaredNumbers = [i*i for i in range(math.ceil(sqrt(n)) + 1)]
        optimized = [MAX_INT for _ in range(n + 1)]


        for i in range(n + 1):
            for number in squaredNumbers:
                if number == i:
                    optimized[i] = 1
                elif number < i:
                    optimizedPastNumber = optimized[i - number] + optimized[number]
                    if optimizedPastNumber < optimized[i]:
                        optimized[i] = optimizedPastNumber

        return optimized[n]

sol = Solution()

print(sol.numSquares(12))
print(sol.numSquares(13))