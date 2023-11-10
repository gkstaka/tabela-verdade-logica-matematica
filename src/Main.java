import Excecoes.ExcecaoCaracteres;
import Excecoes.ExcecaoExpressao;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Entre com uma expressão lógica contendo somente os operadores ^, v e ~: ");
            String expression = scanner.nextLine();
            try {
                Expression expressao = new Expression(expression);
                expressao.showTable();
            } catch (ExcecaoCaracteres | ExcecaoExpressao e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
