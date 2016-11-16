package Assembler;

import OperandPkg.Literal;
import SymbolPkg.*;

import java.io.*;
import java.util.LinkedList;

//  NAME  :  Quazi Irfan
//  CLASS  :  CSc 354
//  ASSIGNMENT : 4
//  DUE DATE : 11/16/16
//  INSTRUCTOR :  Dr. Hamer
//  DESCRIPTION : Assignment 4 : Pass 2

/**
 * Assembler.Main is the Entry point of Assignment 4 : Pass 2
 *
 * Input : A SICXE assembly source.
 * Output : The program will generate 3 files.
 * During Pass 1 : One intermediate file ending in .int with line counter added to the left of every line.
 * During Pass 2 : One update intermediate file ending in .txt extension with object code added to the right of every instruction.
 * And one object file ending in .o extension.
 *
 * If no file is provides as command line argument, the program will ask for a source file.
 * We process every instruction, and print output of Pass 1 in that file.
 *
 */
public class Main {
    public static void main(String[] args) throws IOException{
        SymbolTable symbolTable = new SymbolTable();
        LinkedList<Literal> literalTable = new LinkedList<>();

//        // set input files
//        String inputFile;
//        if(args.length < 1){
//            System.out.println("Missing command like argument.");
//
//            // request the intput SIC/XE Assembly file
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            System.out.print("Enter SIC Assembly Source file : ");
//            inputFile = reader.readLine();
//        } else {
//            inputFile = args[0];
//        }

//        String inputFile = "SICXE Program 4.asm";
//        String inputFile = "CS_Func.asm";
        String inputFile = "Exam2.asm";

        System.out.println("Reading from File : " + inputFile );

        // This function creates an intermediate file in .inc extension, and populates symbol and literal table
        Pass1Utility.generateIntermediate(inputFile, symbolTable, literalTable);

        // print the .inc file
        System.out.println("\n*********** PASS 1 ***********");
        System.out.println("\n> Generated Intermediate File");
        String incFileNmae = inputFile.substring(0, inputFile.indexOf('.')).concat(".int");
        Utility.printFile(incFileNmae);

        // print the symbol table
        System.out.println("\n> Symbol  Value\trflag\tiflag\tmflag");
        int symbolTableSize = symbolTable.size();
        if(symbolTableSize != 0)
            symbolTable.view();
        else
            System.out.println("Empty Symbol Table");

        // print the literal table
        System.out.println("\n> literal\tValue\t\tlength\taddress");
        if(literalTable.size() != 0) {
            for (Literal literal : literalTable)
                System.out.println(literal);
        } else {
            System.out.println("Empty Literal Table");
        }

        // This function creates an updates intermediate file in .txt extension, and object file in .o extension
        Pass2Utility.generateObj(inputFile, symbolTable, literalTable);

        // print the updated intermediate code
        System.out.println("\n\n*********** PASS 2 ***********");
        System.out.println("\n> Adding object code to Intermediate File");
        String txtfileName = inputFile.substring(0, inputFile.indexOf('.')).concat(".txt");
        Utility.printFile(txtfileName);

        // print the symbol table
        if(symbolTable.size() != symbolTableSize) {
            System.out.println("\nUpdated Symbol Table\n> Symbol  Value\trflag\tiflag\tmflag");
            symbolTable.view();
        }
        else
            System.out.println("\nNo external symbol was added during Pass 2.");

        // print the generated object code
        System.out.println("\n> Generated Object Code");
        String ofileName = inputFile.substring(0, inputFile.indexOf('.')).concat(".o");
        Utility.printFile(ofileName);
    }
}
