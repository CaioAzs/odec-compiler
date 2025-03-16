package syntactic;

import java.util.List;

import lexer.Token;
import lexer.afds.ExtendedOperator;
import syntactic.ast.Node;
import syntactic.ast.Tree;
import semantic.Semantic;
import utils.Utils;

public class Parser {

    List<Token> tokens;
    Token token;
    Semantic semantic;
    public String finalString = "";

    public Parser(List<Token> tokens) {
        this.tokens = tokens;

    }

    public Tree parse() {
        Node programNode = new Node("Program");
        Tree tree = new Tree(programNode);
        semantic = new Semantic();

        token = getNextToken();
        if (Program(programNode)) {
            System.out.println("\nSintaticamente correta.\n");
            return tree;
        }
        System.out.println("\nSintaticamente incorreto.\n");
        return tree;
    }

    private boolean Program(Node node) {
        if (START(node) && BLOCO(node) && END(node)) {
            return true;
        }
        return false;
    }

    private Token getNextToken() {
        if (tokens.size() > 0) {
            return tokens.remove(0);
        } else
            return null;
    }

    private void erro(String funcao) {
        System.out.printf("Erro sintático: Função '%s' - Token não aceito: '%s' do tipo '%s' na linha %d.%n",
                funcao, token.getLexema(), token.getTipo(), token.getLinha());
    }

    private void errorSemantic(String node, String id, String type) {
        System.out.println("\n\nErro semântico: " + node + id + " - Linha: " + token.getLinha());
    }

    private boolean BLOCO(Node node) {

        Node bloco = node.addNode("BLOCO");
        switch (token.getTipo()) {
            case "IF":
                if (IF(bloco) && BLOCO(bloco)) {
                    return true;
                }
                break;
            case "FOR":
                if (FOR(bloco) && BLOCO(bloco)) {
                    return true;
                }
                break;
            case "WHILE":
                if (WHILE(bloco) && BLOCO(bloco)) {
                    return true;
                }
                break;
            case "INTEGER":
            case "FLOAT":
            case "STRING":
                if (DECLARATION(bloco) && BLOCO(bloco)) {
                    return true;
                }
                break;
            case "PRINT":
                if (PRINT(bloco) && BLOCO(bloco)) {
                    return true;
                }
                break;
            case "SCAN":
                if (SCAN(bloco) && BLOCO(bloco)) {
                    return true;
                }
                break;
            case "LINE_COMMENT":
                if (LINE_COMMENT(bloco) && BLOCO(bloco)) {
                    return true;
                }
            case "IDENTIFIER":
                if (ASSIGNMENT(bloco) && BLOCO(bloco))
                    return true;
            default:
                return true;
        }
        return true;
    }

    private boolean SCAN(Node node) {
        Node ScanNode = node.addNode("SCAN");
        if (matchTipo("SCAN", ScanNode, "") && matchLexema("{", ScanNode, "")) {
            String type = token.getTipo();
            if (TYPE_SCAN(ScanNode)) {
                String id = token.getLexema();
                if (matchTipo("IDENTIFIER", ScanNode, "") && matchLexema("}", ScanNode, "")
                        && matchLexema(";", ScanNode, "")) {
                    if (type.equals("STRING")) {
                        semantic.appendVar(id, type);
                        traduz(String.format("String %s = scanner.nextLine();", id));
                    }
                    if (type.equals("INTEGER")) {
                        semantic.appendVar(id, type);
                        traduz(String.format("int %s = scanner.nextInt();", id));
                    }
                    if (type.equals("FLOAT")) {
                        semantic.appendVar(id, type);
                        traduz(String.format("float %s = scanner.nextFloat();", id));
                    }
                    return true;
                }
            }
        }
        erro("SCAN");
        return false;
    }

    private boolean TYPE_SCAN(Node node) {
        Node type_scan = node.addNode("SCAN");
        return (matchTipo("INTEGER", type_scan, "") || matchTipo("STRING", type_scan, "")
                || matchTipo("FLOAT", type_scan, ""));
    }

