package rbtree

import java.util.LinkedList
import java.util.IdentityHashMap

enum class Color {
    BLACK,
    RED
}

class Node<T : Comparable<T>>(val value: T,
                              var color: Color = Color.BLACK) : Comparable<Node<T>> {
    var left: Node<T>? = null
    var right: Node<T>? = null
    var parent: Node<T>? = null
    val isRoot: Boolean
        get() = this.parent == null

    override fun toString(): String {
        val right = right.toStringEvenIfNull()
        val left = left.toStringEvenIfNull()
        val parent = parent.toStringEvenIfNull()
        val main = toStringEvenIfNull()
        return "$main.${if (!isRoot) " Parent: $parent." else ""} Left: $left. Right: $right."
    }

    override operator fun compareTo(other: Node<T>): Int = value.compareTo(other.value)

    fun T.compareTo(): Int = value.compareTo(this)

    fun addLeftNode(node: Node<T>) {
        left = node
        node.parent = this
    }

    fun addRightNode(node: Node<T>) {
        right = node
        node.parent = this
    }

    fun addChildNode(node: Node<T>) {
        if (node < this) {
            addLeftNode(node)
            return
        }
        addRightNode(node)
    }

    fun isLeftChild() = this === this.parent?.left
    fun isRightChild() = this === this.parent?.right

    fun uncle(): Node<T>? {
        if (isLeftChild()) return parent?.right
        if (isRightChild()) return parent?.left
        return null
    }

    fun leftRotate() {
        val child = right!!
        val childLeft = child.left
        right = childLeft
        if (childLeft !== null) childLeft.parent = this
        if (isLeftChild()) parent?.left = child
        if (isRightChild()) parent?.right = child
        child.parent = parent
        child.left = this
        parent = child
    }

    fun rightRotate() {
        val child = left!!
        val childRight = child.right
        left = childRight
        if (childRight !== null) childRight.parent = this
        if (isLeftChild()) parent?.left = child
        if (isRightChild()) parent?.right = child
        child.parent = parent
        child.right = this
        parent = child
    }
}

fun <T : Comparable<T>> Node<T>?.toStringEvenIfNull(): String {
    if (this == null) return "Leaf"
    val prefix = if (color == Color.BLACK) "B" else "R"
    val nodeName = if (isRoot) "Root" else "Node"
    return "${prefix}${nodeName}($value)"
}

class Tree<T : Comparable<T>>(val rootValue: T) {
    var root = Node<T>(rootValue)

    fun breadthFirstTraversal(function: (T?) -> Unit) {
        val dequeue = LinkedList<Node<T>?>(listOf(root))
        while (dequeue.isNotEmpty()) {
            val el = dequeue.poll()
            function(el?.value)
            if (el !== null) {
                dequeue.add(el.left)
                dequeue.add(el.right)
            }
        }
    }

    fun breadthFirstNodeTraversal(function: (Node<T>?) -> Unit) {
        val dequeue = LinkedList<Node<T>?>(listOf(root))
        while (dequeue.isNotEmpty()) {
            val el = dequeue.poll()
            function(el)
            if (el !== null) {
                dequeue.add(el.left)
                dequeue.add(el.right)
            }
        }
    }

    fun isValidRedBlackTree(): Pair<Boolean, String> {
        if (root.color == Color.RED) {
            return Pair(false, "Root node should be black.")
        }

        var msg = ""
        breadthFirstNodeTraversal {
            if (msg != "") return@breadthFirstNodeTraversal
            if (it !== null && it.color == Color.RED) {
                val left = it.left
                if (left !== null && left.color == Color.RED) {
                    msg = "2 red children: $it"
                    return@breadthFirstNodeTraversal
                }
                val right = it.right
                if (right !== null && right.color == Color.RED) {
                    msg = "2 red children: $it"
                    return@breadthFirstNodeTraversal
                }
            }
        }
        if (msg != "") return Pair(false, msg)

        var sameLeafBlackHeight : Long = -1
        val blackHeightMap = IdentityHashMap<Node<T>?, Long>()
        blackHeightMap[null] = 0
        breadthFirstNodeTraversal {
            if (msg != "") return@breadthFirstNodeTraversal
            if (it === null) return@breadthFirstNodeTraversal
            val increment = if (it.color == Color.BLACK) 1 else 0
            val blackHeight = blackHeightMap[it.parent]!! + increment
            blackHeightMap[it] = blackHeight

            if ((it.left === null) && (it.right == null)) {
                if (sameLeafBlackHeight == -1.toLong()) {
                    sameLeafBlackHeight = blackHeight
                } else if (sameLeafBlackHeight != blackHeight) {
                    msg = "Expected black height: $sameLeafBlackHeight. Actual: $blackHeight. Node: $it"
                }
            }
        }
        if (msg != "") return Pair(false, msg)

        return Pair(true, "")
    }

    override fun toString(): String {
        val sb = StringBuilder()
        breadthFirstTraversal {
            sb.append(it.toString())
            sb.append(' ')
        }
        return sb.trim().toString()
    }

    fun unbalancedInsert(value: T) : Node<T> {
        var curNode: Node<T>? = this.root
        var curParent: Node<T> = this.root
        while (curNode != null) {
            curParent = curNode
            curNode = if (value < curNode.value) curNode.left else curNode.right
        }
        val newNode = Node(value, color = Color.RED)
        curParent.addChildNode(newNode)
        return newNode
    }

    fun redBlackInsertFixup(newNode: Node<T>) {
        var nodeInserted = newNode
        // newNode must have a parent as it is not root
        var parent = nodeInserted.parent!!
        while (parent.color == Color.RED) {
            val uncle = parent.uncle()
            if (uncle?.color == Color.RED) {
                parent.color = Color.BLACK
                uncle.color = Color.BLACK

                // because the tree is invalid Red-Black tree it means
                // that there are 2 red nodes, thus the parent must have a parent
                nodeInserted = parent.parent!!
                nodeInserted.color = Color.RED
                if (nodeInserted === root) {
                    break
                }
                parent = nodeInserted.parent!!
                continue
            }

            if (parent.isLeftChild()) {
                if (nodeInserted.isRightChild()) {
                    nodeInserted = nodeInserted.parent!! // not root as it is red
                    nodeInserted.leftRotate()
                    parent = nodeInserted.parent!!
                }

                parent.color = Color.BLACK
                parent.parent!!.color = Color.RED
                val toRightRotate = parent.parent!!
                if (toRightRotate == root) {
                    root = toRightRotate.left!!
                }
                toRightRotate.rightRotate()
            } else if (parent.isRightChild()) {
                if (nodeInserted.isLeftChild()) {
                    nodeInserted = nodeInserted.parent!! // not root as it is red
                    nodeInserted.rightRotate()
                    parent = nodeInserted.parent!!
                }

                parent.color = Color.BLACK
                parent.parent!!.color = Color.RED
                val toLeftRotate = parent.parent!!
                if (toLeftRotate == root) {
                    root = toLeftRotate.right!!
                }
                toLeftRotate.leftRotate()
            } else {
                throw RuntimeException("Should be unreachable by the algorithm.")
            }
        }

        root.color = Color.BLACK
    }

    fun balancedInsert(value: T) : Node<T> {
        val node = unbalancedInsert(value)
        redBlackInsertFixup(node)
        return node
    }
}


fun main(args: Array<String>) {
    val tree = Tree<Long>(3)
    tree.unbalancedInsert(10)
    tree.unbalancedInsert(2)
    tree.unbalancedInsert(12)
    println(tree.root)
    println(tree.root.right)
}