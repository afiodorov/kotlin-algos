package rbtree

import kotlin.test.assertEquals
import org.junit.Test

class TestSource {
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

        assertEquals(parent.left, x)
        assertEquals(x.parent, parent)
        assertEquals(x.left, alpha)
        assertEquals(alpha.parent, x)
        assertEquals(x.right, y)
        assertEquals(y.parent, x)
        assertEquals(y.left, beta)
        assertEquals(beta.parent, y)
        assertEquals(y.right, gamma)
        assertEquals(gamma.parent, y)
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

        assertEquals(x.parent, null)
        assertEquals(x.left, alpha)
        assertEquals(alpha.parent, x)
        assertEquals(x.right, y)
        assertEquals(y.parent, x)
        assertEquals(y.left, beta)
        assertEquals(beta.parent, y)
        assertEquals(y.right, gamma)
        assertEquals(gamma.parent, y)
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

        assertEquals(parent.left, x)
        assertEquals(x.parent, parent)
        assertEquals(x.left, alpha)
        assertEquals(alpha.parent, x)
        assertEquals(x.right, y)
        assertEquals(y.parent, x)
        assertEquals(y.left, null)
        assertEquals(y.right, gamma)
        assertEquals(gamma.parent, y)
    }
}