    private boolean PRINT(Node node) {
        Node printNode = node.addNode("PRINT");
        if (matchTipo("PRINT", printNode, "System.out.println") &&
                matchLexema("(", printNode, "(") &&
                (Expression(printNode)) && // Aceita STRING_LITERAL ou
                matchLexema(")", printNode, ")") &&
                matchLexema(";", printNode, ";\n") &&
                BLOCO(printNode)) {
            return true;
        }
        erro("PRINT");
        return false;
    }

    private boolean IF(Node node) {
        Node ifNode = node.addNode("IF");

        if (matchTipo("IF", ifNode, "if") &&
                matchLexema("(", ifNode, "(") &&
                CONDITION(ifNode) &&
                matchLexema(")", ifNode, ")") &&
                matchLexema("{", ifNode, "{\n") &&
                BLOCO(ifNode) &&
                matchLexema("}", ifNode, "}\n")) {

            ELSEIF(ifNode);
            ELSE(ifNode);
            return true;
        }

        erro("IF");
        return false;
    }

    private boolean ELSEIF(Node node) {
        Node elseIfNode = node.addNode("ELSE_IF");

        if (matchTipo("ELSE_IF", elseIfNode, "else if") &&
                matchLexema("(", elseIfNode, "(") &&
                CONDITION(elseIfNode) &&
                matchLexema(")", elseIfNode, ")") &&
                matchLexema("{", elseIfNode, "{\n") &&
                BLOCO(elseIfNode) &&
                matchLexema("}", elseIfNode, "}\n")) {
            return true;
        }
        return true; // ε (vazio) é permitido
    }

    private boolean ELSE(Node node) {
        if (token.getTipo().equals("ELSE")) {
            Node elseNode = node.addNode("ELSE");
            if (matchTipo("ELSE", elseNode, "else") &&
                    matchLexema("{", elseNode, "{\n") &&
                    BLOCO(elseNode) &&
                    matchLexema("}", elseNode, "}\n")) {
                return true;
            }
        }
        return true; // ε (vazio) é permitido
    }

    private boolean FOR(Node node) {
        Node forNode = node.addNode("FOR");

        if (matchTipo("FOR", forNode, "for") &&
                matchLexema("(", forNode, "(") &&
                ASSIGNMENT(forNode) &&
                CONDITION(forNode) &&
                matchLexema(";", forNode, ";") &&
                ASSIGNMENT_FOR(forNode) &&
                matchLexema(")", forNode, ")") &&
                matchLexema("{", forNode, "{\n") &&
                BLOCO(forNode) &&
                matchLexema("}", forNode, "}\n")) {
            return true;
        }

        erro("FOR");
        return false;
    }

    private boolean WHILE(Node node) {
        Node whileNode = node.addNode("WHILE");
        if (matchTipo("WHILE", whileNode, "while") && matchLexema("(", whileNode, "(") &&
                CONDITION(whileNode) && matchLexema(")", whileNode, ")") &&
                matchLexema("{", whileNode, "{\n") && BLOCO(whileNode) && matchLexema("}", whileNode, "}\n")) {
            return true;
        }
        erro("WHILE");
        return false;
    }

    private boolean CONDITION(Node node) {
        Node conditionNode = node.addNode("CONDITION");
        if (Expression(conditionNode) &&
                COMPARISON_OPERATOR(conditionNode) &&
                Expression(conditionNode)) {
            return true;
        }
        erro("CONDITION");
        return false;
    }

    private boolean ASSIGNMENT(Node node) {
        Node assignmentNode = node.addNode("ASSIGNMENT");
        if ((IDENTIFIER(assignmentNode)) &&
                ASSIGN_OPERATOR(assignmentNode) &&
                Expression(assignmentNode) &&
                matchLexema(";", assignmentNode, ";\n")) {
            return true;
        }
        erro("ASSIGNMENT");
        return false;
    }

