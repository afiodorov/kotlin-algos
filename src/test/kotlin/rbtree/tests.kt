package rbtree

import kotlin.test.assertSame
import org.junit.Test
import kotlin.test.assertEquals



class TestNode {
    @Test fun NodeRightRotate() {
        val parent = Node<Long>(0)
        val y = Node<Long>(3)
        parent.addLeftNode(y)
        val x = Node<Long>(10)
        val alpha = Node<Long>(20)
        val beta = Node<Long>(30)
        val gamma = Node<Long>(15)
        x.addLeftNode(alpha)
        x.addRightNode(beta)
        y.addLeftNode(x)
        y.addRightNode(gamma)
        y.rightRotate()

        assertSame(alpha.left, null)
        assertSame(alpha.right, null)
        assertSame(beta.left, null)
        assertSame(beta.right, null)
        assertSame(gamma.left, null)
        assertSame(gamma.right, null)

        assertSame(parent.left, x)
        assertSame(x.parent, parent)
        assertSame(x.left, alpha)
        assertSame(alpha.parent, x)
        assertSame(x.right, y)
        assertSame(y.parent, x)
        assertSame(y.left, beta)
        assertSame(beta.parent, y)
        assertSame(y.right, gamma)
        assertSame(gamma.parent, y)
    }

    @Test fun NodeRightRotateRoot() {
        val y = Node<Long>(3)
        val x = Node<Long>(10)
        val alpha = Node<Long>(20)
        val beta = Node<Long>(30)
        val gamma = Node<Long>(15)
        x.addLeftNode(alpha)
        x.addRightNode(beta)
        y.addLeftNode(x)
        y.addRightNode(gamma)
        y.rightRotate()

        assertSame(alpha.left, null)
        assertSame(alpha.right, null)
        assertSame(beta.left, null)
        assertSame(beta.right, null)
        assertSame(gamma.left, null)
        assertSame(gamma.right, null)

        assertSame(x.parent, null)
        assertSame(x.left, alpha)
        assertSame(alpha.parent, x)
        assertSame(x.right, y)
        assertSame(y.parent, x)
        assertSame(y.left, beta)
        assertSame(beta.parent, y)
        assertSame(y.right, gamma)
        assertSame(gamma.parent, y)
    }

    @Test fun NodeRightRotateNoChild() {
        val parent = Node<Long>(0)
        val y = Node<Long>(3)
        parent.addLeftNode(y)
        val x = Node<Long>(10)
        val alpha = Node<Long>(20)
        val gamma = Node<Long>(15)
        x.addLeftNode(alpha)
        y.addLeftNode(x)
        y.addRightNode(gamma)
        y.rightRotate()

        assertSame(alpha.left, null)
        assertSame(alpha.right, null)
        assertSame(gamma.left, null)
        assertSame(gamma.right, null)

        assertSame(parent.left, x)
        assertSame(x.parent, parent)
        assertSame(x.left, alpha)
        assertSame(alpha.parent, x)
        assertSame(x.right, y)
        assertSame(y.parent, x)
        assertSame(y.left, null)
        assertSame(y.right, gamma)
        assertSame(gamma.parent, y)
    }

    @Test fun NodeLeftRotate() {
        val parent = Node<Long>(0)
        val x = Node<Long>(10)
        parent.addRightNode(x)
        val y = Node<Long>(3)
        val alpha = Node<Long>(20)
        val beta = Node<Long>(30)
        val gamma = Node<Long>(15)
        x.addLeftNode(alpha)
        x.addRightNode(y)
        y.addLeftNode(beta)
        y.addRightNode(gamma)
        x.leftRotate()

        assertSame(alpha.left, null)
        assertSame(alpha.right, null)
        assertSame(beta.left, null)
        assertSame(beta.right, null)
        assertSame(gamma.left, null)
        assertSame(gamma.right, null)

        assertSame(parent.right, y)
        assertSame(y.parent, parent)
        assertSame(x.left, alpha)
        assertSame(alpha.parent, x)
        assertSame(x.right, beta)
        assertSame(beta.parent, x)
        assertSame(y.right, gamma)
        assertSame(gamma.parent, y)
        assertSame(y.left, x)
        assertSame(x.parent, y)
    }

    @Test fun NodeLeftRotateLeftParent() {
        val parent = Node<Long>(0)
        val x = Node<Long>(10)
        parent.addLeftNode(x)
        val y = Node<Long>(3)
        val alpha = Node<Long>(20)
        val beta = Node<Long>(30)
        val gamma = Node<Long>(15)
        x.addLeftNode(alpha)
        x.addRightNode(y)
        y.addLeftNode(beta)
        y.addRightNode(gamma)
        x.leftRotate()

        assertSame(alpha.left, null)
        assertSame(alpha.right, null)
        assertSame(beta.left, null)
        assertSame(beta.right, null)
        assertSame(gamma.left, null)
        assertSame(gamma.right, null)

        assertSame(parent.left, y)
        assertSame(parent.right, null)
        assertSame(y.parent, parent)
        assertSame(x.left, alpha)
        assertSame(alpha.parent, x)
        assertSame(x.right, beta)
        assertSame(beta.parent, x)
        assertSame(y.right, gamma)
        assertSame(gamma.parent, y)
        assertSame(y.left, x)
        assertSame(x.parent, y)
    }
}

class TestTree {
    @Test fun toStringWithNulls() {
        val tree = Tree<Long>(5)
        assertEquals("5 null null", tree.toString())
        tree.root.addRightNode(Node<Long>(10))
        assertEquals("5 null 10 null null", tree.toString())
        tree.root.addLeftNode(Node<Long>(11))
        assertEquals("5 11 10 null null null null", tree.toString())
        tree.root.left?.addRightNode(Node<Long>(15))
        tree.root.right?.addLeftNode(Node<Long>(16))
        assertEquals("5 11 10 null 15 16 null null null null null", tree.toString())
    }

    @Test fun unbalancedInsert() {
        val tree = Tree<Long>(5)
        tree.unbalancedInsert(10)
        tree.unbalancedInsert(2)
        tree.unbalancedInsert(12)
        tree.unbalancedInsert(9)
        tree.unbalancedInsert(1)
        tree.unbalancedInsert(0)
        assertEquals("5 2 10 1 null 9 12 0 null null null null null null null", tree.toString())
    }

    @Test fun unbalancedInsertSorted() {
        val tree = Tree<Long>(10)
        tree.unbalancedInsert(9)
        tree.unbalancedInsert(8)
        tree.unbalancedInsert(7)
        tree.unbalancedInsert(6)
        assertEquals("10 9 null 8 null 7 null 6 null null null", tree.toString())
    }

    @Test fun isValidRedBlackTreeRedRoot() {
        val tree = Tree<Long>(0)
        tree.root.color = Color.RED

        val (isValid, errMesage) = tree.isValidRedBlackTree()
        assertEquals(isValid, false)
        assertEquals(errMesage, "Root node should be black.")
    }

    @Test fun isValidRedBlackTree2Reds() {
        val tree = Tree<Long>(0)
        tree.root.addRightNode(Node<Long>(10, color=Color.RED))
        tree.root.right!!.addRightNode(Node<Long>(11, color=Color.RED))

        val (isValid, errMesage) = tree.isValidRedBlackTree()
        assertEquals(isValid, false)
        assertEquals(errMesage, "2 right children: ${tree.root.right}")
    }
}
