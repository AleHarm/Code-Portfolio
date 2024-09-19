from typing import List


class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        listSize = len(nums)

        for i in range(listSize):
            for j in range(i + 1, listSize):
                currentNum = nums[i] + nums[j]
                if currentNum == target:
                    return [i, j]