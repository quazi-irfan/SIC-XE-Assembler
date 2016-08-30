package SymbolTable;

import java.io.*;

public class Main {
    static int lineNumber = 1;

    public static void main(String[] args) throws IOException{
        SymbolTable symbolTable = new SymbolTable();

        // READ FROM LABEL.TXT TO POPULATE SYMBOL TABLE
        System.out.println(" ** Populating Symbol table ** ");
        BufferedReader inputReader = new BufferedReader(new FileReader("label.txt"));
        String inputLine = inputReader.readLine();

        // extract symbol info and add it to symbol table
        while(inputLine != null){
            symbolTable.add(inputLine);
            inputLine = inputReader.readLine();
            lineNumber++;
        }

        // TEST SYMBOL TABLE
        System.out.println("\n ** Testing Symbol table ** ");
        BufferedReader symbolReader = new BufferedReader(new FileReader("symbol.txt"));
        String symbolQuery = symbolReader.readLine();

        while(symbolQuery != null){
            if(symbolTable.validSymbol(symbolQuery)){
                Node tempNode = symbolTable.search(symbolQuery);
                if(tempNode != null) {
                    System.out.println("Found : " + tempNode);
                } else {
                    System.out.println("Symbol not found : " + symbolQuery);
                }
            } else {
                System.out.println("Invalid Symbol : " + symbolQuery);
            }
            symbolQuery = symbolReader.readLine();
        }

        // PRINT ALL ENTRIES OF SYMBOL TABLE
        System.out.println("\n ** All entries of Symbol Table ** ");
        System.out.println("Symbol\tValue\tRFlag\tIFlag\tMFlag");
        symbolTable.view();

    }

}
