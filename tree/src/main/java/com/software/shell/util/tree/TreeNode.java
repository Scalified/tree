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
 * File created: 2015-07-12 15:48:12
 */

package com.software.shell.util.tree;

import java.io.Serializable;
import java.util.Collection;

/**
 * This interface represents the basic tree data structure
 * <h1>Definition</h1>
 * A tree data structure can be defined recursively (locally) as a collection of nodes
 * (starting at a root node), where each node is a data structure consisting of a value,
 * together with a list of references to nodes (the children), with the constraints that
 * no reference is duplicated, and none points to the root
 * <p>
 * A tree is a (possibly non-linear) data structure made up of nodes or vertices and edges
 * without having any cycle. The tree with no nodes is called the <b>null</b> or
 * <b>empty</b> tree. A tree that is not empty consists of a root node and potentially many
 * levels of additional nodes that form a hierarchy
 * <h1>Terminology</h1>
 * <ul>
 *     <li><b>Node</b> - a single point of a tree</li>
 *     <li><b>Edge</b> - line, which connects two distinct nodes</li>
 *     <li><b>Root</b> - top node of the tree, which has no parent</li>
 *     <li><b>Parent</b> - a node, other than the root, which is connected to other successor
 *                         nodes</li>
 *     <li><b>Child</b> - a node, other than the root, which is connected to predecessor</li>
 *     <li><b>Leaf</b> - a node without children</li>
 *     <li><b>Path</b> - a sequence of nodes and edges connecting a node with a
 *                       descendant</li>
 *     <li><b>Path Length</b> - number of nodes in the path - 1</li>
 *     <li><b>Ancestor</b> - the top parent node of the path</li>
 *     <li><b>Descendant</b> - the bottom child node of the path</li>
 *     <li><b>Siblings</b> - nodes, which have the same parent</li>
 *     <li><b>Subtree</b> - a node in a tree with all of its proper descendants, if any</li>
 *     <li><b>Node Height</b> - the number of edges on the longest downward path between that
 *                              node and a leaf</li>
 *     <li><b>Tree Height</b> - the number of edges on the longest downward path between the
 *                              root and a leaf (root height)</li>
 *     <li><b>Depth (Level)</b> - the path length between the root and the current node</li>
 *     <li><b>Ordered Tree</b> - tree in which nodes has the children ordered</li>
 *     <li><b>Labeled Tree</b> - tree in which a label or value is associated with each node
 *                               of the tree</li>
 *     <li><b>Expression Tree</b> - tree which specifies the association of an expression�s
 *                                  operands and its operators in a uniform way, regardless
 *                                  of whether the association is required by the placement
 *                                  of parentheses in the expression or by the precedence and
 *                                  associativity rules for the operators involved</li>
 *     <li><b>Branching Factor</b> - maximum number of children a node can have</li>
 *     <li><b>Pre order</b> - a form of tree traversal, where the action is called firstly on
 *                           the current node, and then the pre order function is called again
 *                           recursively on each of the subtree from left to right</li>
 *     <li><b>Post order</b> - a form of tree traversal, where the post order function is called
 *                            recursively on each subtree from left to right and then the
 *                            action is called</li>
 * </ul>
 *
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TreeNode<T> extends Iterable<TreeNode<T>>, Serializable, Cloneable {

	/**
	 * Returns the data object stored in the current tree node
	 *
	 * @return data object stored in the current tree node
	 */
	T data();

	/**
	 * Stores the data object into the current tree node
	 *
	 * @param data data object to store into the current tree node
	 */
	void setData(T data);

	/**
	 * Returns the root node of the current node
	 * <p>
	 * Returns itself if the current node is root
	 *
	 * @return root node of the current node; itself,
	 *         if the current node is root
	 */
	TreeNode<T> root();

	/**
	 * Checks whether the current tree node is the root of the tree
	 *
	 * @return {@code true} if the current tree node is root of the tree;
	 *         {@code false} otherwise
	 */
	boolean isRoot();

	/**
	 * Returns the parent node of the current node
	 * <p>
	 * Returns {@code null} if the current node is root
	 *
	 * @return parent node of the current node; {@code null}
	 *         if the current node is root
	 */
	TreeNode<T> parent();

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
	Collection<? extends TreeNode<T>> subtrees();

	/**
	 * Checks whether the current tree node is a leaf, e.g. does not have any
	 * subtrees
	 *
	 * @return {@code true} if the current tree node is a leaf, e.g. does not
	 *         have any subtrees; {@code false} otherwise
	 */
	boolean isLeaf();

	/**
	 * Checks whether among the current tree node subtrees there is
	 * a specified subtree
	 *
	 * @param subtree subtree whose presence within the current tree
	 *                node children is to be checked
	 * @return {@code true} if among the current tree node subtrees
	 *         there is a specified subtree; {@code false} otherwise
	 */
	boolean hasSubtree(TreeNode<T> subtree);

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
	boolean dropSubtree(TreeNode<T> subtree);

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
	boolean contains(TreeNode<T> node);

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
	boolean containsAll(Collection<TreeNode<T>> nodes);

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
	boolean add(TreeNode<T> subtree);

	/**
	 * Removes all the subtrees with all of its descendants from the current
	 * tree node
	 */
	void clear();

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
	boolean remove(TreeNode<T> node);

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
	boolean removeAll(Collection<TreeNode<T>> nodes);

	/**
	 * Traverses the tree in a pre ordered manner starting from the
	 * current tree node and performs the traversal action on each
	 * traversed tree node
	 *
	 * @param action action, which is to be performed on each tree
	 *               node, while traversing the tree
	 */
	void traversePreOrder(TraversalAction<TreeNode<T>> action);

	/**
	 * Traverses the tree in a post ordered manner starting from the
	 * current tree node and performs the traversal action on each
	 * traversed tree node
	 *
	 * @param action action, which is to be performed on each tree
	 *               node, while traversing the tree
	 */
	void traversePostOrder(TraversalAction<TreeNode<T>> action);

	/**
	 * Returns the pre ordered collection of nodes of the current tree
	 *
	 * @return pre ordered collection of nodes of the current tree
	 */
	Collection<? extends TreeNode<T>> preOrdered();

	/**
	 * Returns the post ordered collection of nodes of the current tree
	 *
	 * @return post ordered collection of nodes of the current tree
	 */
	Collection<? extends TreeNode<T>> postOrdered();

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
	Collection<? extends TreeNode<T>> path(TreeNode<T> descendant);

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
	TreeNode<T> commonAncestor(TreeNode<T> node);

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
	boolean isSiblingOf(TreeNode<T> node);

	/**
	 * Checks whether the current tree node is the ancestor of the node specified
	 *
	 * @param node node, which is checked to be the descendant of the current tree
	 *             node
	 * @return {@code true} if the current tree node is the ancestor of the node
	 *         specified; {@code false} otherwise
	 */
	boolean isAncestorOf(TreeNode<T> node);

	/**
	 * Checks whether the current tree node is the descendant of the node specified
	 *
	 * @param node node, which is checked to be the ancestor of the current tree
	 *             node
	 * @return {@code true} if the current tree node is the ancestor of the node
	 *         specified; {@code false} otherwise
	 */
	boolean isDescendantOf(TreeNode<T> node);

	/**
	 * Returns the number of nodes in the entire tree, including the current tree node
	 *
	 * @return number of nodes in the entire tree, including the current tree node
	 */
	long size();

	/**
	 * Returns the height of the current tree node, e.g. the number of edges
	 * on the longest downward path between that node and a leaf
	 *
	 * @return height of the current tree node, e.g. the number of edges
	 * on the longest downward path between that node and a leaf
	 */
	int height();

	/**
	 * Returns the depth (level) of the current tree node within the entire tree,
	 * e.g. the number of edges between the root tree node and the current one
	 *
	 * @return depth (level) of the current tree node within the entire tree,
	 *         e.g. the number of edges between the root tree node and the current
	 *         one
	 */
	int level();

	/**
	 * Creates and returns a copy of this object
	 *
	 * @return a clone of this instance
	 */
	TreeNode<T> clone();

}
