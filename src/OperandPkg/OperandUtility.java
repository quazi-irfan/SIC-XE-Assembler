package OperandPkg;

import SymbolPkg.SymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class OperandUtility{
    public static void evaluateOperand(SymbolTable symbolTable, LinkedList<Literal> literalLnkdLst, String operandFile) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(operandFile));

        String expression = reader.readLine();
        while(expression != null){

            if(expression.charAt(0) != '=') {
                // Handle non-Literal
                Operand operand = new Operand();
                operand.expression = expression;

                expression = expression.toUpperCase();

                if (expression.charAt(0) == '#') {
                    operand.ibit = true;
                    expression = expression.substring(1);
                } else if (expression.charAt(0) == '@') {
                    operand.nbit = true;
                    expression = expression.substring(1);
                } else if (Character.isDigit(expression.charAt(0))) {
                    operand.nbit = false;
                    operand.ibit = true;
                } else {
                    operand.nbit = true;
                    operand.ibit = true;
                }

                if (expression.length() >= 3 && expression.substring(expression.length() - 2, expression.length()).equals(",X")) {
                    if (operand.nbit & !operand.ibit) {
                        System.out.println("@ or # can't be mixed with ,X");
                        return;
                    }
                    operand.xbit = true;
                    expression = expression.substring(0, expression.length() - 2);
                }

                validateExp(expression, operand, symbolTable);

//                print(operand);
                System.out.println(operand);
                expression = reader.readLine();

            } else {
                // Handle Literal
                Literal literal = new Literal();
                literal.name = expression;
                String tempValue = "";

                expression = expression.substring(1);

                if(expression.charAt(0) == 'C' | expression.charAt(0) == 'c'){
                    // Handle Character Literal
                    expression = expression.substring(2, expression.length()-1);

                    for(int i =0;i<expression.length(); i++){
                        char ch = expression.charAt(i);
                        tempValue = tempValue.concat(Integer.toString((int)ch));
                    }

                    literal.length = expression.length();
                    literal.value = tempValue;
                    literal.address = Literal.staticAddress;

                } else if(expression.charAt(0) == 'X' | expression.charAt(0) == 'x'){
                    // Handle Hex Literal
                    expression = expression.substring(2, expression.length()-1);
                    if(expression.length() % 2 != 0){
                        // Hex literal length must divisible by 2
                    }

                    literal.length = expression.length()/2;
                    literal.value = expression;
                    literal.address = Literal.staticAddress;
                }

                // Check duplicate, and insert to Linked List
                if(literalLnkdLst.isEmpty()){
                    literalLnkdLst.add(literal);
                    Literal.staticAddress++;
                    System.out.println(literal);
                } else {
                    for(int i =0; i<literalLnkdLst.size(); i++){
                        if(literal.name.equals(literalLnkdLst.get(i).name)){
                            break;
                        }

                        if(!literal.name.equals(literalLnkdLst.get(i).name) && i+1 == literalLnkdLst.size()){
                            literalLnkdLst.add(literal);
                            Literal.staticAddress++;
                            System.out.println(literal);
                            break;
                        }
                    }
                }

                expression = reader.readLine();
            }
        }
    }

    private static void validateExp(String expression, Operand operand, SymbolTable symbolTable){
        if(expression.indexOf('+') >= 0 || expression.indexOf('-') >= 0){
            // Symbol+Symbol
            StringTokenizer tokenizer = new StringTokenizer(expression, "+-");

            String temp = tokenizer.nextToken();
            int token1Value = Token.getTokenValue(temp, symbolTable);
            boolean token1rflag = Token.getTokenRflag(temp, symbolTable);

            temp = tokenizer.nextToken();
            int token2Value = Token.getTokenValue(temp, symbolTable);
            boolean token2rflag = Token.getTokenRflag(temp, symbolTable);

            // set operands value, and flag
            if(expression.indexOf('+') >= 0)
                operand.value = token1Value + token2Value;
            else
                operand.value = token1Value - token2Value;

            if(evaluateRelocability(token1rflag, token2rflag, expression).equals("true"))
                operand.relocability = true;
            else
                operand.relocability = false;

        } else {
            // Symbol
            operand.value = Token.getTokenValue(expression, symbolTable);
            operand.relocability = Token.getTokenRflag(expression, symbolTable);
        }
    }

    private static String evaluateRelocability(boolean token1rflag, boolean token2rflag, String expression){
        if(expression.indexOf('+') >= 0){
            if(!token1rflag & !token2rflag)
                return "false";
            if(!token1rflag & token2rflag)
                return "true";
            if(token1rflag & !token2rflag)
                return "true";
            if(token1rflag & token2rflag)
                return "error";
        } else if(expression.indexOf('-') >= 0){
            if(!token1rflag& !token2rflag)
                return "false";
            if(!token1rflag & !token2rflag)
                return "error";
            if(token1rflag & !token2rflag)
                return "true";
            if(token1rflag & token2rflag)
                return "false";
        }

        return null;
    }
}

