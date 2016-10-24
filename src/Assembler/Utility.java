package Assembler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
     * Anti Pattern, but it works for now.
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
     * Anti Pattern, but it works for now.
     * @param s Check if the passed string is a Hex number
     * @return returns true is the string parameter was a number
     *         returns false if the string was null, or wasn't a number.
     */
    public static boolean isHex(String s) {
        try {
            Integer.parseInt(s, 16);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
