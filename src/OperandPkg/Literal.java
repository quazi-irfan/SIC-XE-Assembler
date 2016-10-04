package OperandPkg;

public class Literal {
    public static int staticAddress = 1;

    String name, value;
    int length, address;

    public String toString() {
        return name + " " + value + " " + length + " " + address;
    }
}

