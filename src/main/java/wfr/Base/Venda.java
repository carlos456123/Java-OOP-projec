package wfr.Base;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Venda
 * -----
 * Representa uma venda registrada no sistema.
 * Também sabe se serializar para uma linha de texto (CSV) separada por ';'.
 */
public class Venda {

    public static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    public String toCsv() {
        double total = precoUnitario * quantidade;
        return String.format("%s;%s;%d;%s;%.2f;%d;%.2f",
                FORMATO_DATA_HORA.format(dataHora),
                escapar(nomeCliente),
                codigoPratoBase,
                escapar(descricaoPratoFinal),
                precoUnitario,
                quantidade,
                total
        );
    }

    private static String escapar(String texto) {
        return texto == null ? "" : texto.replace(";", ",").trim();
    }
}

