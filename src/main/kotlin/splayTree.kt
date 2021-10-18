class Node(_key: Long, _value: String) {
    val key: Long = _key
    var value: String = _value
    var parent: Node? = null
    var leftChild: Node? = null
    var rightChild: Node? = null
}

class SplayTree {
    var root: Node? = null

    private fun left_zig (node: Node, parent: Node) {
        /*
                parent                              node
               /     \                             /     \
             node     c         ------>           a     parent
            /    \                                     /      \
           a      b                                   b        c

         */
        var b = node.rightChild
        node.rightChild = parent
        parent!!.parent = node
        parent!!.leftChild = b
        b?.parent = parent
        node.parent = null
        root = node
    }
    private fun right_zig (node: Node, parent: Node) {
        /*
               parent                                    node
              /      \                                 /      \
             a       node       ------>             parent     c
                    /    \                         /      \
                   b      c                       a        b
         */
        var b = node.leftChild
        node.leftChild = parent
        parent.parent = node
        parent.rightChild = b
        b?.parent = parent
        node.parent = null
        root = node
    }
    private fun left_zig_zig (node: Node, parent: Node, gParent : Node) {
        /*
                   gParent                         node
                   /      \                       /    \
                parent     d                     a     parent
               /     \                                 /     \
             node     c              ------>          b      gParent
            /    \                                          /      \
           a      b                                        c        d

         */
        var b = node.rightChild
        var c = parent.rightChild
        node.rightChild = parent
        parent.parent = node
        node.parent = gParent!!.parent
        parent.rightChild = gParent
        gParent.parent = parent
        parent.leftChild = b
        b?.parent = parent
        gParent.leftChild = c
        c?.parent = parent
        if (gParent == root) {
            root = node
        }
    }
    private fun right_zig_zig (node : Node, parent: Node, gParent: Node) {
        /*
           gParent                                     node
           /      \                                  /     \
          a      parent                           parent    d
                /      \        ------>          /     \
               b       node                  gParent    c
                      /    \                 /     \
                     c      d               a       b
         */
        var b = parent.leftChild
        var c = node.leftChild
        node.parent = gParent!!.parent
        parent.leftChild = gParent
        gParent.parent = parent
        gParent.rightChild = b
        b?.parent = gParent
        parent.rightChild = c
        c?.parent = parent
        node.leftChild = parent
        parent.parent = node
        if (gParent == root) {
            root = node
        }
    }
    private fun left_zig_zag (node : Node, parent: Node, gParent: Node) {
        /*
                  gParent                                    node
                  /      \                                 /      \
               parent     d                            parent     gParent
              /     \               ------->           /    \      /    \
             a      node                              a      b    c      d
                   /    \
                  b      c
        */
        var b = node.leftChild
        var c = node.rightChild
        node.parent = gParent?.parent
        node.leftChild = parent
        parent.parent = node
        node.rightChild = gParent
        gParent!!.parent = node
        parent.rightChild = b
        b?.parent = parent
        gParent.leftChild = c
        c?.parent = gParent
        if (gParent == root) {
            root = node
        }
    }
    private fun right_zig_zag (node: Node, parent: Node, gParent: Node) {
        /*
            gParent                                  node
            /     \                                /      \
           a     parent                       gParent     parent
                 /    \       ------->        /     \     /    \
               node    d                     a       b   c      d
              /    \
             b      c
         */
        var b = node.leftChild
        var c = node.rightChild
        node.parent = gParent?.parent
        node.leftChild = gParent
        gParent!!.parent = node.parent
        gParent!!.rightChild = b
        b?.parent = gParent
        node.rightChild = parent
        parent.parent = node
        parent.leftChild = c
        c?.parent = parent
        if (gParent == root) {
            root = node
        }
    }
    private fun splay(node: Node) {
        while (node != root) {
            var parent = node.parent
            if (parent == root) {
                if (node == parent!!.leftChild) {
                    left_zig(node, parent)
                } else {
                    right_zig(node, parent)
                }
            } else {
                var gParent = parent!!.parent!!
                if (node == parent.leftChild && parent == gParent.leftChild) {
                    left_zig_zig(node, parent, gParent)
                } else if (node == parent.rightChild && parent == gParent.rightChild){
                    right_zig_zig(node, parent, gParent)
                } else if (node == parent.rightChild && parent == gParent.leftChild) {
                    left_zig_zag(node, parent, gParent)
                } else if (node == parent.leftChild && parent == gParent.rightChild) {
                    right_zig_zag(node, parent, gParent)
                }
            }
        }
    }

    fun add(key: Long, value: String) {
        if (root == null) {
            root = Node(key, value)
        } else {
            var comparedNode = root!!
            while (true) {
                if (key >= comparedNode.key) {
                    if (comparedNode.rightChild != null) {
                        comparedNode = comparedNode.rightChild!!
                        continue
                    } else {
                        comparedNode.rightChild = Node(key, value)
                        comparedNode.rightChild!!.parent = comparedNode
                        splay(comparedNode.rightChild!!)
                        break
                    }
                } else {
                    if (comparedNode.leftChild != null) {
                        comparedNode = comparedNode.leftChild!!
                        continue
                    } else {
                        comparedNode.leftChild = Node(key, value)
                        comparedNode.leftChild!!.parent = comparedNode
                        splay(comparedNode.leftChild!!)
                        break
                    }
                }
            }
        }
    }
    private fun search(key: Long) : Node {
        var comparedNode = root
        while (comparedNode != null) {
            if (key > comparedNode.key) {
                comparedNode = comparedNode.rightChild
            } else if (key < comparedNode.key) {
                comparedNode = comparedNode.leftChild
            } else {
                return comparedNode
            }
        }
        throw NoSuchElementException("error")
    }
    fun set(key: Long, value: String) {
        search(key).value = value
    }
    fun delete(key: Long) {
        search(key)
    }
}

fun main() {
    var a = Node(1, "gay")
    println(a.key)
}