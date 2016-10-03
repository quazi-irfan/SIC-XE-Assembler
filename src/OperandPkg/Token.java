package OperandPkg;

import SymbolPkg.Node;
import SymbolPkg.SymbolTable;

class Token{
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
            node = symbolTable.search(token);
            if(node != null){
                operandValue = node.getValue();
            }
        } else {
            // Symbol is a number
            operandValue = tokenValue;
        }

        return operandValue;
    }
}