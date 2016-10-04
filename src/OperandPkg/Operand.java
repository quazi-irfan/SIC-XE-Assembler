package OperandPkg;

public class Operand {
    public String expression;
    public int value;
    public boolean relocability, nbit, ibit, xbit;

    public String toString() {
        return expression + " " +
                value + " " +
                (relocability ? "Relative":"Absolute") + " " +
                (nbit? "1":"0") + " " +
                (ibit? "1":"0") + " " +
                (xbit? "1":"0");
    }
}
