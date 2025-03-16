package lexer.afds;

import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Map;

import lexer.Token;

public class ExtendedOperator extends AFD {

    public static final Map<String, String> operators = new HashMap<>();

    static {
        // Operadores de comparação
        operators.put("==", "EQUAL");
        operators.put("!=", "NOT_EQUAL");
        operators.put("<=", "LESS_EQUAL");
        operators.put("<", "LESS");
        operators.put(">=", "GREATER_EQUAL");
        operators.put(">", "GREATER");

        // Operadores lógicos
        operators.put("&&", "AND");
        operators.put("||", "OR");
        operators.put("!", "NOT");

        // Operadores de atribuição
        operators.put("=", "ASSIGN");
        operators.put("+=", "PLUS_ASSIGN");
        operators.put("-=", "MINUS_ASSIGN");
        operators.put("*=", "MULTIPLY_ASSIGN");
        operators.put("/=", "DIVIDE_ASSIGN");
        operators.put("%=", "MODULUS_ASSIGN");
    }

    @Override
    public Token evaluate(CharacterIterator code, int line) {
        StringBuilder potentialOperator = new StringBuilder();
        potentialOperator.append(code.current());

        // Verifica se o operador tem um próximo caractere e tenta formar um operador de dois caracteres
        char nextChar = code.next();
        if (nextChar != CharacterIterator.DONE) {
            potentialOperator.append(nextChar);
        }

        // Tenta encontrar um operador de dois caracteres
        String operator = potentialOperator.toString();
        if (operators.containsKey(operator)) {
            code.next(); // Avança o iterador para confirmar o operador completo
            return new Token(operators.get(operator), operator, line);
        }

        // Caso contrário, verifica se é um operador de um caractere (primeiro caractere)
        operator = String.valueOf(potentialOperator.charAt(0));
        if (operators.containsKey(operator)) {
            return new Token(operators.get(operator), operator, line);
        }

        return null; // Nenhum operador reconhecido
    }
}
