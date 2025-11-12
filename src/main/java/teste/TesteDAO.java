package teste;

import dao.ClienteDAO;
import dao.FuncionarioDAO;
import dao.ProdutoDAO;
import dao.VendaDAO;
import model.Cliente;
import model.Funcionario;
import model.Produto;
import model.Venda;
import model.Item_venda; // Importa o modelo atualizado

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TesteDAO {

    public static void main(String[] args) {

        System.out.println("       INICIANDO TESTES DE INFRAESTRUTURA      ");

        final int ID_FUNCIONARIO_TESTE = 1;
        final int ID_CLIENTE_TESTE = 1;
        final int ID_PRODUTO_EXISTENTE = 4;
        final int QNT_ESTOQUE_INSUFICIENTE = 9999;

        ProdutoDAO produtoDAO = new ProdutoDAO();
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        VendaDAO vendaDAO = new VendaDAO();

        // --- 1. Testando Cadastro de Funcionário ---
        Funcionario func = new Funcionario();
        func.setNome("Ana Maria Silva");
        String emailDeTeste = "ana.vendas" + System.currentTimeMillis() + "@email.com";
        String senhaDeTeste = "senha123";
        func.setLogin(emailDeTeste);
        func.setSenhaHash(senhaDeTeste);

        System.out.println("\n--- 1. Testando Cadastro de Funcionário ---");
        if (funcionarioDAO.cadastrar(func)) {
            System.out.println("[OK] SUCESSO: Funcionário cadastrado.");
            System.out.println("==============================================");
            System.out.println(">>> USE ESTE EMAIL PARA LOGAR: " + emailDeTeste);
            System.out.println(">>> USE ESTA SENHA PARA LOGAR: " + senhaDeTeste);
            System.out.println("==============================================");
        } else {
            System.err.println("[ERRO] FALHA: Erro ao cadastrar funcionário.");
        }

        // --- 2. Testando Cadastro de Cliente  ---
        Cliente cliente = new Cliente();
        cliente.setNome("Joao da Silva");
        cliente.setTelefone("79998887766");
        cliente.setCpf("12345678900");
        cliente.setEndereco("Rua das Flores, 10");

        System.out.println("\n--- 2. Testando Cadastro de Cliente (RF01) ---");
        if (clienteDAO.cadastrar(cliente)) {
            System.out.println("[OK] SUCESSO: Cliente cadastrado.");
        } else {
            System.err.println("[ERRO] FALHA: Erro ao cadastrar cliente.");
        }

        // --- 3. Testando Cadastro de Produto  ---
        Produto pao = new Produto();
        pao.setNome("Pao Frances Teste Venda");
        pao.setPreco(new BigDecimal("0.75"));
        pao.setQntEstoque(300);

        System.out.println("\n--- 3. Testando Cadastro de Produto (RF03) ---");
        if (produtoDAO.adicionar(pao)) {
            System.out.println("[OK] SUCESSO: Produto cadastrado.");
        } else {
            System.err.println("[ERRO] FALHA: Erro ao cadastrar produto.");
        }

        // --- 4. Testando Consulta de Produtos ---
        System.out.println("\n--- 4. Testando Consulta de Produtos ---");
        List<Produto> produtos = produtoDAO.listarTodos();
        if (!produtos.isEmpty()) {
            System.out.println("[OK] SUCESSO: Produtos encontrados (" + produtos.size() + " itens).");
        } else {
            System.err.println("[ERRO] FALHA: Nenhum produto encontrado.");
        }

        System.out.println("=============================================");

        // --- INÍCIO DOS TESTES DE TRANSAÇÃO  ---

        Produto produtoParaVenda = produtoDAO.buscarPorId(ID_PRODUTO_EXISTENTE);
        if (produtoParaVenda == null) {
            System.err.println("AVISO CRÍTICO: Produto de teste ID " + ID_PRODUTO_EXISTENTE + " não encontrado. Testes de Venda ignorados.");
            return;
        }
        System.out.println(">>> Estoque inicial do Produto ID " + ID_PRODUTO_EXISTENTE + ": " + produtoParaVenda.getQntEstoque());


        // --- 5. Testando Transação de Venda ---
        Venda vendaSucesso = new Venda();
        vendaSucesso.setIdFuncionario(ID_FUNCIONARIO_TESTE);
        vendaSucesso.setIdCliente(ID_CLIENTE_TESTE);
        vendaSucesso.setData(LocalDateTime.now());

        List<Item_venda> itensSucesso = new ArrayList<>();

        int quantidadeSucesso = 5;
        Item_venda itemSucesso = new Item_venda(produtoParaVenda, quantidadeSucesso);
        itensSucesso.add(itemSucesso);

        vendaSucesso.setItens(itensSucesso);
        vendaSucesso.setValorTotal(itemSucesso.getPrecoTotalItem());

        System.out.println("\n--- 5. Teste de Venda - SUCESSO (COMMIT) ---");
        if (vendaDAO.registrarVenda(vendaSucesso)) {
            System.out.println("[OK] SUCESSO: Venda ID " + vendaSucesso.getId() + " registrada com COMMIT.");
            Produto produtoAposSucesso = produtoDAO.buscarPorId(ID_PRODUTO_EXISTENTE);
            System.out.println(">>> Estoque após SUCESSO: " + produtoAposSucesso.getQntEstoque() +
                    " (Deve ter diminuído " + quantidadeSucesso + " unidades)");
        } else {
            System.err.println("[ERRO] FALHA: A venda de sucesso NÃO deveria falhar.");
        }


        // --- 6. Testando Transação de Venda (Cenário de FALHA - ROLLBACK) ---
        Venda vendaFalha = new Venda();
        vendaFalha.setIdFuncionario(ID_FUNCIONARIO_TESTE);
        vendaFalha.setIdCliente(ID_CLIENTE_TESTE);
        vendaFalha.setData(LocalDateTime.now());

        List<Item_venda> itensFalha = new ArrayList<>();

        Item_venda itemFalha = new Item_venda(produtoParaVenda, QNT_ESTOQUE_INSUFICIENTE);
        itensFalha.add(itemFalha);

        vendaFalha.setItens(itensFalha);
        vendaFalha.setValorTotal(itemFalha.getPrecoTotalItem());

        System.out.println("\n--- 6. Teste de Venda - FALHA (ROLLBACK) ---");

        Produto produtoAntesFalha = produtoDAO.buscarPorId(ID_PRODUTO_EXISTENTE);
        int estoqueAntes = produtoAntesFalha != null ? produtoAntesFalha.getQntEstoque() : -1;
        System.out.println(">>> Estoque ANTES da falha: " + estoqueAntes);


        if (!vendaDAO.registrarVenda(vendaFalha)) {
            System.out.println("[OK] SUCESSO: A venda falhou corretamente (Estoque insuficiente).");
            Produto produtoAposFalha = produtoDAO.buscarPorId(ID_PRODUTO_EXISTENTE);
            int estoqueApos = produtoAposFalha != null ? produtoAposFalha.getQntEstoque() : -1;

            System.out.println(">>> Estoque APÓS falha: " + estoqueApos + " (Deve ser igual ao estoque ANTES: " + estoqueAntes + ")");

            if (estoqueApos == estoqueAntes) {
                System.out.println("[OK] ROLLBACK BEM SUCEDIDO: O estoque não foi alterado, e nenhuma venda ou item foi registrado.");
            } else {
                System.err.println("[ERRO] FALHA CRÍTICA DE TRANSAÇÃO: O estoque foi alterado APESAR da falha de venda.");
            }

        } else {
            System.err.println("[ERRO] FALHA CRÍTICA: A venda de falha NÃO deveria ter sido registrada.");
        }

        // --- INÍCIO DOS TESTES DE RELATÓRIO  ---
        System.out.println("\n--- 7. Teste de Relatório - Buscar Todas as Vendas ---");
        List<Venda> todasVendas = vendaDAO.buscarTodasVendas();

        if (!todasVendas.isEmpty()) {
            System.out.println("[OK] SUCESSO: Total de Vendas encontradas: " + todasVendas.size());
            Venda ultimaVenda = todasVendas.get(0);
            System.out.println("  -> Última Venda ID: " + ultimaVenda.getId() +
                    ", Data: " + ultimaVenda.getData().toLocalDate() +
                    ", Total: R$ " + ultimaVenda.getValorTotal());
        } else {
            System.err.println("[AVISO] FALHA: Nenhuma venda encontrada para o relatório.");
        }

        System.out.println("\n--- 8. Teste de Relatório - Vendas por Período ---");
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime ontem = agora.minusDays(1);
        List<Venda> vendasHoje = vendaDAO.buscarVendasPorPeriodo(ontem, agora);

        if (!vendasHoje.isEmpty()) {
            System.out.println("[OK] SUCESSO: Vendas encontradas nas últimas 24h: " + vendasHoje.size());
        } else {
            System.out.println("[AVISO] Nenhuma venda encontrada nas últimas 24h.");
        }


        System.out.println("=============================================");
        System.out.println("        TESTES DE VENDA CONCLUÍDOS         ");
    }
}