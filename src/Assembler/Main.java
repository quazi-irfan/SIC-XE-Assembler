package Assembler;

import OperandPkg.Literal;
import SymbolPkg.*;

import java.io.*;
import java.util.LinkedList;

//  NAME  :  Quazi Irfan
//  CLASS  :  CSc 354
//  ASSIGNMENT : 3
//  DUE DATE : 11/4/16
//  INSTRUCTOR :  Dr. Hamer
//  DESCRIPTION : Assignment 3

/**
 * Assembler.Main is the Entry point of Assignment 3 : Pass 1
 *
 * Thic class checks if the SIC Assembly source was supplied via command line.
 * If not then the program requests a file name. If file not found, the program terminates.
 * If found, a static method from Pass1Utility class is called. The last parameter of this method is the output file name.
 * We process every instruction, and print output of Pass 1 in that file.
 *
 */
public class Main {
    public static void main(String[] args) throws IOException{
        SymbolTable symbolTable = new SymbolTable();
        LinkedList<Literal> literalLnkdLst = new LinkedList<>();

        // set input files
        String inputFile;
        if(args.length < 1){
            System.out.println("Missing command like argument.");

            // request the intput SIC/XE Assembly file
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter SIC Assembly file : ");
            inputFile = reader.readLine();
        } else {
            inputFile = args[0];
        }


//        Pass1Utility.populateIntermediateFile(inputFile, symbolTable, literalLnkdLst);
        Pass1Utility.populateIntermediateFile("A3_1.txt", symbolTable, literalLnkdLst);
        System.out.println("************************************************************");
        Pass1Utility.populateIntermediateFile("A3_2.txt", symbolTable, literalLnkdLst);
        System.out.println("************************************************************");
        Pass1Utility.populateIntermediateFile("A3_3.txt", symbolTable, literalLnkdLst);
        System.out.println("************************************************************");
        Pass1Utility.populateIntermediateFile("A3_4.txt", symbolTable, literalLnkdLst);

//        // Populate Symbol Table
//        SymbolTableUtility.populateSymbolTable(symbolTable, "A2_labels.txt");
//        System.out.println("*** SYMBOL TABLE ***\nSymbol\t Value\t rflag\t iflag\t mflag\t");
//        symbolTable.view();
//
//        // Evaluate and print Expressions
//        System.out.println("\n*** EXPRESSIONS ***\nExpresion\t\t Value\t Relocatable n i x");
//        OperandUtility.evaluateOperand(symbolTable, literalLnkdLst, "A2_operands.txt");
//
//        // Print contents of the literal table
//        System.out.println("\n*** LITERAL TABLE ***\nName\t\t Value\t\t Size\t Address");
//        for(Object o : literalLnkdLst){
//            System.out.println(o);
//        }
    }
}
