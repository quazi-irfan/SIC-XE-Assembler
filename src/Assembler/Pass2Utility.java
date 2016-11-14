package Assembler;

import OperandPkg.Literal;
import OperandPkg.Operand;
import OperandPkg.OperandUtility;
import SymbolPkg.Node;
import SymbolPkg.SymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 *  Pass2 class of SICXE Assembler
 */
public class Pass2Utility {
    public static ArrayList<String> MRecordLists = new ArrayList<>();

    public static void generateObj(String inputFile, SymbolTable symbolTable, LinkedList<Literal> literalTable) throws IOException{
        inputFile = inputFile.substring(0, inputFile.indexOf('.')).concat(".int");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String instruction = reader.readLine();
        while (instruction != null){
            String objectCode = "";
            String[] fields = getFields(instruction);

            // if field[1] starts with * we have rached the literal dump section
            if(fields[1] != null && fields[1].equals("*")){
                instruction = reader.readLine();
                continue;
            }

            if(fields[2].equals("START")){
                System.out.println(instruction + " " + "H^" + Utility.pad(fields[1]) +"^" + Utility.pad(Pass1Utility.startAddress, 6) +"^"+
                        Utility.pad(Pass1Utility.programLength, 6));// printing objectcode
                instruction = reader.readLine();
                continue;
            }

            if(fields[2].equals("BASE") | fields[2].equals("EQU") | fields[2].equals("RESB") | fields[2].equals("RESW")){
                System.out.println(instruction + " -");    // printing objectcode
                instruction = reader.readLine();
                continue;
            }

            // handle EXTDEF and EXTREF
            if(fields[2].equals("EXTDEF")){
                System.out.print(instruction + " " + "D"); // printing objectcode
                StringTokenizer tokenizer = new StringTokenizer(fields[3], ", ");
                while(tokenizer.hasMoreTokens()){
                    String symbolName = tokenizer.nextToken();
                    int symbolValue = symbolTable.search(symbolName).getValue();
                    objectCode = objectCode.concat("^").concat(Utility.pad(symbolName)).concat("^").concat(Utility.pad(symbolValue, 6));
                }

                System.out.println(objectCode); // printing objectcode
                instruction = reader.readLine();
                continue;
            } else if(fields[2].equals("EXTREF")){
                System.out.print(instruction + " " + "R");      // printing objectcode

                StringTokenizer tokenizer = new StringTokenizer(fields[3], ",");
                while (tokenizer.hasMoreTokens()){
                    String symbolName = tokenizer.nextToken();
                    Node externalSymbol = new Node(symbolName);
                    externalSymbol.value = 0;
                    externalSymbol.rflag = false;
                    externalSymbol.iflag = false;
                    externalSymbol.mflag = false; // redundant because default initializer
                    symbolTable.add(externalSymbol);

                    objectCode = objectCode.concat("^").concat(Utility.pad(symbolName));
                }

                System.out.println(objectCode);               // printing objectcode
                instruction = reader.readLine();
                continue;
            }

            // handle BYTE C'abc'
            if(fields[2].equals("BYTE")){
                // handle BYTE C'AB'
                if(fields[3].contains("C'")){
                    fields[3] = fields[3].substring(fields[3].indexOf("'")+1, fields[3].lastIndexOf("'"));
                    String charHexValue = "";
                    for(int i = 0; i<fields[3].length(); i++) {
                        charHexValue = charHexValue.concat(Integer.toHexString((int) fields[3].charAt(i)));
                    }

                    System.out.println(instruction + " " + charHexValue.toUpperCase()); // printing objectcode
                    instruction = reader.readLine();
                    continue;
                }

                // handle BYTE X'0F'
                else {
                    fields[3] = fields[3].substring(fields[3].indexOf("'")+1, fields[3].lastIndexOf("'"));
                    objectCode = fields[3];

                    System.out.println(instruction + " " + objectCode.toUpperCase()); // printing objectcode
                    MRecordLists.addAll(generateMRecord(fields, symbolTable));
                    instruction = reader.readLine();
                    continue;
                }

            }
            // handle WORD 97 or WORD ONE-TWO
            else if(fields[2].equals("WORD")){
                OperandUtility.evaluateOperand(symbolTable, literalTable, fields[3]);
                objectCode = Utility.pad(OperandUtility.operand.value, 6);

                System.out.println(instruction + " " + objectCode); // printing objectcode
                MRecordLists.addAll(generateMRecord(fields, symbolTable));
                instruction = reader.readLine();
                continue;
            }

            // All the remaining Opcode
            // lineCounter-field[0]   label-field[1]   opcode-field[2]     operand-field[3]
            if(!fields[2].equals("END")){
                // *** Part 1 :: OpcodeNI
                OperandUtility.evaluateOperand(symbolTable, literalTable, fields[3]); // Doesn't handle old style literal operand
                int opcodeNI = Integer.parseInt(OpcodeUtility.getHexCode(fields[2]), 16)
                        + getAddressingMode(OperandUtility.operand);   // opcode hex + addressing mode
                objectCode = objectCode.concat(Utility.pad(opcodeNI, 2));

                // *** Part 2 :: XBPEDisplacement/Address

                // format 1
                if(OpcodeUtility.getFormat(fields[2]) == 1) {
//                    objectCode = objectCode.concat("0000");

                    System.out.println(instruction + " " + objectCode); // printing objectcode
                    instruction = reader.readLine();
                    continue;
                }

                // format 2
                if(OpcodeUtility.getFormat(fields[2]) == 2){
                    StringTokenizer tokenizer = new StringTokenizer(fields[3], ",");

                    while(tokenizer.hasMoreTokens()){
                        String registerName = tokenizer.nextToken();
                        if(registerName != null){
                            objectCode = objectCode.concat(Integer.toString(Utility.getRegisterValue(registerName)));
                        } else {
                            objectCode = objectCode.concat("0");
                        }
                    }

                    while (objectCode.length()<4) {
                        objectCode = objectCode.concat("0");
                    }

                    System.out.println(instruction + " " + objectCode); // printing objectcode
                    instruction = reader.readLine();
                    continue;
                }

                // X bit
                int XBPE = 0;
                if(OperandUtility.operand.Xbit){
                    XBPE += 8;
                }

                // format 3 & BPEdisplacement
                if(OpcodeUtility.getFormat(fields[2]) == 3){

                    // if there is no operand in a format 3 instruction
                    if(fields[3] == null){
                        objectCode = objectCode.concat("0000");

                        System.out.println(instruction + " " + objectCode);
                        instruction = reader.readLine();
                        continue;
                    }

                    // if there is #1000 or #ARRAY as operand in format 3 instruction
                    // for non-relocatable operand, appended the value at the end of object code
                    if(!OperandUtility.operand.relocability & fields[3].charAt(0) != '='){   // if rflag = false == true AND there is no literal
                        objectCode = objectCode.concat(Utility.pad(OperandUtility.operand.value, 4)); // #ARRAY OR 5 or 5+7

                        System.out.println(instruction + " " + objectCode); // printing objectcode
                    }

                    // if there is LDA ONE-2
                    // for relocatable operand calculate XBPE and displacement
                    else {
                        int targetAddress;
                        if(!(fields[3].charAt(0) == '='))
                            targetAddress = OperandUtility.operand.value - getNextLineCounter(Integer.parseInt(fields[0], 16), fields[2],fields[3]);
                        else
                            targetAddress = findLiteralAddress(literalTable, fields[3]) - getNextLineCounter(Integer.parseInt(fields[0], 16), fields[2],fields[3]);

                        // Use P bit
                        // supported positive range 0 to + 2047 | 000 to 7FF | 0000 0000 0000 to 0111 1111 1111
                        // supported negative rnage -2048 to -1 | 800 to FFF | 0111 1111 1111 to 1111 1111 1111
                        if (targetAddress >= -2048 && targetAddress <= 2047) {
                            XBPE += 2;
                            objectCode = objectCode.concat(Utility.pad(XBPE, 1)).concat(Utility.pad(targetAddress, 3));

                            System.out.println(instruction + " " + objectCode); // printing objectcode
                        }

                        // Use B bit, supported range 0 to 4095
                        else {
                            // load the B register          LDB			#ARRAY
                            // and use the BASE directive   BASE		ARRAY
                            // before using Base relative addressing

                            XBPE += 3;
                            // TODO handle base register addressing mode
                            System.out.println("USE BASE RELATIVE ADDRESSING!");
                        }
                    }
                }

                // if there is +LDA
                // format 4 & Address
                else if(OpcodeUtility.getFormat(fields[2]) == 4) {
                    XBPE += 1;
                    OperandUtility.evaluateOperand(symbolTable, literalTable, fields[3]);
                    objectCode = objectCode.concat(Utility.pad(XBPE, 1)).concat(Utility.pad(OperandUtility.operand.value, 5));
                    System.out.println(instruction + " " + objectCode); // printing objectcode

                    MRecordLists.addAll(generateMRecord(fields, symbolTable));

                }

                // after processing format 3 and format 4 instruction, go to next line
                instruction = reader.readLine();
                continue;
            }

            // END directive has been reached
            else {
                // END directive with operand
                if(fields[3] != null) {
                    OperandUtility.evaluateOperand(symbolTable, literalTable, fields[3]);
                    objectCode = objectCode.concat("E^").concat(Utility.pad(OperandUtility.operand.value, 6));
                } else {
                    objectCode = objectCode.concat("E^");
                }
                System.out.println(instruction + " " + objectCode); // printint object code
                instruction = reader.readLine();
                continue;
            }
        }

        for(String mrecord : MRecordLists)
            System.out.println(mrecord);
    }

