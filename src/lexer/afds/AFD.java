package lexer.afds;
import java.text.CharacterIterator;

import lexer.Token;

public abstract class AFD {
    
    public abstract Token evaluate(CharacterIterator code, int line);
    
}
