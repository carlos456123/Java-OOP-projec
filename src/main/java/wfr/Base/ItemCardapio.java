package wfr.Base;

import java.util.Optional;


public enum ItemCardapio {
    Lasanha(1, "Lasanha", 25.00),
    FrangoParmegiana(2, "Frango à parmegiana", 28.00),
    FileParmegiana(3, "Filé à parmegiana", 29.00),
    CamaraoMolhoMadeira(4, "Camarão ao molho madeira", 32.00);

    public final int codigo;
    public final String nome;
    public final double precoBase;

    ItemCardapio(int codigo, String nome, double precoBase) {
        this.codigo = codigo;
        this.nome = nome;
        this.precoBase = precoBase;
    }

    public static Optional<ItemCardapio> porCodigo(int codigo) {
        for (ItemCardapio item : values()) {
            if (item.codigo == codigo) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
}

