package SymbolPkg;

/**
 * SymbolStatus Enum
 */
public enum SymbolStatus {
    Valid,
    ExceedsLength("Symbol length must be between 4-10 characters long."),
    InvalidStartChar("Symbol must start with letters(A...Z or a...z)"),
    ContainsInvalidChar("Symbol can only contain A-Z,a-z, 0-9 and Underscore'_'");

    SymbolStatus(){}

    private String details;
    SymbolStatus(String details){
        this.details = details;
    }

    public String getDetails(){
        return details;
    }
}
