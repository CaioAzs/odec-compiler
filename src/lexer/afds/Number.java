package lexer.afds;

import java.text.CharacterIterator;

import lexer.Token;

public class Number extends AFD {


    @Override
    public Token evaluate(CharacterIterator code, int line) {
        String number = "";
        if (code.current() == '-') {
            number += "-";
            code.next();
        }

        if (Character.isDigit(code.current())) {
            number += readNumber(code);

            if (code.current() == '.') {
                number += ".";
                code.next();
                if (Character.isDigit(code.current())) {
                    number += readNumber(code);
                } else {
                    return null;
                }
            }

            if (endNumber(code)) {
                return new Token("NUMBER", number, line);
            }
        }
        return null;
    }

    private String readNumber(CharacterIterator code) {
        StringBuilder number = new StringBuilder();
        while (Character.isDigit(code.current())) {
            number.append(code.current());
            code.next();
        }
        return number.toString();
    }

    private boolean endNumber(CharacterIterator code) {
        return code.current() == ' ' ||
                code.current() == '+' ||
                code.current() == '-' ||
                code.current() == '*' ||
                code.current() == '/' ||
                code.current() == ')' ||
                code.current() == '}' ||
                code.current() == '=' ||
                code.current() == ';' ||
                code.current() == ',' ||
                Character.isLetter(code.current()) ||
                code.current() == CharacterIterator.DONE ||
                code.current() == '\n';

    }
}
