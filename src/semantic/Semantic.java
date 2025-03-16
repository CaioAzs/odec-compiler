package semantic;

import java.util.HashMap;

public class Semantic {
    private final HashMap<String, String> varMap = new HashMap<>();

    public boolean appendVar(String id, String type){
        if (alredyDeclared(id)){
            return false;
        }
        varMap.put(id, type);
        return true;
    }

    public boolean alredyDeclared(String id){
        return varMap.containsKey(id);
    }
}
