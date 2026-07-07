package wfr.SalvaVenda;

import wfr.Base.Venda;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class ArquivoVendas {

    private final Path arquivoVendas;

    public ArquivoVendas() {
        this(Paths.get("vendas.txt"));
    }

    public ArquivoVendas(Path arquivoVendas) {
        this.arquivoVendas = arquivoVendas;
        garantirArquivo();
    }

    private void garantirArquivo() {
        try {
            if (!Files.exists(arquivoVendas)) {
                Files.createFile(arquivoVendas);
            }
        } catch (IOException ignore) {
        }
    }

    public void gravar(Venda venda) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(
                arquivoVendas,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            bw.write(venda.toCsv());
            bw.newLine();
        }
    }

    public List<String> lerTodasLinhas() throws IOException {
        return Files.readAllLines(arquivoVendas, StandardCharsets.UTF_8);
    }

    public boolean removerUltimaLinha() throws IOException {
        var linhas = lerTodasLinhas();
        if (linhas.isEmpty()) {
            return false;
        }
        linhas.remove(linhas.size() - 1);
        Files.write(arquivoVendas, linhas, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        return true;
    }
}
