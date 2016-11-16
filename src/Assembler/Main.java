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

//        String inputFile = "SICXE Program 4.asm";
//        String inputFile = "CS_Func.asm";
        String inputFile = "A3_4.asm";
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
        System.out.println("\n> Updated Intermediate Code");
        String txtfileName = inputFile.substring(0, inputFile.indexOf('.')).concat(".txt");
        Utility.printFile(txtfileName);

        // print the symbol table
        if(symbolTable.size() != symbolTableSize) {
            System.out.println("\nUpdated Symbol Table\n> Symbol  Value\trflag\tiflag\tmflag");
            symbolTable.view();
        }
        else
            System.out.println("\nNo change in Symbol table during Pass 2.");

        // print the generated object code
        System.out.println("\n> Generated Object Code");
        String ofileName = inputFile.substring(0, inputFile.indexOf('.')).concat(".o");
        Utility.printFile(ofileName);

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

//        Pass1Utility.generateIntermediate("A3_1.asm", symbolTable, literalTable);
//        System.out.println("************************************************************");
//        Pass1Utility.generateIntermediate("A3_2.asm", symbolTable, literalTable);
//        System.out.println("************************************************************");
//        Pass1Utility.generateIntermediate("A3_3.asm", symbolTable, literalTable);
//        System.out.println("************************************************************");
//        Pass1Utility.generateIntermediate("A3_4.asm", symbolTable, literalTable);


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
