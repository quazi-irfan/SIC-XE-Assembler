package Assembler;

import OperandPkg.Literal;
import OperandPkg.OperandUtility;
import SymbolPkg.*;

import java.io.*;
import java.util.LinkedList;

//  NAME  :  Quazi Irfan
//  CLASS  :  CSc 354
//  ASSIGNMENT : 2
//  DUE DATE : 10/5/16
//  INSTRUCTOR :  Dr. Hamer
//  DESCRIPTION : Assignment 2 : Expressions

/**
 * Assembler.Main is the Entry point Assignment 2 : Expressions
 *
 * Thic class checks if two arguments were supplied, or it prompts the user to enter them.
 * If <2 arguments were supplied then it terminates the execution.
 * The user has to reload the program to try again.
 *
 * After setting the input file, the program calls static methods to,
 * 1. Populate the Symbol Table
 * 2. Evaluate and print all valid expressions
 * 3. Print the literals
 *
 */
public class Main {
    public static void main(String[] args) throws IOException{
        SymbolTable symbolTable = new SymbolTable();
        LinkedList<Literal> literalLnkdLst = new LinkedList<>();

        // Set input files
        String symbolFile, operandFile;
        if(args.length < 2){
            System.out.println("Missing command like argument.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter Symbol File : ");
            symbolFile = reader.readLine();
            System.out.print("Enter Expressions File : ");
            operandFile = reader.readLine();
        } else {
            symbolFile = args[0];
            operandFile = args[1];
        }

        // Check the existence of both symbol and operand file
        File f = new File(symbolFile);
        if(!f.exists()) {
            System.out.println(symbolFile + " not found. Please rerun the program to try again.");
            return;
        }

        f = new File(operandFile);
        if(!f.exists()) {
            System.out.println(operandFile + " not found. Please rerun the program to try again.");
            return;
        }

        // Populate Symbol Table

        SymbolTableUtility.populateSymbolTable(symbolTable, "labels.txt");
        System.out.println("*** SYMBOL TABLE ***\nSymbol\t Value\t rflag\t iflag\t mflag\t");
        symbolTable.view();

        // Evaluate and print Expressions
        System.out.println("\n*** EXPRESSIONS ***\nExpresion\t\t Value\t Relocatable n i x");
        OperandUtility.evaluateOperand(symbolTable, literalLnkdLst, "operands.txt");

        // Print contents of the literal table
        System.out.println("\n*** LITERAL TABLE ***\nName\t\t Value\t\t Size\t Address");
        for(Object o : literalLnkdLst){
            System.out.println(o);
        }
    }


}
