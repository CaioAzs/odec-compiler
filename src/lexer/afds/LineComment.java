package lexer.afds;

import java.text.CharacterIterator;

import lexer.Token;

public class LineComment extends AFD {

    @Override
    public Token evaluate(CharacterIterator code, int line) {
       
        if (code.current() == '#' && code.next() == '#') {
            return new Token("LINE_COMMENT", readLineComment(code), line); 
        }
        return null;
    }

    private String readLineComment(CharacterIterator code) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("##");
        code.next();
        while (code.current() != CharacterIterator.DONE && code.current() != '\n') {
            stringBuilder.append(code.current());
            code.next();
        }
        return stringBuilder.toString();
    }
}
