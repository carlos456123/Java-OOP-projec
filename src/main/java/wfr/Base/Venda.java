package wfr.Base;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class Venda {

    public static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Quantidade de colunas esperadas no CSV (mesma ordem gerada por toCsv()).
    private static final int TOTAL_COLUNAS = 7;

    private final LocalDateTime dataHora;
    private final String nomeCliente;
    private final int codigoPratoBase;
    private final String descricaoPratoFinal; // prato + adicionais
    private final double precoUnitario;
    private final int quantidade;

    public Venda(LocalDateTime dataHora, String nomeCliente, Prato prato, int quantidade) {
        this.dataHora = dataHora;
        this.nomeCliente = nomeCliente;
        this.codigoPratoBase = prato.getCodigo();
        this.descricaoPratoFinal = prato.getDescricao();
        this.precoUnitario = prato.getPreco();
        this.quantidade = quantidade;
    }

    // Construtor privado usado para reconstruir uma Venda a partir de uma linha do CSV,
    // sem precisar de um objeto Prato (que não existe mais nesse ponto do programa).
    private Venda(LocalDateTime dataHora, String nomeCliente, int codigoPratoBase,
                  String descricaoPratoFinal, double precoUnitario, int quantidade) {
        this.dataHora = dataHora;
        this.nomeCliente = nomeCliente;
        this.codigoPratoBase = codigoPratoBase;
        this.descricaoPratoFinal = descricaoPratoFinal;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    public String toCsv() {
        return String.format("%s;%s;%d;%s;%.2f;%d;%.2f",
                FORMATO_DATA_HORA.format(dataHora),
                escapar(nomeCliente),
                codigoPratoBase,
                escapar(descricaoPratoFinal),
                precoUnitario,
                quantidade,
                getTotal()
        );
    }

    /**
     * Reconstrói uma Venda a partir de uma linha de texto no formato gerado por toCsv().
     * Retorna Optional.empty() se a linha estiver malformada, em vez de lançar exceção
     * ou devolver null — mantém o mesmo estilo já usado em ItemCardapio.porCodigo().
     */
    public static Optional<Venda> fromCsv(String linha) {
        if (linha == null || linha.isBlank()) {
            return Optional.empty();
        }
        String[] colunas = linha.split(";");
        if (colunas.length < TOTAL_COLUNAS) {
            return Optional.empty();
        }
        try {
            LocalDateTime dataHora = LocalDateTime.parse(colunas[0].trim(), FORMATO_DATA_HORA);
            String nomeCliente = colunas[1].trim();
            int codigoPratoBase = Integer.parseInt(colunas[2].trim());
            String descricaoPratoFinal = colunas[3].trim();
            double precoUnitario = parseDoubleSeguro(colunas[4]);
            int quantidade = Integer.parseInt(colunas[5].trim());
            // colunas[6] é o total, mas ele é recalculado por getTotal() -> não precisa guardar separado.

            return Optional.of(new Venda(dataHora, nomeCliente, codigoPratoBase,
                    descricaoPratoFinal, precoUnitario, quantidade));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public int getCodigoPratoBase() {
        return codigoPratoBase;
    }

    public String getDescricaoPratoFinal() {
        return descricaoPratoFinal;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getTotal() {
        return precoUnitario * quantidade;
    }

    /** Compara o nome do cliente ignorando maiúsculas/minúsculas — usado nas buscas. */
    public boolean nomeClienteContem(String termo) {
        if (termo == null) return false;
        return nomeCliente.toLowerCase(Locale.ROOT).contains(termo.toLowerCase(Locale.ROOT));
    }

    private static double parseDoubleSeguro(String texto) {
        try {
            return Double.parseDouble(texto.replace(",", ".").trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static String escapar(String texto) {
        return texto == null ? "" : texto.replace(";", ",").trim();
    }
}