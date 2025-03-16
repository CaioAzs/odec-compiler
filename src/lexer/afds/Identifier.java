package lexer.afds;

import java.text.CharacterIterator;

import lexer.Token;

public class Identifier extends AFD {

    @Override
    public Token evaluate(CharacterIterator code, int line) {

        if (Character.isLetter(code.current()) || code.current() == '_') {
            String identifier = readIdentifier(code);
            if (endIdentifier(code)) {
                return new Token("IDENTIFIER", identifier, line);
            }
        }
        return null;
    }

    private String readIdentifier(CharacterIterator code) {
        StringBuilder identifier = new StringBuilder();
        identifier.append(code.current());
        code.next();

        while (Character.isLetterOrDigit(code.current()) || code.current() == '_') {
            identifier.append(code.current());
            code.next();
        }
        return identifier.toString();
    }

    private boolean endIdentifier(CharacterIterator code) {
        return code.current() == ' ' ||
                code.current() == '+' ||
                code.current() == '-' ||
                code.current() == '*' ||
                code.current() == '/' ||
                code.current() == '=' ||
                code.current() == '<' ||
                code.current() == '>' ||
                code.current() == '%' ||
                code.current() == '!' ||
                code.current() == '{' ||
                code.current() == '}' ||
                code.current() == '(' ||
                code.current() == ')' ||
                code.current() == '[' ||
                code.current() == ']' ||
                code.current() == '.' ||
                code.current() == ';' ||
                code.current() == ',' ||
                code.current() == CharacterIterator.DONE ||
                code.current() == '\n';
    }
}