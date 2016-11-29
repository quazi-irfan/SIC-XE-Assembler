package Assembler;

import java.util.ArrayList;

/**
 *
 */
class TRecordList {
    private int count = 0;
    private int length = 0;
    public ArrayList<String > allrecords = new ArrayList<>();
    private String record = "";

    public void add(String objCode, String[] fields){
        // first time
        if (count == 0){
            record = "T^" + Utility.padAddress(Integer.parseInt(fields[0], 16), 6) + "^" + objCode;

            length += objCode.length() / 2;
            count++;
        }

        else {
            // start a new t record
            if(count % 10 == 0){
                // update address and add them to the list

                allrecords.add(insertLengthInfo(record));
                record = "T^" + Utility.padAddress(Integer.parseInt(fields[0], 16), 6) + "^" + objCode;

                length += objCode.length() / 2;
                count++;
            }

            // we already have a pending t record
            else {
                record = record.concat("^").concat(objCode);

                length += objCode.length() / 2;
                count++;
            }
        }
    }

    public String insertLengthInfo(String record){
        StringBuilder sb = new StringBuilder(record);

        sb.insert(9, Utility.padAddress(length, 2).concat("^"));
        length = 0;

        return sb.toString();
    }

    public ArrayList<String> getAllTRecords(){
        // append all the pending records
        if(length > 1)
            allrecords.add(insertLengthInfo(record));

        return allrecords;

    }

    public void terminateTRecord(){
        if(length > 1) {
            allrecords.add(insertLengthInfo(record));
            count = 0;
            record = "";
        }
    }

}