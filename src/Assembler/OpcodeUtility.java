package Assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * OpCodeUtility class provides static methods to read opcode information form instruction.txt
 * It provides two static method : int getFormat(String opcode) and string getHexCode(String opcode)
 */

public class OpcodeUtility {
    /**
     * Get the format code for a given opcode. For example, LDA returns 3
     * @param opcode Opcode to look up.
     * @return Returns opcode if found from instructions.txt, or returns -1
     */
    public static int getFormat(String opcode){
        try(BufferedReader inputReader = new BufferedReader(new FileReader("instructions.txt"))){
            String instruction = inputReader.readLine();
            StringTokenizer tokenizer;
            while(instruction != null){
                tokenizer = new StringTokenizer(instruction, " ");
                if(tokenizer.hasMoreTokens()){
                    if(tokenizer.nextToken().equals(opcode)){
                        return Integer.parseInt(tokenizer.nextToken());
                    }
                }
                instruction = inputReader.readLine();
            }
        } catch (IOException e){
            System.out.println("Problem opening instructions.txt file.");
        }

        return -1;
    }

    /**
     * Get the Hex code for a given opcode. For example, LDA returns 00
     * @param opcode Opcode to look up.
     * @return Returns Hex code as String if found from instructions.txt, or returns null
     */
    public static String getHexCode(String opcode){
        try(BufferedReader inputReader = new BufferedReader(new FileReader("instructions.txt"))){
            String instruction = inputReader.readLine();
            StringTokenizer tokenizer;
            while(instruction != null){
                tokenizer = new StringTokenizer(instruction, " ");
                if(tokenizer.hasMoreTokens()){
                    if(tokenizer.nextToken().equals(opcode)){
                        tokenizer.nextToken();
                        return tokenizer.nextToken();
                    }
                }
                instruction = inputReader.readLine();
            }
        } catch (IOException e){
            System.out.println("Problem opening instructions.txt file.");
        }

        return null;
    }
}
