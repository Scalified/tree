/*
 * Copyright 2015 Shell Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * File created: 2015-10-29 21:49:25
 */

package com.software.shell.tree.multinode;

import com.software.shell.tree.TreeNode;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Implementation of the K-ary (multi node) tree data structure,
 * based on the leftmost-child-right-sibling representation
 *
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
public class LinkedTreeNode<T> extends MultiTreeNode<T> {

	/**
	 * Current UID of this object used for serialization
	 */
	private static final long serialVersionUID = 1L;

	private LinkedTreeNode<T> leftMostNode;

	private LinkedTreeNode<T> rightSiblingNode;

	/**
	 * Creates an instance of this class
	 *
	 * @param data data to store in the current tree node
	 */
	public LinkedTreeNode(T data) {
		super(data);
	}

	@Override
	public Collection<? extends TreeNode<T>> subtrees() {
		if (isLeaf()) {
			return Collections.emptyList();
		}
		Collection<TreeNode<T>> subtrees = new LinkedList<>();
		subtrees.add(leftMostNode);
		LinkedTreeNode<T> nextSubtree = leftMostNode.rightSiblingNode;
		while (nextSubtree != null) {
			subtrees.add(nextSubtree);
			nextSubtree = nextSubtree.rightSiblingNode;
		}
		return subtrees;
	}

	@Override
	public boolean add(TreeNode<T> subtree) {
		if (subtree == null) {
			return false;
		}
		assignParent(subtree, this);
		if (isLeaf()) {
			leftMostNode = (LinkedTreeNode<T>) subtree;
		} else {
			LinkedTreeNode<T> nextSubtree = leftMostNode;
			while (nextSubtree.rightSiblingNode != null) {
				nextSubtree = nextSubtree.rightSiblingNode;
			}
			nextSubtree.rightSiblingNode = (LinkedTreeNode<T>) subtree;
		}
		return true;
	}

	@Override
	public boolean dropSubtree(TreeNode<T> subtree) {
		if (subtree == null
				|| isLeaf()
				|| subtree.isRoot()) {
			return false;
		}
		if (leftMostNode.equals(subtree)) {
			removeParentAssignment(subtree);
			leftMostNode = leftMostNode.rightSiblingNode;
			return true;
		} else {
			LinkedTreeNode<T> nextSubtree = leftMostNode;
			while (nextSubtree.rightSiblingNode != null) {
				if (nextSubtree.rightSiblingNode.equals(subtree)) {
					removeParentAssignment(subtree);
					nextSubtree.rightSiblingNode = nextSubtree.rightSiblingNode.rightSiblingNode;
					return true;
				} else {
					nextSubtree = nextSubtree.rightSiblingNode;
				}
			}
		}
		return false;
	}

	@Override
	public void clear() {
		if (!isLeaf()) {
			removeParentAssignment(leftMostNode);
			leftMostNode = null;
		}
	}

	@Override
	public TreeNodeIterator iterator() {
		return new TreeNodeIterator() {

			@Override
			protected TreeNode<T> leftMostNode() {
				return leftMostNode;
			}

			@Override
			protected TreeNode<T> rightSiblingNode() {
				return rightSiblingNode;
			}

		};
	}

	@Override
	public boolean isLeaf() {
		return leftMostNode == null;
	}

}
