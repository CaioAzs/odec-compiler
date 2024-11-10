# ODEC COMPILER - Um compilador invertido.

Este projeto é um compilador implementado em Java que realiza a análise léxica, sintática e semântica de uma linguagem personalizada. Ele é capaz de transformar o código dessa linguagem para Java (levando em conta as limitações, e implementações feitas). A linguagem personalizada segue a gramática apresentada abaixo, incluindo instruções de controle como `if`, `else`, `while`, `for`, e comandos de impressão e input.

## Funcionalidades

- **Análise Léxica**: Identifica e cria tokens a partir do código fonte.
- **Análise Sintática**: Estrutura o código de acordo com as regras da gramática livre de contexto (GLC) da linguagem.
- **Análise Semântica**: Verifica SOMENTE a redeclaração de variáveis.
- **Geração de Código**: Transforma o código da linguagem personalizada em código Java, pronto para execução.

## Estrutura do Projeto

O compilador está organizado em pacotes e classes que executam cada etapa do processo de compilação. Abaixo, alguns dos arquivos principais:

- **`Main.java`**: Contém o ponto de entrada do compilador. Ele recebe argumentos para controlar a exibição de tokens e da árvore de análise.
- **`Lexer.java`**: Responsável pela análise léxica, transformando o código em uma sequência de tokens.
- **`Parser.java`**: Realiza a análise sintática, criando uma árvore de análise sintática (AST).
- **`Tree.java`**: Representa a estrutura da AST.
- **Arquivo de entrada**: Code.odec: nele o código em ODEC deve ser inserido, para posterior tradução para Java.
- **Arquivos de Saída**: Código Java gerado a partir do código em ODEC.
 
## REGEX para reconhecimento de Tokens

- **ExtendedOperator**: `(!=|==|<=|>=|<|>|\|\||&&|[=!]=?|[-+*/%]=?)`
- **Identifier**: `[a-zA-Z_][a-zA-Z0-9_]*`
- **Limiters**: `[()\{\};]`
- **LineComment**: `##.*`
- **Number**: `^[0-9]+(\.[0-9]+)?$`
- **Operator**: `[\/\+\-*\%]`
- **ReservedWord**: `\^fi|\^esle|\^elihw|\^rof|\^taolf|\^tni|\^gnirts|\^fiesle|\^START|\^END|\^tnirp|\^nacs`
- **StringLiteral**: `"([^"\\]*(\\.[^"\\]*)*)"`

## Gramática (GLC) da Linguagem

### Estrutura de Programa

```plaintext
<Program> -> START <Bloco> END
<Bloco> -> <Statement> <Bloco> | ε
<Statement> -> <Print> | <If> | <While> | <For> | <Assignment>
<COMMENT> -> "##" (?:[^"\\]|\\.) "\n"
<Print> -> PRINT "(" <Expression> ")" ";"
<If> -> IF "(" <Condition> ")" "{" <Bloco> "}" <ElseIf> <Else>
<ElseIf> -> ELSE_IF "(" <Condition> ")" "{" <Bloco> "}" | ε
<Else> -> ELSE "{" <Bloco> "}" | ε
<While> -> WHILE "(" <CONDITION> ")" "{" <Bloco> "}"
<For> -> FOR "(" <Assignment> ";" <CONDITION> ";" <Assignment_For> ")" "{" <Bloco> "}"
<Assignment_For> -> IDENTIFIER <AssignOperator> <Expression>   //WITHOUT THE SEMICOLON
<Assignment> -> IDENTIFIER <AssignOperator> <Expression> ";"
<DECLARATION> -> TYPE ID ; | TYPE <Assignment>
<CONDITION> -> <Expression> <ComparisonOperator> <Expression>
<SCANNER> -> <SCAN> "{" <TYPE_SCAN> <IDENTIFIER> "}" ";"
<TYPE_SCAN> ->    INT // vira nextInt
                | FLOAT // vira nextFloat
                | STRING_LITERAL // vira nextLine
```

### Expressões (eliminando recursividade a esquerda)

```plaintext
<Expression> -> <Term> <Expression'>
<Expression'> -> "+" <Term> <Expression'> | "-" <Term> <Expression'> | ε
<Term> -> <Factor> <Term'>
<Term'> -> "*" <Factor> <Term'> | "/" <Factor> <Term'> | "%" <Factor> <Term'>
<Factor> -> "(" <Expression> ")" | IDENTIFIER | NUMBER | STRING_LITERAL

```

### Terminais

```plaintext
<AssignOperator> -> "=" | "+=" | "-=" | "*=" | "/=" | "%="
<ComparisonOperator> -> "==" | "!=" | "<=" | ">=" | "<" | ">"
IDENTIFIER -> [a-zA-Z_][a-zA-Z0-9_]*
NUMBER -> ^[0-9]+(\.[0-9]+)?$
STRING_LITERAL -> "[a-zA-Z_][a-zA-Z0-9_]*"
START -> "^START"
END -> "^END"
PRINT -> "^tnirp"
IF -> "^fi"
ELSE -> "^esle"
ELSE_IF -> "^fiesle"
WHILE -> "^elihw"
FOR -> "^rof"
SCAN -> "^nacs"
INT -> "^tni" 
FLOAT -> "^taolf"
```

## Uso

Para compilar o projeto e executar o compilador, siga as etapas:

### Compilação

Compile o projeto com o comando (IMPORTANTE: NAVEGUE ATÉ A PASTA SRC):
```bash
javac *.java
```

### Execução

Execute o compilador com os argumentos `<printTokens>` e `<printTree>` para decidir se quer visualizar os tokens gerados e a árvore de análise sintática:

```bash
java Main.java <printTokens> <printTree>
```

- **`<printTokens>`**: Use `true` para exibir tokens no terminal; `false` caso contrário.
- **`<printTree>`**: Use `true` para exibir a árvore de análise; `false` caso contrário.

### Exemplo

```bash
java Main.java true true
```

Este comando exibirá a lista de tokens gerados e a árvore de análise sintática no console.

## Estrutura de Código

- **Identificadores**: Variáveis devem começar com letras ou sublinhado e podem conter números.
- **Tipos**: Suporta `INT`, `FLOAT`, e `STRING`.
- **Estruturas de Controle**: `if`, `else if`, `else`, `while`, `for`, `scanner`(input), `prints`.
- **Comentários**: Suportados com `##`.

## Exemplo de Código de Entrada

```plaintext
^START
    ^tni a;
    a = 2;
    ^tni b;
    b = 3;
    ^fi(a >= b){
        ^tnirp(a);
    }^fiesle(a <= b){
        ^tnirp(b);
    }^esle{
        ^tnirp("Vish, mo fita");
    }
^END
```

## Saída Esperada (em Code.java)

```java
import java.util.Scanner;

public class Code {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int a;
		a = 2;
		int b;
		b = 3;
		if (a >= b) {
			System.out.println(a);
		} else if (a <= b) {
			System.out.println(b);
		} else {
			System.out.println("Vish, mo fita");
		}

		scanner.close();
	}
}

```

## Desenvolvedores

Caio de Souza Conceição | souzascaio23cs@gmail.com <br>
Lucas Dias Batista | lucasdbatista1@gmail.com
---
