package tubes.pbo.maven.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KonekDatabase {
private String url = "jdbc:mysql://localhost:3306/stickman";
private String username = "root";
private String password = "#Deaenze123";

//public Connection getConn() throws SQLException {
//    return DriverManager.getConnection(url,username,password);
//}
public void getInput(int level_stress,int jauh_dari_tuhan) throws SQLException  {

    try(Connection connection = DriverManager.getConnection(url,username,password)){
        String sql = "INSERT INTO iman (level_stress, jauh_dari_tuhan) VALUES (?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,level_stress);
            statement.setInt(2,jauh_dari_tuhan);
            statement.executeUpdate();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

}
}
