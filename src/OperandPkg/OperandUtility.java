package OperandPkg;

import Assembler.Utility;
import SymbolPkg.SymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * This class holds the static method to process Expressions.
 */
public class OperandUtility{
    private static String original;

    /**
     * This method does the initial Expression evalution.
     *
     * @param symbolTable Take the symbol table to compare the Exparesions with
     * @param literalLnkdLst Holds the Linked link populated with literals
     * @param operandFile Source file to read the Expressions from
     */
    public static void evaluateOperand(SymbolTable symbolTable, LinkedList<Literal> literalLnkdLst, String operandFile) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(operandFile));

        String expression = reader.readLine();
        while(expression != null && expression.length() > 0){

            original = expression;

            if(expression.charAt(0) != '=') {
                // Handle non-Literal
                Operand operand = new Operand();

                expression = expression.toUpperCase();
                operand.expression = expression;

                // operand n,i anx x bits are 0 by default
                if (expression.charAt(0) == '#') {
                    // for #expression
                    operand.ibit = true;
                    expression = expression.substring(1);
                } else if (expression.charAt(0) == '@') {
                    // for @expression
                    operand.nbit = true;
                    expression = expression.substring(1);
                } else if (Character.isDigit(expression.charAt(0)) & !(expression.contains("+") | expression.contains("-"))) {
                    // for 6
                    operand.nbit = false;
                    operand.ibit = true;
                } else if ((expression.charAt(0) == '+' | expression.charAt(0) == '-') & Character.isDigit(expression.charAt(1))){
                    // for +6 or -6
                    operand.nbit = false;
                    operand.ibit = true;
                } else {
                    // for expression OR expression+expression
                    operand.nbit = true;
                    operand.ibit = true;
                }

                // handles expression,X
                if (expression.length() >= 3 && expression.substring(expression.length() - 2, expression.length()).equals(",X")) {
                    // Check if mixing @ or # with ,X
                    if (original.contains("@") | original.contains("#") ) {
                        System.out.println(original + " (Error : @ or # can't be mixed with ,X)");
                        expression = reader.readLine();
                        continue;
                    }

                    // We have valid expression,X
                    operand.xbit = true;
                    expression = expression.substring(0, expression.length() - 2);
                }

                // evaluate the expression
                String expressionStatus = validateExp(expression, operand, symbolTable);
                if(!expressionStatus.equals("valid")){
                    System.out.println(expressionStatus);
                    expression = reader.readLine();
                    continue;
                };

                System.out.println(operand);
                expression = reader.readLine();

            } else {
                // Handle Literal
                Literal literal = new Literal();

                String tempValue = "";

                expression = expression.substring(1);

                if(expression.charAt(0) == 'C' | expression.charAt(0) == 'c'){
                    // Handle Character Literal
                    StringBuilder strbld = new StringBuilder(expression);
                    strbld.setCharAt(0, 'C');
                    expression = strbld.toString();
                    literal.name = expression;

                    if(expression.charAt(1) != '\'' & expression.charAt(expression.length()-1) != '\''){
                        System.out.println(original + " (Error : Character literal must be enclosed with \' and \' character)");
                        expression = reader.readLine();
                        continue;
                    }

                    expression = expression.substring(2, expression.length()-1);

                    for(int i =0;i<expression.length(); i++){
                        char ch = expression.charAt(i);
                        tempValue = tempValue.concat(Integer.toHexString((int) ch));
                    }

                    literal.length = expression.length();
                    literal.value = tempValue;
                    literal.address = Literal.staticAddress;

                } else if(expression.charAt(0) == 'X' | expression.charAt(0) == 'x'){
                    // Handle Hex Literal
                    expression = expression.toUpperCase();
                    literal.name = expression;

                    if(expression.charAt(1) != '\'' & expression.charAt(expression.length()-1) != '\''){
                        System.out.println(original + " (Error : Character string must be enclosed with \' and \' character)");
                        expression = reader.readLine();
                        continue;
                    }

                    expression = expression.substring(2, expression.length()-1);

                    try{
                        Integer.parseInt(expression, 16);
                    }catch (NumberFormatException e){
                        System.out.println(original + " (Error : Invalid Hex value)");
                        expression = reader.readLine();
                        continue;
                    }

                    if(expression.length() % 2 != 0){
                        System.out.println(original + " (Error : Hex literal value lengh must be a multipel of 2)");
                        expression = reader.readLine();
                        continue;
                    }

                    literal.length = expression.length()/2;
                    literal.value = expression;
                    literal.address = Literal.staticAddress;
                }

                // Check duplicate, and insert to Linked List
                if(literalLnkdLst.isEmpty()){
                    literalLnkdLst.add(literal);
                    Literal.staticAddress++;
//                    System.out.println(literal);
                } else {
                    for(int i =0; i<literalLnkdLst.size(); i++){
                        if(literal.name.equals(literalLnkdLst.get(i).name)){
                            break;
                        }

                        if(!literal.name.equals(literalLnkdLst.get(i).name) && i+1 == literalLnkdLst.size()){
                            literalLnkdLst.add(literal);
                            Literal.staticAddress++;
//                            System.out.println(literal);
                            break;
                        }
                    }
                }

                expression = reader.readLine();
            }
        }
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
    private static String validateExp(String expression, Operand operand, SymbolTable symbolTable){
        StringTokenizer tokenizer = new StringTokenizer(expression, "+-");

         if((expression.contains("+") || expression.contains("-")) & tokenizer.countTokens() > 1){
            // handles Symbol+Symbol, Symbol+literal

             String token1 = tokenizer.nextToken();

            int token1Value = Token.getTokenValue(token1, symbolTable);
            if(token1Value == -1) {
                return token1 + " (Error : Symbol Not found on Symbol Table)";
            } else if(token1Value == -2){
                return token1 + " (Error : Invalid Symbol)";
            }
            boolean token1rflag = Token.getTokenRflag(token1, symbolTable);

            String token2 = tokenizer.nextToken();
            int token2Value = Token.getTokenValue(token2, symbolTable);
            if(token2Value == -1) {
                return token2 + " (Error : Symbol Not found on Symbol Table)";
            } else if(token2Value == -2){
                return token2 + " (Error : Invalid Symbol)";
            }
            boolean token2rflag = Token.getTokenRflag(token2, symbolTable);

             if(Utility.isInteger(token1) & Utility.isInteger(token2) & original.contains("@"))
                 return original + " (Error : @Literal or #Literal is not illegal)";

             if(Utility.isInteger(token1) & Utility.isInteger(token2))
                 operand.nbit = false;

            // Set the value and flag of Operand
            if(expression.indexOf('+') >= 0)
                operand.value = token1Value + token2Value;
            else
                operand.value = token1Value - token2Value;

            String relocability = evaluateRelocability(token1rflag, token2rflag, expression);
            if(relocability.equals("true"))
                operand.relocability = true;
            else if(relocability.equals("false"))
                operand.relocability = false;
            else{
                return original + " (Error : " + relocability + ")";
            }


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
             } else {
                 // for symbolic token
                 operand.value = Token.getTokenValue(token, symbolTable);

                 if(operand.value  == -1) {
                     return token + " (Error : Symbol Not found on Symbol Table)";
                 } else if(operand.value == -2){
                     return token + " (Error : Invalid Symbol)";
                 }
             }


            operand.relocability = Token.getTokenRflag(token, symbolTable);
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

        return null;
    }

}

