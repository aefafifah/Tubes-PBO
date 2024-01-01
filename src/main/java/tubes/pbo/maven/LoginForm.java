package tubes.pbo.maven;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

public class LoginForm extends JDialog {
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel loginPanel;

    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMaximumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ImageIcon logo;
//        try {
//            logo = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/key (1).png")));
//        } catch (IOException e) {
//            // Handle the exception
//            System.out.println("Error loading image: " + e.getMessage());
//            logo = null;
//        }
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                Users user = getAuthenticatedUser(email, password);

                if (user != null) {
                    dispose();
                    System.out.println("Successful Authentication of: " + user.getName());
                    System.out.println("Email: " + user.getEmail());
                    System.out.println("Phone: " + user.getPhone());
                    System.out.println("Address: " + user.getAddress());
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email Password Invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        pack();
        setVisible(true); // Dipindahkan ke sini setelah semua komponen ditambahkan
    }

    private Users getAuthenticatedUser(String email, String password) {
        Users user = null;
        final String DB_URL =  "jdbc:mysql://localhost:3306/stickman";
        final String USERNAME = "root";
        final String Password = "#Deaenze123";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, Password);

            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new Users();
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setReligion(resultSet.getString("religion"));
                user.setPassword(resultSet.getString("password"));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {

        var loginForm = new LoginForm(null);
    }
}
