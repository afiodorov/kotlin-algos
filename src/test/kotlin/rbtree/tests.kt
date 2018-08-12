package rbtree

import kotlin.test.assertEquals
import org.junit.Test

class TestSource {
    @Test fun NodeRightRotate() {
        val n1 = Node<Long>(3)
        val n2 = Node<Long>(10)
        n1.addRightNode(n2)
        assertEquals(n2, n1.right)
        assertEquals(1, 2)
    }
}