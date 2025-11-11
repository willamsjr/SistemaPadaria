package dao;

import conexao.ConexaoDB;
import model.Agendamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {

    public boolean cadastrar(Agendamento agendamento) {
        String sql = "INSERT INTO agendamento (data_agendamento, nome_cliente, telefone_cliente, descricao_pedido, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(agendamento.getDataAgendamento()));
            stmt.setString(2, agendamento.getNomeCliente());
            stmt.setString(3, agendamento.getTelefoneCliente());
            stmt.setString(4, agendamento.getDescricaoPedido());
            stmt.setString(5, agendamento.getStatus());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar agendamento: " + e.getMessage());
            return false;
        }
    }

    public List<Agendamento> buscarTodos() {
        String sql = "SELECT id_agendamento, data_agendamento, nome_cliente, telefone_cliente, descricao_pedido, status " +
                "FROM agendamento ORDER BY data_agendamento DESC";

        List<Agendamento> agendamentos = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Agendamento ag = new Agendamento();
                ag.setId(rs.getInt("id_agendamento"));
                ag.setDataAgendamento(rs.getTimestamp("data_agendamento").toLocalDateTime());
                ag.setNomeCliente(rs.getString("nome_cliente"));
                ag.setTelefoneCliente(rs.getString("telefone_cliente"));
                ag.setDescricaoPedido(rs.getString("descricao_pedido"));
                ag.setStatus(rs.getString("status"));

                agendamentos.add(ag);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar agendamentos: " + e.getMessage());
        }
        return agendamentos;
    }

    public boolean atualizarStatus(int idAgendamento, String novoStatus) {
        String sql = "UPDATE agendamento SET status = ? WHERE id_agendamento = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoStatus);
            stmt.setInt(2, idAgendamento);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do agendamento: " + e.getMessage());
            return false;
        }
    }

    public boolean deletar(int idAgendamento) {
        String sql = "DELETE FROM agendamento WHERE id_agendamento = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAgendamento);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar agendamento: " + e.getMessage());
            return false;
        }
    }

    public int contarAgendamentosPendentes() {
        String sql = "SELECT COUNT(*) AS count FROM agendamento WHERE status = 'Pendente'";
        int count = 0;

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt("count");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao contar agendamentos pendentes: " + e.getMessage());
        }
        return count;
    }
}