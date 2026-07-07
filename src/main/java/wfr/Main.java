package wfr;

import wfr.Base.Decorator.AdicionalMolhoMadeira;
import wfr.Base.ItemCardapio;
import wfr.Base.Prato;
import wfr.Base.Decorator.AdicionalQueijo;
import wfr.Base.Decorator.AdicionalSalada;
import wfr.Base.Decorator.AdicionalMolhoMadeira;
import wfr.Factory.FactoryPrato;
import wfr.Gestor.GestorDeVendas;
import wfr.Leitor.LeitorDeTexto;
import wfr.Singleton.ClassSingleton;

import java.util.Optional;

/**
 * Main
 * ----
 * Classe principal do sistema WFRestaurant.
 */
public class Main {

    public static void main(String[] args) {

        ClassSingleton contexto = ClassSingleton.getInstancia();

        LeitorDeTexto leitor = new LeitorDeTexto();
        GestorDeVendas gestor = new GestorDeVendas(contexto.getArquivoVendas());
        FactoryPrato factoryPrato = contexto.getFactoryPrato();

        System.out.println("Iniciando " + contexto.getNomeAplicacao());
        System.out.println();

        while (true) {
            mostrarMenu();
            int opcao = leitor.lerInteiro("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> listarCardapio();
                case 2 -> registrarVenda(leitor, gestor, factoryPrato);
                case 3 -> gestor.listarTodasComTotal();
                case 4 -> {
                    String termo = leitor.lerLinhaNaoVazia("Nome do cliente (parcial ou completo): ");
                    gestor.buscarPorCliente(termo);
                }
                case 5 -> gestor.resumoPorPrato();
                case 6 -> gestor.desfazerUltimaVenda();
                case 0 -> {
                    System.out.println("Encerrando aplicação. Até logo!");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        }
    }

    private static void mostrarMenu() {
        System.out.println("===== WFRestaurant =====");
        System.out.println("[1] Listar cardápio");
        System.out.println("[2] Registrar venda (com adicionais)");
        System.out.println("[3] Mostrar todas as vendas e total");
        System.out.println("[4] Buscar vendas por cliente");
        System.out.println("[5] Resumo por prato (quantidade e total)");
        System.out.println("[6] Desfazer última venda");
        System.out.println("[0] Sair");
    }

    private static void listarCardapio() {
        System.out.println("---- CARDÁPIO ----");
        for (ItemCardapio item : ItemCardapio.values()) {
            System.out.printf("%d) %s — R$ %.2f%n",
                    item.codigo, item.nome, item.precoBase);
        }
        System.out.println("---- ADICIONAIS ----");
        System.out.println("1) Adicional Queijo  (+ R$ 2,00)");
        System.out.println("2) Adicional Salada  (+ R$ 3,00)");
        System.out.println("3) Adicional Molho   (+ R$ 4,00)");
    }

    private static void registrarVenda(
            LeitorDeTexto leitor,
            GestorDeVendas gestor,
            FactoryPrato factoryPrato) {

        System.out.println("---- REGISTRAR VENDA ----");
        String nomeCliente = leitor.lerLinhaNaoVazia("Nome do cliente: ");

        listarCardapio();
        int codigoPrato = leitor.lerInteiro("Código do prato: ");
        Optional<ItemCardapio> opcionalItem = ItemCardapio.porCodigo(codigoPrato);
        if (opcionalItem.isEmpty()) {
            System.out.println("Código de prato inválido.");
            return;
        }

        Prato prato = factoryPrato.criarPorCodigo(codigoPrato);
        if (prato == null) {
            System.out.println("Não foi possível criar o prato.");
            return;
        }

        // Decorators (adicionais)
        while (true) {
            int opcAdicional = leitor.lerInteiro(
                    "Adicionar adicional? [1=Queijo, 2=Salada, 3=Molho, 0=nenhum/continuar]: ");
            if (opcAdicional == 0) {
                break;
            }
            switch (opcAdicional) {
                case 1 -> prato = new AdicionalQueijo(prato);
                case 2 -> prato = new AdicionalSalada(prato);
                case 3 -> prato = new AdicionalMolhoMadeira(prato);
                default -> System.out.println("Adicional inválido.");
            }
        }

        int quantidade = leitor.lerInteiro("Quantidade: ");
        if (quantidade <= 0) {
            System.out.println("Quantidade deve ser positiva.");
            return;
        }

        boolean ok = gestor.cadastrarVenda(nomeCliente, prato, quantidade);
        if (ok) {
            double total = prato.getPreco() * quantidade;
            System.out.printf("Venda registrada: %s | %s — Unit R$ %.2f | Qtd %d | Total R$ %.2f%n",
                    nomeCliente, prato.getDescricao(), prato.getPreco(), quantidade, total);
        }
    }
}
