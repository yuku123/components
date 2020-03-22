package com.zifang.util.zex.structure.tree;

public class BinaryTreeDemo {

	private TreeNode root = null;

	public BinaryTreeDemo() {
		root = new TreeNode(null, 5);
	}

	/**
	 * 添加二叉树
	 * 
	 * @param treeNode
	 */
	public void addBinaryTree(TreeNode treeNode, int key) {
		if (treeNode == null) {
			treeNode = new TreeNode(null, key);
		} else {
			int data = treeNode.key;
			// 插入左侧
			if (data > key) {
				if (treeNode.lchild == null) {
					treeNode.lchild = new TreeNode(treeNode, key);
				} else {
					addBinaryTree(treeNode.lchild, key);
				}
			}
			// 插入右侧
			else if (data < key) {
				if (treeNode.rchild == null) {
					treeNode.rchild = new TreeNode(treeNode, key);
				} else {
					addBinaryTree(treeNode.rchild, key);
				}
			} else {
				System.out.println("[" + key + "]此数字已经存在，插入失败！");
			}
		}
	}

	/**
	 * 查询元素
	 * 
	 * @return
	 */
	public TreeNode search(int key) {
		TreeNode treeNode = root;
		while (treeNode != null) {
			if (treeNode.key > key) {
				treeNode = treeNode.lchild;
			} else if (treeNode.key < key) {
				treeNode = treeNode.rchild;
			} else {
				return treeNode;
			}
		}
		return null;
	}

	/**
	 * 中序
	 * 
	 * @param treeNode
	 */
	public void inOrder(TreeNode treeNode) {
		if (treeNode != null) {
			visited(treeNode);
			inOrder(treeNode.lchild);
			inOrder(treeNode.rchild);
		}
	}

	/**
	 * 前序
	 * 
	 * @param treeNode
	 */
	public void preorder(TreeNode treeNode) {
		if (treeNode != null) {
			preorder(treeNode.lchild);
			visited(treeNode);
			preorder(treeNode.rchild);
		}
	}

	/**
	 * 后序
	 * 
	 * @param treeNode
	 */
	public void postorder(TreeNode treeNode) {
		if (treeNode != null) {
			postorder(treeNode.lchild);
			postorder(treeNode.rchild);
			visited(treeNode);
		}
	}

	public void visited(TreeNode treeNode) {
		System.out.println(treeNode.key);
	}

	public void delTreeNode(int key) throws Exception {
		TreeNode treeNode = search(key);
		if (treeNode == null) {
			throw new Exception("此树中不存在要删除的这个节点！");
		}
		delTreeNode(treeNode);
	}

	public void delTreeNode(TreeNode treeNode) throws Exception {
		/**
		 * 首先看第一种情况：（删除没有子节点的节点） 删除没有子节点的节点只需要将被删除节点的父节点指向空即可
		 */
		// 第一种情况： 删除没有子节点的节点
		if (treeNode.lchild == null && treeNode.rchild == null) {
			if (treeNode == root) {
				// 判断是否为根节点,如果是根节点就删除整个树
				root = null;
			} else if (treeNode.parent.lchild == treeNode) {
				// 如果这个节点是父节点的左节点，则将父节点的左节点设为空
				treeNode.parent.lchild = null;
			} else if (treeNode.parent.rchild == treeNode) {
				// 如果这个节点是父节点的右节点，则将父节点的右节点设为空
				treeNode.parent.rchild = null;
			}
		}

		/**
		 * 第二种情况：（删除只有一个子节点的节点） 删除有一个子节点的节点，只需要将被删除节点的父节点指向删除节点的子节点即可
		 */
		// 第二种情况：删除只有一个子节点的节点

		// 如果要删除的节点只有左节点
		if (treeNode.lchild != null && treeNode.rchild == null) {
			if (treeNode == root) {
				root = treeNode.lchild;
			} else if (treeNode.parent.lchild == treeNode) {
				treeNode.parent.lchild = treeNode.lchild;
				treeNode.lchild.parent = treeNode.parent;
			} else if (treeNode.parent.rchild == treeNode) {
				treeNode.parent.rchild = treeNode.lchild;
				treeNode.lchild.parent = treeNode.parent;
			}
		}

		// 如果要删除的节点只有右节点
		if (treeNode.lchild == null && treeNode.rchild != null) {
			if (treeNode == root) {
				// 判断是否为根节点
				root = treeNode.rchild;
			} else if (treeNode.parent.lchild == treeNode) {
				// 父节点的左子节点设置成treeNode的右子节点
				treeNode.parent.lchild = treeNode.rchild;
				// treeNode的右子节点的父节点设置成treeNode的父节点
				treeNode.rchild.parent = treeNode.parent;
			} else if (treeNode.parent.rchild == treeNode) {
				treeNode.parent.rchild = treeNode.rchild;
				treeNode.rchild.parent = treeNode.parent;
			}
		}

		// 第三种情况：如果要删除的节点有两个子节点，即左右子节点都非空
		/**
		 * 第三种情况：（删除有两个子节点的节点，即左右子树都非空） 删除有两个子节点的节点，到底谁来替代被删除的节点的位置呢？是左节点，还是右节点，
		 * 代替以后这个子节点的子节点应该怎么安排？一系列的问题都出来了
		 * 。。。简便的方法就是要找一个节点代替这个被删除的节点，这就要从二叉搜索树的定义来看
		 * 。因为二叉搜索树是有序的，我们要找的节点在这棵树上，而且这个节点要比被删除的左节点大
		 * ，比右节点小。先看看这个已被删除节点的右节点为根的子树的所有节点的值都要比被删除节点大
		 * ，这是二叉搜索树定义的，但是要在这个集合中找到最小的一个
		 * ，来代替被删除的节点，那就要在这棵子树上一直往左找。这个节点比被删除的节点的右节点小
		 * ，且比左节点大，那这个节点就叫做被删除节点的后继节点，用这个节点来代替被删除节点。
		 */

		// 方法是用要删除的节点的后续节点代替要删除的节点，并且删除后续节点（删除后续节点的时候同样的递归操作）
		// 其实，这里要用到的最多也就会发生两次，即后续节点不会再继续递归的删除下一个后续节点了
		// 因为，要删除的节点的后续节点肯定是 要删除的那个节点的右子树的最小关键字，而这个最小关键字肯定不会有左节点
		// 所以，在删除后续节点的时候肯定不会用到（ 两个节点都非空的判断 ），如有有子节点，肯定就是有一个右节点。
		if (treeNode.lchild != null && treeNode.rchild != null) {
			// 先找出后续节点
			TreeNode successorNode = successor(treeNode);
			if (treeNode == root) {
				root.key = successorNode.key;
			} else {
				treeNode.key = successorNode.key;// 赋值，将后续节点的值赋给要删除的那个节点
			}
			delTreeNode(successorNode); // 递归的删除后续节点
		}
	}

