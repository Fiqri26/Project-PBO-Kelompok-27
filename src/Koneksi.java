import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/puzzel";
    private static final String USER = "root";
    private static final String PASS = "";

    private Connection connection;

    public Connection openConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Koneksi berhasil ke database!");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Gagal membuka koneksi: " + e.getMessage());
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Koneksi ditutup.");
            }
        } catch (SQLException e) {
            System.err.println("Gagal menutup koneksi: " + e.getMessage());
        }
    }
}