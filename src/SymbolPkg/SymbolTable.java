package SymbolPkg;

import Assembler.Utility;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * SymbolTable uses a BinaryTree to store symbols and it's attributes.
 * This class also provides error checking before data entry.
 * In some cases, i.e. printing the Binary table, it forwards the request to the Binary Tree. It doesn't handle printing yet.
 *
 * Use empty constructor to initialize a SymbolTable.
 */

public class SymbolTable {
    BinaryTree binaryTree = new BinaryTree();

    public void add(Node symbol){
        Node tempNode = binaryTree.search(symbol);
        if(tempNode != null){
            tempNode.setMflag(true);
        }
        else {
            binaryTree.insert(symbol);
        }
    }

    /**
     * Read an entire line, validate every token. Upon successful validation, enter the symbol into the symbol table.
     * This function will use StringTokenizer to parse individual attributes.
     * If any invalid entry, the function returns without entering the symbol into the symbol table.
     *
     * @param line Feed an entire line from the source file.
     */
    public void addLine(String line){
        StringTokenizer tokenizer = new StringTokenizer(line);
        Node node = new Node();

        while(tokenizer.hasMoreTokens()){
            String tempToken = tokenizer.nextToken();

            // check for valid symbol
            SymbolStatus symbolStatus = SymbolTableUtility.validateSymbol(tempToken);
            if(symbolStatus == SymbolStatus.Valid){
                // Check for duplication
                Node tempNode = binaryTree.search(new Node(tempToken)); // returns null is Node isn't found
                if(tempNode != null){
                    // duplicate found, so set the mflag to true
                    tempNode.setMflag(true);
                    System.out.println("Duplicate Symbol : " + tempToken);
                    break;
                } else {
                    node.setKey(tempToken);
                }
            // invalid symbol
            } else {
                System.out.println("Invalid Symbol : " + tempToken + " -> " + symbolStatus.getDetails());
                break;  // Once an error found the entire line is discarded
            }
            tempToken = tokenizer.nextToken(); // Valid symbol is found, now try to get the value of the symbol

            // check for valid value
            if(Utility.isInteger(tempToken)){
                node.setValue(Integer.parseInt(tempToken));
            } else {
                System.out.println("Invalid Value : " + tempToken);
                break;
            }
            tempToken = tokenizer.nextToken();  // Valid value of the symbol is found, now try to get the rflag of the symbol

            // check for valid rflag
            RFlagStatus rFlagStatus = SymbolTableUtility.validateRFlag(tempToken);
            if(!(rFlagStatus == RFlagStatus.Invalid)){
                if(rFlagStatus == RFlagStatus.False)
                        node.setRflag(false);
                    else
                        node.setRflag(true);
                } else {
                    System.out.println("Invalid Rflag : " + tempToken);
                    break;
            }

            // set the remaining iflag and mflag to true and false by default
            node.setIflag(true);
            node.setMflag(false);
            binaryTree.insert(node);
        }
    }

    /**
     * Print the binary tree.
     * Placeholder method to call the view method of the Binary tree class.
     * The binarytree is prineted on the console.
     */
    public void view(){
        binaryTree.view();
    }

    /**
     * Search for a node in the Binary tree.
     * Its a placeholder method to call the search method of the binary tree class.
     * @param symbol String value of the symbol we are looking for.
     * @return Returns the instance of the Node we are looking for.
     */
    public Node search(String symbol){
        return binaryTree.search(new Node(symbol));
    }

    /**
     * Call getAll() method of the BinaryTree.
     * @return returns an ArrayList<Node> of all sybmols in the symbol table.
     */
    public ArrayList<Node> getAll(){
        return binaryTree.getAll();
    }

    /**
     * Get all external symbols
     * @return Returns a ArrayList<Node> that contains all symbols from the symbo table with iflag set to true.
     */
    public ArrayList<Node> getAllExternal(){
        ArrayList<Node> allExternal = new ArrayList<>();

        for(Node symbol : getAll()){
            if(!symbol.iflag){
                allExternal.add(symbol);
            }
        }

        return allExternal;
    }

    public ArrayList<Node> getAllInternal(){
        ArrayList<Node> allInternal = new ArrayList<>();

        for(Node symbol : getAll()){
            if(symbol.iflag){
                allInternal.add(symbol);
            }
        }

        return allInternal;
    }

    public int size(){
        return getAll().size();
    }
}
