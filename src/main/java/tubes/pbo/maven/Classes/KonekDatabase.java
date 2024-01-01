package tubes.pbo.maven.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KonekDatabase {

    //public Connection getConn() throws SQLException {
//    return DriverManager.getConnection(url,username,password);
//}
public void getInput(int id_user,int level_stress,int jauh_dari_tuhan) throws SQLException  {

    String url = "jdbc:mysql://localhost:3306/stickman";
    String username = "root";
    String password = "#Deaenze123";
    try(Connection connection = DriverManager.getConnection(url, username, password)){
        String sql = "INSERT INTO iman (user_id,level_stress, jauh_dari_tuhan) VALUES (?, ?,?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id_user);
            statement.setInt(2,level_stress);
            statement.setInt(3,jauh_dari_tuhan);
            statement.executeUpdate();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

}
}
