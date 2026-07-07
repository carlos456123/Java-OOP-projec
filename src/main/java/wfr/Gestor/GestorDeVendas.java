package wfr.Gestor;

import wfr.Base.ItemCardapio;
import wfr.Base.Prato;
import wfr.Base.Venda;
import wfr.SalvaVenda.ArquivoVendas;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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
        List<Venda> vendas = carregarVendasValidas();
        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda encontrada.");
            return;
        }

        double totalGeral = 0.0;
        for (Venda venda : vendas) {
            System.out.println(venda.toCsv());
            totalGeral += venda.getTotal();
        }

        System.out.printf("---- TOTAL DE VENDAS (%d registros): R$ %.2f ----%n",
                vendas.size(), totalGeral);
    }

    public void buscarPorCliente(String termo) {
        System.out.println("---- BUSCAR POR CLIENTE ----");

        double totalCliente = 0.0;
        int registros = 0;

        for (Venda venda : carregarVendasValidas()) {
            if (venda.nomeClienteContem(termo)) {
                System.out.println(venda.toCsv());
                totalCliente += venda.getTotal();
                registros++;
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

        for (Venda venda : carregarVendasValidas()) {
            int codigo = venda.getCodigoPratoBase();
            quantidadePorCodigo.merge(codigo, venda.getQuantidade(), Integer::sum);
            totalPorCodigo.merge(codigo, venda.getTotal(), Double::sum);
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

    /**
     * Lê todas as linhas do arquivo e converte cada uma em Venda via Venda.fromCsv(),
     * descartando silenciosamente linhas malformadas. Centraliza aqui a leitura +
     * conversão que antes estava duplicada em 3 métodos diferentes.
     */
    private List<Venda> carregarVendasValidas() {
        List<String> linhas;
        try {
            linhas = arquivoVendas.lerTodasLinhas();
        } catch (IOException e) {
            System.out.println("Erro ao ler vendas: " + e.getMessage());
            return List.of();
        }

        List<Venda> vendas = new ArrayList<>();
        for (String linha : linhas) {
            Venda.fromCsv(linha).ifPresent(vendas::add);
        }
        return vendas;
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

}