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
 * File created: 2015-07-18 13:50:34
 */

package com.software.shell.tree.multinode;

import com.software.shell.tree.TreeNode;

import java.util.Collection;
import java.util.Collections;

/**
 * This interface represents the K-ary (multiple node) tree data
 * structure
 * <h1>Definition</h1>
 * <p>
 * <b>K-ary tree</b> - tree, in which each node has no more than k subtrees
 *
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
public interface MultiTreeNode<T> extends TreeNode<T> {

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
	Collection<? extends MultiTreeNode<T>> siblings();

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
	boolean addSubtrees(Collection<? extends MultiTreeNode<T>> subtrees);

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
	boolean hasSubtrees(Collection<? extends MultiTreeNode<T>> subtrees);

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
	boolean dropSubtrees(Collection<? extends MultiTreeNode<T>> subtrees);

}