	// 获取给定节点在中序遍历下的后续节点
	public TreeNode successor(TreeNode node) throws Exception {
		if (node == null) {
			throw new Exception("此树为空树！");
		}
		// 分两种情况考虑，此节点是否有右子树
		// 当这个节点有右子树的情况下，那么右子树的最小关键字节点就是这个节点的后续节点
		if (node.rchild != null) {
			return minElemNode(node.rchild);
		}

		// 当这个节点没有右子树的情况下,即 node.rchild == null
		// 如果这个节点的父节点的左子树 与 这个节点相同的话，那么就说明这个父节点就是后续节点了
		// 难道这里还需要进行两次if语句吗？不需要了，这里用一个while循环就可以了
		TreeNode parentNode = node.parent;
		while (parentNode != null && parentNode.rchild == node) {
			node = parentNode;
			parentNode = parentNode.parent;
		}
		return parentNode;
	}

	// 获取二叉树的最小关键字节点
	public TreeNode minElemNode(TreeNode node) throws Exception {
		if (node == null) {
			throw new Exception("此树为空树！");
		}
		TreeNode pNode = node;
		while (pNode.lchild != null) {
			pNode = pNode.lchild;
		}
		return pNode;
	}

	public static void main(String[] args) throws Exception {
		BinaryTreeDemo demo = new BinaryTreeDemo();
		demo.addBinaryTree(demo.root, 2);
		demo.addBinaryTree(demo.root, 6);
		demo.addBinaryTree(demo.root, 5);
		demo.addBinaryTree(demo.root, 15);
		demo.addBinaryTree(demo.root, 20);
		demo.addBinaryTree(demo.root, 3);
		demo.addBinaryTree(demo.root, 1);
		demo.addBinaryTree(demo.root, 13);
		// 中序
		System.out.println("------中序------");
		demo.inOrder(demo.root);

		// 前序
		System.out.println("------前序------");
		demo.preorder(demo.root);

		// 后序
		System.out.println("------后序------");
		demo.postorder(demo.root);

		int key = 15;
		TreeNode node = demo.search(key);
		if (node != null) {
			System.out.println("是否存在数字【" + key + "】存在");
		} else {
			System.out.println("是否存在数字【" + key + "】不存在");
		}

		demo.delTreeNode(key);
		// 中序
		System.out.println("------中序------");
		demo.inOrder(demo.root);

		// 前序
		System.out.println("------前序------");
		demo.preorder(demo.root);

		// 后序
		System.out.println("------后序------");
		demo.postorder(demo.root);
	}

	static class TreeNode {
		private int key;
		private TreeNode lchild;
		private TreeNode rchild;
		private TreeNode parent;

		public TreeNode(TreeNode parent, int key) {
			this.parent = parent;
			this.key = key;
		}

	}
}