    /**
     * Generate M records given the Symbol Table, and the full instruction as an array of string.
     * @param fields Given instruction split into array of strings
     * @param symbolTable symbol table to check for the rflag of the symbol found in the operand
     * @return Returns the list of generated M records.
     */
    private static ArrayList<String> generateMRecord(String[] fields, SymbolTable symbolTable) {
        ArrayList<String> MRecordList = new ArrayList<>();

        int offset = 0;
        String nibbles;
        if(fields[2].equals("WORD") | fields[2].equals("BYTE")){
            nibbles = "06";
            offset = 0;
        }
        else {
            nibbles = "05";
            offset = 1;
        }

        // always M record for external symbol
        for (Node symbol : symbolTable.getAllExternal()) {
            int index = fields[3].indexOf(symbol.getKey()); // find if the symbol exists in the operand

            if (index != -1) {
                char ch = getSign(fields[3], index); // check for sign
                String genMRec = "M^" + Utility.pad(Integer.parseInt(fields[0], 16) + offset, 6) + "^" + nibbles + "^" + ch + Utility.pad(symbol.getKey());
                MRecordList.add(genMRec);
            }
        }

        // if the operand is relocatable, M record for all relocatable symbols
        if(OperandUtility.operand.relocability) {
            for (Node symbol : symbolTable.getAll()) {
                int index = fields[3].indexOf(symbol.getKey()); // find if the symbol exists in the operand
                String controlSection = (symbol.getIflag() ? Pass1Utility.controlSectionName : symbol.getKey()); // identify control section

                if (index != -1 && symbol.rflag) {
                    char ch = getSign(fields[3], index); // check for sign
                    String genMRec = "M^" + Utility.pad(Integer.parseInt(fields[0], 16) + offset, 6) + "^" + nibbles + "^" + ch + Utility.pad(controlSection);
                    MRecordList.add(genMRec);
                    break;
                }
            }
        }

        return MRecordList;
    }

