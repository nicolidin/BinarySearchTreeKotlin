import kotlin.system.exitProcess

//Data class in kotlin is the equivalent of a structure. A data class can't have methods.
data class Contact(val name: String, val phoneNumber: String , val address: String)

data class Node(val content: Pair<String, Contact>, var leftChild: Node? = null, var rightChild: Node? = null)

data class MyBinarySearchTree(var rootNode: Node? = null)

tailrec fun insertNode(currentNode: Node, newNode: Node) {
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
        exitProcess(84)
    }
}

tailrec fun findMinNode(currentNode: Node): Node {
    if (currentNode.leftChild == null) {
        return currentNode;
    }
    return findMinNode(currentNode.leftChild!!);
}

tailrec fun findParentNodeByChildKey(currentNode: Node, key: String) : Node? {
    if (currentNode.leftChild?.content?.first == key || currentNode.rightChild?.content?.first == key) {
        return currentNode
    }
    if (key < currentNode.content.first) {
        currentNode.leftChild ?: exitProcess(84)
        return findParentNodeByChildKey(currentNode.leftChild!!, key)
    }
    else if (key > currentNode.content.first) {
        currentNode.rightChild ?: exitProcess(84)
        return findParentNodeByChildKey(currentNode.rightChild!!, key)
    }
    else { return null }  // root node

}

tailrec fun findNodeByKey(currentNode: Node, key: String): Node? {
    if (key < currentNode.content.first) {
        currentNode.leftChild ?: return null
        return findNodeByKey(currentNode.leftChild!!, key)
    }
    if (key > currentNode.content.first) {
        currentNode.rightChild ?: return null
        return findNodeByKey(currentNode.rightChild!!, key)
    }
    else { return currentNode }
}

fun deleteNode(key: String, tree: MyBinarySearchTree) {
    val nodeToDelete = findNodeByKey(tree.rootNode!!, key) ?: exitProcess(84)
    val parentOfNodeToDelete = findParentNodeByChildKey(tree.rootNode!!, key);
    val nodeSuccessor : Node?;

    if (nodeToDelete.rightChild != null) {
        nodeSuccessor = findMinNode(nodeToDelete.rightChild!!)
    } else if (nodeToDelete.leftChild != null) {
        nodeSuccessor = nodeToDelete.leftChild
    } else {// leaf node
        nodeSuccessor = null
    }

    val parentOfSuccessor: Node?;
    if (nodeSuccessor != null) {
        parentOfSuccessor = findParentNodeByChildKey(tree.rootNode!!, nodeSuccessor.content.first)
    } else {
        parentOfSuccessor = null
    };
    // assign node successor or null (for leaf) to child of parent node to delete
    if (nodeToDelete === parentOfNodeToDelete?.leftChild) {
        parentOfNodeToDelete.leftChild = nodeSuccessor;
    } else if (nodeToDelete === parentOfNodeToDelete?.rightChild) {
        parentOfNodeToDelete.rightChild = nodeSuccessor;
    } else if (nodeToDelete === tree.rootNode) {
        tree.rootNode = nodeSuccessor
    }

    //assign delete side on nodeSuccessor
    if (nodeSuccessor != nodeToDelete.leftChild) { nodeSuccessor?.leftChild = nodeToDelete.leftChild }
    if (nodeSuccessor != nodeToDelete.rightChild) { nodeSuccessor?.rightChild = nodeToDelete.rightChild } // assign nodeSuccessor.rightChild only if findMinNode got a left leaf node else if it's the same.

    // remove successor from it's parent
    if (parentOfSuccessor?.leftChild === nodeSuccessor) {
        parentOfSuccessor?.leftChild = null
    } else if (parentOfSuccessor?.rightChild === nodeSuccessor) {
        parentOfSuccessor?.rightChild = null
    }
}

fun searchNode(key: String, tree: MyBinarySearchTree): Node? {
    tree.rootNode ?: exitProcess(84)

    return findNodeByKey(tree.rootNode!!, key)
}

fun addNode(content: Pair<String, Contact>,  tree: MyBinarySearchTree) {
    val newNode =  Node(content)

    if (tree.rootNode == null) {
        tree.rootNode = newNode;
    } else {
        insertNode(tree.rootNode!!, newNode)
    }
}

fun main() {
    val myBinaryWithKeyPhone = MyBinarySearchTree()
    addNode(Pair("+353 1 666 9354", Contact("nico", "+353 1 666 9354", "24 allée gabriel peri")), myBinaryWithKeyPhone)
    addNode(Pair("+353 1 256 9720", Contact("jean", "+353 1 256 9720", "11 allée gabriel peri")), myBinaryWithKeyPhone)
    addNode(Pair("+353 1 256 9876", Contact("thibault", "+353 1 256 9876", "12 allée gabriel peri")), myBinaryWithKeyPhone)
    addNode(Pair("+353 1 256 2968", Contact("adrien", "+353 1 256 2968", "111 allée gabriel peri")), myBinaryWithKeyPhone)
    addNode(Pair("+353 1 121 2968", Contact("laurianne", "+353 1 121 2968", "122 allée gabriel peri")), myBinaryWithKeyPhone)
    addNode(Pair("+353 1 139 9876", Contact("clément", "+353 1 139 9876", "176 allée gabriel peri")), myBinaryWithKeyPhone)
    addNode(Pair("+353 1 258 9799", Contact("stéphane", "+353 1 258 9799", "211 allée gabriel peri")), myBinaryWithKeyPhone)
    addNode(Pair("+353 1 258 0099", Contact("hugo", "+353 1 258 0099", "1 allée gabriel peri")), myBinaryWithKeyPhone)
    deleteNode("+353 1 666 9354", myBinaryWithKeyPhone)
    println(searchNode("+353 1 256 9720", myBinaryWithKeyPhone))


    val myBinaryWithKeyName = MyBinarySearchTree()
    addNode(Pair("nico", Contact("nico", "+353 1 666 9354", "24 allée gabriel peri")), myBinaryWithKeyName)
    addNode(Pair("jean", Contact("jean", "+353 1 256 9720", "11 allée gabriel peri")), myBinaryWithKeyName)
    addNode(Pair("thibault", Contact("thibault", "+353 1 256 9876", "12 allée gabriel peri")), myBinaryWithKeyName)
    addNode(Pair("adrien", Contact("adrien", "+353 1 256 2968", "25 allée gabriel peri")), myBinaryWithKeyName)
    addNode(Pair("laurianne", Contact("laurianne", "+353 1 121 2968", "122 allée gabriel peri")), myBinaryWithKeyName)
    addNode(Pair("clément", Contact("clément", "+353 1 139 9876", "176 allée gabriel peri")), myBinaryWithKeyName)
    addNode(Pair("stéphane", Contact("stéphane", "+353 1 258 9799", "211 allée gabriel peri")), myBinaryWithKeyName)
    addNode(Pair("hugo", Contact("hugo", "+353 1 258 0099", "1 allée gabriel peri")), myBinaryWithKeyName)
    addNode(Pair("paulo", Contact("paulo", "+353 1 111 0099", "2 allée gabriel peri")), myBinaryWithKeyName)
    deleteNode("nico", myBinaryWithKeyName)
    println(searchNode("jean", myBinaryWithKeyName))
}