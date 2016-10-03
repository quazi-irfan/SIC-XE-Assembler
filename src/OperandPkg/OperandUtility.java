package OperandPkg;

import SymbolPkg.SymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class OperandUtility{
    public static void evaluateOperand(SymbolTable symbolTable, LinkedList<Operand> literalLnkdLst, String operandFile) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(operandFile));

        String expression = reader.readLine();
        while(expression != null){
            Operand operand = new Operand();
            operand.expression = expression;

            if(expression.charAt(0) == '=') {
                expression = reader.readLine();
                continue;
            }

            if(expression.charAt(0) != '=')
                expression = expression.toUpperCase();

            if(expression.charAt(0) == '#'){
                operand.ibit = true;
                expression = expression.substring(1);
            }
            else if(expression.charAt(0) == '@'){
                operand.nbit = true;
                expression = expression.substring(1);
            } else if(Character.isDigit(expression.charAt(0))){
                operand.nbit = false;
                operand.ibit = true;
            }
            else {
                operand.nbit = true;
                operand.ibit = true;
            }

            if(expression.length() >= 3 && expression.substring(expression.length()-2, expression.length()).equals(",X")){
                if(operand.nbit & !operand.ibit){
                    System.out.println("@ or # can't be mixed with ,X");
                    return;
                }
                operand.xbit = true;
                expression = expression.substring(0, expression.length()-2);
            }

            validateExp(expression, operand, symbolTable);

            print(operand);

            // go to the next line
            expression = reader.readLine();
        }
    }

    private static void validateExp(String expression, Operand operand, SymbolTable symbolTable){
        if(expression.indexOf('+') >= 0 || expression.indexOf('-') >= 0){

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
            operand.value = Token.getTokenValue(expression, symbolTable);
            operand.relocability = Token.getTokenRflag(expression, symbolTable);
        }
    }

    public static void print(Operand operand){
        System.out.print( "\n" + operand.expression + "\t\t" + operand.value + "\t");
        System.out.print( operand.relocability? "Relative" : "Absolute" );
        System.out.print( operand.nbit? " 1 ":" 0 ");
        System.out.print( operand.ibit? " 1 ":" 0 ");
        System.out.print( operand.xbit? " 1 ":" 0 ");
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

