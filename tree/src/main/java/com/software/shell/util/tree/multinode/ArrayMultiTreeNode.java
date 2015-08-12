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
 * File created: 2015-07-12 15:50:33
 */

package com.software.shell.util.tree.multinode;

import com.software.shell.util.tree.AbstractTreeNode;
import com.software.shell.util.tree.TraversalAction;
import com.software.shell.util.tree.TreeNode;
import com.software.shell.util.tree.TreeNodeException;

import java.util.*;

/**
 * Implementation of the K-ary (multi node) tree data structure,
 * base on the resizable array
 *
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
public class ArrayMultiTreeNode<T> extends AbstractMultiTreeNode<T> {

	/**
	 * Current UID of this object used for serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default initial branching factor, that is the number of subtrees
	 * this node can have before getting resized
	 */
	private static final int DEFAULT_BRANCHING_FACTOR = 10;

	/**
	 * The maximum size of array to allocate.
	 * Some VMs reserve some header words in an array.
	 * Attempts to allocate larger arrays may mResult in
	 * OutOfMemoryError: Requested array size exceeds VM limit
	 */
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	/**
	 * Array, which holds the references to the current tree node subtrees
	 */
	private Object[] subtrees;

	/**
	 * Number of subtrees currently present in the current tree node
	 */
	private int subtreesSize;

	/**
	 * Current branching factor of the current tree node
	 */
	private final int branchingFactor;

	/**
	 * Constructs the {@link ArrayMultiTreeNode} instance
	 *
	 * @param data data to store in the current tree node
	 */
	public ArrayMultiTreeNode(T data) {
		super(data);
		this.branchingFactor = DEFAULT_BRANCHING_FACTOR;
		this.subtrees = new Object[branchingFactor];
	}

	/**
	 * Constructs the {@link ArrayMultiTreeNode} instance
	 *
	 * @param data data to store in the current tree node
	 * @param branchingFactor initial branching factor, that is the number
	 *                        of subtrees the current tree node can have
	 *                        before getting resized
	 */
	public ArrayMultiTreeNode(T data, int branchingFactor) {
		super(data);
		if (branchingFactor < 0) {
			throw new IllegalArgumentException("Branching factor can not be negative");
		}
		this.branchingFactor = branchingFactor;
		this.subtrees = new Object[branchingFactor];
	}

	/**
	 * Returns the first subtree of the current tree node if the current
	 * tree node is not a leaf
	 *
	 * @return first subtree of the current tree node if the current tree
	 *         node is not a leaf
	 * @throws TreeNodeException an exception that is thrown in case if the
	 *                           current tree node is leaf
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AbstractTreeNode<T> firstSubtree() {
		if (isLeaf()) {
			String message = String.format("Failed to determine the first subtree. " + NODE_IS_LEAF_MESSAGE, this);
			throw new TreeNodeException(message);
		}
		return (AbstractTreeNode<T>) subtrees[0];
	}

	/**
	 * Returns the collection of the child nodes of the current node
	 * with all of its proper descendants, if any
	 * <p>
	 * Returns {@code null} if the current node is leaf
	 *
	 * @return collection of the child nodes of the current node with
	 *         all of its proper descendants, if any;
	 *         {@code null} if the current node is leaf
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<TreeNode<T>> subtrees() {
		if (isLeaf()) {
			return Collections.<TreeNode<T>>singletonList(this);
		}
		Collection<TreeNode<T>> mSubtrees = new ArrayList<>(subtreesSize);
		for (int i = 0; i < subtreesSize; i++) {
			TreeNode<T> mSubtree = (TreeNode<T>) subtrees[i];
			mSubtrees.add(mSubtree);
		}
		return mSubtrees;
	}

	/**
	 * Checks whether the current tree node is a leaf, e.g. does not have any
	 * subtrees
	 * <p>
	 * Overridden to have a faster array implementation
	 *
	 * @return {@code true} if the current tree node is a leaf, e.g. does not
	 *         have any subtrees; {@code false} otherwise
	 */
	@Override
	public boolean isLeaf() {
		return subtreesSize == 0;
	}

	/**
	 * Checks whether among the current tree node subtrees there is
	 * a specified subtree
	 * <p>
	 * Overridden to have a faster array implementation
	 *
	 * @param subtree subtree whose presence within the current tree
	 *                node children is to be checked
	 * @return {@code true} if among the current tree node subtrees
	 *         there is a specified subtree; {@code false} otherwise
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean hasSubtree(TreeNode<T> subtree) {
		if (isLeaf() || subtree == null ||subtree.isRoot()) {
			return false;
		}
		for (int i = 0; i < subtreesSize; i++) {
			TreeNode<T> mSubtree = (TreeNode<T>) subtrees[i];
			if (subtree.equals(mSubtree)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Drops the first occurrence of the specified subtree from the current
	 * tree node
	 * <p>
	 * Checks whether the current tree node was changed as a result of
	 * the call
	 *
	 * @param subtree subtree to drop from the current tree node
	 * @return {@code true} if the current tree node was changed as a result
	 *         of the call; {@code false} otherwise
	 */
	@Override
	public boolean dropSubtree(TreeNode<T> subtree) {
		if (isLeaf() || subtree == null || subtree.isRoot()) {
			return false;
		}
		int mSubtreeIndex = indexOf(subtree);
		if (mSubtreeIndex < 0) {
			return false;
		}
		int mNumShift = subtreesSize - mSubtreeIndex - 1;
		if (mNumShift > 0) {
			System.arraycopy(subtrees, mSubtreeIndex + 1, subtrees, mSubtreeIndex, mNumShift);
		}
		subtrees[--subtreesSize] = null;
		unAssignParent(subtree);
		return true;
	}

	/**
	 * Returns the index of the first occurrence of the specified subtree
	 * within subtrees array; {@code -1} if the subtrees array does not contain
	 * such subtree
	 *
	 * @param subtree subtree to find the index of
	 * @return index of the first occurrence of the specified subtree within
	 *         subtrees array; {@code -1} if the subtrees array does not contain
	 *         such subtree
	 */
	@SuppressWarnings("unchecked")
	private int indexOf(TreeNode<T> subtree) {
		for (int i = 0; i < subtreesSize; i++) {
			TreeNode<T> mSubtree = (TreeNode<T>) subtrees[i];
			if (mSubtree.equals(subtree)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks whether the current tree node with all of its descendants
	 * (entire tree) contains the specified node
	 * <p>
	 * Overridden to have a faster array implementation
	 *
	 * @param node node whose presence within the current tree node with
	 *             all of its descendants (entire tree) is to be checked
	 * @return {@code true} if the current node with all of its descendants
	 *         (entire tree) contains the specified node; {@code false}
	 *         otherwise
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(TreeNode<T> node) {
		if (isLeaf() || node == null || node.isRoot()) {
			return false;
		}
		for (int i = 0; i < subtreesSize; i++) {
			TreeNode<T> mSubtree = (TreeNode<T>) subtrees[i];
			if (mSubtree.equals(node)) {
				return true;
			}
			if (mSubtree.contains(node)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds the subtree with all of its descendants to the current tree node
	 * <p>
	 * {@code null} subtree cannot be added, in this case return result will
	 * be {@code false}
	 * <p>
	 * Checks whether this tree node was changed as a result of the call
	 *
	 * @param subtree subtree to add to the current tree node
	 * @return {@code true} if this tree node was changed as a
	 *         result of the call; {@code false} otherwise
	 */
	@Override
	public boolean add(TreeNode<T> subtree) {
		if (subtree == null) {
			return false;
		}
		assignParent(subtree, this);
		ensureSubtreesCapacity(subtreesSize + 1);
		subtrees[subtreesSize++] = subtree;
		return true;
	}

	/**
	 * Increases the capacity of the subtrees array, if necessary, to
	 * ensure that it can hold at least the number of subtrees specified
	 * by the minimum subtrees capacity argument
	 *
	 * @param minSubtreesCapacity the desired minimum subtrees capacity
	 */
	private void ensureSubtreesCapacity(int minSubtreesCapacity) {
		if (minSubtreesCapacity > subtrees.length) {
			increaseSubtreesCapacity(minSubtreesCapacity);
		}
	}

	/**
	 * Increases the subtrees array capacity to ensure that it can hold
	 * at least the number of elements specified by the minimum subtrees
	 * capacity argument
	 *
	 * @param minSubtreesCapacity the desired minimum subtrees capacity
	 */
	private void increaseSubtreesCapacity(int minSubtreesCapacity) {
		int oldSubtreesCapacity = subtrees.length;
		int newSubtreesCapacity = oldSubtreesCapacity + (oldSubtreesCapacity >> 1);
		if (newSubtreesCapacity < minSubtreesCapacity) {
			newSubtreesCapacity = minSubtreesCapacity;
		}
		if (newSubtreesCapacity > MAX_ARRAY_SIZE) {
			if (minSubtreesCapacity < 0) {
				throw new OutOfMemoryError();
			}
			newSubtreesCapacity = minSubtreesCapacity > MAX_ARRAY_SIZE ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
		}
		subtrees = Arrays.copyOf(subtrees, newSubtreesCapacity);
	}

	/**
	 * Removes all the subtrees with all of its descendants from the current
	 * tree node
	 */
	@Override
	public void clear() {
		if (!isLeaf()) {
			for (int i = 0; i < subtreesSize; i++) {
				subtrees[i] = null;
			}
			subtreesSize = 0;
		}
	}

	/**
	 * Removes the first occurrence of the specified node from the entire tree,
	 * starting from the current tree node and traversing in a pre order manner
	 * <p>
	 * Checks whether the current tree node was changed as a result of the call
	 * <p>
	 * Overridden to have a faster array implementation
	 *
	 * @param node node to remove from the entire tree
	 * @return {@code true} if the current tree node was changed as a result of
	 *         the call; {@code false} otherwise
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(TreeNode<T> node) {
		if (isLeaf()
				|| node == null
				|| node.isRoot()) {
			return false;
		}
		if (dropSubtree(node)) {
			return true;
		}
		for (int i = 0; i < subtreesSize; i++) {
			TreeNode<T> mSubtree = (TreeNode<T>) subtrees[i];
			if (mSubtree.remove(node)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Traverses the tree in a pre ordered manner starting from the
	 * current tree node and performs the traversal action on each
	 * traversed tree node
	 * <p>
	 * Overridden to have a faster array implementation
	 *
	 * @param action action, which is to be performed on each tree
	 *               node, while traversing the tree
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void traversePreOrder(TraversalAction<TreeNode<T>> action) {
		action.perform(this);
		if (!isLeaf()) {
			for (int i = 0; i < subtreesSize; i++) {
				ArrayMultiTreeNode<T> mSubtree = (ArrayMultiTreeNode<T>) subtrees[i];
				mSubtree.traversePreOrder(action);
			}
		}
	}

	/**
	 * Traverses the tree in a post ordered manner starting from the
	 * current tree node and performs the traversal action on each
	 * traversed tree node
	 * <p>
	 * Overridden to have a faster array implementation
	 *
	 * @param action action, which is to be performed on each tree
	 *               node, while traversing the tree
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void traversePostOrder(TraversalAction<TreeNode<T>> action) {
		if (!isLeaf()) {
			for (int i = 0; i < subtreesSize; i++) {
				ArrayMultiTreeNode<T> mSubtree = (ArrayMultiTreeNode<T>) subtrees[i];
				mSubtree.traversePostOrder(action);
			}
		}
		action.perform(this);
	}

	/**
	 * Returns the height of the current tree node, e.g. the number of edges
	 * on the longest downward path between that node and a leaf
	 * <p>
	 * Overridden to have a faster array implementation
	 *
	 * @return height of the current tree node, e.g. the number of edges
	 * on the longest downward path between that node and a leaf
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int height() {
		if (isLeaf()) {
			return 0;
		}
		int mHeight = 0;
		for (int i = 0; i < subtreesSize; i++) {
			TreeNode<T> mSubtree = (TreeNode<T>) subtrees[i];
			mHeight = Math.max(mHeight, mSubtree.height());
		}
		return mHeight + 1;
	}

	/**
	 * Returns the collection of nodes, which have the same parent
	 * as the current node; {@link Collections#emptyList()} if the current
	 * tree node is root or if the current tree node has no subtrees
	 * <p>
	 * Overridden to have a faster array implementation
	 *
	 * @return collection of nodes, which have the same parent as
	 *         the current node; {@link Collections#emptyList()} if the
	 *         current tree node is root or if the current tree node has
	 *         no subtrees
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends MultiTreeNode<T>> siblings() {
		if (isRoot()) {
			String message = String.format("Unable to find the siblings. " + NODE_IS_ROOT_MESSAGE, root());
			throw new TreeNodeException(message);
		}
		ArrayMultiTreeNode<T> mParent = (ArrayMultiTreeNode<T>) parent;
		int mParentSubtreesSize = mParent.subtreesSize;
		if (mParentSubtreesSize == 1) {
			return Collections.<MultiTreeNode<T>>emptyList();
		}
		Object[] mParentSubtreeObjects = mParent.subtrees;
		Collection<MultiTreeNode<T>> mSiblings = new ArrayList<>(mParentSubtreesSize - 1);
		for (int i = 0; i < mParentSubtreesSize; i++) {
			MultiTreeNode<T> mSubtree = (MultiTreeNode<T>) mParentSubtreeObjects[i];
			if (!mSubtree.equals(this)) {
				mSiblings.add(mSubtree);
			}
		}
		return mSiblings;
	}

	/**
	 * Adds the collection of the subtrees with all of theirs descendants
	 * to the current tree node
	 * <p>
	 * Checks whether this tree node was changed as a result of the call
	 *
	 * @param subtrees collection of the subtrees with all of their
	 *                 descendants
	 * @return {@code true} if this tree node was changed as a
	 *         result of the call; {@code false} otherwise
	 */
	@Override
	public boolean addSubtrees(Collection<? extends MultiTreeNode<T>> subtrees) {
		if (!isEligible(subtrees)) {
			return false;
		}
		for (MultiTreeNode<T> mSubtree : subtrees) {
			assignParent(mSubtree, this);
		}
		Object[] subtreesArray = subtrees.toArray();
		int subtreesArrayLength = subtreesArray.length;
		ensureSubtreesCapacity(subtreesSize + subtreesArrayLength);
		System.arraycopy(subtreesArray, 0, this.subtrees, subtreesSize, subtreesArrayLength);
		subtreesSize += subtreesArrayLength;
		return subtreesArrayLength != 0;
	}

}
