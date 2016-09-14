package SymbolTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BinaryTree{
    private Node rootNode;
    private int count = 0;

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

        // Not necessay, because duplication check is done before calling insert method
//        else if(newNode.getKey().compareTo(parentNode.getKey()) == 0){
//            // Duplicate key - set mflag to false
//        }

    }

    public void insert(Node newNode){
        // insert starts with comparing with rootNode
        insert(this.rootNode, newNode);
    }

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

    public Node search(Node node){
        // search starts with comparing with rootNode
        return search(this.rootNode, node);
    }

    private void view(Node node){
        if(node.leftNode != null){
            view(node.leftNode);
        }

        // TODO add it to another Data structure, that can be accessed via binaryTree.getAll()
        {
            System.out.println(node);
            count++;
            if((count % 20)==0){
                System.out.println("\n Press Enter key to see the next 20 symbols.");
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

    public void view(){
        if(rootNode != null){
            view(rootNode);
        }
    }

}


