/*
 * Copyright 2016 Scalified <http://www.scalified.com>
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
 */

package com.scalified.tree.multinode;

import com.scalified.tree.TreeNodeTest;
import com.scalified.tree.TreeNode;
import com.scalified.tree.TreeNodeException;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class MultiTreeNodeTest extends TreeNodeTest {

	/*
	 * Test tree structure
	 *
	 *   +- root(ROOT_DATA)
	 *      +- node_1(NODE_DATA_1)
	 *      +- node_2(NODE_DATA_2)
	 *      |  +- node_3(NODE_DATA_3)
	 *      |  |  +- node_4(NODE_DATA_4)
	 *      |  |  +- node_5(NODE_DATA_1)
	 *      |  |  |  +- node6 (NODE_DATA4)
	 *      |  +- node_7(null)
	 *      |  +- node_8(NODE_DATA_1)
	 *      +- node9(NODE_DATA_4)
	 *      |  +- node10(null)
	 *
	 */

	@Test
	public void testSiblings() {
		// Test the specified tree nodes are the siblings of the current node
		String message = "Tree node siblings were incorrectly determined";

		Collection<TreeNode<String>> mSiblingsLevel1 = new HashSet<>(2);
		mSiblingsLevel1.add(node2);
		mSiblingsLevel1.add(node9);

		Collection<TreeNode<String>> mSiblingsLevel2 = new HashSet<>(2);
		mSiblingsLevel2.add(node3);
		mSiblingsLevel2.add(node7);

		assertEquals(message, mSiblingsLevel1, ((MultiTreeNode<String>) node1).siblings());
		assertEquals(message, mSiblingsLevel2, ((MultiTreeNode<String>) node8).siblings());

		assertEquals(message, Collections.emptySet(), ((MultiTreeNode<String>) node6).siblings());
	}

	@Test(expected = TreeNodeException.class)
	public void testSiblingsRootNodeException() {
		// Test the root node sibling call throws exception
		((MultiTreeNode<String>) root).siblings();
	}

	@Test
	public void testAddSubtrees() {
		// Test the specified tree nodes are successfully added
		String messageAddExpected = "The specified tree node was expected to be added but actually was not";
		String messageAddResultTrueExpected =
				"The tree nodes addition result was expected to be true, but actually was false";
		Collection<MultiTreeNode<String>> mSubtreesToAdd = new ArrayList<>(3);
		MultiTreeNode<String> nodeToAdd1 = (MultiTreeNode<String>) createTreeNode("NODE_TO_ADD_1");
		MultiTreeNode<String> nodeToAdd2 = (MultiTreeNode<String>) createTreeNode("NODE_TO_ADD_2");
		MultiTreeNode<String> nodeToAdd3 = (MultiTreeNode<String>) createTreeNode("NODE_TO_ADD_3");
		mSubtreesToAdd.add(nodeToAdd1);
		mSubtreesToAdd.add(nodeToAdd2);
		mSubtreesToAdd.add(nodeToAdd3);
		assertTrue(messageAddResultTrueExpected, ((MultiTreeNode<String>) node7).addSubtrees(mSubtreesToAdd));
		assertTrue(messageAddExpected, node7.hasSubtree(nodeToAdd1));
		assertTrue(messageAddExpected, node7.hasSubtree(nodeToAdd2));
		assertTrue(messageAddExpected, node7.hasSubtree(nodeToAdd3));

		// Test the specified tree nodes are not added
		String messageAddResultFalseExpected =
				"The tree nodes addition result was expected to be false, but actually was true";
		assertFalse(messageAddResultFalseExpected,
				((MultiTreeNode<String>) node8).addSubtrees(null));
		assertFalse(messageAddResultFalseExpected,
				((MultiTreeNode<String>) node1).addSubtrees(Collections.singletonList(null)));
		assertFalse(messageAddResultFalseExpected,
				((MultiTreeNode<String>) node10).addSubtrees(Collections.emptyList()));
	}

	@Test
	public void testHasSubtrees() {
		// Test the current tree node contains all of the specified subtrees
		String messageContains =
				"The current tree node was expected to contain all of the subtrees, but actually was not";
		Collection<MultiTreeNode<String>> mSubtreesContain1 = new ArrayList<>(2);
		mSubtreesContain1.add((MultiTreeNode<String>) node2);
		mSubtreesContain1.add((MultiTreeNode<String>) node9);
		assertTrue(messageContains, ((MultiTreeNode<String>) root).hasSubtrees(mSubtreesContain1));

		Collection<MultiTreeNode<String>> mSubtreesContain2 = new ArrayList<>(3);
		mSubtreesContain2.add((MultiTreeNode<String>) node3);
		mSubtreesContain2.add((MultiTreeNode<String>) node7);
		mSubtreesContain2.add((MultiTreeNode<String>) node8);
		assertTrue(messageContains, ((MultiTreeNode<String>) node2).hasSubtrees(mSubtreesContain2));

		List<MultiTreeNode<String>> mSubtreesContain3 = Collections.singletonList((MultiTreeNode<String>) node6);
		assertTrue(messageContains, ((MultiTreeNode<String>) node5).hasSubtrees(mSubtreesContain3));

		// Test the current tree node does not contain at least one of the the specified subtrees
		String messageNotContains =
				"The current tree node was expected to contain all of the subtrees, but actually was not";

		Collection<MultiTreeNode<String>> mSubtreesNotContain1 = new ArrayList<>(4);
		mSubtreesNotContain1.add((MultiTreeNode<String>) node3);
		mSubtreesNotContain1.add((MultiTreeNode<String>) node7);
		mSubtreesNotContain1.add((MultiTreeNode<String>) node8);
		mSubtreesNotContain1.add((MultiTreeNode<String>) node4);
		assertFalse(messageNotContains, ((MultiTreeNode<String>) root).hasSubtrees(mSubtreesNotContain1));

		Collection<MultiTreeNode<String>> mSubtreesNotContain2 = new ArrayList<>(2);
		mSubtreesNotContain2.add((MultiTreeNode<String>) node5);
		mSubtreesNotContain2.add((MultiTreeNode<String>) anotherNode);
		assertFalse(messageNotContains, ((MultiTreeNode<String>) node3).hasSubtrees(mSubtreesNotContain2));

		assertFalse(messageNotContains, ((MultiTreeNode<String>) node1).hasSubtrees(mSubtreesContain1));
		assertFalse(messageNotContains, ((MultiTreeNode<String>) node2).hasSubtrees(null));
		assertFalse(messageNotContains,
				((MultiTreeNode<String>) node2).hasSubtrees(Collections.emptyList()));
		assertFalse(messageNotContains,
				((MultiTreeNode<String>) node2).hasSubtrees(Collections.singletonList(null)));
	}

	@Test
	public void testDropSubtrees() {
		// Test the specified tree nodes are successfully removed from the current tree node
		String messageRemoveExpected = "The specified subtree was expected to be dropped, but actually was not";
		String messageRemoveResultTrue = "The subtrees removal result was expected to be true, but actually was false";
		Collection<MultiTreeNode<String>> mSubtreesToRemove1 = new ArrayList<>(3);
		mSubtreesToRemove1.add((MultiTreeNode<String>) node3);
		mSubtreesToRemove1.add((MultiTreeNode<String>) node7);
		mSubtreesToRemove1.add((MultiTreeNode<String>) node8);
		assertTrue(messageRemoveResultTrue, ((MultiTreeNode<String>) node2).dropSubtrees(mSubtreesToRemove1));
		assertFalse(messageRemoveExpected, node2.hasSubtree(node3));
		assertFalse(messageRemoveExpected, node2.hasSubtree(node7));
		assertFalse(messageRemoveExpected, node2.hasSubtree(node8));

		Collection<MultiTreeNode<String>> mSubtreesToRemove2 = new ArrayList<>(2);
		mSubtreesToRemove2.add((MultiTreeNode<String>) node9);
		mSubtreesToRemove2.add((MultiTreeNode<String>) anotherNode);
		assertTrue(messageRemoveResultTrue, ((MultiTreeNode<String>) root).dropSubtrees(mSubtreesToRemove2));
		assertFalse(messageRemoveExpected, root.contains(node9));

		// Test the specified tree nodes are not removed from the current tree node
		String messageRemoveResultFalse =
				"The subtrees removal result was expected to be false, but actually was true";
		assertFalse(messageRemoveResultFalse,
				((MultiTreeNode<String>) node1).dropSubtrees(Collections.singletonList((MultiTreeNode<String>) node2)));
		assertFalse(messageRemoveResultFalse,
				((MultiTreeNode<String>) root).dropSubtrees(Collections.emptyList()));
		assertFalse(messageRemoveResultFalse,
				((MultiTreeNode<String>) node1).dropSubtrees(Collections.singletonList(null)));
	}

}
