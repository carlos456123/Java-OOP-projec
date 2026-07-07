package wfr.Factory;

import wfr.Base.ItemCardapio;
import wfr.Base.Prato;
import wfr.Base.PratoBase;

/**
 * FactoryPrato (Factory Method)
 * -----------------------------
 * Responsável por criar objetos Prato a partir do código do cardápio.
 */
public class FactoryPrato {

    public Prato criarPorCodigo(int codigo) {
        var opcionalItem = ItemCardapio.porCodigo(codigo);
        if (opcionalItem.isEmpty()) {
            return null;
        }
        var item = opcionalItem.get();
        return new PratoBase(item.codigo, item.nome, item.precoBase);
    }
}
