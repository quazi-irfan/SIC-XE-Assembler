package LinkerLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static Integer startMemory = Integer.parseInt("03860", 16);
    public static Integer startingCSAddress = startMemory;
    public static Integer executionAddres = null;
    public static Integer length = 0;

    public static void main(String[] args) throws IOException{
        ExternalSymbolTable externalSymbolTable = new ExternalSymbolTable();

//        String[] objectPrograms = {"prog1.txt", "func1.txt"};  // Source : Loading and Linking Example 4 a5
//        String[] objectPrograms = {"prog2.txt", "func2.txt"}; // Source : Loading and Linking Example 2 c

//        String[] objectPrograms = args;

        // Pass 1
        for(String fileName : args){
            try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String instruction = reader.readLine();
                while(instruction != null){
                    LinkerLoader.Pass1(externalSymbolTable, instruction);
                    instruction = reader.readLine();
                }
            } catch (IOException e){
                System.out.println("Problem opening file during Pass 1.");
            }
        }

        // memory map; initializing it to ??
        int rowCount = (int) Math.ceil((double)length/16);
        ArrayList<String> memoryMap = new ArrayList<>();
        for(int i = 0; i<rowCount * 16; i++){
            memoryMap.add("??");
        }

        // Pass 2
        for(String fileName : args){
            try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String instruction = reader.readLine();
                while(instruction != null){
                    LinkerLoader.Pass2(externalSymbolTable, memoryMap, instruction);
                    instruction = reader.readLine();
                }
            } catch (IOException e){
                System.out.println("Problem opening file during Pass 2.");
            }
        }

        // print the memory map
        System.out.println("Memory Layout : ");
        System.out.println("      0  1  2  3  4  5  6  7  8  9  A  B  C  D  E  F");
        for(int i = 0; i<rowCount; i++){
            System.out.print(Integer.toHexString(startMemory + (i*16)).substring(0, 3).concat("X "));
            for(int j = 0; j< 16; j++) {
                System.out.print(memoryMap.get(i*16 + j) + " ");
            }
            System.out.println();
        }

        // Print the Execution Address
        System.out.println("\nExecution Address : " + Integer.toHexString(executionAddres));

        // Print the External Symbol Table
        System.out.println("\nExternal Symbol Table : ");
        System.out.println("CSECT\tSYMBOL\tADDR\tCSADDR\tLDADDR\tLENGTH");
        for(Map.Entry<String, Integer[]> m : externalSymbolTable.CSList.entrySet()){
            String CSlabel = m.getKey();
            String CSstartAddress = Integer.toHexString(m.getValue()[0]);
            String CSlenth = Integer.toHexString(m.getValue()[1]);

            System.out.printf("%-25s%-18s%s\n", CSlabel, CSstartAddress, CSlenth);
        }

        for(Map.Entry<String, Integer[]> m : externalSymbolTable.SymbolList.entrySet()){
            String SymbolLabel = m.getKey();
            String SymbolAddress = Integer.toHexString(m.getValue()[0]);
            String SymbolOffset = Integer.toHexString(m.getValue()[1]);

            System.out.printf("%12s%8s%18s\n", SymbolLabel, SymbolOffset, SymbolAddress);
        }
    }
}
