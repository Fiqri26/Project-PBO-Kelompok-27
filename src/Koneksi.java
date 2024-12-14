import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/puzzel";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static List<Puzzel> fetchSongs() {
        List<Puzzel> myPuzzle = new ArrayList<>();
        try (
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM pictzzle")
        ) {
            while (resultSet.next()) {
                myPuzzle.add(new Puzzel(
                    resultSet.getInt("Id"),
                    resultSet.getString("ImagePath")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myPuzzle;
    }
}
