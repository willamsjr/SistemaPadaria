package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexao.ConexaoDB;
import model.Funcionario;

public class FuncionarioDAO {

    public Funcionario autenticar(String login, String senhaHash) {
        String sql = "SELECT id_func, nome, login FROM funcionario WHERE login = ? AND senha = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senhaHash);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario funcionario = new Funcionario();
                    funcionario.setId(rs.getInt("id_func"));
                    funcionario.setNome(rs.getString("nome"));
                    funcionario.setLogin(rs.getString("login"));
                    return funcionario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar funcionário: " + e.getMessage());
        }
        return null;
    }
}