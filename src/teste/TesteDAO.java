package teste;

import dao.ClienteDAO;
import dao.FuncionarioDAO;
import dao.ProdutoDAO;
import model.Cliente;
import model.Funcionario;
import model.Produto;

import java.math.BigDecimal;
import java.util.List;

public class TesteDAO {

    public static void main(String[] args) {

        System.out.println("       INICIANDO TESTES DE INFRAESTRUTURA      ");

        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        Funcionario func = new Funcionario();
        func.setNome("Ana Maria Silva");
        func.setLogin("ana.vendas");
        func.setSenhaHash("senha123");

        System.out.println("\n--- 1. Testando Cadastro de Funcionário ---");
        if (funcionarioDAO.cadastrar(func)) {
            System.out.println("[OK] SUCESSO: Funcionário cadastrado.");
        } else {
            System.err.println("[ERRO] FALHA: Erro ao cadastrar funcionário. Verifique o banco de dados (tabela 'funcionario').");
        }

        // TESTE DE CADASTRO DE CLIENTE

        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente cliente = new Cliente();
        cliente.setNome("Joao da Silva");
        cliente.setTelefone("79998887766");
        cliente.setCpf("12345678900");
        cliente.setEndereco("Rua das Flores, 10");

        System.out.println("\n--- 2. Testando Cadastro de Cliente (RF01) ---");
        if (clienteDAO.cadastrar(cliente)) {
            System.out.println("[OK] SUCESSO: Cliente cadastrado.");
        } else {
            System.err.println("[ERRO] FALHA: Erro ao cadastrar cliente. Verifique o banco de dados (tabela 'cliente').");
        }

        // TESTE DE CADASTRO DE PRODUTO

        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto pao = new Produto();
        pao.setNome("Pao Frances");
        pao.setPreco(new BigDecimal("0.75"));
        pao.setQntEstoque(300);

        System.out.println("\n--- 3. Testando Cadastro de Produto (RF03) ---");
        if (produtoDAO.cadastrar(pao)) {
            System.out.println("[OK] SUCESSO: Produto cadastrado.");
        } else {
            System.err.println("[ERRO] FALHA: Erro ao cadastrar produto. Verifique o banco de dados (tabela 'produto').");
        }

        // TESTE DE CONSULTA DE PRODUTOS

        System.out.println("\n--- 4. Testando Consulta de Produtos ---");
        List<Produto> produtos = produtoDAO.buscarTodos();
        if (!produtos.isEmpty()) {
            System.out.println("[OK] SUCESSO: Produtos encontrados (" + produtos.size() + " itens):");
            for (Produto p : produtos) {
                System.out.println("  -> ID: " + p.getId() + ", Nome: " + p.getNome() + ", Estoque: " + p.getQntEstoque());
            }
        } else {
            System.err.println("[ERRO] FALHA: Nenhum produto encontrado. A consulta falhou.");
        }

        System.out.println("=============================================");
    }
}