    private boolean ASSIGNMENT_FOR(Node node) {
        Node assignmentNode = node.addNode("ASSIGNMENT_FOR");
        if ((IDENTIFIER(assignmentNode)) &&
                ASSIGN_OPERATOR(assignmentNode) &&
                Expression(assignmentNode)) {
            return true;
        }
        erro("ASSIGNMENT INSIDE FOR LOOP");
        return false;
    }

    private boolean DECLARATION(Node node) {
        Node declarationNode = node.addNode("DECLARATION");

        String type = token.getTipo();
        if (TYPE(declarationNode)) {
            String id = token.getLexema();
            if (matchTipo("IDENTIFIER", declarationNode, token.getLexema())
                    && matchLexema(";", declarationNode, ";\n")) {
                if (semantic.appendVar(id, type)) {
                    return true;
                } else {
                    errorSemantic("DECLARATION", id, type);
                    return false;
                }
            }
        }
        erro("DECLARATION");
        return false;
    }

    // -----------------------EXPRESSAO ELIMINANDO RECURSÃO A
    // ESQUERDA-----------------------------------------

    private boolean Expression(Node node) {
        Node expressionNode = node.addNode("EXPRESSION");
        if (Term(expressionNode) && SecondExpression(expressionNode)) {
            return true;
        }
        erro("Expression");
        return false;
    }

    private boolean SecondExpression(Node node) {
        if (token.getLexema().equals("+") || token.getLexema().equals("-")) {
            Node secondExpressionNode = node.addNode("EXPRESSION'");
            matchLexema(token.getLexema(), secondExpressionNode, token.getLexema());
            if (Term(secondExpressionNode) && SecondExpression(secondExpressionNode)) {
                return true;
            }
        }
        return true; // ε (vazio) é permitido
    }

    private boolean Term(Node node) {
        Node termNode = node.addNode("TERM");
        if (Factor(termNode) && SecondTerm(termNode)) {
            return true;
        }
        erro("Term");
        return false;
    }

    private boolean SecondTerm(Node node) {
        if (token.getLexema().equals("*") || token.getLexema().equals("/") || token.getLexema().equals("%")) {
            Node SecondTermNode = node.addNode("TERM'");
            matchLexema(token.getLexema(), SecondTermNode, token.getLexema());
            if (Factor(SecondTermNode) && SecondTerm(SecondTermNode)) {
                return true;
            }
        }
        return true; // ε (vazio) é permitido
    }

    private boolean Factor(Node node) {
        Node factorNode = node.addNode("FACTOR");
        if (matchLexema("(", factorNode, "(")) {
            if (Expression(factorNode) && matchLexema(")", factorNode, ")")) {
                return true;
            }
        } else if (IDENTIFIER(factorNode) || NUMBER(factorNode) || STRING_LITERAL(factorNode)) {
            return true;
        }
        return false;
    }

    // ----------------------------------------------------------------

    // TERMINAIS

    private boolean LINE_COMMENT(Node node) {
        Node commentNode = node.addNode("COMMENT");
        if (matchTipo("LINE_COMMENT", commentNode, (token.getLexema().replace("##", "//")) + "\n")) {
            return true;
        }
        erro("COMMENT");
        return false;
    }

    public boolean TYPE(Node node) {
        Node typeNode = node.addNode("TYPE");
        if (matchTipo("INTEGER", typeNode, "int ") ||
                matchTipo("FLOAT", typeNode, "float ") ||
                matchTipo("STRING", typeNode, "String ")) {
            return true;
        }
        return false;
    }

