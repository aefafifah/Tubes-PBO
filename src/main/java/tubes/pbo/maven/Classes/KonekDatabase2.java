package tubes.pbo.maven.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KonekDatabase2 {
    private String url = "jdbc:mysql://localhost:3306/stickman";
    private String username = "root";
    private String password = "#Deaenze123";

    public void getInput2(int userId, boolean ngantuk, boolean capek) throws SQLException {

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO sehat (id_user, ngantuk, capek) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                statement.setBoolean(2, ngantuk);
                statement.setBoolean(3, capek);
                statement.executeUpdate();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}
