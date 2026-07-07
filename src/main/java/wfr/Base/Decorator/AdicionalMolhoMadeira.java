package wfr.Base.Decorator;

import wfr.Base.Prato;

/**
 * AdicionalMolhoMadeira
 * ---------------------
 * Decorator que adiciona molho madeira extra ao prato.
 */
public class AdicionalMolhoMadeira extends PratoDecorator {

    public AdicionalMolhoMadeira(Prato pratoInterno) {
        super(pratoInterno);
    }

    @Override
    public String getDescricao() {
        return pratoInterno.getDescricao() + " + Adicional Molho Madeira";
    }

    @Override
    public double getPreco() {
        return pratoInterno.getPreco() + 4.00;
    }
}

