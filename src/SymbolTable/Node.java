package SymbolTable;

public class Node{
    // reference to left and right node
    public Node leftNode, rightNode;

    // this node
    String key;
    public int value;
    public boolean rflag, iflag, mflag;

    Node(){}

    Node(String key){
        if(key.length() > 4){
            key = key.substring(0, 4);
        }

        this.key = key.toUpperCase();
    }

    Node(String key, int value, boolean rflag, boolean iflag, boolean mflag){
        if(key.length() > 4){
            key = key.substring(0, 4);
        }
        this.key = key.toUpperCase();
        this.value = value;
        this.rflag = rflag;
        this.iflag = iflag;
        this.mflag = mflag;
    }

    // HACK printing attributes of a symbol.
    @Override
    public String toString(){
        return this.key + "\t" +
                this.value + "\t\t" +
                ((this.rflag) ? 1 : 0) + "\t\t" +
                ((this.iflag) ? 1 : 0) + "\t\t" +
                ((this.mflag) ? 1 : 0);
    }

    public String getKey(){
        return key;
    }

    // getter & setter : Key
    public void setKey(String key) {
        if(key.length() > 4){
            key = key.substring(0, 4);
        }
        this.key = key.toUpperCase();
    }

    public int getValue() {
        return value;
    }

    // getter & setter : Value
    public void setValue(int value) {
        this.value = value;
    }

    public boolean getRflag() {
        return rflag;
    }

    // getter & setter : rflag
    public void setRflag(boolean rflag) {
        this.rflag = rflag;
    }

    // getter & setter : iflag
    public boolean getIflag() {
        return iflag;
    }

    public void setIflag(boolean iflag) {
        this.iflag = iflag;
    }

    // getter & setter : mflag
    public boolean getMflag() {
        return mflag;
    }

    public void setMflag(boolean mflag) {
        this.mflag = mflag;
    }
}