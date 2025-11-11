package dao;

import conexao.ConexaoDB;
import model.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public boolean cadastrar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome, login, senha_hash) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getLogin());
            stmt.setString(3, funcionario.getSenhaHash());
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar funcionário: " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar crucial para o LoginController
    public Funcionario buscarPorLogin(String login) {
        String sql = "SELECT id, nome, login, senha_hash FROM funcionario WHERE login = ?";
        Funcionario funcionario = null;

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    funcionario = new Funcionario();
                    funcionario.setId(rs.getInt("id"));
                    funcionario.setNome(rs.getString("nome"));
                    funcionario.setLogin(rs.getString("login"));
                    funcionario.setSenhaHash(rs.getString("senha_hash"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar funcionário por login: " + e.getMessage());
        }
        return funcionario;
    }

    // Método de Autenticação para o LoginController
    public Funcionario autenticar(String login, String senha) {
        Funcionario funcionario = buscarPorLogin(login);

        if (funcionario != null && funcionario.getSenhaHash().equals(senha)) {
            return funcionario;
        }
        return null;
    }

    public List<Funcionario> buscarTodos() {
        String sql = "SELECT id, nome, login, senha_hash FROM funcionario";
        List<Funcionario> funcionarios = new ArrayList<>();
        return funcionarios;
    }
}