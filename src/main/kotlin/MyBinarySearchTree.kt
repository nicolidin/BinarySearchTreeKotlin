import java.lang.RuntimeException

data class Contact(val name: String, val phoneNumber: String , val address: String)

data class Node<K: Comparable<K>, V>(val content: Pair<K, V>, var leftChild: Node<K, V>? = null, var rightChild: Node<K, V>? = null)

class MyBinarySearchTree<K : Comparable<K>, V> {
    var rootNode: Node<K, V>? = null;

    private tailrec fun insertNode(currentNode: Node<K, V>, newNode: Node<K, V>) {
        if (newNode.content.first < currentNode.content.first) {
            if (currentNode.leftChild == null) {
                currentNode.leftChild = newNode;
            } else {
                insertNode(currentNode.leftChild!!, newNode)
            }
        } else if (newNode.content.first > currentNode.content.first) {
            if (currentNode.rightChild == null) {
                currentNode.rightChild = newNode;
            } else {
                insertNode(currentNode.rightChild!!, newNode)
            }
        } else {
            throw RuntimeException("can't add existing number")
        }
    }

    private tailrec fun findMinNode(currentNode: Node<K, V>): Node<K, V> {
        if (currentNode.leftChild == null) {
            return currentNode;
        }
        return this.findMinNode(currentNode.leftChild!!);
    }

    private tailrec fun findParentNodeByChildKey(currentNode: Node<K, V>, key: K) : Node<K, V>? {
        if (currentNode.leftChild?.content?.first == key || currentNode.rightChild?.content?.first == key) {
            return currentNode
        }
        return when {
            key < currentNode.content.first -> {
                currentNode.leftChild ?: throw RuntimeException("no parent found")
                findParentNodeByChildKey(currentNode.leftChild!!, key)
            }
            key > currentNode.content.first -> {
                currentNode.rightChild ?: throw RuntimeException("no parent found")
                findParentNodeByChildKey(currentNode.rightChild!!, key)
            }
            else -> null // root node
        }
    }

    private tailrec fun findNodeByKey(currentNode: Node<K, V>, key: K): Node<K, V>? {
        return when {
            key < currentNode.content.first -> {
                currentNode.leftChild ?: return null
                findNodeByKey(currentNode.leftChild!!, key)
            }
            key > currentNode.content.first -> {
                currentNode.rightChild ?: return null
                findNodeByKey(currentNode.rightChild!!, key)
            }
            else -> currentNode
        }
    }

    fun deleteNode(key: K) {
        val nodeToDelete = this.findNodeByKey(this.rootNode!!, key) ?: throw RuntimeException("can't delete non existing node");
        val parentOfNodeToDelete = this.findParentNodeByChildKey(this.rootNode!!, key);

        val nodeSuccessor : Node<K, V>? = when {
            nodeToDelete.rightChild != null -> {
                this.findMinNode(nodeToDelete.rightChild!!)
            }
            nodeToDelete.leftChild != null -> {
                nodeToDelete.leftChild
            }
            else -> null // leaf node
        }
        when {
            // assign node successor or null to child of parent node to delete
            nodeToDelete === parentOfNodeToDelete?.leftChild -> {
                parentOfNodeToDelete.leftChild = nodeSuccessor;
            }
            nodeToDelete === parentOfNodeToDelete?.rightChild -> {
                parentOfNodeToDelete.rightChild = nodeSuccessor;
            }
            nodeToDelete === this.rootNode -> { this.rootNode = nodeSuccessor }
        }
        val parentOfSuccessor = if (nodeSuccessor != null && nodeToDelete !== this.rootNode) {
            this.findParentNodeByChildKey(this.rootNode!!, nodeSuccessor.content.first)
        } else null;
        // remove successor from it's parent (parentOfSuccessor)
        if (parentOfSuccessor?.leftChild != null) {
           parentOfSuccessor.leftChild = null
        }
        //assign delete side on nodeSuccessor
        nodeSuccessor?.leftChild = nodeToDelete.leftChild;
        if (nodeSuccessor != nodeToDelete.rightChild) { nodeSuccessor?.rightChild = nodeToDelete.rightChild } // assign nodeSuccessor.rightChild only if findMinNode got a left leaf node else if it's the same.
    }


