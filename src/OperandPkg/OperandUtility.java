package OperandPkg;

import Assembler.Utility;
import SymbolPkg.SymbolStatus;
import SymbolPkg.SymbolTable;
import SymbolPkg.SymbolTableUtility;

import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * This class holds the static method to process Expressions.
 */
public class OperandUtility{
    private static String original;
    public static Operand operand;
    public static Literal literal;

    /**
     * This method does the initial Expression evalution.
     *
     * @param symbolTable Take the symbol table to compare the Exparesions with
     * @param literalTable Holds the Linked link populated with literals
     * @param expression operand to be evaluated
     */
    public static String evaluateOperand(SymbolTable symbolTable, LinkedList<Literal> literalTable, String expression) throws IOException{
        operand = new Operand();
        literal = new Literal();

        if(expression != null && expression.length() > 0){
            // original string hold the unaltered expression
            original = expression;

            if(expression.length() == 1 && expression.contains("*"))
                return "valid";

            if(expression.charAt(0) != '=') {
                // *** Handle non-Literal ***

                // If not literal make everything upper cased.
                expression = expression.toUpperCase();

                operand.expression = expression;

                // operand n,i anx x bits are 0 by default
                if (expression.charAt(0) == '#') {
                    // for #expression
                    operand.Ibit = true;
                    expression = expression.substring(1);
                } else if (expression.charAt(0) == '@') {
                    // for @expression
                    operand.Nbit = true;
                    expression = expression.substring(1);
                } else if (Character.isDigit(expression.charAt(0)) & !(expression.contains("+") | expression.contains("-"))) {
                    // for 6 and doesn't' contain + or - char
                    operand.Nbit = false;
                    operand.Ibit = true;
                } else if (expression.length() > 1
                        &&
                        ((expression.charAt(0) == '+' | expression.charAt(0) == '-') & Character.isDigit(expression.charAt(1)))){
                    // for +6 or -6
                    operand.Nbit = false;
                    operand.Ibit = true;
                } else if(expression.contains("C'") | expression.contains("c'") | expression.contains("X'") | expression.contains("x'")){
                    return "valid";
//                    operand.relocability = false;
                } else {
                    // for expression OR expression+expression
                    // but for literal + literal Nbit and Ibit has to be set 0 and 1
                    operand.Nbit = true;
                    operand.Ibit = true;
                }

                // checks for expression,X
                if (expression.length() >= 3 && expression.substring(expression.length() - 2, expression.length()).equals(",X")) {
                    // Check if mixing @ or # with ,X
                    if (original.contains("@") | original.contains("#") ) {
                        return original + " (Error : @ or # can't be mixed with ,X)";
                    }

                    // We have valid expression,X
                    operand.Xbit = true;
                    expression = expression.substring(0, expression.length() - 2);
                }

                // evaluate the expression red+3, one-two
                String expressionStatus = evaluateExpression(expression, operand, symbolTable);
                if(!expressionStatus.equals("valid")){
                    return expressionStatus;
                }

//                System.out.println(operand);

                // operand evaluation successful
                return "valid";

            } else {
                // Handle Literal =C'ABC' and =X'1E'

                // Convert =C'ABC' or =X'1E' to C'ABC' or X'1E'
                if(expression.contains("="))
                    expression = expression.substring(1);

                // test for literal starting with 'C' or 'c', 'X' or 'x' character
                if(!(expression.charAt(0) == 'C' | expression.charAt(0)=='c' | expression.charAt(0) == 'X' | expression.charAt(0) == 'x')){
                    return original + " (Error : Character literal must start with 'C'/'c' or 'X'/'x' character)";
                } else {
                    // Convert c'ABC' to C'ABC' to ensure they are the same literal
                    StringBuilder stringBuilder = new StringBuilder(expression);
                    if(expression.charAt(0) == 'C' | expression.charAt(0)=='c'){
                        stringBuilder.setCharAt(0, 'C');
                    } else {
                        stringBuilder.setCharAt(0, 'X');
                    }
                    expression = stringBuilder.toString();
                }

                // test for literals starting and ending with ' character
                if(expression.charAt(1) != '\'' & expression.charAt(expression.length()-1) != '\''){
                    return original + " (Error : Literal must be enclosed with \' and \' character)";
                }

                // *** Handle Character Literals ***
                if(expression.charAt(0) == 'C' | expression.charAt(0) == 'c'){

                    literal.name = expression;

                    // converting C'Test' to Test
                    expression = expression.substring(2, expression.length()-1);

                    // Convert characters to equivalent Hex values. Char A > 65(10) > 41(16)
                    String charHexString = "";
                    for(int i =0;i<expression.length(); i++){
                        char ch = expression.charAt(i);
                        charHexString = charHexString.concat(Integer.toHexString((int) ch));
                    }

                    // populate the literal object
                    literal.length = expression.length();
                    literal.value = charHexString;
                    literal.address = Literal.currentStaticAddress;

                } else if(expression.charAt(0) == 'X' | expression.charAt(0) == 'x'){
                    // *** Handle Hex Literal ***

                    // convert 1e to 1E
                    expression = expression.toUpperCase();

                    literal.name = expression;

                    // converting X'1E' to 1E
                    expression = expression.substring(2, expression.length()-1);

                    // Test if the Hex value is valid
                    if(!Utility.isHex(expression)){
                        return original + " (Error : Invalid Hex value)";
                    }

                    // Test if the Hex value length
                    if(expression.length() % 2 != 0){
                        return original + " (Error : Hex literal value length must be a multiple of 2)";
                    }

                    // populate the literal object
                    literal.length = expression.length()/2;
                    literal.value = expression;
                    literal.address = Literal.currentStaticAddress;
                }

                //  *** Insert literal into literal table ***
                if(literalTable.isEmpty()){
                    // insert if the literal list is empty
                    literalTable.add(literal);
                    Literal.currentStaticAddress++;
                } else {
                    // literal list isn't empty
                    for(int i =0; i<literalTable.size(); i++){
                        // Check for duplicate literals
                        if(literal.name.equals(literalTable.get(i).name)){
                            break;
                        }

                        // Not duplicate literal found and end of the file is reached, so add the literal to the linked list
                        if(!literal.name.equals(literalTable.get(i).name) && i+1 == literalTable.size()){
                            literalTable.add(literal);
//                            System.out.println(literal);
                            Literal.currentStaticAddress++;
                            break;
                        }
                    }
                }

                // literal evaluation successful
                return "valid";
            }
        }

        // Empty operand
        return "valid";
    }

