class Solution:
    def tribonacci(self, n: int) -> int:
        solutions = [0] * (n + 1)

        solutions[0] = 0
        solutions[1] = 1
        solutions[2] = 1

        for i in range(3, n + 1):
            solutions[i] = solutions[i - 3] + solutions[i - 2] + solutions[i - 1]

        return solutions[n]
