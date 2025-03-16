package lexer;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

import lexer.afds.AFD;
import lexer.afds.ExtendedOperator;
import lexer.afds.Identifier;
import lexer.afds.Limiters;
import lexer.afds.LineComment;
import lexer.afds.Number;
import lexer.afds.Operator;
import lexer.afds.ReservedWord;
import lexer.afds.StringLiteral;

public class Lexer {
    private List<Token> tokens;
    private List<AFD> afds;
    private CharacterIterator code;
    public int currentLine;

    public Lexer() {

    }

    public Lexer(String code) {
        tokens = new ArrayList<>();
        afds = new ArrayList<>();
        this.code = new StringCharacterIterator(code);
        this.currentLine = 1;

        afds.add(new LineComment());
        afds.add(new ExtendedOperator());
        afds.add(new Operator());
        afds.add(new Number());
        afds.add(new ReservedWord());
        afds.add(new Identifier());
        afds.add(new Limiters());
        afds.add(new StringLiteral());
    }

    public void skipWhiteSpace() {
        while (code.current() == ' ' || code.current() == '\n' || code.current() == '\t' || code.current() == '\r') {
            if (code.current() == '\n' || (code.current() == '\r' && code.current() == '\n')) {
                currentLine++;
            }
            code.next();
        }
    }

    public List<Token> getTokens() {
        boolean accepted;

        while (code.current() != CharacterIterator.DONE) {
            accepted = false;
            skipWhiteSpace();
            if (code.current() == CharacterIterator.DONE)
                break;
            for (AFD afd : afds) {
                int pos = code.getIndex();
                Token t = afd.evaluate(code, currentLine);
                if (t != null) {
                    accepted = true;
                    tokens.add(t);
                    break;
                } else {
                    code.setIndex(pos);
                }
            }
            if (accepted)
                continue;

            String lexema = "" + code.current();
            code.next();
            while (Character.isLetterOrDigit(code.current())) {
                lexema += code.current();
                code.next();
            }
            throw new RuntimeException(
                    "Erro léxico -> Token não reconhecido na linha: " + currentLine + ": " + lexema);
        }
        return tokens;
    }
}