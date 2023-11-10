import Excecoes.ExcecaoCaracteres;
import Excecoes.ExcecaoExpressao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
    private ArrayList<String> expressionArray;
    private LinkedHashSet<Character> propositions;
    private int numberPropositions;
    private HashMap<Character, ArrayList<Boolean>> valueProposition;

    public Expression(String expressao) throws ExcecaoCaracteres, ExcecaoExpressao {
        String tempExpression = expressao;
        tempExpression = tempExpression.trim().replaceAll(" ", "").toLowerCase(); // sanitize

        // regex só aceita letras minusculas, ~ e ^, caso contrario, joga uma exceção de caracteres
        Pattern pattern = Pattern.compile("^[a-z~^]+$");
        Matcher matcher = pattern.matcher(tempExpression);
        if (! matcher.find()) {
            throw new ExcecaoCaracteres();
        }

        this.propositions = new LinkedHashSet<>();
        this.numberPropositions = 0;
        getPropositions(tempExpression); // Adiciona as proposições em um hashset e conta o tamanho do hashset

        // Testa a expressão para verificar se os operadores e as proposições estão corretos, caso contrario, joga uma excecao de expressão
        if (! validateExpression(tempExpression)) {
            throw new ExcecaoExpressao();
        }

        // Transforma a string de entrada em um array de Strings formado pelos caracteres.
        this.expressionArray = new ArrayList<>();
        for (int i = 0; i < tempExpression.length(); i++) {
            this.expressionArray.add(tempExpression.substring(i, i + 1));
        }

        this.valueProposition = new HashMap<>();

        System.out.println("Expressão: " + this.expressionArray);
        System.out.println("Proposições: " + this.propositions);
        System.out.println("Número de proposições: " + this.numberPropositions);


        createTable();
    }

    private void getPropositions(String expression) {
        // Adiciona as proposições em um hashset e conta o tamanho do hashset
        for (char c : expression.toCharArray()) {
            if (! (c == '^' || c == 'v' || c == '~')) {
                this.propositions.add(c);
            }
        }
        this.numberPropositions = propositions.size();
    }

    private boolean validateExpression(String expression) {
        System.out.println(expression);
        // Testa a expressão para verificar se os operadores e as proposições estão corretos
        // Se a expressão não tiver nada, retorna false
        if (this.numberPropositions == 0) {
            System.out.println("Não há proposições na expressão");
            return false;
        }
        boolean expectProposition = false; // Qualquer letra que não seja 'v'
        boolean expectOperator = false; // Operadores '^' ou 'v'
        boolean expectNot = false; // Negação
        for (int i = 0; i < expression.length(); i++) {
            // Avalia o comeco da expressão
            if (i == 0 && (expression.charAt(i) == 'v' || expression.charAt(i) == '^')) {
                // Testa se o primeiro caracter da expressão começa com operadores ^ e v
                System.out.println("Não se inicia com operadores ^ e v");
                return false;
            } else if (i == 0 && Character.isLetter(expression.charAt(i))) {
                // Se o começo for uma letra, espera um operador ^ ou v após
                expectOperator = true;
                expectProposition = false;
                expectNot = false;
                continue;
            } else if (i == 0 && expression.charAt(i) == '~') {
                // Se o começo for um operador de negação, espera uma proposicao após
                expectOperator = false;
                expectProposition = true;
                expectNot = false;
                continue;
            }

            // Avalia o final da expressão
            if (i == (expression.length() - 1) && (expression.charAt(i) == 'v' || expression.charAt(i) == '^' || expression.charAt(i) == '~')) {
                // O último caracter não deve ser 'v', '^' ou '~'
                System.out.println("Nao se finaliza com operadores ^, v e ~");
                return false;
            }

            // Avalia o caracter diferentes de '~', '^' e 'v'
            if (expectProposition && Character.isLetter(expression.charAt(i)) && expression.charAt(i) != 'v') {
                // Proposição no lugar correto
                // Um operador é esperado após uma proposição
                expectOperator = true;
                expectProposition = false;
                expectNot = false;
            } else if (! expectProposition && Character.isLetter(expression.charAt(i)) && expression.charAt(i) != 'v') {
                // Se não é esperanda uma proposição, mas recebe uma, a expressão é inválida
                System.out.println("Não pode haver duas proposições seguidas");
                return false;
            }

            // Avalia o caracter '~'
            if (expectNot && expression.charAt(i) == '~') {
                // '~' no lugar correto
                // Uma proposição é esperada após uma negação válida
                expectOperator = false;
                expectProposition = true;
                expectNot = false;
                continue;
            } else if (! expectNot && expression.charAt(i) == '~') {
                // Se não é esperado um operador de negação, mas recebe um, a expressão é inválida
                System.out.println("Negacao no local errado");
                return false;
            }

            // Avalia o caracter '^' e 'v'
            if (expectOperator && (expression.charAt(i) == '^' || expression.charAt(i) == 'v') && i != (expression.length() - 1)) {
                // Operador no lugar correto
                // Uma negação ou uma proposição é esperada após um operador
                expectOperator = false;
                expectProposition = true;
                expectNot = true;
                continue;
            } else if (! expectOperator && (expression.charAt(i) == '^' || expression.charAt(i) == 'v')) {
                // Se não é esperando um operador, mas recebe um, a expressão é inválida
                System.out.println("Nao pode haver dois operadores ^ ou v seguidos");
                return false;
            }


        }
        return true;
    }


    public void createTable() {
        // Monta a tabela com os valores de cada proposição
        int numRows = (int) Math.pow(2, this.numberPropositions);
        int numSignificantBits = this.numberPropositions;

        int i = numSignificantBits - 1;
        Iterator iterator = this.propositions.iterator(); // Melhor maneira de iterar sobre o hashset
        while (iterator.hasNext()) {
            Character c = (Character) iterator.next();
            ArrayList<Boolean> propositionValues = new ArrayList<>();
            for (int j = numRows - 1; j >= 0; j--) {
                // And aplicado ao número binário, pega a posição do bit significativo avaliado
                propositionValues.add((j & (int) Math.pow(2, i)) != 0);
            }
            this.valueProposition.put(c, propositionValues);
            i--;
        }
//        this.valueProposition.forEach((k, v) -> {
//            System.out.println(k + ": " + v);
//
//        });
    }

    private char bitToChar(Boolean bit) {
        if (bit) {
            return 'V';
        }
        return 'F';
    }


    private ArrayList<Boolean> createRowValues(int index) {
        // Transforma o HashMap em uma ArrayList<Boolean> para os valores de determinada linha
        ArrayList<Boolean> rowValues = new ArrayList<>();
        Iterator iterator = this.propositions.iterator(); // Melhor maneira de iterar sobre o hashset
        while (iterator.hasNext()) {
            Character c = (Character) iterator.next();
            rowValues.add(this.valueProposition.get(c).get(index));
        }
        return rowValues;
    }

    private boolean evaluateRow(int rowNumberIndex) {
        ArrayList<String> tempExpression = new ArrayList<>();
        // Faz uma deep copy da expressão para o array temporário, evita erros de indexação devido a futuras manipulações
        for (int i = 0; i < this.expressionArray.size(); i++) {
            tempExpression.add(this.expressionArray.get(i));
        }

        ArrayList<Boolean> positionValue = new ArrayList<>();
        // Cria um array de valores booleanos, ou nulo, para cada item da expressão (inclui operadores)
        for (int i = 0; i < tempExpression.size(); i++) {
            if (tempExpression.get(i).equals("v") || tempExpression.get(i).equals("^") || tempExpression.get(i).equals("~")) {
                // Se for operador, adiciona nulo
                positionValue.add(null);
            } else {
                // Se for proposição, adiciona o valor da proposicão na linha determinada
                positionValue.add(valueProposition.get(tempExpression.get(i).charAt(0)).get(rowNumberIndex));
            }
        }

        // Primeira iteração sobre a expressão para colapsar os operadores '~'
        for (int i = 0; i < tempExpression.size(); i++) {
            if (tempExpression.get(i).equals("~")) {
                // Achou um '~' no índice i
                // O valor da próxima proposição no indice i + 1 será invertido
                positionValue.set(i, ! positionValue.get(i + 1));
                positionValue.remove(i + 1); // Colapsa o '~'
                String pop = tempExpression.remove(i + 1);
                tempExpression.set(i, tempExpression.get(i) + pop); // Concatena o '~' com a proposicao seguinte
//                System.out.println(tempExpression);
//                System.out.println(positionValue);
                i = 0; // Reseta o loop para recomeçar o índice
            }
        }
        // Segunda iteração sobre a expressão para colapsar os operadores '^'
        for (int i = 0; i < tempExpression.size(); i++) {
            if (tempExpression.get(i).equals("^")) {
                // Achou um '^' no indice i
                // Operador AND para os valores das proposições no indice i-1 e i + 1 (À direita e à esquerda do operador)
                positionValue.set(i - 1, positionValue.get(i - 1) && positionValue.get(i + 1));
                positionValue.remove(i); // Colapsa o '^'
                positionValue.remove(i); // Colapsa a segunda proposição
                String pop1 = tempExpression.remove(i);
                String pop2 = tempExpression.remove(i);
                tempExpression.set(i - 1, tempExpression.get(i - 1) + pop1 + pop2); // Concatena a proposição à esquerda
                // Com o operador '^' e a proposição seguinte
//                System.out.println(tempExpression);
//                System.out.println(positionValue);
                i = 0; // Reseta o loop para recomeçar o índice
            }
        }

        // Terceira iteração sobre a expressão para colapsar os operadores 'v'
        for (int i = 0; i < tempExpression.size(); i++) {
            if (tempExpression.get(i).equals("v")) {
                // Achou um 'v' no indice i
                // Operador OR para os valores das proposições no indice i-1 e i + 1 (À direita e à esquerda do operador)
                positionValue.set(i - 1, positionValue.get(i - 1) || positionValue.get(i + 1));
                positionValue.remove(i); // Colapsa o 'v'
                positionValue.remove(i); // Colapsa a segunda proposição
                String pop1 = tempExpression.remove(i);
                String pop2 = tempExpression.remove(i);
                tempExpression.set(i - 1, tempExpression.get(i - 1) + pop1 + pop2); // Concatena a proposição à esquerda
                // Com o operador 'v' e a proposição seguinte
//                System.out.println(tempExpression);
//                System.out.println(positionValue);
                i = 0; // Reseta o loop para recomeçar o índice
            }
        }
        return positionValue.get(0);
    }

    public void showTable() {

        int numRows = (int) Math.pow(2, this.numberPropositions);
        System.out.println("Número de linhas: " + numRows);
        Iterator iterator = this.propositions.iterator();
        // Print proposições
        while (iterator.hasNext()) {
            Character c = (Character) iterator.next();
            System.out.print("| " + c + " ");
        }

        // Print expressão
        System.out.print("| ");
        for (String s : this.expressionArray) {
            System.out.print(s);
        }
        System.out.println(" |");

        for (int i = 0; i < numRows; i++) {
            ArrayList<Boolean> row = createRowValues(i);
            for (int j = 0; j < this.numberPropositions; j++)
                // Print valor da proposição na linha
                System.out.print("| " + bitToChar(row.get(j)) + " ");
            evaluateRow(i);
            // Print valor da expressão na linha
            System.out.println("| " + bitToChar(evaluateRow(i)) + " |");
        }
    }
}
