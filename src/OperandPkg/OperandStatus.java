package OperandPkg;

/**
 * OperandStatus Enum
 */
public enum OperandStatus {
    Valid,
    NoAllowed_IndirectImmediate_with_Indexing("(Error : @ or # can't be mixed with ,X)"),
    CharNotFound_LiteralTermination("(Error : Literal must be enclosed with ' and ' character)"),
    Invalid_HexValue("(Error : Invalid Hex value)"),
    Invalid_HexLength("(Error : Hex literal value length must be a multiple of 2)"),

    SymbolNotFound("(Error : Symbol Not found on Symbol Table)"),
    InvalidSymbol("(Error : Invalid Symbol)"),
    NoAllowed_IndirectImmediate_with_Literal("(Error : @Literal or #Literal is not legal)");

    OperandStatus(){}

    String details;
    OperandStatus(String details){
        this.details = details;
    }

    public String getDetails(){
        return details;
    }
}