    /**
     * Returns array of strings containing fields of an intermediate instruction.
     * If any field doesn't exists, it's set to null.
     * @param instruction the intermediate instruction to be processed
     * @return an array of string of length 4
     */
    private static String[] getFields(String instruction) {
        // 0 lineCounter     15 label      30 opcode         45 operand

        String[] fields = new String[4]; // all array elements are initialized to 'null'

        StringTokenizer tokenizer = new StringTokenizer(instruction);

        // get line count
        fields[0] = tokenizer.nextToken();

        // get full length label if exists
        if(!(instruction.charAt(15) <= 32)) {
            fields[1] = tokenizer.nextToken();
        }

        // get opcode
        fields[2] = tokenizer.nextToken();

        // get operand if exists
        if(tokenizer.hasMoreTokens())
            fields[3] = tokenizer.nextToken();

        return fields;
    }

    /**
     *
     * @param operand
     * @return
     */
    public static int getAddressingMode(Operand operand){
        if(!operand.Nbit && operand.Ibit)
            return 1;
        else if(operand.Nbit && !operand.Ibit)
            return 2;
        else
            return 3;
    }

    /**
     *
     * @param literalTable
     * @param literalExpression
     * @return
     */
    private static int findLiteralAddress(LinkedList<Literal> literalTable, String literalExpression){
        literalExpression = literalExpression.substring(1);

        for(Literal literal : literalTable){
            if(literal.name.equals(literalExpression))
                return literal.address;
        }

        return -1;
    }

    /**
     *
     * @param currentLineCounter
     * @param opcode
     * @param operand
     * @return
     */
    private static int getNextLineCounter(int currentLineCounter, String opcode, String operand){
        int opcodeFormat = OpcodeUtility.getFormat(opcode);

        // handle LDA STA
        if(opcodeFormat != 0){
            return currentLineCounter + opcodeFormat;
        }

        // handle BYTE, WORD, RESB, RESW
        else {
            if(opcode.equals("BYTE")){
                String temp = operand.substring(operand.indexOf('\'')+1, operand.lastIndexOf('\''));
                if(operand.contains("C"))
                    return currentLineCounter + temp.length();
                else
                    return currentLineCounter + temp.length() / 2;
            }

            else if(opcode.equals("RESW")){
                return currentLineCounter + 3 * Integer.parseInt(operand);
            }

            else if(opcode.equals("RESB")){
                return currentLineCounter + Integer.parseInt(operand);
            }
        }

        return currentLineCounter;
    }

    /**
     *
     * @param operand
     * @param indexOfSymbol
     * @return
     */
    private static char getSign(String operand, int indexOfSymbol){
        try {
            if (operand.charAt(indexOfSymbol - 1) == '-') {
                return '-';
            }
        } catch(StringIndexOutOfBoundsException e) {
            return '+';
        }

        return '+';
    }
}
