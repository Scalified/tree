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
 * File created: 2015-07-18 14:05:46
 */

package com.software.shell.util.tree;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Abstract implementation of the basic tree data structure
 *
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractTreeNode<T> implements TreeNode<T> {

	/**
	 * Message used to indicate that the specified tree node was not found in the specified tree
	 */
	protected static final String NODE_NODE_FOUND_MESSAGE = "The specified tree node %1$s was not found in the " +
			"current tree node %2$s";

	/**
	 * Message used to indicate that the specified tree node is root
	 */
	protected static final String NODE_IS_ROOT_MESSAGE = "The tree node %1$s is root";

	/**
	 * Message used to indicate that the specified tree node is leaf
	 */
	protected static final String NODE_IS_LEAF_MESSAGE = "The tree node %1$s is leaf";

	/**
	 * Message used to indicate that the specified tree node is not the descendant of the current tree node
	 */
	protected static final String NODE_IS_NOT_THE_DESCENDANT_MESSAGE = "The specified tree node %1$s is not the " +
			"descendant of tree node %2$s";

	/**
	 * Message used to indicate that the tree node is null
	 */
	protected static final String NODE_IS_NULL_MESSAGE = "The specified tree node is null";

	/**
	 * Hash generator, used to get a unique hash for each created tree node
	 */
	private static final AtomicLong HASH_GENERATOR = new AtomicLong(0);

	/**
	 * A unique hash, used to distinguish or compare the tree nodes
	 */
	private final long hash = HASH_GENERATOR.getAndIncrement();

	/**
	 * Reference to the parent tree node. Is {@code null} if the current tree node is root
	 */
	protected TreeNode<T> parent;

	/**
	 * Data store in the current tree node
	 */
	protected T data;

	/**
	 * Overrides default constructor
	 *
	 * @param data data to store in the current tree node
	 */
	protected AbstractTreeNode(T data) {
		this.data = data;
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
	protected abstract AbstractTreeNode<T> firstSubtree();

	/**
	 * Returns the next right sibling of the current tree node if the current
	 * tree node is not root
	 *
	 * @return next right sibling of the current tree node if the current tree
	 *         node is not root
	 * @throws TreeNodeException an exception that is thrown in case if the
	 *                           current tree node is root
	 */
	protected abstract AbstractTreeNode<T> nextSibling();

	/**
	 * Assigns the specified parent tree node reference to the specified tree node
	 *
	 * @param node tree node to assign the parent tree node to
	 * @param parent tree node to assign as parent reference
	 * @param <T> type of the data stored in the tree nodes
	 */
	protected static <T> void assignParent(TreeNode<T> node, TreeNode<T> parent) {
		((AbstractTreeNode<T>) node).parent = parent;
	}

	/**
	 * Unassigns the parent reference from the specified tree node
	 *
	 * @param node tree node to unassign the parent tree node reference
	 * @param <T> type of the data stored in the tree nodes
	 */
	protected static <T> void unAssignParent(TreeNode<T> node) {
		((AbstractTreeNode<T>) node).parent = null;
	}

	/**
	 * Checks whether the input collection is eligible to be passed as a parameter
	 * <p>
	 * @param collection input collection to check
	 * @param <T> type of the data, which parametrises the collection
	 * @return {@code true} if the input collection is eligible to be passed as a
	 *         parameter; {@code false} otherwise
	 */
	protected static <T> boolean isEligible(Collection<T> collection) {
		if (collection == null || collection.isEmpty()) {
			return false;
		}
		for (T mItem : collection) {
			if (mItem != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Populates the input collection with the tree nodes, while traversing the tree
	 *
	 * @param collection input collection to populate
	 * @return traversal action, which populates the input collection with the tree nodes
	 */
	protected static <T> TraversalAction<TreeNode<T>> populateAction(final Collection<TreeNode<T>> collection) {
		return new TraversalAction<TreeNode<T>>() {
			@Override
			public void perform(TreeNode<T> node) {
				collection.add(node);
			}
		};
	}

	/**
	 * Returns the data object stored in the current tree node
	 *
	 * @return data object stored in the current tree node
	 */
	@Override
	public T data() {
		return data;
	}

	/**
	 * Stores the data object into the current tree node
	 *
	 * @param data data object to store into the current tree node
	 */
	@Override
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * Returns the root node of the current node
	 * <p>
	 * Returns itself if the current node is root
	 *
	 * @return root node of the current node; itself,
	 *         if the current node is root
	 */
	@Override
	public TreeNode<T> root() {
		if (isRoot()) {
			return this;
		}
		TreeNode<T> mNode = this;
		do {
			mNode = mNode.parent();
		} while (!mNode.isRoot());
		return mNode;
	}

	/**
	 * Checks whether the current tree node is the root of the tree
	 *
	 * @return {@code true} if the current tree node is root of the tree;
	 *         {@code false} otherwise
	 */
	@Override
	public boolean isRoot() {
		return parent == null;
	}

	/**
	 * Returns the parent node of the current node
	 * <p>
	 * Returns {@code null} if the current node is root
	 *
	 * @return parent node of the current node; {@code null}
	 *         if the current node is root
	 */
	@Override
	public TreeNode<T> parent() {
		return parent;
	}

	/**
	 * Checks whether the current tree node is a leaf, e.g. does not have any
	 * subtrees
	 *
	 * @return {@code true} if the current tree node is a leaf, e.g. does not
	 *         have any subtrees; {@code false} otherwise
	 */
	@Override
	public boolean isLeaf() {
		return subtrees().isEmpty();
	}

	/**
	 * Checks whether among the current tree node subtrees there is
	 * a specified subtree
	 *
	 * @param subtree subtree whose presence within the current tree
	 *                node children is to be checked
	 * @return {@code true} if among the current tree node subtrees
	 *         there is a specified subtree; {@code false} otherwise
	 */
	@Override
	public boolean hasSubtree(TreeNode<T> subtree) {
		if (isLeaf()
				|| subtree == null
				|| subtree.isRoot()) {
			return false;
		}
		for (TreeNode<T> mSubtree : subtrees()) {
			if (mSubtree.equals(subtree)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the current tree node with all of its descendants
	 * (entire tree) contains the specified node
	 *
	 * @param node node whose presence within the current tree node with
	 *             all of its descendants (entire tree) is to be checked
	 * @return {@code true} if the current node with all of its descendants
	 *         (entire tree) contains the specified node; {@code false}
	 *         otherwise
	 */
	@Override
	public boolean contains(TreeNode<T> node) {
		if (isLeaf()
				|| node == null
				|| node.isRoot()) {
			return false;
		}
		for (TreeNode<T> mSubtree : subtrees()) {
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
	 * Checks whether the current tree node with all of its descendants
	 * (entire tree) contains all of the nodes from the specified collection
	 * (the place of nodes within a tree is not important)
	 *
	 * @param nodes collection of nodes to be checked for containment
	 *              within the current tree node with all of its descendants
	 *              (entire tree)
	 * @return {@code true} if the current tree node with all of its
	 *         descendants (entire tree) contains all of the nodes from the
	 *         specified collection; {@code false} otherwise
	 */
	@Override
	public boolean containsAll(Collection<TreeNode<T>> nodes) {
		if (isLeaf()
				|| !isEligible(nodes)) {
			return false;
		}
		for (TreeNode<T> node : nodes) {
			if (!contains(node)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Removes the first occurrence of the specified node from the entire tree,
	 * starting from the current tree node and traversing in a pre order manner
	 * <p>
	 * Checks whether the current tree node was changed as a result of the call
	 *
	 * @param node node to remove from the entire tree
	 * @return {@code true} if the current tree node was changed as a result of
	 *         the call; {@code false} otherwise
	 */
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
		for (TreeNode<T> mSubtree : subtrees()) {
			if (mSubtree.remove(node)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes all of the collection's nodes from the entire tree, starting from
	 * the current tree node and traversing in a pre order manner
	 * <p>
	 * Checks whether the current tree node was changed as a result of the call
	 *
	 * @param nodes collection containing nodes to be removed from the entire tree
	 * @return {@code true} if the current tree node was changed as a result
	 *         of the call; {@code false} otherwise
	 */
	@Override
	public boolean removeAll(Collection<TreeNode<T>> nodes) {
		if (isLeaf()
				|| !isEligible(nodes)) {
			return false;
		}
		boolean mResult = false;
		for (TreeNode<T> mNode : nodes) {
			boolean mRemoveResult = remove(mNode);
			if (!mResult && mRemoveResult) {
				mResult = true;
			}
		}
		return mResult;
	}

	/**
	 * Traverses the tree in a pre ordered manner starting from the
	 * current tree node and performs the traversal action on each
	 * traversed tree node
	 *
	 * @param action action, which is to be performed on each tree
	 *               node, while traversing the tree
	 */
	@Override
	public void traversePreOrder(TraversalAction<TreeNode<T>> action) {
		action.perform(this);
		if (!isLeaf()) {
			for (TreeNode<T> mSubtree : subtrees()) {
				mSubtree.traversePreOrder(action);
			}
		}
	}

	/**
	 * Traverses the tree in a post ordered manner starting from the
	 * current tree node and performs the traversal action on each
	 * traversed tree node
	 *
	 * @param action action, which is to be performed on each tree
	 *               node, while traversing the tree
	 */
	@Override
	public void traversePostOrder(TraversalAction<TreeNode<T>> action) {
		if (!isLeaf()) {
			for (TreeNode<T> mSubtree : subtrees()) {
				mSubtree.traversePostOrder(action);
			}
		}
		action.perform(this);
	}

	/**
	 * Returns the pre ordered collection of nodes of the current tree
	 *
	 * @return pre ordered collection of nodes of the current tree
	 */
	@Override
	public Collection<TreeNode<T>> preOrdered() {
		if (isLeaf()) {
			return Collections.<TreeNode<T>>singletonList(this);
		}
		final Collection<TreeNode<T>> mPreOrdered = new ArrayList<>();
		TraversalAction<TreeNode<T>> action = populateAction(mPreOrdered);
		traversePreOrder(action);
		return mPreOrdered;
	}

	/**
	 * Returns the post ordered collection of nodes of the current tree
	 *
	 * @return post ordered collection of nodes of the current tree
	 */
	@Override
	public Collection<TreeNode<T>> postOrdered() {
		if (isLeaf()) {
			return Collections.<TreeNode<T>>singletonList(this);
		}
		final Collection<TreeNode<T>> mPostOrdered = new ArrayList<>();
		TraversalAction<TreeNode<T>> action = populateAction(mPostOrdered);
		traversePostOrder(action);
		return mPostOrdered;
	}

	/**
	 * Returns the collection of nodes, which connect the current node
	 * with its descendants
	 *
	 * @param descendant the bottom child node for which the path is calculated
	 * @return collection of nodes, which connect the current node with its descendants
	 * @throws TreeNodeException exception that may be thrown in case if the
	 *                           current node does not have such descendant or if the
	 *                           specified tree node is root
	 */
	@Override
	public Collection<? extends TreeNode<T>> path(TreeNode<T> descendant) {
		if (isLeaf()
				|| descendant == null
				|| this.equals(descendant)) {
			return Collections.singletonList(this);
		}
		String errorMessage = "Unable to build the path between tree nodes. ";
		if (descendant.isRoot()) {
			String message = String.format(errorMessage + NODE_IS_ROOT_MESSAGE, descendant);
			throw new TreeNodeException(message);
		}
		List<TreeNode<T>> mPath = new LinkedList<>();
		TreeNode<T> mNode = descendant;
		mPath.add(mNode);
		do {
			mNode = mNode.parent();
			mPath.add(0, mNode);
			if (this.equals(mNode)) {
				return mPath;
			}
		} while (!mNode.isRoot());
		String message = String.format(errorMessage + NODE_IS_NOT_THE_DESCENDANT_MESSAGE, descendant, this);
		throw new TreeNodeException(message);
	}

	/**
	 * Returns the common ancestor of the current node and the node specified
	 *
	 * @param node node, which the common ancestor is determined for,
	 *             along with the current node
	 * @return common ancestor of the current node and the node specified
	 * @throws TreeNodeException exception that may be thrown in case if the
	 *                          specified tree node is null or the specified tree node
	 *                          does not belong to the current tree or if any of the tree
	 *                          nodes either the current one or the specified one is root
	 */
	@Override
	public TreeNode<T> commonAncestor(TreeNode<T> node) {
		String errorMessage = "Unable to find the common ancestor between tree nodes. ";
		if (node == null) {
			String message = errorMessage + NODE_IS_NULL_MESSAGE;
			throw new TreeNodeException(message);
		}
		if (!this.root().contains(node)) {
			String message = String.format(errorMessage + NODE_NODE_FOUND_MESSAGE, node, this);
			throw new TreeNodeException(message);
		}
		if (this.isRoot() || node.isRoot()) {
			String message = String.format(errorMessage + NODE_IS_ROOT_MESSAGE, this.isRoot() ? this : node);
			throw new TreeNodeException(message);
		}
		if (this.equals(node) || node.isSiblingOf(this)) {
			return parent();
		}
		int mThisLevel = this.level();
		int mThatLevel = node.level();
		return mThisLevel > mThatLevel ? node.parent() : this.parent();
	}

	/**
	 * Checks whether the current tree node is a sibling of the specified node,
	 * e.g. whether the current tree node and the specified one both have the
	 * same parent
	 *
	 * @param node node, which sibling with the current tree node is to be checked
	 * @return {@code true} if the current tree node is a sibling of the specified
	 *         node, e.g. whether the current tree node and the specified one both
	 *         have the same parent; {@code false} otherwise
	 */
	@Override
	public boolean isSiblingOf(TreeNode<T> node) {
		return node != null
				&& !isRoot()
				&& !node.isRoot()
				&& this.parent().equals(node.parent());
	}

	/**
	 * Checks whether the current tree node is the ancestor of the node specified
	 *
	 * @param node node, which is checked to be the descendant of the current tree
	 *             node
	 * @return {@code true} if the current tree node is the ancestor of the node
	 *         specified; {@code false} otherwise
	 */
	@Override
	public boolean isAncestorOf(TreeNode<T> node) {
		if (isLeaf()
				|| node == null
				|| node.isRoot()
				|| this.equals(node)) {
			return false;
		}
		TreeNode<T> mNode = node;
		do {
			mNode = mNode.parent();
			if (this.equals(mNode)) {
				return true;
			}
		} while (!mNode.isRoot());
		return false;
	}

	/**
	 * Checks whether the current tree node is the descendant of the node specified
	 *
	 * @param node node, which is checked to be the ancestor of the current tree
	 *             node
	 * @return {@code true} if the current tree node is the ancestor of the node
	 *         specified; {@code false} otherwise
	 */
	@Override
	public boolean isDescendantOf(TreeNode<T> node) {
		if (node == null
				|| this.isRoot()
				|| node.isLeaf()
				|| this.equals(node)) {
			return false;
		}
		TreeNode<T> mNode = this;
		do {
			mNode = mNode.parent();
			if (node.equals(mNode)) {
				return true;
			}
		} while (!mNode.isRoot());
		return false;
	}

	/**
	 * Returns the number of nodes in the entire tree, including the current tree node
	 *
	 * @return number of nodes in the entire tree, including the current tree node
	 */
	@Override
	public long size() {
		if (isLeaf()) {
			return 1;
		}
		final long[] count = {0};
		TraversalAction<TreeNode<T>> action = new TraversalAction<TreeNode<T>>() {
			@Override
			public void perform(TreeNode<T> node) {
				count[0]++;
			}
		};
		traversePreOrder(action);
		return count[0];
	}

	/**
	 * Returns the height of the current tree node, e.g. the number of edges
	 * on the longest downward path between that node and a leaf
	 *
	 * @return height of the current tree node, e.g. the number of edges
	 * on the longest downward path between that node and a leaf
	 */
	@Override
	public int height() {
		if (isLeaf()) {
			return 0;
		}
		int mHeight = 0;
		for (TreeNode<T> mSubtree : subtrees()) {
			mHeight = Math.max(mHeight, mSubtree.height());
		}
		return mHeight + 1;
	}

	/**
	 * Returns the depth (level) of the current tree node within the entire tree,
	 * e.g. the number of edges between the root tree node and the current one
	 *
	 * @return depth (level) of the current tree node within the entire tree,
	 *         e.g. the number of edges between the root tree node and the current
	 *         one
	 */
	@Override
	public int level() {
		if (isRoot()) {
			return 0;
		}
		int mLevel = 0;
		TreeNode<T> mNode = this;
		do {
			mNode = mNode.parent();
			mLevel++;
		} while (!mNode.isRoot());
		return mLevel;
	}

	/**
	 * Creates and returns a copy of this object
	 *
	 * @return a clone of this instance
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TreeNode<T> clone() {
		try {
			return (TreeNode<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			String message = "Unable to clone the current tree node";
			throw new TreeNodeException(message, e);
		}
	}

	/**
	 * Indicates whether some object equals to this one
	 *
	 * @param obj the reference object with which to compare
	 * @return {@code true} if this object is the same as the obj
	 *         argument; {@code false} otherwise
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null
				|| getClass() != obj.getClass()) {
			return false;
		}
		AbstractTreeNode<T> that = (AbstractTreeNode<T>) obj;
		return this.hash == that.hash
				&& (this.data() != null ? this.data().equals(that.data()) : that.data() == null);
	}

	/**
	 * Returns the hash code value of this object
	 *
	 * @return hash code value of this object
	 */
	@Override
	public int hashCode() {
		int mHashCode = (int) (this.hash ^ (this.hash >>> 32));
		if (data() != null) {
			mHashCode = 31 * mHashCode + data().hashCode();
		}
		return mHashCode;
	}

	/**
	 * Returns the string representation of this object
	 *
	 * @return string representation of this object
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("\n");
		final int mTopNodeLevel = level();
		TraversalAction<TreeNode<T>> action = new TraversalAction<TreeNode<T>>() {
			@Override
			public void perform(TreeNode<T> node) {
				int mNodeLevel = node.level() - mTopNodeLevel;
				for (int i = 0; i < mNodeLevel; i++) {
					builder.append("|  ");
				}
				builder
						.append("+- ")
						.append(node.data())
						.append("\n");
			}
		};
		traversePreOrder(action);
		return builder.toString();
	}

	/**
	 * Returns an iterator over the elements in this tree in proper sequence
	 * <p>
	 * The returned iterator is <b>fail-fast</b>
	 *
	 * @return an iterator over the elements in this tree in proper sequence
	 */
	@Override
	public Iterator<TreeNode<T>> iterator() {
		return new TreeNodeIterator();
	}

	/**
	 * Tree node iterator implementation
	 */
	private class TreeNodeIterator implements Iterator<TreeNode<T>> {

		/**
		 * An expected size of the tree node required to check
		 * whether the tree node was changed during <b>foreach</b>
		 * iteration
		 */
		long expectedSize = size();

		/**
		 * Reference to the current tree node within iteration
		 */
		TreeNode<T> currentNode;

		/**
		 * Reference to the next tree node within iteration
		 */
		AbstractTreeNode<T> nextNode = AbstractTreeNode.this;

		/**
		 * Indicates whether there is a next tree node available
		 * within iteration
		 */
		boolean nextNodeAvailable = true;

		/**
		 * Returns {@code true} if the iteration has more elements;
		 * otherwise returns {@code false}
		 *
		 * @return {@code true} if the iteration has more elements;
		 *         {@code false} otherwise
		 */
		@Override
		public boolean hasNext() {
			return nextNodeAvailable;
		}

		/**
		 * Returns the next element in the iteration
		 *
		 * @return the next element in the iteration
		 * @throws NoSuchElementException if the iteration has no more elements
		 */
		@Override
		public TreeNode<T> next() {
			checkForConcurrentModification();
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			currentNode = nextNode;
			if (nextNode.isLeaf()) {
				if (nextNode.isRoot()) {
					nextNodeAvailable = false;
				} else {
					do {
						AbstractTreeNode<T> mCurrent = nextNode;
						nextNode = (AbstractTreeNode<T>) nextNode.parent();
						if (mCurrent.equals(AbstractTreeNode.this)) {
							nextNodeAvailable = false;
							break;
						}
						AbstractTreeNode<T> mNextSibling = mCurrent.nextSibling();
						if (mNextSibling != null) {
							nextNode = mNextSibling;
							break;
						}
					} while (true);
				}
			} else {
				nextNode = nextNode.firstSubtree();
			}
			return currentNode;
		}

		/**
		 * Checks whether tree node was changed during <b>foreach</b>
		 * iteration and throws {@link ConcurrentModificationException}
		 * exception if so
		 */
		void checkForConcurrentModification() {
			if (expectedSize != size()) {
				throw new ConcurrentModificationException();
			}
		}

		/**
		 * Removes from the underlying tree the last element returned by this
		 * iterator (optional operation)
		 * <p>
		 * This method can be called only once per call to {@link #next}.
		 * The behavior of an iterator is unspecified if the underlying tree
		 * is modified while the iteration is in progress in any way other
		 * than by calling this method
		 *
		 * @throws IllegalStateException an exception that may be thrown in case
		 *                               if remove was performed without any
		 *                               iteration
		 * @throws TreeNodeException an exception that may be thrown in case if
		 *                           remove was performed on a root node
		 */
		@Override
		public void remove() {
			String errorMessage = "Failed to remove the tree node. ";
			if (!isIterationStarted()) {
				throw new IllegalStateException(errorMessage + "The iteration has not been performed yet");
			}
			if (currentNode.isRoot()) {
				String message = String.format(errorMessage + NODE_IS_ROOT_MESSAGE, currentNode);
				throw new TreeNodeException(message);
			}
			if (currentNode.equals(AbstractTreeNode.this)) {
				throw new TreeNodeException(errorMessage + "The starting node can't be removed");
			}
			checkForConcurrentModification();
			AbstractTreeNode<T> mCurrent = (AbstractTreeNode<T>) currentNode;
			while (true) {
				if (mCurrent.isRoot()) {
					nextNodeAvailable = false;
					break;
				} else if (mCurrent.nextSibling() != null) {
					nextNode = mCurrent.nextSibling();
					break;
				} else {
					mCurrent = (AbstractTreeNode<T>) mCurrent.parent();
				}
			}
			TreeNode<T> mParent = currentNode.parent();
			mParent.dropSubtree(currentNode);
			currentNode = mParent;
			expectedSize = size();
		}

		/**
		 * Returns whether iteration has been started
		 *
		 * @return {@code true} if iteration has been started; {@code false} otherwise
		 */
		boolean isIterationStarted() {
			return currentNode != null;
		}

	}

}
