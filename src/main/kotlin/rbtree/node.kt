package rbtree

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

    fun unbalancedInsert(value: T) {
        var curNode: Node<T>? = this.root
        var curParent: Node<T> = this.root
        while (curNode != null) {
            curParent = curNode
            curNode = if (value < curNode.value) curNode.left else curNode.right
        }
        curParent.addChildNode(Node(value, color = Color.RED))
    }

    fun breadthFirstTraversal(function: (T?) -> Unit) {
        val dequeue = java.util.LinkedList<Node<T>?>(listOf(root))
        while(dequeue.isNotEmpty()) {
            val el = dequeue.poll()
            function(el?.value)
            if(el !== null) {
                dequeue.add(el.left)
                dequeue.add(el.right)
            }
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        breadthFirstTraversal {
            sb.append(it.toString())
            sb.append(' ')
        }
        return sb.trim().toString()
    }
}

fun <T : Comparable<T>> rbInsertFixup(root: Node<T>, newNode: Node<T>) {
    var newNode = newNode
    var parent = newNode.parent!! // newNode always has a parent
    while (parent.color == Color.RED) {
        // parent is not root since root is black, thus it has a parent
        val uncle = parent.uncle()
        if (uncle?.color == Color.RED) {
            parent.color = Color.BLACK
            uncle.color = Color.BLACK

            newNode = parent.parent!!
            parent = newNode.parent!!
            continue
        }

        if (parent.isLeftChild()) {
            if (newNode.isRightChild()) {
                newNode = newNode.parent!!
                // newNode.leftRotate()
            }

            newNode.parent!!.color = Color.BLACK
            newNode.parent!!.parent!!.color = Color.RED
            // newNode.parent!!.parent!!.rightRotate()
        } else if (parent.isRightChild()) {

        } else {
            throw RuntimeException("Should be unreachable by the algorithm.")
        }
    }

    root.color = Color.BLACK
}

fun main(args: Array<String>) {
    val tree = Tree<Long>(3)
    tree.unbalancedInsert(10)
    tree.unbalancedInsert(2)
    tree.unbalancedInsert(12)
    println(tree.root)
    println(tree.root.right)
}