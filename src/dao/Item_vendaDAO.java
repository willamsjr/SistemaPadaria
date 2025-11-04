package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import model.Item_venda;

public class Item_vendaDAO {

    public void cadastrarItens(Connection conn, int id_venda, List<Item_venda> itens) throws SQLException {
        String sql = "INSERT INTO item_venda (id_venda, id_produto, qntd, preco_unit) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Item_venda item : itens) {
                stmt.setInt(1, id_venda);
                stmt.setInt(2, item.getIdProduto());
                stmt.setInt(3, item.getQuantidade());
                stmt.setBigDecimal(4, item.getPrecoUnit());

                stmt.addBatch();
            }

            stmt.executeBatch();
        }
    }
}