package org.example.frames;

import org.example.daos.UserDao;
import org.example.implementations.UserDaoImpl;
import org.example.models.User;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class Login extends JFrame {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public Login() {
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        // Create main panel with GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Left panel (green) with welcome message
        leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(34, 139, 34));
        JLabel welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(Color.WHITE);
        leftPanel.add(welcomeLabel);

        // Right panel (login form)
        rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        rightPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        rightPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        rightPanel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        rightPanel.add(loginButton, gbc);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);
        setVisible(true);
    }

    private void handleLogin() {

        UserDao userDao = new UserDaoImpl();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Optional<User> user = userDao.findByUsername(username);

        if(user.isEmpty()){
            JOptionPane.showMessageDialog(this, "Utilisateur non trouvé !", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Simple validation (replace with your authentication logic)
            if (password.equals(user.get().getPassword())) {
                this.dispose(); // Close login window
                SwingUtilities.invokeLater(() -> new Root()); // Open Root frame
            } else {
                JOptionPane.showMessageDialog(this, "Mot de passe incorrect !", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
