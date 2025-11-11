package dao;

import conexao.ConexaoDB;
import model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProdutoDAO {

    public Produto buscarPorId(int id) {
        String sql = "SELECT id, nome, preco, qntEstoque FROM produto WHERE id = ?";
        Produto produto = null;

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPreco(rs.getBigDecimal("preco"));
                    produto.setQntEstoque(rs.getInt("qntEstoque"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
        }
        return produto;
    }

    public boolean decrementarEstoque(int id_produto, int quantidade) {
        String sql = "UPDATE produto SET qntEstoque = qntEstoque - ? WHERE id = ? AND qntEstoque >= ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, id_produto);
            stmt.setInt(3, quantidade);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao decrementar estoque do produto ID " + id_produto + ": " + e.getMessage());
            return false;
        }
    }
    

    public boolean cadastrar(Produto produto) {
        String sql = "INSERT INTO produto (nome, preco, qntEstoque) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setBigDecimal(2, produto.getPreco());
            stmt.setInt(3, produto.getQntEstoque());
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar produto: " + e.getMessage());
            return false;
        }
    }

    public List<Produto> buscarTodos() {
        String sql = "SELECT id, nome, preco, qntEstoque FROM produto";
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getBigDecimal("preco"));
                produto.setQntEstoque(rs.getInt("qntEstoque"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os produtos: " + e.getMessage());
        }
        return produtos;
    }
}