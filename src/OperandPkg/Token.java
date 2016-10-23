package OperandPkg;

import SymbolPkg.Node;
import SymbolPkg.SymbolTable;

/**
 * Token is an smallest elements on an Expression. For example, in @Red+Red expression, Red is a token.
 *
 * This class holds some static method to check the validity of the token against the symbol table and get it's value.
 */
class Token{
    /**
     * Get the rflag of the given token
     *
     * @param token String value of the token
     * @param symbolTable Check against this symbol table
     * @return returns true is the token is a valid symbol on the symbol table, false otherwise.
     */
    public static boolean getTokenRflag(String token, SymbolTable symbolTable){
        boolean operantRflag = false;

        Integer tokenValue = null;
        try {
            tokenValue = Integer.valueOf(token);
        } catch (NumberFormatException e){
        }

        Node node = new Node();

        if(tokenValue == null){
            // Symbol is not a number
            node = symbolTable.search(token);
            if(node != null){
                // symbol found in the symbol table. Return the rflag
                operantRflag = node.getRflag();
            }
        } else {
            // Symbol is a number, return false
            operantRflag = false;
        }

        return operantRflag;
    }

    /**
     * This method returns the value of the token.
     * @param token String value of the token
     * @param symbolTable Check against this symbol table
     * @return Returns the value from the symbol table if the token is a valid symbol.
     *         Returns -1 if the token is a valid symbol, but isn't in the symbol table.
     *         Returns -2 if the token is not a valid symbol.
     *         TODO : Returns other values to explain why it's no a valid symbol
     */

    public static Integer getTokenValue(String token, SymbolTable symbolTable){
        Integer operandValue = null;

        Integer tokenValue = null;
        try {
            tokenValue = Integer.valueOf(token);
        } catch (NumberFormatException e){
        }

        Node node = new Node();

        if(tokenValue == null){
            // Symbol is not a number
            if(!validateSymbol(token).equals("valid")) {
                operandValue = -2;
                return operandValue;
            }

            node = symbolTable.search(token);
            if(node != null){
                operandValue = node.getValue();
            } else {
                operandValue = -1;
            }
        } else {
            // Symbol is a number
            operandValue = tokenValue;
        }

        return operandValue;
    }

    /**
     * DUPLICATE CODE : REMOVE IT
     *
     * Function to check the validity of a symbol. It performes 3 checks.
     * The first check is if the symbol is longer then 10 characters.
     * Second check is if the symbol starts with numeric character.
     * Third check is if any character on the symbol is anything other than alphabate, number or underscore.
     *
     *
     * @param symbol Symbol string to check
     * @return if symbol is valid "valid" string is returned, otherwise "invalid" string is returned.
     */
    private static String validateSymbol(String symbol) {
        // check if symbol lenth exceeds 10 characters
        if(symbol.length() > 10){
            return "Symbol length must be less than 10 characters.";
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
}