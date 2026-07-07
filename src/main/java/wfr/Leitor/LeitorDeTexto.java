package wfr.Leitor;

import java.util.Scanner;

/**
 * EntradaConsole
 * --------------
 * Centraliza leitura de dados pelo teclado.
 */
public class LeitorDeTexto {

    private final Scanner scanner = new Scanner(System.in);

    public int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    public String lerLinhaNaoVazia(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            if (!linha.isEmpty()) {
                return linha;
            }
            System.out.println("Entrada vazia. Tente novamente.");
        }
    }
}

