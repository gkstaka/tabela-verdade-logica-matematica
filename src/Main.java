import Excecoes.ExcecaoCaracteres;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Entre com uma expressão lógica contendo somente os operadores ^, v e ~: ");
            String buffer = scanner.nextLine();

            try {
                Expressao expressao = new Expressao(buffer);
//

//                int numeroProposicoes = expressao.contarProposicoes();

//                System.out.println("Numero de proposicoes: " + numeroProposicoes);
//                System.out.println(expressao.getProposicoes());
                if (expressao.validarExpressao()) {
                    System.out.println();
                    System.out.println("Expressao valida");
                    expressao.montarTabela();
                    expressao.mostrarTabela();
                    System.out.println();
                }
                else{
                    System.out.println("Expressao nao valida");
                }


            } catch (ExcecaoCaracteres e) {
                System.out.println("Existe caracteres nao validos na expressao");
            }
        }
    }
}