package Assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * This Utility class is meant for temporary code segments that doesn't fit in other classes.
 *
 */

public class Utility {
    /**
     *  This method prompts the user the press Enter to continue.
     */
    public static void enterToContinue(){
        System.out.print(" *** Press Enter to continue...");
        try{
            BufferedReader tempHalt = new BufferedReader(new InputStreamReader(System.in));
            tempHalt.readLine();
        } catch (IOException e){
            System.out.println(e);
        }
    }

    /**
     * // TODO ANTI PATTERN; DON'T USE EXCEPTION TO CONTROL PROGRAM FLOW
     * Returns true/false if a given string is an integer(radix 10)
     * @param s Check if the passed string is a integer number
     * @return returns true is the string parameter was a number
     *         returns false if the string was null, or wasn't a number.
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    /**
     * // TODO ANTI PATTERN; DON'T USE EXCEPTION TO CONTROL PROGRAM FLOW
     * @param s Check if the passed string is a Hex number
     * @return returns true is the string parameter was a number
     *         returns false if the string was null, or wasn't a number.
     */
    public static boolean isHex(String s) {
        try {
            Long.parseLong(s, 16);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    /**
     * Given decimal number this fucntion will return a left padded hex number of given length.
     * Requesting padding of a number that can't be accomodated with the length will return wrong value.
     * @param decimalValue The decimal value we need to pad
     * @param length The desired length
     * @return  Returns the hex value of the decimal number with left padded with zero.
     */
    public static String padAddress(int decimalValue, int length){
        int padCount;

        // for negative value, trim some least significant digits
        if(decimalValue < 0){
            String negHexValue = Integer.toHexString(decimalValue);
            String resizedHexValue = negHexValue.substring(negHexValue.length()-length, negHexValue.length());  // fffffc > ffc
            return resizedHexValue.toUpperCase();
        }

        // when hex value is within 10 and FF (inclusive)
        if(decimalValue >= 16 && decimalValue <= 255)
            padCount = length - 2;

        // when hex value is within 100 and FFF (inclusive)
        else if(decimalValue >= 256 && decimalValue <= 4095)
            padCount = length - 3;

        // when hex value is within 1000 and FFFF (inclusive)
        else if(decimalValue >= 4096 && decimalValue <= 65535)
            padCount = length - 4;

        // when hex value is within 0 and F
        else
            padCount = length - 1;

        // add zero's
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<padCount; i++)
            sb.append('0');

        // add the hex value after zero
        sb.append(Integer.toHexString(decimalValue));

        return sb.toString().toUpperCase();
    }

    /**
     * Pad a string to 4 character length.
     * If the string has less than 4 characters, it will add trailing spaces.
     * If the string has more than 4 characters, it will return the first 4 characters.
     * If the string has exactly 4 characters, it will return the string unaltered.
     * @param s Given string to pad.
     * @return Return the padded string.
     */
    public static String padLabel(String s){
        int padCount;

        // if label size < 4
        if(s.length() < 4) {
            padCount = 4 - s.length();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < padCount; i++)
                sb.append(' ');

            s = s.concat(sb.toString());

            return s;
        }

        // if label size > 4
        else if(s.length() > 4) {
            return s.substring(0, 4);
        }

        // if label size = 4
        else {
            return s;
        }
    }

    /**
     * Register values of SICXE computer.
     * Given register name, we get the value of the register.
     * @param s Provide the name of the register.
     * @return Gets the value of the register.
     */
    public static int getRegisterValue(String s){
        HashMap<String, Integer> registerTable= new HashMap<>();
        registerTable.put("A", 0);
        registerTable.put("X", 1);
        registerTable.put("L", 2);
        registerTable.put("B", 3);
        registerTable.put("S", 4);
        registerTable.put("T", 5);
        registerTable.put("F", 6);
        registerTable.put("PC", 8);
        registerTable.put("SW", 9);

        if(!Utility.isInteger(s))
            return registerTable.get(s);
        else
            return Integer.parseInt(s);
    }

    /**
     * Utility function to print a given file.
     * @param fileName Provide a file name.
     * @throws IOException Throws Excetion if problem occures.
     */
    public static void printFile(String fileName) throws IOException{
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line = reader.readLine();
            while (line != null){
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e){
            System.out.println("Problem opening file : " + fileName);
        }

    }

    /**
     * Remove comment from a given insturction.
     * @param instruction The instruction to remove comment from.
     * @return  Return instruction without comment.
     */
    public static String removeComment(String instruction){
        if(instruction.contains("$")) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < instruction.indexOf('$'); i++) {
                sb.append(instruction.charAt(i));
            }
            instruction = sb.toString();
        }

        return instruction;
    }
}
