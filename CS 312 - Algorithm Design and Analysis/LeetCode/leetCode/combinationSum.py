import copy
from typing import List

class TreeNode:
    def __init__(self, value, parent):
        self.value = value
        self.parent = parent
        self.children = []

class Solution:
    def combinationSum(self, candidates: List[int], target: int) -> List[List[int]]:


        def createTree():
            root = TreeNode([], None)
            recursCreateNode(root)
            return root

        def recursCreateNode(node):

            if sum(node.value) == target:
                return

            for nextNum in candidates:
                if sum(node.value) + nextNum <= target:
                    newValue = copy.copy(node.value)
                    newValue.append(nextNum)
                    newNode = TreeNode(newValue, node)
                    node.children.append(newNode)
                    recursCreateNode(newNode)

        def recursCreateCombos(node):

            if sum(node.value) == target:
                combinations.add(tuple(sorted(node.value)))

            for child in node.children:
                recursCreateCombos(child)


        def getConvertedSetOfTuples():
            convertedCombos = []

            for combination in combinations:
                combinationList = []
                for number in combination:
                    combinationList.append(number)
                convertedCombos.append(combinationList)

            return convertedCombos



        combinations = set()
        root = createTree()
        recursCreateCombos(root)

        return getConvertedSetOfTuples()


sol = Solution()

candidates = [2,3,6,7]
target = 7
print(sol.combinationSum(candidates, target))