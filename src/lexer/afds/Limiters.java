package lexer.afds;

import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Map;

import lexer.Token;

public class Limiters extends AFD {

    private static final Map<Character, String> limiters = new HashMap<>();

    static {
        limiters.put('(', "LPAREN");
        limiters.put(')', "RPAREN");
        limiters.put('{', "LBRACE");
        limiters.put('}', "RBRACE");
        limiters.put(';', "SEMICOLON");
    }

    @Override
    public Token evaluate(CharacterIterator code, int line) {
        char current = code.current();
        
        if (limiters.containsKey(current)) {
            String tokenType = limiters.get(current);
            code.next();
            return new Token(tokenType, String.valueOf(current), line);
        }
        
        return null;
    }
}
