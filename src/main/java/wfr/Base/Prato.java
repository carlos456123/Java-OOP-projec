package wfr.Base;

public interface Prato {
    int getCodigo();            // código do prato base (1..4)
    String getNomeBase();       // nome do prato base (ex.: "Lasanha")
    String getDescricao();      // nome + adicionais (ex.: "Lasanha + Adicional Queijo")
    double getPreco();          // preço final (base + adicionais)
}

