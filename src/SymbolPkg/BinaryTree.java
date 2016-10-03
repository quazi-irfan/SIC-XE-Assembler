package SymbolPkg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  BinaryTree class manages the Binary Tree.
 *  It provides insert, search and view the contents of the Binary Tree.
 *  It doesn't check for any error in the Node.
 *  When viewing the content of the BST, after evey 20 lines the user is prompted to press Enter key to continue.
 *
 *  When inserting, searching and viewing the binary tree, it uses a public no-argument method(s) which in turns calls
 *  a private method. The private method is a recursive method that does all the heavy lifting.
 *
 */

public class BinaryTree{
    private Node rootNode;
    private int viewCount = 0;

    /**
     * Private method to insert the node to the binary tree.
     *
     * @param parentNode Current node. We start with the rootNode as parent Node,
     *                   and if it's null, the new node is the root node.
     * @param newNode New Node we want to insert.
     */
    private void insert(Node parentNode, Node newNode){
        // If the parentNode is null, this is the first node in the tree
        if(parentNode == null){
            rootNode = newNode;
            return;
        }

        // if the parentNode isn't null
        if(newNode.getKey().compareTo(parentNode.getKey()) < 0){
            if(parentNode.leftNode != null){
                    insert(parentNode.leftNode, newNode);
            } else {
                parentNode.leftNode = newNode;
            }
        }
        else if(newNode.getKey().compareTo(parentNode.getKey()) > 0){
            if(parentNode.rightNode != null){
                    insert(parentNode.rightNode, newNode);
            } else {
                parentNode.rightNode = newNode;
            }
        }

//        Not necessay, because duplication check is done before calling insert method
//        else if(newNode.getKey().compareTo(parentNode.getKey()) == 0){
//            // Duplicate key - set mflag to false
//        }

    }

    /**
     * Insert a new node in the binary tree.
     *
     * @param newNode New node we want to insert.
     */
    public void insert(Node newNode){
        // insert starts with comparing with rootNode
        insert(this.rootNode, newNode);
    }

    /**
     * Private search method of binary tree. This method is called by the public one argument search method.
     *
     * @param parentNode We start search with parent node as the rootNode.
     * @param node the node we are looking for.
     * @return Returns null if search yeilds no result.
     */

    private Node search(Node parentNode, Node node){
        if(parentNode == null){
            return null;
        }
        else if(node.getKey().equals(parentNode.getKey())){
            return parentNode;
        }
        else if(node.getKey().compareTo(parentNode.getKey()) < 0){
            return search(parentNode.leftNode, node);
        }
        else if(node.getKey().compareTo(parentNode.getKey()) > 0){
            return search(parentNode.rightNode, node);
        }

        // placeholder, code execution shouldn't come to this point.
        return null;
    }

    /**
     * Searches for a Node is the Binary Tree
     *
     * @param node search for this node
     * @return returns the node if found
     */

    public Node search(Node node){
        // search starts with comparing with rootNode
        return search(this.rootNode, node);
    }

    /**
     * This protected view method is the actual recursive method that travels the binary tree.
     *
     * @param node It starts with the rootNode and recursively other nodes are passed through
     *             this parameter until the leaves are reached.
     */
    private void view(Node node){
        if(node.leftNode != null){
            view(node.leftNode);
        }

        // TODO add it to another Data structure, that can be accessed via binaryTree.getAll()
        {
            System.out.println(node);
            viewCount++;
            if((viewCount % 20)==0){
                System.out.println(" * Press Enter key to see the next 20 symbols.");
                try{
                    BufferedReader tempHalt = new BufferedReader(new InputStreamReader(System.in));
                    tempHalt.readLine();
                } catch (IOException e){
                    System.out.println(e);
                }
            }
        }


        if(node.rightNode != null){
            view(node.rightNode);
        }
    }

    /**
     *  Prints the BinaryTree on the console
     *
     *  This is a convenience method. Internally this method calls
     *  view(rootNode).
     *
     */
    public void view(){
        if(rootNode != null){
            view(rootNode);
        }
    }

}

