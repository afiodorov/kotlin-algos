package rbtree

import kotlin.test.assertSame
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
}