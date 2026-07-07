package wfr.Gestor;

import wfr.Base.ItemCardapio;
import wfr.Base.Prato;
import wfr.Base.Venda;
import wfr.SalvaVenda.ArquivoVendas;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * GestorDeVendas
 * --------------
 * Camada de regras de negócio das vendas.
 */
public class GestorDeVendas {

    private final ArquivoVendas arquivoVendas;

    public GestorDeVendas(ArquivoVendas arquivoVendas) {
        this.arquivoVendas = arquivoVendas;
    }

    public boolean cadastrarVenda(String nomeCliente, Prato prato, int quantidade) {
        if (nomeCliente == null || nomeCliente.isBlank()) return false;
        if (prato == null || quantidade <= 0) return false;

        Venda venda = new Venda(LocalDateTime.now(), nomeCliente.trim(), prato, quantidade);
        try {
            arquivoVendas.gravar(venda);
            return true;
        } catch (IOException e) {
            System.out.println("Falha ao salvar venda: " + e.getMessage());
            return false;
        }
    }

    public void listarTodasComTotal() {
        System.out.println("---- VENDAS REGISTRADAS ----");
        List<String> linhas;
        try {
            linhas = arquivoVendas.lerTodasLinhas();
        } catch (IOException e) {
            System.out.println("Erro ao ler vendas: " + e.getMessage());
            return;
        }
        if (linhas.isEmpty()) {
            System.out.println("Nenhuma venda encontrada.");
            return;
        }

        double totalGeral = 0.0;
        int registros = 0;

        for (String linha : linhas) {
            System.out.println(linha);
            String[] colunas = linha.split(";");
            if (colunas.length >= 7) {
                totalGeral += parseDoubleSeguro(colunas[6]);
                registros++;
            }
        }

        System.out.printf("---- TOTAL DE VENDAS (%d registros): R$ %.2f ----%n",
                registros, totalGeral);
    }

    public void buscarPorCliente(String termo) {
        System.out.println("---- BUSCAR POR CLIENTE ----");
        String alvo = termo == null ? "" : termo.toLowerCase(Locale.ROOT);

        List<String> linhas;
        try {
            linhas = arquivoVendas.lerTodasLinhas();
        } catch (IOException e) {
            System.out.println("Erro ao ler vendas: " + e.getMessage());
            return;
        }

        double totalCliente = 0.0;
        int registros = 0;

        for (String linha : linhas) {
            String[] colunas = linha.split(";");
            if (colunas.length >= 7) {
                String cliente = colunas[1].toLowerCase(Locale.ROOT);
                if (cliente.contains(alvo)) {
                    System.out.println(linha);
                    totalCliente += parseDoubleSeguro(colunas[6]);
                    registros++;
                }
            }
        }

        System.out.printf("---- TOTAL DO CLIENTE (%d registros): R$ %.2f ----%n",
                registros, totalCliente);
    }

    public void resumoPorPrato() {
        System.out.println("---- RESUMO POR PRATO ----");

        Map<Integer, Integer> quantidadePorCodigo = new LinkedHashMap<>();
        Map<Integer, Double> totalPorCodigo = new LinkedHashMap<>();
        for (ItemCardapio item : ItemCardapio.values()) {
            quantidadePorCodigo.put(item.codigo, 0);
            totalPorCodigo.put(item.codigo, 0.0);
        }

        List<String> linhas;
        try {
            linhas = arquivoVendas.lerTodasLinhas();
        } catch (IOException e) {
            System.out.println("Erro ao ler vendas: " + e.getMessage());
            return;
        }

        for (String linha : linhas) {
            String[] colunas = linha.split(";");
            if (colunas.length >= 7) {
                try {
                    int codigo = Integer.parseInt(colunas[2].trim());
                    int quantidade = Integer.parseInt(colunas[5].trim());
                    double total = parseDoubleSeguro(colunas[6]);

                    quantidadePorCodigo.put(
                            codigo,
                            quantidadePorCodigo.getOrDefault(codigo, 0) + quantidade
                    );
                    totalPorCodigo.put(
                            codigo,
                            totalPorCodigo.getOrDefault(codigo, 0.0) + total
                    );
                } catch (NumberFormatException ignore) {
                }
            }
        }

        double totalGeral = 0.0;
        for (ItemCardapio item : ItemCardapio.values()) {
            int q = quantidadePorCodigo.getOrDefault(item.codigo, 0);
            double t = totalPorCodigo.getOrDefault(item.codigo, 0.0);
            totalGeral += t;
            System.out.printf("%d) %s — Qtd: %d | Total: R$ %.2f%n",
                    item.codigo, item.nome, q, t);
        }
        System.out.printf("TOTAL GERAL: R$ %.2f%n", totalGeral);
    }

    public void desfazerUltimaVenda() {
        try {
            boolean removida = arquivoVendas.removerUltimaLinha();
            if (removida) {
                System.out.println("Última venda removida com sucesso.");
            } else {
                System.out.println("Não há vendas para remover.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao alterar arquivo: " + e.getMessage());
        }
    }

    private static double parseDoubleSeguro(String texto) {
        try {
            return Double.parseDouble(texto.replace(",", ".").trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
