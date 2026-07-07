package wfr.Base.Decorator;

import wfr.Base.Prato;

/**
 * PratoDecorator
 * --------------
 * Classe base para todos os decoradores de Prato (adicionais).
 */
public abstract class PratoDecorator implements Prato {
    protected final Prato pratoInterno;
    protected PratoDecorator(Prato pratoInterno) {
        this.pratoInterno = pratoInterno;
    }
    @Override
    public int getCodigo() {
        return pratoInterno.getCodigo();
    }
    @Override
    public String getNomeBase() {
        return pratoInterno.getNomeBase();
    }
}