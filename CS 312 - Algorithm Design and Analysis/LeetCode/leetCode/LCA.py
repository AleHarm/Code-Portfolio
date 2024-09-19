# Definition for a binary tree node.
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class AugmentedTreeNode(TreeNode):
    def __init__(self, x, parent, depth):
        super().__init__(x)
        self.val = x
        self.left = None
        self.right = None
        self.depth = depth
        self.parent = parent

class Solution:
    def lowestCommonAncestor(self, root: 'TreeNode', p: 'TreeNode', q: 'TreeNode') -> 'TreeNode':

        augmentedTreeRoot = self.createAugementedBinaryTree(root)

        LCAAugmentedNode = self.findLCA(augmentedTreeRoot, find_node(augmentedTreeRoot, p.val), find_node(augmentedTreeRoot, q.val))

        return self.deAugmentNode(LCAAugmentedNode)

    def findLCA(self, root: AugmentedTreeNode, pNode: AugmentedTreeNode, qNode: AugmentedTreeNode):

        if pNode.depth < qNode.depth:
            higherNode = pNode
            lowerNode = qNode
        else:
            higherNode = qNode
            lowerNode = pNode


        while lowerNode.depth != higherNode.depth:
            lowerNode = lowerNode.parent

        if lowerNode == higherNode:
            return higherNode

        while lowerNode != higherNode:
            lowerNode = lowerNode.parent
            higherNode = higherNode.parent

        return higherNode # Doesn't matter which is returned, lower or higher are same here

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

    def deAugmentNode(self, node):

        if node == None:
            return None

        normalNode = TreeNode(node.val)
        normalNode.right = self.deAugmentNode(node.right)
        normalNode.left = self.deAugmentNode(node.left)
        return normalNode

#ChatGPT generated binary tree builder
def build_binary_tree_with_nodes(arr, p, q):


    root = build_binary_tree(arr)
    node_p = find_node(root, p)
    node_q = find_node(root, q)
    return root, node_p, node_q

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

arr = [3, 5, 1, 6, 2, 0, 8, None, None, 7, 4]
p = 5
q = 4
root, node_p, node_q = build_binary_tree_with_nodes(arr, p, q)
print(root)
sol.lowestCommonAncestor(root, node_p, node_q)