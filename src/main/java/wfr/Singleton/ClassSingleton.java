package wfr.Singleton;

import wfr.Factory.FactoryPrato;
import wfr.SalvaVenda.ArquivoVendas;


public final class ClassSingleton {

    private static final ClassSingleton INSTANCIA = new ClassSingleton();

    private final String nomeAplicacao;
    private final FactoryPrato factoryPrato;
    private final ArquivoVendas arquivoVendas;

    private ClassSingleton() {
        this.nomeAplicacao = "WFRestaurant";
        this.factoryPrato = new FactoryPrato();
        this.arquivoVendas = new ArquivoVendas();
    }

    public static ClassSingleton getInstancia() {
        return INSTANCIA;
    }

    public String getNomeAplicacao() {
        return nomeAplicacao;
    }

    public FactoryPrato getFactoryPrato() {
        return factoryPrato;
    }

    public ArquivoVendas getArquivoVendas() {
        return arquivoVendas;
    }
}
