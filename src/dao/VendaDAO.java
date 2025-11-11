package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import conexao.ConexaoDB;
import model.Venda;
import model.Item_venda;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    private Item_vendaDAO itemVendaDAO = new Item_vendaDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    public boolean registrarVenda(Venda venda) {
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            System.err.println("RN01: Não é possível registrar uma venda sem produtos.");
            return false;
        }

        Connection conn = null;
        boolean sucesso = false;

        try {
            conn = ConexaoDB.getConnection();
            conn.setAutoCommit(false);

            int id_venda_gerada = inserirVendaPrincipal(conn, venda);

            if (id_venda_gerada > 0) {
                venda.setId(id_venda_gerada);

                itemVendaDAO.cadastrarItens(conn, id_venda_gerada, venda.getItens());

                if (!atualizarEstoque(conn, venda.getItens())) {
                    throw new SQLException("Falha na atualização do estoque (RN02). Revertendo transação.");
                }

                conn.commit();
                sucesso = true;
            }

        } catch (SQLException e) {
            System.err.println("ERRO na transação de venda! Tentando ROLLBACK...");
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Rollback realizado com sucesso.");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Erro ao tentar rollback: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
            }
        }
        return sucesso;
    }

    private int inserirVendaPrincipal(Connection conn, Venda venda) throws SQLException {
        String sql = "INSERT INTO venda (data, valor_total, id_funcionario, id_cliente) VALUES (NOW(), ?, ?, ?)";
        int id_venda_gerada = -1;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setBigDecimal(1, venda.getValorTotal());
            stmt.setInt(2, venda.getIdFuncionario());

            if (venda.getIdCliente() != null) {
                stmt.setInt(3, venda.getIdCliente());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id_venda_gerada = rs.getInt(1);
                }
            }
        }
        return id_venda_gerada;
    }

    private boolean atualizarEstoque(Connection conn, List<Item_venda> itens) throws SQLException {
        String sql = "UPDATE produto SET qnt_estoque = qnt_estoque - ? WHERE id_produto = ? AND qnt_estoque >= ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Item_venda item : itens) {
                stmt.setInt(1, item.getQuantidade());
                stmt.setInt(2, item.getIdProduto());
                stmt.setInt(3, item.getQuantidade());

                stmt.addBatch();
            }

            int[] resultados = stmt.executeBatch();

            for (int resultado : resultados) {
                if (resultado <= 0) {
                    return false;
                }
            }
            return true;
        }
    }

    public List<Venda> buscarTodasVendas() {
        String sql = "SELECT id_venda, id_cliente, id_funcionario, data, valor_total FROM venda ORDER BY data DESC";
        return buscarVendasPorQuery(sql);
    }

    public List<Venda> buscarVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT id_venda, id_cliente, id_funcionario, data, valor_total FROM venda WHERE data BETWEEN ? AND ? ORDER BY data DESC";
        return buscarVendasPorQuery(sql, inicio, fim);
    }


    // Método Auxiliar para mapear o ResultSet
    private List<Venda> buscarVendasPorQuery(String sql, Object... params) {
        List<Venda> vendas = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i] instanceof LocalDateTime) {
                        stmt.setTimestamp(i + 1, java.sql.Timestamp.valueOf((LocalDateTime) params[i]));
                    } else {
                        stmt.setObject(i + 1, params[i]);
                    }
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Venda venda = new Venda();
                    venda.setId(rs.getInt("id_venda"));
                    venda.setIdCliente(rs.getInt("id_cliente"));
                    venda.setIdFuncionario(rs.getInt("id_funcionario"));
                    venda.setData(rs.getTimestamp("data").toLocalDateTime());
                    venda.setValorTotal(rs.getBigDecimal("valor_total"));
                    vendas.add(venda);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar vendas: " + e.getMessage());
        }
        return vendas;
    }
}