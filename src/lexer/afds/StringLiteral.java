package lexer.afds;

import java.text.CharacterIterator;

import lexer.Token;

public class StringLiteral extends AFD {

    @Override
    public Token evaluate(CharacterIterator code, int line) {
        if (code.current() == '"') { 
            code.next();
            String stringContent = readString(code);
            if (endString(code)) {
                code.next(); 
                return new Token("STRING_LITERAL", stringContent, line);
            }
        }
        return null;
    }

    private String readString(CharacterIterator code) {
        StringBuilder stringContent = new StringBuilder();
        stringContent.append("\"");
        
        while (code.current() != CharacterIterator.DONE && code.current() != '"') {
            if (code.current() == '\\') {
                code.next();
                if (code.current() != CharacterIterator.DONE) {
                    stringContent.append(code.current());
                }
            } else {
                stringContent.append(code.current());
            }
            code.next();
        }
        stringContent.append("\"");
        
        return stringContent.toString();
    }

    private boolean endString(CharacterIterator code) {
        return code.current() == '"' || code.current() == CharacterIterator.DONE;
    }
}
