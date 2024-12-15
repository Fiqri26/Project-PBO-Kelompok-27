import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/puzzel";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<Puzzel> getPuzzel() {
        List<Puzzel> myPuzzle = new ArrayList<>();
        String query = "SELECT * FROM pictzzel";

        try (
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                int level = resultSet.getInt("Level");
                String imagePath = resultSet.getString("ImagePath");

                myPuzzle.add(new Puzzel(id, level, imagePath));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching data from database: " + e.getMessage());
            e.printStackTrace();
        }

        return myPuzzle;
    }
}
