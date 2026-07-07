package wfr.Factory;

import wfr.Base.ItemCardapio;
import wfr.Base.Prato;
import wfr.Base.PratoBase;

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
