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
}
