package tubes.pbo.maven;

import tubes.pbo.maven.form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog {
    private int authenticatedUserId;
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
                    System.out.println("Successful Authentication of: " + user.getName());
                    System.out.println("Email: " + user.getEmail());
                    System.out.println("Phone: " + user.getPhone());
                    System.out.println("Address: " + user.getAddress());
                    authenticatedUserId = user.getId(); // Perbarui authenticatedUserId
                    dispose();
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

    public int getAuthenticatedUserId() {
        return authenticatedUserId;
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
                user.setId(resultSet.getInt("id")); // Perbarui id pada objek user
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

    private void openRegistrationForm() {
        form registrationForm = new form();
        registrationForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registrationForm.setVisible(true);
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        int authenticatedUserId = loginForm.getAuthenticatedUserId();
        System.out.println("Authenticated User ID: " + authenticatedUserId);
    }
}
