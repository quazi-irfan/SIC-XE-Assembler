package SymbolPkg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SymbolTableUtility {
    public static void populateSymbolTable(SymbolTable symbolTable, String symbolFile){
        // Populate the Symbol Table
        System.out.println("*** Populating Symbol table : " + symbolFile + " ** ");
        try(BufferedReader inputReader = new BufferedReader(new FileReader(symbolFile))){
            String inputLine = inputReader.readLine();

            // extract symbol info and add it to symbol table
            while(inputLine != null){
                symbolTable.add(inputLine);
                inputLine = inputReader.readLine();
            }
            System.out.println();
        } catch (IOException e){
            System.out.println("Problem opening file : " + symbolFile);
        }

    }

    public static void testSymbolTable(SymbolTable symbolTable, String symbolFile) throws IOException{
        // Test the symbol table with a test symbols
        System.out.println("\n*** Reading symbols from file : " + symbolFile + " ** ");

            try (BufferedReader symbolReader = new BufferedReader(new FileReader(symbolFile))) {
                String symbolQuery = symbolReader.readLine();

                while (symbolQuery != null) {
                    // REMOVE LEADING WHITE SPACE
                    symbolQuery = symbolQuery.replaceAll("\\s+","");

                    SymbolStatus symbolStatus = symbolTable.validateSymbol(symbolQuery);
                    if (symbolStatus == SymbolStatus.Valid) {
                        // Input Symbol is valid. Now look for it in the Symbol Table.
                        Node tempNode = symbolTable.search(symbolQuery);
                        if (tempNode != null) {
                            System.out.println("Found Symbol : " + tempNode);
                        } else {
                            System.out.println("Symbol not found : " + symbolQuery);
                        }
                    } else {
                        // Input Symbol is invalid. Report error.
                        System.out.println("Invalid Symbol : " + symbolQuery + " -> " + symbolStatus.getDetails());
                    }
                    symbolQuery = symbolReader.readLine(); // read the next symbol from the file.
                }
            } catch (IOException e) {
                // Problem opening file to read from
                System.out.println("\n * Problem opening file : " + symbolFile);
            }

    }
}
