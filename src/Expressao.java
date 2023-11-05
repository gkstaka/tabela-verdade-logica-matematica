import Excecoes.ExcecaoCaracteres;

import javax.lang.model.type.NullType;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expressao {
    private char[] expressao;
    private LinkedHashSet<Character> proposicoes;
    private int numeroProposicoes;
    private ArrayList<ArrayList<Boolean>> valorProposicoes;

    public Expressao(String expressao) throws ExcecaoCaracteres {

        String tempExpressao = expressao;
        tempExpressao = tempExpressao.trim().replaceAll(" ", "").toLowerCase();

        Pattern pattern = Pattern.compile("^[a-z~^]+$");
        Matcher matcher = pattern.matcher(tempExpressao);
        if (! matcher.find()) {
            throw new ExcecaoCaracteres();
        }

        this.expressao = tempExpressao.toCharArray();
        this.proposicoes = new LinkedHashSet<>();
        this.numeroProposicoes = 0;
        this.valorProposicoes = new ArrayList<ArrayList<Boolean>>();
    }

    public void mostrarExpressao() {
        System.out.println(this.expressao);
    }

    public LinkedHashSet<Character> getProposicoes() {
        return this.proposicoes;
    }

    public int contarProposicoes() {
        for (char c : this.expressao) {
            if (! (c == '^' || c == 'v' || c == '~')) {
                this.proposicoes.add(c);
            }
        }
        this.numeroProposicoes = proposicoes.size();
        return this.numeroProposicoes;
    }

    public boolean validarExpressao() {
        if (this.numeroProposicoes == 0) {
            System.out.println("Nao ha proposicoes nessa expressao");
            return false;
        }
        boolean proposicaoEsperada = false;
        boolean operadorEsperado = false;
        boolean negacaoEsperada = false;
        for (int i = 0; i < this.expressao.length; i++) {
            System.out.println("Caracter sendo avaliado: " + this.expressao[i]);
            // avalia o comeco da expressao
            if (i == 0 && (this.expressao[i] == 'v' || this.expressao[i] == '^')) {
                // nao deve comecar com operadores ^ e v
                System.out.println("Nao se inicia com operadores ^ e v");
                return false;
            } else if (i == 0 && Character.isLetter(this.expressao[i])) {
                // se o comeco for uma letra
                operadorEsperado = true;
                proposicaoEsperada = false;
                negacaoEsperada = false;
                System.out.println("Esperado um operador ^ ou v");
                continue;
            } else if (i == 0 && this.expressao[i] == '~') {
                operadorEsperado = false;
                proposicaoEsperada = true;
                negacaoEsperada = false;
                System.out.println("Esperado uma proposicao");
                continue;
            }

            if (operadorEsperado && (this.expressao[i] == '^' || this.expressao[i] == 'v') && i != (this.expressao.length - 1)) {
                // depois de uma operacao certa, espera-se uma negacao ou uma proposicao
                operadorEsperado = false;
                proposicaoEsperada = true;
                negacaoEsperada = true;
                System.out.println("Operador no local correto = " + this.expressao[i]);
                continue;
            } else if (! operadorEsperado && (this.expressao[i] == '^' || this.expressao[i] == 'v')) {
                // se nao esta esperando um operador e recebe um operador, a expressao é invalida
                System.out.println("Nao pode haver dois operadores ^ ou v seguidos");
                return false;
            }
            if (negacaoEsperada && this.expressao[i] == '~') {
                // uma proposicao é esperada apos uma negacao valida
                operadorEsperado = false;
                proposicaoEsperada = true;
                negacaoEsperada = false;
                System.out.println("Proposicao no local correto = " + this.expressao[i]);
                continue;
            } else if (! negacaoEsperada && this.expressao[i] == '~') {
                // A negacao nao esta no lugar certo
                System.out.println("Negacao no local errado");
                return false;
            }
            if (proposicaoEsperada && Character.isLetter(this.expressao[i]) && this.expressao[i] != 'v') {
                operadorEsperado = true;
                proposicaoEsperada = false;
                negacaoEsperada = false;
                System.out.println("proposicao no local correto = " + this.expressao[i]);
            } else if (! proposicaoEsperada && Character.isLetter(this.expressao[i]) && this.expressao[i] != 'v') {
                System.out.println("Nao pode haver duas proposicoes seguidas");
                return false;
            }
            // avalia o final da expressao
            if (i == (this.expressao.length - 1) && (this.expressao[i] == 'v' || this.expressao[i] == '^' || this.expressao[i] == '~')) {
                System.out.println("Nao se finaliza com operadores ^, v e ~");
                System.out.println(this.expressao[i]);
                return false;
            }
        }

        return true;
    }

    public void montarTabela() {
        int numeroLinhas = (int) Math.pow(2, this.numeroProposicoes);
        int numeroBitsSignificativos = this.numeroProposicoes;
        for (int i = 0; i < numeroLinhas; i++) {
            ArrayList<Boolean> valorLinha = new ArrayList<>();
            for (int j = 0; j < numeroBitsSignificativos; j++) {
                valorLinha.add((i & (int) Math.pow(2, j)) != 0);
            }
            this.valorProposicoes.add(valorLinha);
        }
        System.out.println((valorProposicoes));
    }

    private char converterBit(Boolean bit) {
        if (bit) {
            return 'V';
        }
        return 'F';
    }

    public void mostrarTabela() {
        for (char c : this.proposicoes) {
            System.out.print("| " + c + " ");
        }
        System.out.print("| ");
        for (char c : this.expressao) {
            System.out.print(c);
        }
        System.out.println(" |");
        int numeroLinhas = (int) Math.pow(2, this.numeroProposicoes);
        int numeroBitsSignificativos = this.numeroProposicoes;

        for (int i = 0; i < numeroLinhas; i++) {
            ArrayList<Boolean> valorLinha = valorProposicoes.get(i);
            for (int j = 0; j < numeroBitsSignificativos; j++) {
                System.out.print("| " + converterBit(valorLinha.get(j)) + " ");
            }
            System.out.println("|");
        }
    }
}
