package tubes.pbo.maven.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KonekDatabase2 {
    private String url = "jdbc:mysql://localhost:3306/stickman";
    private String username = "root";
    private String password = "#Deaenze123";

    //public Connection getConn() throws SQLException {
//    return DriverManager.getConnection(url,username,password);
//}
    public void getInput2(boolean ngantuk ,boolean capek) throws SQLException  {

        try(Connection connection = DriverManager.getConnection(url,username,password)){
            String sql = "INSERT INTO sehat (ngantuk, capek) VALUES (?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setBoolean(1,ngantuk);
                statement.setBoolean(2,capek);
                statement.executeUpdate();
            }
            catch (SQLException sqlException){
                sqlException.printStackTrace();
            }
        }

    }
}
