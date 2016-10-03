package SymbolPkg;

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
     * Add an entire line from the source file to the symbol table.
     * This function will use StringTokenizer to parse individual attributes.
     * Upon successful parsing, a new node is added to the binary tree.
     * If any error is occurs, this function returns ignoring the remainging line.
     *
     * @param line Feed an entire line from the source file.
     */
    public void add(String line){
        StringTokenizer tokenizer = new StringTokenizer(line);
        Node node = new Node();

        while(tokenizer.hasMoreTokens()){
            String tempToken = tokenizer.nextToken();

            // check for valid symbol
            String tokenStatus = validateSymbol(tempToken);
            if(tokenStatus.equals("valid")){
                // Check for duplication
                Node tempNode = binaryTree.search(new Node(tempToken)); // Symbols are automatically truncated to it's first 4 chars
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
                System.out.println("Invalid Symbol : " + tempToken + " -> " + tokenStatus);
                break;  // Once an error occured the entire line is discarded
            }
            tempToken = tokenizer.nextToken();

            // check for valid value
            if(validValue(tempToken)){
                node.setValue(Integer.parseInt(tempToken));
            } else {
                System.out.println("Invalid Value : " + tempToken);
                break;
            }
            tempToken = tokenizer.nextToken();

            // check for valid rflag
            tokenStatus = validateRFlag(tempToken);
            if(tokenStatus.equals("valid")){
                if(Character.isDigit(tempToken.charAt(0))){
                    if(tempToken.charAt(0) == '0')
                        node.setRflag(false);
                    else
                        node.setRflag(true);
                }

                tempToken = tempToken.toUpperCase();

                if(tempToken.equals("F") | tempToken.equals("FALSE"))
                    node.setRflag(false);
                else
                    node.setRflag(true);

            } else {
                System.out.println("Invalid Rflag : " + tempToken);
                break;
            }

            // valid Node is crated; now add it to the binaryTree
            node.setIflag(true);
            node.setMflag(false);
            binaryTree.insert(node);
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
    public String validateSymbol(String symbol) {
        // check if symbol lenth exceeds 10 characters
        if(symbol.length() > 10){
            return "Symbol length must be between 4-10 characters long.";
        }

        // check if symbol doesn't start with letters
        if(!Character.isAlphabetic(symbol.charAt(0))){
            return "Symbol must start with letters(A...Z or a...z)";
        }

        // check for alphabetic, digits and underscore
        for(int i = 1; i<symbol.length(); i++){
            char c = symbol.charAt(i);
            if(!(Character.isAlphabetic(c) | Character.isDigit(c) | (c == '_'))){
                return "Symbol can only contain A-Z,a-z, 0-9 and Underscore'_'";
            }
        }

        // otherwise the symbol is valid
        return "valid";
    }

    /**
     * Uses Interger.parseInt to check the validity of the 'value' of the Node.
     * It accepts +5, but 5+ is rejected.
     *
     * @param value String value of the 'value'
     * @return True if valid 'value', and false otherwise.
     */
    private boolean validValue(String value) {
        try {
            Integer.parseInt(value);
        } catch (Exception e){
            return false;
        }

        return true;
    }

    /**
     * Checks if rFlag is either 0, f, false, False, 1, t, true, True
     *
     * @param rflag String value of rFlag
     * @return returns "valid" if rflag is valid, or "invalid" otherwise.
     */
    private String validateRFlag(String rflag) {
        if(rflag.length() == 1){
            char[] c = rflag.toCharArray();
            if(Character.isDigit(c[0])){
                if(rflag.equals("0") | rflag.equals("1"))
                    return "valid";
                else
                    return "invalid";
            }
        }

        rflag = rflag.toUpperCase();

        if(!(rflag.equals("FALSE") | rflag.equals("F") | rflag.equals("TRUE") | rflag.equals("T")))
            return "invalid";

        return "valid";
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
