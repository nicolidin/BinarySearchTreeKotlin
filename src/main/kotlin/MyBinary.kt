import java.lang.RuntimeException

data class Contact(val name: String, val phoneNumber: String , val address: String)

data class MyNode(val value: Contact, var leftChild: MyNode? = null, var rightChild: MyNode? = null)

class MyBinary {
    private var rootNode: MyNode? = null;

    private tailrec fun insertNode(currentNode: MyNode, newNode: MyNode) {
        if (newNode.value.phoneNumber < currentNode.value.phoneNumber) {
            if (currentNode.leftChild == null) {
                currentNode.leftChild = newNode;
            } else {
                insertNode(currentNode.leftChild!!, newNode)
            }
        } else if (newNode.value.phoneNumber > currentNode.value.phoneNumber) {
            if (currentNode.rightChild == null) {
                currentNode.rightChild = newNode;
            } else {
                insertNode(currentNode.rightChild!!, newNode)
            }
        } else {
            throw RuntimeException("can't add existing number")
        }
    }

    fun addNode(value: Contact) {
        val newNode =  MyNode(value)

        if (rootNode == null) {
            this.rootNode = newNode;
        } else {
            this.insertNode(this.rootNode!!, newNode)
        }
    }

    private tailrec fun findMinNode(currentNode: MyNode): MyNode {
        if (currentNode.leftChild == null) {
            return currentNode;
        }
        return this.findMinNode(currentNode.leftChild!!);
    }

    private tailrec fun findParentNodeByChildKey(currentNode: MyNode, key: String) : MyNode? {
        if (currentNode.leftChild?.value?.phoneNumber == key || currentNode.rightChild?.value?.phoneNumber == key) {
            return currentNode
        }
        return when {
            key < currentNode.value.phoneNumber -> {
                currentNode.leftChild ?: throw RuntimeException("no parent found")
                findParentNodeByChildKey(currentNode.leftChild!!, key)
            }
            key > currentNode.value.phoneNumber -> {
                currentNode.rightChild ?: throw RuntimeException("no parent found")
                findParentNodeByChildKey(currentNode.rightChild!!, key)
            }
            else -> null // root node
        }
    }

    private tailrec fun findNodeByKey(currentNode: MyNode, key: String): MyNode? {
        return when {
            key < currentNode.value.phoneNumber -> {
                currentNode.leftChild ?: return null
                findNodeByKey(currentNode.leftChild!!, key)
            }
            key > currentNode.value.phoneNumber -> {
                currentNode.rightChild ?: return null
                findNodeByKey(currentNode.rightChild!!, key)
            }
            else -> currentNode
        }
    }

    fun deleteNode(key: String) {
        val nodeToDelete = this.findNodeByKey(this.rootNode!!, key) ?: throw RuntimeException("can't delete non existing node");
        val parentOfNodeToDelete = this.findParentNodeByChildKey(this.rootNode!!, key);

        val nodeSuccessor : MyNode? = when {
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
            this.findParentNodeByChildKey(this.rootNode!!, nodeSuccessor.value.phoneNumber)
        } else null;
        // remove successor from it's parent (parentOfSuccessor)
        if (parentOfSuccessor?.leftChild != null) {
           parentOfSuccessor.leftChild = null
        }
        //assign delete side on nodeSuccessor
        nodeSuccessor?.leftChild = nodeToDelete.leftChild;
        if (nodeSuccessor != nodeToDelete.rightChild) { nodeSuccessor?.rightChild = nodeToDelete.rightChild } // assign nodeSuccessor.rightChild only if findMinNode got a left leaf node else if it's the same.
    }


    fun searchNode(key: String): MyNode? {
        this.rootNode ?: throw RuntimeException("can't find node for an empty tree");

        return this.findNodeByKey(this.rootNode!!, key)
    }
}

fun main() {
    val myBinary = MyBinary()

    myBinary.addNode(Contact("nico", "8", "24 allée gabriel peri"))
    myBinary.addNode(Contact("nico", "3", "24 allée gabriel peri"))
    myBinary.addNode(Contact("nico", "9", "24 allée gabriel peri"))
    myBinary.addNode(Contact("nico", "1", "24 allée gabriel peri"))
    myBinary.addNode(Contact("nico", "6", "24 allée gabriel peri"))
    myBinary.addNode(Contact("nico", "14", "24 allée gabriel peri"))
    myBinary.addNode(Contact("nico", "4", "24 allée gabriel peri"))
    myBinary.addNode(Contact("nico", "7", "24 allée gabriel peri"))
    myBinary.addNode(Contact("nico", "13", "24 allée gabriel peri"))

    println("yow first print")
    println(myBinary.searchNode("8"))
    myBinary.deleteNode("8")
    println("yow scond print")
    println(myBinary.searchNode("9"))
}


