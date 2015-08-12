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

import java.util.*;

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

	/**
	 * Returns the next right sibling of the current tree node if the current
	 * tree node is not root
	 *
	 * @return next right sibling of the current tree node if the current tree
	 *         node is not root
	 * @throws TreeNodeException an exception that is thrown in case if the
	 *                           current tree node is root
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AbstractTreeNode<T> nextSibling() {
		if (isRoot()) {
			String message = String.format("Failed to determine the next sibling. " + NODE_IS_ROOT_MESSAGE, this);
			throw new TreeNodeException(message);
		}
		Collection<? extends TreeNode<T>> mParentSubtrees = parent().subtrees();
		if (mParentSubtrees.size() == 1) {
			return null;
		} else {
			Iterator<? extends TreeNode<T>> mSubtreesIterator = mParentSubtrees.iterator();
			while (mSubtreesIterator.hasNext()) {
				if (this.equals(mSubtreesIterator.next())
						&& mSubtreesIterator.hasNext()) {
					return (AbstractTreeNode<T>) mSubtreesIterator.next();
				}
			}
			return null;
		}
	}

}
