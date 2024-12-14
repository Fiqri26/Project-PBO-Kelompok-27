import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Koneksi{
    private static final String URL = "jdbc:mysql://localhost:3306/puzzel";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static List<Puzzel> getPuzzel() {
        List<Puzzel> myPuzzle = new ArrayList<>();
        try (
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM pictzzel")
        ) {
            while (resultSet.next()) {
                String imagePath = resultSet.getString("ImagePath");
                myPuzzle.add(new Puzzel(
                    resultSet.getInt("Id"),
                    imagePath
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myPuzzle;
    }

}
