package wfr.Base;


/**
 * PratoBase
 * ---------
 * Implementação concreta de um prato simples, sem adicionais.
 */
public class PratoBase implements Prato {
    private final int codigo;
    private final String nomeBase;
    private final double precoBase;

    public PratoBase(int codigo, String nomeBase, double precoBase) {
        this.codigo = codigo;
        this.nomeBase = nomeBase;
        this.precoBase = precoBase;
    }

    @Override
    public int getCodigo() {
        return codigo;
    }

    @Override
    public String getNomeBase() {
        return nomeBase;
    }

    @Override
    public String getDescricao() {
        return nomeBase; // sem adicionais
    }

    @Override
    public double getPreco() {
        return precoBase;
    }
}

