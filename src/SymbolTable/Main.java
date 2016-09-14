package SymbolTable;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

    static int lineNumber = 1;

    public static void main(String[] args) throws IOException{
        SymbolTable symbolTable = new SymbolTable();

        // READ FROM LABEL.TXT TO POPULATE SYMBOL TABLE
        System.out.println(" ** Populating Symbol table ** ");
        try(BufferedReader inputReader = new BufferedReader(new FileReader("label.txt"))){
            String inputLine = inputReader.readLine();

            // extract symbol info and add it to symbol table
            while(inputLine != null){
                symbolTable.add(inputLine);
                inputLine = inputReader.readLine();
                lineNumber++;
            }
            System.out.println();
        } catch (IOException e){
            System.out.println("Problem opening label.txt");
        }

        // TEST SYMBOL TABLE
        System.out.println(" ** Reading symbols from file **");
        System.out.print("Enter space separated file names : ");
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer consoleToken = new StringTokenizer(console.readLine());

        ArrayList<String> fileNames = new ArrayList<>();
        while (consoleToken.hasMoreTokens()){
            fileNames.add(consoleToken.nextToken());
        }

        for(int i = 0; i<fileNames.size(); i++) {
            try (BufferedReader symbolReader = new BufferedReader(new FileReader(fileNames.get(i)))) {
                System.out.println("\n * Reading File : " + fileNames.get(i));
                String symbolQuery = symbolReader.readLine();

                while (symbolQuery != null) {
                    // REMOVE LEADING WHITE SPACE
                    symbolQuery = symbolQuery.replaceAll("\\s+","");
                    if (symbolTable.validSymbol(symbolQuery)) {
                        Node tempNode = symbolTable.search(symbolQuery);
                        if (tempNode != null) {
                            System.out.println("Found : " + tempNode);
                        } else {
                            System.out.println("Symbol not found : " + symbolQuery);
                        }
                    } else {
                        System.out.println("Invalid Symbol : " + symbolQuery);
                    }
                    symbolQuery = symbolReader.readLine();
                }
            } catch (IOException e) {
                System.out.println("\n * Problem opening file : " + fileNames.get(i));
            }
        }

        // PRINT ALL ENTRIES OF SYMBOL TABLE
        System.out.println("\n ** All entries of Symbol Table ** ");
        System.out.println("Symbol\tValue\tRFlag\tIFlag\tMFlag");
        symbolTable.view();

    }

}
