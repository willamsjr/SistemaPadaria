package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import conexao.ConexaoDB;
import model.Cliente;

public class ClienteDAO {

    public boolean cadastrar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, telefone, cpf, endereco) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getEndereco());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Cliente buscarPorTelefone(String telefone) {
        String sql = "SELECT id_cliente, nome, telefone, cpf, endereco FROM cliente WHERE telefone = ?";
        return buscarClientePorQuery(sql, telefone);
    }

    public Cliente buscarPorCpf(String cpf) {
        String sql = "SELECT id_cliente, nome, telefone, cpf, endereco FROM cliente WHERE cpf = ?";
        return buscarClientePorQuery(sql, cpf);
    }

    private Cliente buscarClientePorQuery(String sql, String parametro) {
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, parametro);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id_cliente"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setTelefone(rs.getString("telefone"));
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setEndereco(rs.getString("endereco"));
                    return cliente;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente: " + e.getMessage());
        }
        return null;
    }
}