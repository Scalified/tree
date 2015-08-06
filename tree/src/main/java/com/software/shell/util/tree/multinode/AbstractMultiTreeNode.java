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
 * File created: 2015-07-18 14:09:41
 */

package com.software.shell.util.tree.multinode;

import com.software.shell.util.tree.AbstractTreeNode;
import com.software.shell.util.tree.TreeNode;
import com.software.shell.util.tree.TreeNodeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Abstract implementation of the K-ary (multiple node) tree data structure
 *
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
abstract class AbstractMultiTreeNode<T> extends AbstractTreeNode<T> implements MultiTreeNode<T> {

	/**
	 * Overrides default constructor
	 *
	 * @param data data to store in the current tree node
	 */
	protected AbstractMultiTreeNode(T data) {
		super(data);
	}

	/**
	 * Size of the tree, starting from the current tree node
	 */
	protected int size = 1;

	/**
	 * Returns the number of nodes in the entire tree, including the current tree node
	 *
	 * @return number of nodes in the entire tree, including the current tree node
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Changes the size on each tree node vertically up starting
	 * from the current tree node and moving up until the root node
	 * <p>
	 * Is called in case of adding or removing any subtree
	 * <p>
	 * Positive argument value means increasing the size; negative -
	 * decreasing
	 *
	 * @param delta delta, which size was changed at
	 */
	protected void changeSize(int delta) {
		AbstractMultiTreeNode<T> mNode = this;
		do {
			mNode.size += delta;
			mNode = (AbstractMultiTreeNode<T>) mNode.parent();
		} while (mNode != null);
	}

	/**
	 * Returns the collection of nodes, which have the same parent
	 * as the current node; {@link Collections#emptyList()} if the current
	 * tree node is root or if the current tree node has no subtrees
	 *
	 * @return collection of nodes, which have the same parent as
	 *         the current node; {@link Collections#emptyList()} if the
	 *         current tree node is root or if the current tree node has
	 *         no subtrees
	 */
	@Override
	public Collection<? extends MultiTreeNode<T>> siblings() {
		if (isRoot()) {
			String message = String.format("Unable to find the siblings. " + NODE_IS_ROOT_MESSAGE, root());
			throw new TreeNodeException(message);
		}
		Collection<? extends TreeNode<T>> mParentSubtrees = parent.subtrees();
		int mParentSubtreesSize = mParentSubtrees.size();
		if (mParentSubtreesSize == 1) {
			return Collections.<MultiTreeNode<T>> emptyList();
		}
		Collection<MultiTreeNode<T>> mSiblings = new ArrayList<>(mParentSubtreesSize - 1);
		for (TreeNode<T> mSubtree : mParentSubtrees) {
			if (!mSubtree.equals(this)) {
				mSiblings.add((MultiTreeNode<T>) mSubtree);
			}
		}
		return mSiblings;
	}

	/**
	 * Checks whether among the current tree node subtrees there are
	 * all of the subtrees from the specified collection
	 *
	 * @param subtrees collection of subtrees to be checked for containment
	 *                 within the current tree node subtrees
	 * @return {@code true} if among the current tree node subtrees
	 *         there are all of the subtrees from the specified collection;
	 *         {@code false} otherwise
	 */
	@Override
	public boolean hasSubtrees(Collection<? extends MultiTreeNode<T>> subtrees) {
		if (isLeaf()
				|| !isEligible(subtrees)) {
			return false;
		}
		for (MultiTreeNode<T> mSubtree : subtrees) {
			if (!this.hasSubtree(mSubtree)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Removes all of the collection's subtrees from the current tree node
	 * <p>
	 * Checks whether the current tree node was changed as a result of
	 * the call
	 *
	 * @param subtrees collection containing subtrees to be removed from the
	 *                 current tree node
	 * @return {@code true} if the current tree node was changed as a result
	 *         of the call; {@code false} otherwise
	 */
	@Override
	public boolean dropSubtrees(Collection<? extends MultiTreeNode<T>> subtrees) {
		if (isLeaf()
				|| !isEligible(subtrees)) {
			return false;
		}
		boolean mResult = false;
		for (MultiTreeNode<T> mSubtree : subtrees) {
			boolean mCurrentResult = dropSubtree(mSubtree);
			if (!mResult && mCurrentResult) {
				mResult = true;
			}
		}
		return mResult;
	}

}
