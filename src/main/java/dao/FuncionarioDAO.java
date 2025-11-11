package dao;

import conexao.ConexaoDB;
import model.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public boolean cadastrar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome, login, senha_hash) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getLogin());
            stmt.setString(3, funcionario.getSenhaHash());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        funcionario.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar funcionário: " + e.getMessage());
            return false;
        }
    }

    public Funcionario buscarPorLogin(String login) {
        String sql = "SELECT id_funcionario, nome, login, senha_hash FROM funcionario WHERE login = ?";
        Funcionario funcionario = null;

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    funcionario = new Funcionario();
                    funcionario.setId(rs.getInt("id_funcionario"));
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

    public Funcionario autenticar(String login, String senha) {
        Funcionario funcionario = buscarPorLogin(login);

        if (funcionario != null && funcionario.getSenhaHash().equals(senha)) {
            return funcionario;
        }
        return null;
    }

    public List<Funcionario> buscarTodos() {
        String sql = "SELECT id_funcionario, nome, login, senha_hash FROM funcionario";
        List<Funcionario> funcionarios = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt("id_funcionario"));
                funcionario.setNome(rs.getString("nome"));
                funcionario.setLogin(rs.getString("login"));
                funcionario.setSenhaHash(rs.getString("senha_hash"));
                funcionarios.add(funcionario);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os funcionários: " + e.getMessage());
        }
        return funcionarios;
    }
}