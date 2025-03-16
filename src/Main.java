import lexer.*;
import syntactic.Parser;
import syntactic.ast.Tree;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java Main <printTokens> <printTree>");
            System.out.println("<printTokens>: true para printar tokens, false caso contrário.");
            System.out.println("<printTree>: true para printar a árvore, false caso contrário.");
            return;
        }

        boolean printTokens = Boolean.parseBoolean(args[0]);
        boolean printTree = Boolean.parseBoolean(args[1]);

        List<Token> tokens;
        String data;
        try {
            String path = "Code.odec";
            data = Files.readString(Paths.get(path));
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return;
        }

        Lexer lexer = new Lexer(data);
        tokens = lexer.getTokens();

        if (printTokens) {
            System.out.println("Tokens:");
            for (Token token : tokens) {
                System.out.println(token);
            }
        }

        Parser parser = new Parser(tokens);
        try {
            Tree tree = parser.parse();

            if (printTree) {
                tree.printTree();
            }

            try (FileWriter writer = new FileWriter("Code.java", false)) {
                writer.write(parser.finalString);
                System.out.println("\nCódigo gerado com sucesso!\n");
            } catch (IOException e) {
                System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
            }
        } catch (RuntimeException e) {
            System.err.println("Erro de análise: " + e.getMessage());
        }
    }
}
