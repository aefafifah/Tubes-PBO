package tubes.pbo.maven.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KonekDatabase3 {
    private static final String url = "jdbc:mysql://localhost:3306/stickman";
    private static final String username = "root";
    private static final String password = "#Deaenze123";

    public void saveReminder(int userId, String title, int hour, int minute) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO reminder (userid, title, hour, minute) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                statement.setString(2, title);
                statement.setInt(3, hour);
                statement.setInt(4, minute);
                statement.executeUpdate();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

}
