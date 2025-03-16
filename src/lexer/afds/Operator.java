package lexer.afds;
import java.text.CharacterIterator;

import lexer.Token;

public class Operator extends AFD {

    @Override
    public Token evaluate(CharacterIterator code, int line) {

        switch (code.current()) {
            case '+':
                code.next();
                return new Token("PLUS", "+", line);
            case '-':
                code.next();
                return new Token("MINUS", "-", line);
            case '*':
                code.next();
                return new Token("MULTIPLY", "*", line);
            case '/':
                code.next();
                return new Token("DIVIDE", "/", line);
            case '%':
                code.next();
                return new Token("MODULUS", "%", line);
            default:
                return null;
        }
    }
}
