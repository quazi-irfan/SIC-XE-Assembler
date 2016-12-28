package LinkerLoader;

import java.util.HashMap;
import java.util.Map;

public class ExternalSymbolTable {
    public Map<String, Integer[]> CSList =  new HashMap<>();
    public Map<String, Integer[]> SymbolList =  new HashMap<>();

    ExternalSymbolTable(){
        CSList =  new HashMap<>();
        SymbolList =  new HashMap<>();
    }
}