    private boolean ASSIGN_OPERATOR(Node node) {
        Node assignOpNode = node.addNode("ASSIGN_OPERATOR");
        if (matchLexema(Utils.getKeyByValue(ExtendedOperator.operators, "MINUS_ASSIGN"), assignOpNode,
                token.getLexema()) ||
                matchLexema(Utils.getKeyByValue(ExtendedOperator.operators, "ASSIGN"), assignOpNode, token.getLexema())
                ||
                matchLexema(
                        Utils.getKeyByValue(ExtendedOperator.operators, "PLUS_ASSIGN"), assignOpNode, token.getLexema())
                ||
                matchLexema(Utils.getKeyByValue(ExtendedOperator.operators, "MULTIPLY_ASSIGN"), assignOpNode,
                        token.getLexema())
                ||
                matchLexema(Utils.getKeyByValue(ExtendedOperator.operators, "DIVIDE_ASSIGN"), assignOpNode,
                        token.getLexema())
                ||
                matchLexema(Utils.getKeyByValue(ExtendedOperator.operators, "MODULUS_ASSIGN"), assignOpNode,
                        token.getLexema())) {
            return true;
        }
        return false;
    }

    private boolean COMPARISON_OPERATOR(Node node) {
        Node compOpNode = node.addNode("COMPARISON_OPERATOR");
        if (matchLexema(Utils.getKeyByValue(ExtendedOperator.operators, "EQUAL"), compOpNode, token.getLexema()) ||
                matchLexema(Utils.getKeyByValue(ExtendedOperator.operators, "NOT_EQUAL"), compOpNode, token.getLexema())
                ||
                matchLexema(Utils.getKeyByValue(ExtendedOperator.operators, "LESS_EQUAL"), compOpNode,
                        token.getLexema())
                ||
                matchLexema(Utils.getKeyByValue(ExtendedOperator.operators, "GREATER_EQUAL"), compOpNode,
                        token.getLexema())
                ||
                matchLexema(Utils.getKeyByValue(ExtendedOperator.operators, "LESS"), compOpNode, token.getLexema()) ||
                matchLexema(Utils.getKeyByValue(ExtendedOperator.operators, "GREATER"), compOpNode,
                        token.getLexema())) {
            return true;
        }
        return false;
    }

    private boolean IDENTIFIER(Node node) {
        Node identifierNode = node.addNode("IDENTIFIER");
        String id = token.getLexema();
        if (matchTipo("IDENTIFIER", identifierNode, token.getLexema())) {
            if (semantic.alredyDeclared(id)) {
                return true;
            } else {
                errorSemantic("Variável não declarada: ", id, token.getTipo());
            }
        }
        //erro("IDENTIFIER");
        return false;
    }

    private boolean NUMBER(Node node) {
        Node numberNode = node.addNode("NUMBER");
        if (token.getTipo() == "NUMBER") {
            if (token.getLexema().contains(".")) {
                return matchTipo("NUMBER", numberNode, (token.getLexema() + "f"));

            } else
                return matchTipo("NUMBER", numberNode, (token.getLexema()));
        }
        //erro("NUMBER");
        return false;
    }

    private boolean STRING_LITERAL(Node node) {
        Node string_literal = node.addNode("STRING_LITERAL");
        if (matchTipo("STRING_LITERAL", string_literal, token.getLexema())) {
            return true;
        }
        //erro("STRING_LITERAL");
        return false;
    }

    private boolean START(Node node) {
        Node startNode = node.addNode("START");
        if (matchTipo("START", startNode,
                "import java.util.Scanner;\n\n public class Code{ \n\tpublic static void main (String[] args){ \n\t\t Scanner scanner = new Scanner(System.in);\n")) {
            return true;
        }
        erro("START");
        return false;
    }

    private boolean END(Node node) {
        Node endNode = node.addNode("END");
        if (matchTipo("END", endNode, "\nscanner.close();\n\t}\n}\n")) {
            return true;
        }
        erro("END");
        return false;
    }

    // Funções de Match

    private boolean matchLexema(String lexema, Node node, String newcode) {
        if (token.getLexema().equals(lexema)) {
            node.addNode(token.getLexema());
            token = getNextToken();
            traduz(newcode);
            return true;
        }
        return false;
    }

    private boolean matchTipo(String tipo, Node node, String newcode) {
        if (token.getTipo().equals(tipo)) {
            node.addNode(token.getLexema());
            traduz(newcode);
            token = getNextToken();
            return true;
        }
        return false;
    }

    public void traduz(String code) {
        finalString = finalString + code;
    }
}