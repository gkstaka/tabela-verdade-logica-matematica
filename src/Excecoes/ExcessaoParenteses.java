package Excecoes;

public class ExcessaoParenteses extends Excecao{
    public ExcessaoParenteses(String message) {
        super("Há parenteses que nao foram abertos e/ou fechados");
    }
}
