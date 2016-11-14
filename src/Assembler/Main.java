package Assembler;

import OperandPkg.Literal;
import OperandPkg.OperandUtility;
import SymbolPkg.*;

import java.io.*;
import java.util.LinkedList;

//  NAME  :  Quazi Irfan
//  CLASS  :  CSc 354
//  ASSIGNMENT : 3
//  DUE DATE : 11/4/16
//  INSTRUCTOR :  Dr. Hamer
//  DESCRIPTION : Assignment 4

/**
 * Assembler.Main is the Entry point of Assignment 4 : Pass 2
 *
 * Input : Intermediate file(file format ending with .int) created from assignment 1
 * If not then the program requests a file name. If file not found, the program terminates.
 * If found, a static method from Pass1Utility class is called. The last parameter of this method is the output file name.
 * We process every instruction, and print output of Pass 1 in that file.
 *
 */
public class Main {
    public static void main(String[] args) throws IOException{
        SymbolTable symbolTable = new SymbolTable();
        LinkedList<Literal> literalTable = new LinkedList<>();

//        // Populate Symbol Table
//        System.out.println("*** SYMBOL TABLE ***\nSymbol\t Value\t rflag\t iflag\t mflag\t");
//        BufferedReader symbolReader = new BufferedReader(new FileReader("OldTestFiles/A2_labels.txt"));
//        String symbolLine = symbolReader.readLine();
//        while(symbolLine != null){
//            symbolTable.addLine(symbolLine);
//            symbolLine = symbolReader.readLine();
//        }
//        symbolTable.view();
//
//        // Evaluate and print Expressions
//        System.out.println("\n*** EXPRESSIONS ***\nExpresion\t\t Value\t Relocatable n i x");
//        BufferedReader operandReader = new BufferedReader(new FileReader("OldTestFiles/A2_operands.txt"));
//        String operandLine = operandReader.readLine();
//        while(operandLine != null) {
//            OperandUtility.evaluateOperand(symbolTable, literalTable, operandLine);
//            operandLine = operandReader.readLine();
//        }

        String inputFile = "CS_Func.txt";
        Pass1Utility.populateTableGenerateInt(inputFile, symbolTable, literalTable);

        // print the symbol table and literal table
        System.out.println("> Symbol  Value\trflag\tiflag\tmflag");

        // TODO are the relocaability of registers are false? Does it matter?
        symbolTable.addLine("A  0   false");
        symbolTable.addLine("X  1   false");
        symbolTable.addLine("L  2   false");
        symbolTable.addLine("B  3   false");
        symbolTable.addLine("S  4   false");
        symbolTable.addLine("T  5   false");
        symbolTable.addLine("F  6   false");
        symbolTable.addLine("PC  8   false");
        symbolTable.addLine("SW  9   false");

        symbolTable.view();
        System.out.println("\n> literal\tValue\tlength\taddress");
        for(Literal l : literalTable)
            System.out.println(l);

        System.out.println("\n> Generated Object code");
        Pass2Utility.generateObj(inputFile, symbolTable, literalTable);

//        Pass1Utility.populateTableGenerateInt("A3_1.txt", symbolTable, literalTable);
//        System.out.println("************************************************************");
//        Pass1Utility.populateTableGenerateInt("A3_2.txt", symbolTable, literalTable);
//        System.out.println("************************************************************");
//        Pass1Utility.populateTableGenerateInt("A3_3.txt", symbolTable, literalTable);
//        System.out.println("************************************************************");
//        Pass1Utility.populateTableGenerateInt("A3_4.txt", symbolTable, literalTable);


//        // Print contents of the literal table
//        System.out.println("\n*** LITERAL TABLE ***\nName\t\t Value\t\t Size\t Address");
//        for(Object o : literalTable){
//            System.out.println(o);
//        }

//        // set input files
//        String inputFile;
//        if(args.length < 1){
//            System.out.println("Missing command like argument.");
//
//            // request the intput SIC/XE Assembly file
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            System.out.print("Enter SIC Assembly Intermediate file : ");
//            inputFile = reader.readLine();
//        } else {
//            inputFile = args[0];
//        }
    }
}