    fun searchNode(key: K): Node<K, V>? {
        this.rootNode ?: throw RuntimeException("can't find node for an empty tree");

        return this.findNodeByKey(this.rootNode!!, key)
    }

    fun addNode(content: Pair<K, V>) {
        val newNode =  Node(content)

        if (rootNode == null) {
            this.rootNode = newNode;
        } else {
            this.insertNode(this.rootNode!!, newNode)
        }
    }
}

fun main() {
    val myBinaryWithKeyPhone = MyBinarySearchTree<String, Contact>()

    myBinaryWithKeyPhone.addNode(Pair("123", Contact("nico", "+353 1 666 9354", "24 allée gabriel peri")))
    myBinaryWithKeyPhone.addNode(Pair("003", Contact("jean", "+353 1 256 9720", "11 allée gabriel peri")))
    myBinaryWithKeyPhone.addNode(Pair("246", Contact("thibault", "+353 1 256 9876", "12 allée gabriel peri")))
    myBinaryWithKeyPhone.addNode(Pair("001", Contact("adrien", "+353 1 256 2968", "25 allée gabriel peri")))
    myBinaryWithKeyPhone.addNode(Pair("113", Contact("laurianne", "+353 1 121 2968", "122 allée gabriel peri")))
    myBinaryWithKeyPhone.addNode(Pair("234", Contact("clément", "+353 1 139 9876", "176 allée gabriel peri")))
    myBinaryWithKeyPhone.addNode(Pair("256", Contact("stéphane", "+353 1 258 9799", "211 allée gabriel peri")))
    myBinaryWithKeyPhone.addNode(Pair("258", Contact("hugo", "+353 1 258 0099", "1 allée gabriel peri")))

    println(myBinaryWithKeyPhone.searchNode("123"))
    myBinaryWithKeyPhone.deleteNode("123")
    println(myBinaryWithKeyPhone.rootNode)

    //println(myBinaryWithKeyPhone.searchNode("234"))

   // val myBinaryWithKeyName = MyBinarySearchTree<String, Contact>()

   // myBinaryWithKeyName.addNode(Pair("nico", Contact("nico", "+353 1 666 9354", "24 allée gabriel peri")))
   // myBinaryWithKeyName.addNode(Pair("jean", Contact("jean", "+353 1 256 9720", "11 allée gabriel peri")))
   // myBinaryWithKeyName.addNode(Pair("thibault", Contact("thibault", "+353 1 256 9876", "12 allée gabriel peri")))
   // myBinaryWithKeyName.addNode(Pair("adrien", Contact("adrien", "+353 1 256 2968", "25 allée gabriel peri")))
   // myBinaryWithKeyName.addNode(Pair("laurianne", Contact("laurianne", "+353 1 121 2968", "122 allée gabriel peri")))
   // myBinaryWithKeyName.addNode(Pair("clément", Contact("clément", "+353 1 139 9876", "176 allée gabriel peri")))
   // myBinaryWithKeyName.addNode(Pair("stéphane", Contact("stéphane", "+353 1 258 9799", "211 allée gabriel peri")))
   // myBinaryWithKeyName.addNode(Pair("hugo", Contact("hugo", "+353 1 258 0099", "1 allée gabriel peri")))
   // myBinaryWithKeyName.addNode(Pair("paulo", Contact("paulo", "+353 1 111 0099", "2 allée gabriel peri")))

   // println(myBinaryWithKeyName.searchNode("nico"))
   // myBinaryWithKeyName.deleteNode("nico")
   // println(myBinaryWithKeyName.searchNode("jean"))
}


