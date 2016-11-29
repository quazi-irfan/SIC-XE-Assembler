package SymbolPkg;

import Assembler.Utility;

/**
 * Each Node is an entry to the Binary Search Tree.
 */

public class Node{
    // reference to left and right node
    public Node leftNode, rightNode;

    // this node
    String key;
    public int value;
    public boolean rflag = true, iflag = true, mflag; // default is false

    /**
     * Default no parameter constructor
     */
    public Node(){}

    /**
     * Node constructor accepting the 'key' string. Key is the symbol in our case.
     * @param key Pass a String of any length, and the string will be truncated and set to uppercase.
     */
    public Node(String key){
        if(key.length() > 4){
            key = key.substring(0, 4);
        }

        this.key = key.toUpperCase();
    }

    /**
     * Node constructor that accepts all parameters of a Node.
     *
     * @param key This string the key of the nodes in out Binary tree.
     * @param value Value of the Symbol
     * @param rflag rFlag of the symbol
     * @param iflag iFlag of the symbol
     * @param mflag mFlag of the symbol
     */
    public Node(String key, int value, boolean rflag, boolean iflag, boolean mflag){
        if(key.length() > 4){
            key = key.substring(0, 4);
        }
        this.key = key.toUpperCase();
        this.value = value;
        this.rflag = rflag;
        this.iflag = iflag;
        this.mflag = mflag;
    }

    /**
     * Overridden method from Object class.
     * @return A space separated attributes of 'this' node is returned as String
     */
    @Override
    public String toString(){
        String output = String.format("%1$-8s %2$-9s %3$-7d %4$-7d %5$d",
                key,
                Utility.padAddress(value, 5),
//                Integer.toHexString(value).toUpperCase(),
                (this.rflag) ? 1 : 0,
                (this.iflag) ? 1 : 0,
                (this.mflag) ? 1 : 0);
        return output;
    }

    /**
     * Getter method of Key of this Node.
     * @return Returns the Key
     */
    public String getKey(){
        return key;
    }

    /**
     * Setter method of Key of this Node.
     * @param key Set the key of this Node.
     */
    public void setKey(String key) {
        if(key.length() > 4){
            key = key.substring(0, 4);
        }
        this.key = key.toUpperCase();
    }

    /**
     * Getter method of the value of this Node.
     * @return Returns the value of this Node.
     */
    public int getValue() {
        return value;
    }

    /**
     * Setter method of the value of this Node.
     * @param value Set the value of this Node.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Getter method of the rFlag of this Node.
     * @return Get the rFlag of this Node.
     */
    public boolean getRflag() {
        return rflag;
    }

    /**
     * Setter method of the rFlag of this Node.
     * @param rflag Set the rFlag of this Node.
     */
    public void setRflag(boolean rflag) {
        this.rflag = rflag;
    }

    /**
     * Getter method of the iFlag of this Node.
     * @return Get the iFlag of this Node.
     */
    public boolean getIflag() {
        return iflag;
    }

    /**
     * Setter method of the iFlag of this Node.
     * @param iflag Set the iFlag of this Node.
     */
    public void setIflag(boolean iflag) {
        this.iflag = iflag;
    }

    /**
     * Getter method of the mFlag of this Node.
     * @return Get the mFlag of this Node.
     */
    public boolean getMflag() {
        return mflag;
    }

    /**
     * Setter method of the mFlag of this Node.
     * @param mflag Set the mFlag of this Node.
     */
    public void setMflag(boolean mflag) {
        this.mflag = mflag;
    }
}