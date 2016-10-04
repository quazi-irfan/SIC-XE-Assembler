import OperandPkg.Literal;
import OperandPkg.Operand;
import OperandPkg.OperandUtility;
import SymbolPkg.*;


import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;


//  NAME  :  Quazi Irfan
//  CLASS  :  CSc 354
//  ASSIGNMENT : 1
//  DUE DATE : 9/14/16
//  INSTRUCTOR :  Dr. Hamer
//  DESCRIPTION : Assignment 1 : Symbol Table


/**
 * Main class of Assignment 1. This class is within SymbolTable package.
 *
 * Currently everything is contained within static Main method.
 * Three stages are labeled as Stage X. This code mostly read the file and
 * delegates responsibilities to other classes.
 *
 */
public class Main {
    public static void main(String[] args) throws IOException{
        SymbolTable symbolTable = new SymbolTable();
        LinkedList<Literal> literalLnkdLst = new LinkedList<>();

        if(args.length < 2){
            System.out.println("Please use command \"javac Main labels.txt operands.txt\"");
            return;
        }

        String symbolFile = args[0];
        SymbolUtility.populateSymbolTable(symbolTable, symbolFile);

        String operandFile = args[1];
        OperandUtility.evaluateOperand(symbolTable, literalLnkdLst, operandFile);

        // print the literal linked list
//        System.out.println(literalLnkdLst);
    }

}
