package LinkerLoader;

import java.util.ArrayList;

public class LinkerLoader {
    public static Integer CSlength = null;

    public static void Pass1(ExternalSymbolTable externalSymbolTable, String instruction){
        if(instruction.charAt(0) == 'H'){
            String CSlabel = instruction.substring(1, 5); // from 1 to 4
            CSlabel = CSlabel.replaceAll(" ", "");
            Integer CSstartAddress = Main.startingCSAddress;
            CSlength = Integer.parseInt(instruction.substring(11, 17), 16); // from 11 to 16

            externalSymbolTable.CSList.put(CSlabel, new Integer[]{CSstartAddress, CSlength});
        }

        else if(instruction.charAt(0) == 'D'){
            int dRecCount = instruction.substring(1).length() / 10;
            for(int i = 0; i<dRecCount; i++){
                String SymbolLabel = instruction.substring(i*10 + 1, i*10 + 5);
                SymbolLabel = SymbolLabel.replaceAll(" ", "");
                Integer SymbolOffset = Integer.parseInt(instruction.substring(i*10 + 5, i*10 + 11), 16);
                Integer SymbolAddress = Main.startingCSAddress +SymbolOffset;

                externalSymbolTable.SymbolList.put(SymbolLabel, new Integer[]{SymbolAddress, SymbolOffset});
            }
        }

        else if(instruction.charAt(0) == 'E'){
            if(instruction.length() > 1) {
                // Update the execution address
                String executionAddress = instruction.substring(1, 7);
                Main.executionAddres = Main.startMemory + Integer.parseInt(executionAddress, 16);
            }
                // Update the starting address of the next CS
                Main.startingCSAddress += CSlength;

                // Update the total length
                Main.length += CSlength;
        }
    }

    public static Integer CSStartAddress = null;

    public static void Pass2(ExternalSymbolTable externalSymbolTable, ArrayList<String> memoryMap, String instruction){
        if(instruction.charAt(0) == 'H'){
            String CSlabel = instruction.substring(1, 5);
            CSlabel = CSlabel.replaceAll(" ", "");
            CSStartAddress = externalSymbolTable.CSList.get(CSlabel)[0] - Main.startMemory; // 3860(10) = 14432(16)
        }
        else if(instruction.charAt(0) == 'T'){
            Integer Taddress = Integer.parseInt(instruction.substring(1, 7), 16);
            Integer Tlength = Integer.parseInt(instruction.substring(7, 9), 16);
            String Trecord = instruction.substring(9, instruction.length());

            // memory address = CS start address + T record offset
            Integer memoryAddress = CSStartAddress + Taddress;

            for(int i = 0; i<Tlength; i++){
                memoryMap.set(memoryAddress, Trecord.substring(i*2, i*2+2));
                memoryAddress += 1;
            }
        }

        else if(instruction.charAt(0) == 'M'){
            // get the address specified in the M record
            Integer Maddress = Integer.parseInt(instruction.substring(1, 7), 16);
            Integer memoryAddress = CSStartAddress + Maddress;

            // get the six nibbles as a string from the address
            String nibbles = memoryMap.get(memoryAddress).concat(memoryMap.get(memoryAddress+1)).concat(memoryMap.get(memoryAddress+2));

            // get the length of nibbles we need to modify, and resize it if necessary
            Integer length = Integer.parseInt(instruction.substring(7, 9), 16);
            if(length == 5){
                nibbles = nibbles.substring(1);
            }

            // Get the value of the nibble
            Integer value = Integer.parseInt(nibbles, 16);

            // get symbol address
            String SymbolLabel = instruction.substring(10, instruction.length());
            SymbolLabel = SymbolLabel.replaceAll(" ", "");
            Integer SymbolAddress;
            if(externalSymbolTable.CSList.get(SymbolLabel) == null)
                SymbolAddress = externalSymbolTable.SymbolList.get(SymbolLabel)[0];
            else
                SymbolAddress = externalSymbolTable.CSList.get(SymbolLabel)[0];

            // get the sign, and calculate the final value
            char sign = instruction.charAt(9);
            if(sign == '+'){
                value += SymbolAddress;
            } else if(sign == '-'){
                value -= SymbolAddress;
            }

            // Convert the result to hex string
            nibbles = Integer.toHexString(value);

            // resize the string, in case of format 4 instruction
            if(length == 5){
                // restore the first character
                String temp = new String();
                temp = temp.concat(String.valueOf(memoryMap.get(memoryAddress).charAt(0)));

                // pad it to 5 character long
                nibbles = padLeft(nibbles, 5);

                // append the first character
                nibbles = temp.concat(nibbles);
            }

            // append in case of WORD directive
            else {
                nibbles = padLeft(nibbles, 6);
            }

            // replace the 3 memory mapping
            memoryMap.set(memoryAddress, nibbles.substring(0,2));
            memoryMap.set(memoryAddress+1, nibbles.substring(2,4));
            memoryMap.set(memoryAddress+2, nibbles.substring(4,6));

        }

    }

    static String padLeft(String str, int i){
        int padCount =  i - str.length();

        // no padding required
        if(padCount <= 0){
            return str;
        }

        // padding required
        else {
            StringBuilder sb = new StringBuilder();

            for(int j = 0 ;j<padCount; j++){
                sb.append('0');
            }
            sb.append(str);

            return sb.toString();
        }
    }
}
