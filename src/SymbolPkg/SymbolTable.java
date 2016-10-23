package SymbolPkg;

import Assembler.Utility;

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

    /**
     * Read an entire line, validate every token. Upon successful validation, enter the symbol into the symbol table.
     * This function will use StringTokenizer to parse individual attributes.
     * If any invalid entry, the function returns without entering the symbol into the symbol table.
     *
     * @param line Feed an entire line from the source file.
     */
    public void add(String line){
        StringTokenizer tokenizer = new StringTokenizer(line);
        Node node = new Node();

        while(tokenizer.hasMoreTokens()){
            String tempToken = tokenizer.nextToken();

            // check for valid symbol
            SymbolStatus symbolStatus = validateSymbol(tempToken);
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
            RFlagStatus rFlagStatus = validateRFlag(tempToken);
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
            System.out.println(node);
        }

    }

    /**
     * Function to check the validity of a symbol. It performes 3 checks.
     * The first check is if the symbol is longer then 10 characters.
     * Second check is if the symbol starts with numeric character.
     * Third check is if any character on the symbol is anything other than alphabate, number or underscore.
     *
     *
     * @param symbol Symbol string to check
     * @return if symbol is valid "valid" string is returned, otherwise "invalid" string is returned.
     */
    public SymbolStatus validateSymbol(String symbol) {
        // check if symbol lenth exceeds 10 characters
        if(symbol.length() > 10){
            return SymbolStatus.ExceedsLength;
        }

        // check if symbol doesn't start with letters
        if(!Character.isAlphabetic(symbol.charAt(0))){
            return SymbolStatus.InvalidStartChar;
        }

        // check for alphabetic, digits and underscore
        for(int i = 1; i<symbol.length(); i++){
            char c = symbol.charAt(i);
            if(!(Character.isAlphabetic(c) | Character.isDigit(c) | (c == '_'))){
                return SymbolStatus.InvalidChar;
            }
        }

        // otherwise the symbol is valid
        return SymbolStatus.Valid;
    }

    /**
     * Checks if rFlag is either 0, f, false, False, 1, t, true, True
     *
     * @param rflag String value of rFlag
     * @return returns "valid" if rflag is valid, or "invalid" otherwise.
     */
    private RFlagStatus validateRFlag(String rflag) {
        if(rflag.length() == 1){
            // accept 0 or 1 as rflag
            if(Character.isDigit(rflag.charAt(0))){
                if(rflag.equals("0"))
                    return RFlagStatus.False;
                if(rflag.equals("1"))
                    return RFlagStatus.True;
                else
                    return RFlagStatus.Invalid;
            }
        }

        // accept tRue or FaLse as rflag
        rflag = rflag.toUpperCase();

        if(rflag.equals("FALSE") | rflag.equals("F"))
            return RFlagStatus.False;
        else if(rflag.equals("TRUE") | rflag.equals("T"))
            return RFlagStatus.True;
        else
            return RFlagStatus.Invalid;
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
}
