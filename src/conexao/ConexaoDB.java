package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/?user=root";
    private static final String USER = "root@127.0.0.1:3306";
    private static final String PASSWORD = "1234";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/?user=root", "root@127.0.0.1:3306", "1234");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC do MySQL não encontrado. Verifique seu arquivo .jar.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados!");
            System.err.println("Código do Erro: " + e.getErrorCode());
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Connection conexao = getConnection();
        if (conexao != null) {
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");

            try {
                conexao.close();
                System.out.println("Conexão fechada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Falha ao estabelecer a conexão.");
        }

    }
}
