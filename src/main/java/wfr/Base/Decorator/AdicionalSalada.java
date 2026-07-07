package wfr.Base.Decorator;

import wfr.Base.Prato;

public class AdicionalSalada extends PratoDecorator {

    public AdicionalSalada(Prato pratoInterno) {
        super(pratoInterno);
    }

    @Override
    public String getDescricao() {
        return pratoInterno.getDescricao() + " + Adicional Salada";
    }

    @Override
    public double getPreco() {
        return pratoInterno.getPreco() + 3.00;
    }
}
