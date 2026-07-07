package wfr.Base.Decorator;

import wfr.Base.Prato;

/**
 * AdicionalQueijo
 * ---------------
 * Decorator que adiciona queijo ao prato.
 */
public class AdicionalQueijo extends PratoDecorator {

    public AdicionalQueijo(Prato pratoInterno) {
        super(pratoInterno);
    }

    @Override
    public String getDescricao() {
        return pratoInterno.getDescricao() + " + Adicional Queijo";
    }

    @Override
    public double getPreco() {
        return pratoInterno.getPreco() + 2.00;
    }
}

