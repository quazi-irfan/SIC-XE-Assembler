package OperandPkg;

/**
 * Each Operand is a processed non-literal Expression
 */
public class Operand {
    public String expression;
    public int value;
    public boolean relocability, nbit, ibit, xbit;

    public boolean errorflag;
    public boolean errorStatus;

    /*
     * This method returns a formatted operand attribute
     */
    public String toString() {
        String output = String.format("%1$-16s %2$-7d %3$-11s %4$s %5$s %6$s",
                expression,
                value,
                (relocability ? "Relative":"Absolute"),
                (nbit? "1":"0"),
                (ibit? "1":"0"),
                (xbit? "1":"0"));

        return output;
    }
}
