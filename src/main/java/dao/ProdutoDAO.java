package dao;

import conexao.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Produto;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; // IMPORTANTE: Deve existir

public class ProdutoDAO {

    private Connection connection;

    public ProdutoDAO() {
        this.connection = ConexaoDB.getConnection();
    }

    public int contarEstoqueBaixo() {
        String sql = "SELECT COUNT(*) FROM produto WHERE qnt_estoque <= ?";
        int contagem = 0;

        int nivelEstoqueBaixo = 5;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, nivelEstoqueBaixo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    contagem = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar estoque baixo: " + e.getMessage());
        }
        return contagem;
    }

    public void adicionar(Produto produto) {
        String sql = "INSERT INTO produto(nome, preco, qnt_estoque) VALUES(?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQntEstoque());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar produto: " + e.getMessage());
        }
    }

    public List<Produto> listarTodos() {
        String sql = "SELECT * FROM produto";
        List<Produto> produtos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setIdProduto(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome"));

                // LINHA 46 ou 47: CORREÇÃO OBRIGATÓRIA
                // Lendo como BigDecimal e convertendo para double para evitar o erro.
                produto.setPreco(rs.getBigDecimal("preco").doubleValue());

                produto.setQntEstoque(rs.getInt("qnt_estoque"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

}