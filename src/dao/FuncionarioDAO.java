package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexao.ConexaoDB;
import model.Funcionario;

public class FuncionarioDAO {

    public Funcionario autenticar(String login, String senhaHash) {
        return null;
    }

    public boolean cadastrar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome, login, senha) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getLogin());
            stmt.setString(3, funcionario.getSenhaHash());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar funcionário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}