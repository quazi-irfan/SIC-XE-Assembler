package Assembler;

import java.util.ArrayList;

/**
 *
 */
class TRecordList {
    private int count = 0;
    public ArrayList<String > allrecords = new ArrayList<>();
    private String record = "";

    public void add(String objCode, String[] fields){
        if (count == 0){
            record = "T^" + Utility.pad(Integer.parseInt(fields[0], 16), 6);
            count++;
        }

        // start a new t record
        if(count % 10 == 0){
            // update address and add them to the list
            allrecords.add(record);
            record = "T^" + Utility.pad(Integer.parseInt(fields[0], 16), 6);
            count++;
        }

        // we already have a pending t record
        else {
            record = record.concat("^").concat(objCode);
            count++;
        }
    }

}