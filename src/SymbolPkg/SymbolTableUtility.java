package SymbolPkg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SymbolTableUtility {
    public static void testSymbolTable(SymbolTable symbolTable, String symbolFile) throws IOException{
        // Test the symbol table with a test symbols
        System.out.println("\n*** Reading symbols from file : " + symbolFile + " ** ");

            try (BufferedReader symbolReader = new BufferedReader(new FileReader(symbolFile))) {
                String symbolQuery = symbolReader.readLine();

                while (symbolQuery != null) {
                    // REMOVE LEADING WHITE SPACE
                    symbolQuery = symbolQuery.replaceAll("\\s+","");

                    SymbolStatus symbolStatus = validateSymbol(symbolQuery);
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

    /**
     * Function to check the validity of a symbol. It performes 3 checks.
     * The first check is if the symbol is longer then 10 characters.
     * Second check is if the symbol starts with numeric character.
     * Third check is if any character on the symbol is anything other than alphabate, number or underscore.
     *
     *
     * @param symbol Symbol string to check
     * @return returns SymbolStatus enum
     */
    public static SymbolStatus validateSymbol(String symbol) {
        // check if symbol lenth exceeds 10 characters
        if(symbol.length() > 10){
            return SymbolStatus.ExceedsLength;
        }

        // check if symbol doesn't start with letters
        if(!Character.isAlphabetic(symbol.charAt(0))){
            return SymbolStatus.InvalidStartChar;
        }

        // check for alphabetic, digits and underscore
        for(int i = 1; i<symbol.length(); i++){
            char c = symbol.charAt(i);
            if(!(Character.isAlphabetic(c) | Character.isDigit(c) | (c == '_'))){
                return SymbolStatus.ContainsInvalidChar;
            }
        }

        // otherwise the symbol is valid
        return SymbolStatus.Valid;
    }

    /**
     * Checks if rFlag is either 0, f, false, False, 1, t, true, True
     *
     * @param rflag String value of rFlag
     * @return returns RFlagStatus enum
     */
    public static RFlagStatus validateRFlag(String rflag) {
        if(rflag.length() == 1){
            // accept 0 or 1 as rflag
            if(Character.isDigit(rflag.charAt(0))){
                if(rflag.equals("0"))
                    return RFlagStatus.False;
                if(rflag.equals("1"))
                    return RFlagStatus.True;
                else
                    return RFlagStatus.Invalid;
            }
        }

        // accept tRue or FaLse as rflag
        rflag = rflag.toUpperCase();

        if(rflag.equals("FALSE") | rflag.equals("F"))
            return RFlagStatus.False;
        else if(rflag.equals("TRUE") | rflag.equals("T"))
            return RFlagStatus.True;
        else
            return RFlagStatus.Invalid;
    }
}
