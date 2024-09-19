from typing import Optional, List


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class AugmentedTreeNode(TreeNode):
    def __init__(self, x, parent, depth):
        super().__init__(x)
        self.val = x
        self.left = None
        self.right = None
        self.depth = depth
        self.parent = parent
class Solution:
    def levelOrder(self, root: Optional[TreeNode]) -> List[List[int]]:

        def recursAddToArray(node):

            if node == None:
                return

            if (len(levelOrder) - 1) < node.depth:
                levelOrder.append([node.val])
            else:
                levelOrder[node.depth].append(node.val)

            recursAddToArray(node.left)
            recursAddToArray(node.right)


        levelOrder = []
        augRoot = self.createAugementedBinaryTree(root)
        recursAddToArray(augRoot)

        return levelOrder

    def createAugementedBinaryTree(self, root):

        augmentedTreeRoot = self.dfsAssignParentAndDepth(root, None, 0)
        return augmentedTreeRoot

    def dfsAssignParentAndDepth(self, node, parentNode, depth):
        if(node == None):
            return None
        newAugmentedNode = AugmentedTreeNode(node.val, parentNode, depth)
        newAugmentedNode.left = self.dfsAssignParentAndDepth(node.left, newAugmentedNode, depth + 1)
        newAugmentedNode.right = self.dfsAssignParentAndDepth(node.right, newAugmentedNode, depth + 1)
        return newAugmentedNode

def build_binary_tree(arr):
    if not arr:
        return None
    nodes = [TreeNode(val) if val is not None else None for val in arr]
    for i in range(len(arr)):
        if nodes[i] is not None:
            left_child_idx = 2 * i + 1
            right_child_idx = 2 * i + 2
            if left_child_idx < len(arr):
                nodes[i].left = nodes[left_child_idx]
            if right_child_idx < len(arr):
                nodes[i].right = nodes[right_child_idx]
    return nodes[0]

def find_node(root, val):
    if root is None or root.val == val:
        return root
    left_result = find_node(root.left, val)
    if left_result:
        return left_result
    return find_node(root.right, val)



sol = Solution()


array = [3,9,20,None,None,15,7]
print(sol.levelOrder(build_binary_tree(array)))