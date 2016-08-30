package SymbolTable;

import java.util.StringTokenizer;

public class SymbolTable {
    BinaryTree binaryTree = new BinaryTree();

    public void add(String line){
        StringTokenizer tokenizer = new StringTokenizer(line);
        Node node = new Node();

        while(tokenizer.hasMoreTokens()){
            String tempToken = tokenizer.nextToken();

            // check for valid symbol
            if(validSymbol(tempToken)){
                // Check for duplication
                Node tempNode = binaryTree.search(new Node(tempToken)); // Symbols are automatically truncated to it's first 4 chars
                if(tempNode != null){
                    // duplicate found, so set the mflag to true
                    tempNode.setMflag(true);
                    System.out.println("Duplicate Symbol at line " + Main.lineNumber + " : " + tempToken);
                    break;
                } else {
                    node.setKey(tempToken);
                }

            } else {
                System.out.println("Invalid Symbol at line " + Main.lineNumber + " : " + tempToken);
                break;  // Once an error occured the entire line is discarded
            }
            tempToken = tokenizer.nextToken();

            // check for valid value
            if(validValue(tempToken)){
                node.setValue(Integer.parseInt(tempToken));
            } else {
                System.out.println("Invalid Value at line " + Main.lineNumber + " : " + tempToken);
                break;
            }
            tempToken = tokenizer.nextToken();

            // check for valid rflag
            if(validRflag(tempToken)){
                if(tempToken.equals("0") | tempToken.equals("f") | tempToken.equals("false")){
                    node.setRflag(false);
                } else {
                    node.setRflag(true);
                }
            } else {
                System.out.println("Invalid Rflag at line " + Main.lineNumber + " : " + tempToken);
                break;
            }

            // valid Node is crated; now add it to the binaryTree
            node.setIflag(true);
            node.setMflag(false);
            binaryTree.insert(node);
        }

    }

    public boolean validSymbol(String symbol) {
        // check if symbol lenth exceeds 10 characters
        if(symbol.length() > 10){
            return false;
        }

        // check if symbol doesn't start with letters
        if(!Character.isAlphabetic(symbol.charAt(0))){
            return false;
        }

        // check for alphabetic, digits and underscore
        for(int i = 1; i<symbol.length(); i++){
            char c = symbol.charAt(i);
            if(!(Character.isAlphabetic(c) | Character.isDigit(c) | (c == '_'))){
                return false;
            }
        }

        // otherwise the symbol is valid
        return true;
    }

    private boolean validValue(String value) {
        try {
            Integer.parseInt(value);
        } catch (Exception e){
            return false;
        }

        return true;
    }

    private boolean validRflag(String rflag) {
        return rflag.equals("0") | rflag.equals("f") | rflag.equals("false") | rflag.equals("False")
                | rflag.equals("1") | rflag.equals("t") | rflag.equals("true") | rflag.equals("True");
    }

    public void view(){
        binaryTree.view();
    }

    public Node search(String symbol){
        return binaryTree.search(new Node(symbol));
    }
}