    /**
     * After striping the expression of off @,# and = the remaining expression is evaluated in thie method.
     * Example Expression to be processed: Symbol+Symbol, Symbol+literal, literal+literal
     *
     * @param expression The expression to be processed
     * @param operand The operand we are working with. The Value of the expression will be set as the value of this operand.
     * @param symbolTable see if the symbols are in the symbol table
     * @return If no error is found, "valid" string is returned, otherwise description of the error is returned.
     *         The method is terminated upon an error.
     */
    private static String evaluateExpression(String expression, Operand operand, SymbolTable symbolTable){
         StringTokenizer tokenizer = new StringTokenizer(expression, "+-");

         if((expression.contains("+") || expression.contains("-")) & tokenizer.countTokens() > 1){
            // handles Symbol+Symbol, Symbol+literal

             String token1 = tokenizer.nextToken();
             String token2 = tokenizer.nextToken();

             int token1Value, token2Value;
             boolean token1rflag, token2rflag;

             // Get the value for token1Value and token1rflag
             if(Utility.isInteger(token1)){
                 // if token1 is a numeric literal
                 token1Value = Integer.parseInt(token1);
                 token1rflag = false;
             }else if(SymbolTableUtility.validateSymbol(token1) == SymbolStatus.Valid){
                    // if token1 is a symbol
                    if(symbolTable.search(token1) != null){
                        token1Value = symbolTable.search(token1).getValue();
                        token1rflag = symbolTable.search(token1).getRflag();
                    } else {
                        return token1 + " (Error : Undefined Symbol)";
                    }
             } else {
                 // token1 is neither literal nor symbol
                 return token1 + " (Error : Invalid Symbol)";
             }

             // Get the value for token2Value and token2rflag
             if(Utility.isInteger(token2)){
                 // if token2 is a numeric literal
                 token2Value = Integer.parseInt(token2);
                 token2rflag = false;
             }else if(SymbolTableUtility.validateSymbol(token2) == SymbolStatus.Valid){
                 // if token2 is a symbol
                 if(symbolTable.search(token2) != null){
                     token2Value = symbolTable.search(token2).getValue();
                     token2rflag = symbolTable.search(token2).getRflag();
                 } else {
                     return token2 + " (Error : Undefined Symbol)";
                 }
             } else {
                 // token2 is neither literal nor symbol
                 return token2 + " (Error : Invalid Symbol)";
             }

            // Check for @literal+literal
            if(Utility.isInteger(token1) & Utility.isInteger(token2) & original.contains("@"))
                return original + " (Error : @Literal or #Literal is not legal)";

            // FIX : for expression 2+2 Nbit and Ibit should be 0 and 1
            if(Utility.isInteger(token1) & Utility.isInteger(token2)) {
                operand.Nbit = false;
                operand.Ibit = true;
            }

            // Set the value and flag of the Operand
            if(expression.indexOf('+') >= 0)
                operand.value = token1Value + token2Value;
            else
                operand.value = token1Value - token2Value;

            String relocability = evaluateRelocability(token1rflag, token2rflag, expression);
            if(relocability.equals("true"))
                operand.relocability = true;
            else if(relocability.equals("false"))
                operand.relocability = false;
            else
                return original + " (Error : " + relocability + ")";

        } else {
            // Symbol / single literal

            String token = expression;

            // Check for @literal
            if(original.contains("@") & Utility.isInteger(token)){
                return original + " (Error : @Literal or #Literal is not legal)";
            }

            // Set the value and flag of Operand
             if(Utility.isInteger(token)) {
                 // for Numeric token
                 operand.value = Integer.parseInt(token);
                 operand.relocability = false;
             } else {
                 // for symbolic token
                 if(SymbolTableUtility.validateSymbol(token) == SymbolStatus.Valid){
                     if(symbolTable.search(token) != null){
                         operand.value = symbolTable.search(token).getValue();
                         operand.relocability = symbolTable.search(token).getRflag();
                     } else {
                         return token + " (Error : Undefined Symbol)";
                     }
                 } else {
                     return token + " (Error : Invalid Symbol)";
                 }
             }
        }

        return "valid";
    }

    /**
     * Given two flags this method calculates the relocability.
     *
     * @param token1rflag rflag of first token
     * @param token2rflag rflag of second token
     * @param expression check if the tokens are seperated with + or - character
     * @return returns the calculated the rflag
     */
    private static String evaluateRelocability(boolean token1rflag, boolean token2rflag, String expression){
        // true is absolute; false is relative
        if(expression.indexOf('+') >= 0){
            if(!token1rflag & !token2rflag) // absolute + absolute
                return "false";
            if(!token1rflag & token2rflag)
                return "true";
            if(token1rflag & !token2rflag)
                return "true";
            if(token1rflag & token2rflag)
                return "Relative + Relative = Error";
        } else if(expression.indexOf('-') >= 0){
            if(!token1rflag & !token2rflag)
                return "false";
            if(!token1rflag & token2rflag)
                return "Absolute - Relative = Error";
            if(token1rflag & !token2rflag)  // relative - absolute
                return "true";
            if(token1rflag & token2rflag)
                return "false";
        }

        // unhandled return value
        return null;
    }

}

