package tubes.pbo.maven;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class form extends JDialog {
    private JTextField tfnama;
    private JTextField tfemail;
    private JTextField tfphone;
    private JTextField tfaddress;
    private JTextField tfagama;
    private JButton selesaiButton;
    public JPanel tpt;
    private JPasswordField pspassword;

    private int registeredUserId;

    public form() {
        selesaiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();

                // Menutup form setelah registrasi selesai
                dispose();
            }
        });
    }
    public int getRegisteredUserId() {
        return registeredUserId;
    }
    private void registerUser() {
        String name = tfnama.getText();
        String email = tfemail.getText();
        String phone = tfphone.getText();
        String address = tfaddress.getText();
        String religion = tfagama.getText();
        String password = String.valueOf(pspassword.getPassword());

        if (validateInput(name, email, phone, address, religion, password)) {
            Users newUser = new Users();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setAddress(address);
            newUser.setReligion(religion);
            newUser.setPassword(password);

            int rowsAffected = saveUser(newUser);

            if (rowsAffected > 0) {
                // Set the registeredUserId after successful registration
                registeredUserId = newUser.getId();
                JOptionPane.showMessageDialog(this, "Registration successful dear " + name + "!");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all the required fields.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private boolean validateInput(String name, String email, String phone, String address, String religion, String password) {
        // Perform validation logic here
        return !name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !address.isEmpty() && !religion.isEmpty() && !password.isEmpty();
    }

    private int saveUser(Users user) {
        final String DB_URL = "jdbc:mysql://localhost:3306/stickman";
        final String USERNAME = "root";
        final String PASSWORD = "#Deaenze123";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // Use RETURNING id in the SQL query for PostgreSQL, if you're using MySQL, use Statement.RETURN_GENERATED_KEYS
            String sql = "INSERT INTO users (name, email, phone, address, religion, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getReligion());
            preparedStatement.setString(6, user.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the user was inserted successfully
            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                    System.out.println("Generated User ID: " + user.getId());  // Print generated ID
                } else {
                    System.out.println("Failed to retrieve generated keys.");
                }
            } else {
                System.out.println("User insertion failed.");
            }

            conn.close();

            return rowsAffected;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        form registrationForm = new form();
        registrationForm.setContentPane(registrationForm.tpt);
        registrationForm.setTitle("REGISTRATION FORM....");
        registrationForm.setSize(600, 400);
        registrationForm.setVisible(true);
        registrationForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
}
