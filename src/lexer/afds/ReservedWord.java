package lexer.afds;

import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Map;

import lexer.Token;

public class ReservedWord extends AFD {

    public static final Map<String, String> reservedWords = new HashMap<>();
    
    static {
        reservedWords.put("^fi", "IF");
        reservedWords.put("^esle", "ELSE");
        reservedWords.put("^elihw", "WHILE");
        reservedWords.put("^rof", "FOR");
        reservedWords.put("^taolf", "FLOAT");
        reservedWords.put("^tni", "INTEGER");
        reservedWords.put("^gnirts", "STRING");
        reservedWords.put("^fiesle", "ELSE_IF");
        reservedWords.put("^START", "START");
        reservedWords.put("^END", "END");
        reservedWords.put("^tnirp", "PRINT");
        reservedWords.put("^nacs", "SCAN");
    }
    
    @Override
    public Token evaluate(CharacterIterator code, int line) {
        String word = readWord(code);
        
        if (reservedWords.containsKey(word) && endOfReservedWord(code)) {
            return new Token(reservedWords.get(word), word, line);
        }

        return null;
    }

    private String readWord(CharacterIterator code) {
        StringBuilder word = new StringBuilder();

        if (code.current() == '^'){
            word.append(code.current());
            code.next();
            if (Character.isLetter(code.current())) {
                word.append(code.current());
                code.next();
    
                while (Character.isLetterOrDigit(code.current())) {
                    word.append(code.current());
                    code.next();
                }
            }
        }
        return word.toString();
    }

    private boolean endOfReservedWord(CharacterIterator code) {
        return code.current() == ' ' ||
                code.current() == '{' ||
                code.current() == '}' ||
                code.current() == '(' ||
                code.current() == ')' ||
                code.current() == '[' ||
                code.current() == ']' ||
                code.current() == ';' ||
                code.current() == ',' ||
                code.current() == '.' ||
                code.current() == CharacterIterator.DONE ||
                code.current() == '\t' ||
                code.current() == '\r' ||
                code.current() == '\n';
                
    }
}