package SymbolPkg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SymbolUtility {
    public static void populateSymbolTable(SymbolTable symbolTable, String symbolFile){
        // Populate the Symbol Table
//        System.out.println(" ** Populating Symbol table ** ");
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
        // Stage 2 : TEST SYMBOL TABLE
        System.out.println(" ** Reading symbols from file **");
//        System.out.print("Enter space separated file names : ");
//        InputStreamReader consoleInput = new InputStreamReader(System.in);
//        BufferedReader consoleReader2 = new BufferedReader(consoleInput);
//        StringTokenizer consoleToken = new StringTokenizer(consoleReader2.readLine());
//
//        ArrayList<String> fileNames = new ArrayList<String>();
//        while (consoleToken.hasMoreTokens()){
//            fileNames.add(consoleToken.nextToken());
//        }

//        consoleInput.close();


            try (BufferedReader symbolReader = new BufferedReader(new FileReader(symbolFile))) {
                System.out.println(" * Reading File : " + symbolFile);
                String symbolQuery = symbolReader.readLine();

                while (symbolQuery != null) {
                    // REMOVE LEADING WHITE SPACE
                    symbolQuery = symbolQuery.replaceAll("\\s+","");

                    String symbolStatus = symbolTable.validateSymbol(symbolQuery);
                    if (symbolStatus.equals("valid")) {
                        Node tempNode = symbolTable.search(symbolQuery);
                        if (tempNode != null) {
                            System.out.println("Found Symbol : " + tempNode);
                        } else {
                            System.out.println("Symbol not found : " + symbolQuery);
                        }
                    } else {
                        System.out.println("Invalid Symbol : " + symbolQuery + " -> " + symbolStatus);
                    }
                    symbolQuery = symbolReader.readLine();
                }
            } catch (IOException e) {
                System.out.println("\n * Problem opening file : " + symbolFile);
            }

//            if(i+1 < fileNames.size()){
//                System.out.println(" * Press Enter to see the next file.");
//                try{
//                    BufferedReader tempHalt = new BufferedReader(new InputStreamReader(System.in));
//                    tempHalt.readLine();
//                } catch (IOException e){
//                    System.out.println(e);
//                }
//            }


//        try{
//            System.out.println("\n * Press enter to see all symbol table entries.");
//            BufferedReader tempHalt = new BufferedReader(new InputStreamReader(System.in));
//            tempHalt.readLine();
//        } catch (IOException e){
//            System.out.println(e);
//        }

    }
}
