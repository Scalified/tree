# Tree Data Structure Java Library

[![Build Status](https://travis-ci.org/Scalified/tree.svg)](https://travis-ci.org/Scalified/tree)
[![Maven Central](https://img.shields.io/maven-central/v/com.scalified/tree.svg)](http://search.maven.org/#search|gav|1|g%3A%22com.scalified%22%20AND%20a%3A%22tree%22)

## Description

This Library contains different implementations of the tree data structures, such as K-ary, binary, expression trees etc.

## Requirements

The Library requires [Java SE Development Kit 7](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html#jdk-7u80-oth-JPR) or higher

## Gradle dependency

```java
dependencies {
	compile 'com.scalified:tree:0.2.5'
}
```

## Theory

### Definition

A **tree data structure** can be defined recursively (locally) as a collection of nodes (starting at a root node), 
where each node is a data structure consisting of a value, together with a list of references to nodes (the children), 
with the constraints that no reference is duplicated, and none points to the root

A tree is a (possibly non-linear) data structure made up of nodes or vertices and edges without having any cycle. 
The tree with no nodes is called the *null* or *empty* tree. A tree that is not empty consists of a root node and 
potentially many levels of additional nodes that form a hierarchy

### Terminology

**Node** - a single point of a tree
<br> **Edge** - line, which connects two distinct nodes
<br> **Root** - top node of the tree, which has no parent
<br> **Parent** - a node, other than the root, which is connected to other successor nodes
<br> **Child** - a node, other than the root, which is connected to predecessor
<br> **Leaf** - a node without children
<br> **Path** - a sequence of nodes and edges connecting a node with a descendant
<br> **Path Length** - number of nodes in the path - 1
<br> **Ancestor** - the top parent node of the path
<br> **Descendant** - the bottom child node of the path
<br> **Siblings** - nodes, which have the same parent
<br> **Subtree** - a node in a tree with all of its proper descendants, if any
<br> **Node Height** - the number of edges on the longest downward path between that node and a leaf
<br> **Tree Height** - the number of edges on the longest downward path between the root and a leaf (root height)
<br> **Depth (Level)** - the path length between the root and the current node
<br> **Ordered Tree** - tree in which nodes has the children ordered
<br> **Labeled Tree** - tree in which a label or value is associated with each node of the tree
<br> **Expression Tree** - tree which specifies the association of an expression's operands and its operators in a 
                    uniform way, regardless of whether the association is required by the placement of parentheses 
                    in the expression or by the precedence and associativity rules for the operators involved
<br> **Branching Factor** - maximum number of children a node can have
<br> **Pre order** - a form of tree traversal, where the action is called firstly on the current node, and then the 
              pre order function is called again recursively on each of the subtree from left to right
<br> **Post order** - a form of tree traversal, where the post order function is called recursively on each subtree from 
               left to right and then the action is called

### Trees Representation

#### Array-of-Pointers

One of the simplest ways of representing a tree is to use for each node a structure, which contains an array of 
pointers to the children of that node
<br>The maximum number of children a node can have is known as **branching factor (BF)**
<br>The *ith* component of the array at a node contains a pointer to the *ith* child of that node. 
A missing child can be represented by a **null** pointer

![Array-of-Pointers Representation](https://github.com/Scalified/tree/blob/master/assets/array_of_pointers_representation.png)

**Array-of-Pointers** representation makes it possible to quickly access the *ith* child of any node
<br>This representation, however, is very wasteful of space when only a few nodes in the tree have many children. 
In this case, most of the pointers in the arrays will be **null**

> The Library, however, is optimized for using Array-of-Pointers representation likewise it is done in the 
**java.util.ArrayList**. The subtrees size grows automatically in case of reaching the limit. Additionally you may
specify what is called a *branching factor*, that the initial number of subtrees before grow (like *capacity* in 
the **java.util.ArrayList**)

#### Leftmost-Child—Right-Sibling

In the **Leftmost-Child—Right-Sibling** representation each node contains a pointer to its leftmost child; a node does 
not have pointers to any of its other children. In order to locate the second and subsequent children of a node *n*, 
the children link list created, in which each child *c* points to the child of *n* immediately to the right of *c*.
That node is called the right sibling of *c*

![Leftmost-Child-Right-Sibling Representation](https://github.com/Scalified/tree/blob/master/assets/leftmost_child_right_sibling_representation.png)

In the figure above *n3* is the right sibling of *n2*, *n4* is the right sibling of *n3*, and *n4* has no right sibling. 
The children of *n1* can be found by following its leftmost-child pointer to *n2*, then the right-sibling pointer to 
*n3*, and then the right-sibling pointer of *n3* to *n4*. *n4*, in turn has no right-sibling pointer (it is **null** 
indeed), therefore *n1* has no more children
<br>The figure above can be represented in such a way:

![Leftmost-Child-Right-Sibling Another Representation](https://github.com/Scalified/tree/blob/master/assets/leftmost_child_right_sibling_another_representation.png)

The downward arrows are the leftmost-child links; the sideways arrows are the right-sibling links

### Recursions on Trees

![Recursion on Trees](https://github.com/Scalified/tree/blob/master/assets/recursion_on_trees.png)

#### Traversal

**Traversal** is a process of visiting each node in a tree data structure, exactly once, in a systematic way

**Pre order** is a form of tree traversal, where the action is called firstly on the current node, and then the 
pre order function is called again recursively on each subtree from left to right
<br>Pre order listing on the figure is: **+ a − b c d**

**Post order** is a form of tree traversal, where the post order function is called recursively on each subtree 
from left to right and then the action is called
<br>Post order listing on the figure is: **a b c − d * +**

#### Evaluating Expression Tree

There are three types of expressions: **infix**, **prefix** and **postfix**

**Infix** are expressions in the ordinary notation, where binary operators appear between their operands. For the 
figure above the *infix* expression is: **a + (b − c) * d**. The equivalent *prefix* and *postfix* expressions are: 
**+ a − b c d** for *prefix* and **a b c − d * +** for *postfix*
<br>Having either the *prefix* or *postfix* expression the *infix* expression can be constructed in a simple way. 
For instance, in order to construct the *infix* expression from prefix one, firstly the operator that is followed by 
the required number of operands with no embedded operators must be found
<br>In prefix expression **+ a * − b c d** the **− b c** is such a string, since the minus sign, like all operators 
in running example takes two operands. This subexpression is then replaced by a new symbol, say **x = − b c**. Then, 
this process of identifying an operator followed by its operands repeated again. Now the expression is: **+ a * x d**. 
The next subexpression will be **y = * x d**. This reduces the target expression to **+ a y**, which is lastly 
converted to just **a + y**
<br>The remainder of the infix expression **a + y** then is reconstructed by retracing the steps above. Firstly, 
the *y* is substituted to **x * d**, changing the target expression to: **a + (x * d)**. And lastly the *x* is replaced 
by **− b c** or simply **b − c**. So the target expression now is: **a + ((b − c) * d)** or **a + (b − c) * d**
<br>The conversion from *postfix* to *infix* expression uses the same algorithm. The only difference is that firstly 
the operator, which is preceded by the requisite number of operands must be found in order to decompose the *postfix* 
expression

#### Structural Induction

**Structural Induction** is a proof method that is used to prove some statement *S(T)* is true for all trees *T*

![Structural Induction](https://github.com/Scalified/tree/blob/master/assets/structural_induction.png)

For the basis *S(T)* must be shown to be true when *T* consists of a single node. For the induction, *T* is supposed 
to be a tree with root *r* and children *c1*, *c2*, …, *ck*, for some *k ≥ 1*. If *T1*, *T2*, …, *Tk* are the subtrees 
of *T* whose roots are *c1*, *c2*, …, *ck* respectively, then the inductive step is to assume that *S(T)* is true 
for all trees *T*. Structural Induction does not make reference to the exact number of nodes in a tree, except to 
distinguish the basis (one node) from  the inductive step (more than one node)

The general template for Structural Induction has the following outline:

1. Specify the statement *S(T)* to be proved, where *T* is a tree
2. Prove the basis, that *S(T)* is true whenever *T* is a tree with a single node
3. Set up the inductive step by letting *T* be a tree with root *r* and *k ≥ 1* subtrees, *T1*, *T2*, …, *Tk*. 
State that the inductive hypothesis assumed: *S(Ti)* is true for each of the subtrees *Ti*, *i = 1*, *2*, …, *k*
4. Prove that *S(T)* is true under the assumptions mentioned in step 3

Structural Induction is generally needed to prove the correctness of a recursive program that acts on trees

As an example of using the Structural Induction to prove the correctness of statement the  evaluate function 
(listed above) can be used. This function is applied to a tree *T* by given a pointer to the root of *T* as a 
value of its argument *n*. It then computes the value of the expression represented by *T*
<br>The following statement must be proved:
<br>**S(T)**: The value returned by evaluate function when called on the root of *T* equals the value of the arithmetic 
expression represented by *T*
<br>For the basis, *T* consists of a single node. That is, the argument *n* is a (pointer to a) leaf. Since the 
operator field has the value *‘i’* when the node represents and operand, the function succeeds

![Structural Induction Nodes](https://github.com/Scalified/tree/blob/master/assets/structural_induction_nodes.png)

If the node *n* is not a (pointer to a) leaf, the inductive hypothesis is that *S(T’)* is true for each tree *T’* 
rooted at one of the children of *n*. *S(T)* then must be proved for the tree *T* rooted at *n*
<br>Since operators are assumed to be binary, *n* has two subtrees. By the inductive hypothesis, the values of 
*value_1* and *value_2* are the values of the left and right subtrees. In the figure, *value_1* holds the value of 
*T1* and *value_2* holds the value of *T2*
<br>Whatever operator appears at the root, *n* is applied to the two values: *value_1* and *value_2*. For example, 
if the root holds, *+*, then the value returned is *value_1 + value_2*, as it should be for an expression that is 
the sum of the expressions of trees *T1* and *T2*
<br>Now, *S(T)* holds for all expression trees *T*, and, therefore, the function evaluate correctly evaluates trees 
that represent expressions


## Usage

[**TreeNode**](https://github.com/Scalified/tree/blob/master/tree/src/main/java/com/scalified/tree/TreeNode.java) - 
is the top interface, which represents the basic tree data structures. It describes the basic methods, which are 
implemented by all the trees

### Creation

Suppose you need to create the following tree with **String** data using array-of-pointer representation:

![K-ary tree](https://github.com/Scalified/tree/blob/master/assets/k_ary_tree.png)

The following code snippet shows how to build such tree: 

```java
// Creating the tree nodes
TreeNode<String> n1 = new ArrayMultiTreeNode<>("n1");
TreeNode<String> n2 = new ArrayMultiTreeNode<>("n2");
TreeNode<String> n3 = new ArrayMultiTreeNode<>("n3");
TreeNode<String> n4 = new ArrayMultiTreeNode<>("n4");
TreeNode<String> n5 = new ArrayMultiTreeNode<>("n5");
TreeNode<String> n6 = new ArrayMultiTreeNode<>("n6");
TreeNode<String> n7 = new ArrayMultiTreeNode<>("n7");

// Assigning tree nodes
n1.add(n2);
n1.add(n3);
n1.add(n4);
n2.add(n5);
n2.add(n6);
n4.add(n7);
```

### Storing And Retrieving Data

Data is stored in a tree node during creation:

```java
// Storing string data during creation
TreeNode<String> node = new ArrayMultiTreeNode<>("data");

// Null values can also be stored:
TreeNode<String> nullNode = new ArrayMultiTreeNode<>(null);
```

Data can be changed after node creation:

```java
// Setting the data
node.setData("data");

// Getting the data
String data = node.data();
```

### Identifying the Root

```java
// Checking whether the current node is root
boolean result = node.isRoot();

// Getting the reference to the root node
TreeNode<String> root = node.root();
```

> NOTE: Calling *root()* on the root node returns this root node itself

### Identifying the Parent

```java
// Getting the reference to the parent node
TreeNode<String> parent = node.parent();
```
> NOTE: If the current *node* is root, calling *node.parent()* returns *null*

### Subtrees

```java
// Retrieving subtrees as a new collection
Collection<? extends TreeNode<String>> subtrees = node.subtrees();

// Checking whether the specified subtree is present
TreeNode<String> subtreeToCheck = new ArrayMultiTreeNode<>("subtreeToCheck");
boolean resultHasSubtree = node.hasSubtree(subtreeToCheck);

// Removing the entire subtree with all of its descendants from node
TreeNode<String> subtreeToDrop = new ArrayMultiTreeNode<>("subtreeToDrop");
boolean resultDropSubtree = node.dropSubtree(subtreeToDrop);
```

### Checking For Presence

```java
// Checking whether the tree node contains the specified node
TreeNode<String> nodeToCheck = new ArrayMultiTreeNode<>("nodeToCheck");
boolean resultContains = node.contains(nodeToCheck);

// Checking whether the tree node contains all of the collection's nodes
Collection<TreeNode<String>> collectionToCheck;
// ... collection initialization skipped
boolean resultContainsAll = node.containsAll(collectionToCheck);
```

### Finding nodes

```java
// Finding the first occurence of the tree node within the tree with the specified data
TreeNode<String> nodeToFind = node.find("dataToFind");

// Finding the collection of all tree nodes within the tree with the specified data
Collection<TreeNode<String>> nodesToFind = node.findAll("dataToFind");
```

### Modification

```java
// Adding new subtree / tree node
TreeNode<String> nodeToAdd = new ArrayMultiTreeNode<>("nodeToAdd");
node.add(nodeToAdd);

// Removing the tree node from entire tree
TreeNode<String> nodeToRemove = new ArrayMultiTreeNode<>("nodeToRemove");
boolean resultRemove = node.remove(nodeToRemove);

// Removing all of the tree nodes specified in the collection from the entire tree
Collection<TreeNode<String>> collectionToRemove;
// ... collection initialization skipped
boolean resultRemoveAll = node.removeAll(collectionToRemove);

// Removing all of the subtrees with all of theirs descendants from the entire tree
node.clear();
```

### Traversal

[TraversalAction](https://github.com/Scalified/tree/blob/master/tree/src/main/java/com/scalified/tree/TraversalAction.java)
allows to define an action, which has a single method **perform(TreeNode<T>)**. This method is called during traversal
on each node visited

```java
// Creating traversal action
TraversalAction<TreeNode<String>> action = new TraversalAction<TreeNode<String>>() {
	@Override
	public void perform(TreeNode<String> node) {
		System.out.println(node.data()); // any other action goes here
	}
	
	@Override
	public boolean isCompleted() {
	    return false; // return true in order to stop traversing
	}
};

// Traversing pre order
node.traversePreOrder(action);

// Traversing post order
node.traversePostOrder(action);
```

### Iteration

```java
// Iterating over the tree elements using foreach
for (TreeNode<String> node : root) {
	System.out.println(node.data()); // any other action goes here
}

// Iterating over the tree elements using Iterator
Iterator<TreeNode<String>> iterator = root.iterator();
while (iterator.hasNext()) {
	TreeNode<String> node = iterator.next();
}
```

> NOTE: Iteration starts from the root node and goes in a pre ordered manner
> <br>Iterator support removing by calling **remove()** while iterating
> <br>Removing any tree node while iterating using foreach throws **ConcurrentModificationException**

### Converting To Collection

```java
// Retrieving all the tree nodes as a pre ordered collection of elements
Collection<? extends TreeNode<String>> preOrderedCollection = node.preOrdered();

// Retrieving all the tree nodes as a post ordered collection of elements
Collection<? extends TreeNode<String>> postOrderedCollection = node.postOrdered();
```

> NOTE: Any modification of the retrieved collection won't cause the correspondent modification in the tree

### Additional Useful Methods

```java
// Checking whether the current node is leaf
boolean nodeIsLeaf = node.isLeaf();

// Retrieving the collection of nodes, which connect the current node with its descendants
TreeNode<String> descendant = new ArrayMultiTreeNode<>("descendant");
Collection<? extends TreeNode<String>> path = node.path(descendant);

// Getting the common ancestor
TreeNode<String> anotherNode = new ArrayMultiTreeNode<>("anotherNode");
TreeNode<String> commonAncestor = node.commonAncestor(anotherNode);

// Checking whether the node is the sibling of the current node
TreeNode<String> sibling = new ArrayMultiTreeNode<>("sibling");
boolean nodeIsSibling = node.isSiblingOf(sibling);

// Checking whether the current node is ancestor of another node
TreeNode<String> anotherDescendantNode = new ArrayMultiTreeNode<>("anotherDescendantNode");
boolean nodeIsAncestor = node.isAncestorOf(anotherDescendantNode);

// Checking whether the current node is descendant of another node
TreeNode<String anotherAncestorNode = new ArrayMultiTreeNode<>("anotherAncestorNode");
boolean nodeIsDescendant = node.isDescendantOf(anotherAncestorNode);

// Getting the size of entire tree including the current tree node
int size = node.size();

// Getting the level of the current tree node within the whole tree
int level = node.level();

// Getting the height of the current tree node
int height = node.height();
```

### K-ary (multinode) Trees

[**MultiTreeNode**](https://github.com/Scalified/tree/blob/master/tree/src/main/java/com/scalified/tree/multinode/MultiTreeNode.java) - 
interface, which adds additional methods for multi tree node (K-ary) tree data structures:

```java
// Getting the siblings of the current tree node
Collection<? extends MultiTreeNode<String>> siblings = node.siblings();

// Checking whether the current tree node has all of the subtrees specified in the collection
Collection<? extends MultiTreeNode<String>> collectionToCheck;
// ... collection initialization skipped
boolean resultHasSubtrees = node.hasSubtrees(collectionToCheck);

// Adding all of the subtrees from the collection to the current tree node
Collection<? extends MultiTreeNode<String>> collectionToAdd;
// ... collection initialization skipped
boolean resultAddSubtrees = node.addSubtrees(collectionToAdd);

// Removing all of the subtrees specified in the collection from the current tree node
Collection<? extends MultiTreeNode<String>> collectionToRemove;
// ... collection initialization skipped
boolean resultRemoveSubtrees = node.removeSubtrees(collectionToRemove);
```

K-ary (multi node) trees are represented by **MultiTreeNode** interface and have 2 implementations:

* [**ArrayMultiTreeNode**](https://github.com/Scalified/tree/blob/master/tree/src/main/java/com/scalified/tree/multinode/ArrayMultiTreeNode.java) - 
implementation based on the array-of-pointers representation
* [**LinkedMultiTreeNode**](https://github.com/Scalified/tree/blob/master/tree/src/main/java/com/scalified/tree/multinode/LinkedMultiTreeNode.java) -
implementation based on the leftmost-child-right-sibling representation

**LinkedMultiTreeNode** is better optimized for trees with many nodes, whereas **ArrayMultiTreeNode** is better for large node trees

## License

```
  Copyright 2016 Scalified <http://www.scalified.com>
 
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
```

## Scalified Links

* [Scalified](http://www.scalified.com)
* [Scalified Official Facebook Page](https://www.facebook.com/scalified)
* <a href="mailto:info@scalified.com?subject=[Tree]: Proposals And Suggestions">Scalified Support</a>
