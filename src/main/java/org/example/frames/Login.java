package org.example.frames;

import org.example.daos.UserDao;
import org.example.implementations.UserDaoImpl;
import org.example.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class Login extends JFrame {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Font poppinsBold;
    private Font poppinsRegular;

    private final Color PRIMARY_COLOR = new Color(25, 135, 84);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color TEXT_COLOR = new Color(33, 37, 41);

    public Login() {
        loadFonts();
        initializeFrame();
        setupComponents();
        setVisible(true);
    }

    private void loadFonts() {
        try {
            InputStream isBold = getClass().getResourceAsStream("/fonts/Poppins/Poppins-Bold.ttf");
            InputStream isRegular = getClass().getResourceAsStream("/fonts/Poppins/Poppins-Regular.ttf");
            poppinsBold = Font.createFont(Font.TRUETYPE_FONT, isBold);
            poppinsRegular = Font.createFont(Font.TRUETYPE_FONT, isRegular);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(poppinsBold);
            ge.registerFont(poppinsRegular);
        } catch (IOException | FontFormatException e) {
            poppinsBold = new Font("Arial", Font.BOLD, 12);
            poppinsRegular = new Font("Arial", Font.PLAIN, 12);
        }
    }

    private void initializeFrame() {
        setTitle("Système de Gestion Médicale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(BACKGROUND_COLOR);
    }

    private void setupComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(BACKGROUND_COLOR);

        setupLeftPanel();
        setupRightPanel();

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);
    }

    private void setupLeftPanel() {
        leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(PRIMARY_COLOR);

        JPanel welcomeTextPanel = new JPanel(new GridBagLayout());
        welcomeTextPanel.setBackground(PRIMARY_COLOR);
        GridBagConstraints welcomeGbc = new GridBagConstraints();
        welcomeGbc.insets = new Insets(5, 5, 5, 5);

        JLabel welcomeLabel = new JLabel("Bienvenue");
        welcomeLabel.setFont(poppinsBold.deriveFont(36f));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeGbc.gridy = 0;
        welcomeTextPanel.add(welcomeLabel, welcomeGbc);

        JLabel subtitleLabel = new JLabel("S.G Visites Médicale");
        subtitleLabel.setFont(poppinsRegular.deriveFont(18f));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        welcomeGbc.gridy = 1;
        welcomeTextPanel.add(subtitleLabel, welcomeGbc);

        leftPanel.add(welcomeTextPanel);
    }

    private void setupRightPanel() {
        rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Login title
        JLabel loginTitle = new JLabel("Connexion");
        loginTitle.setFont(poppinsBold.deriveFont(24f));
        loginTitle.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 8, 20, 8);
        rightPanel.add(loginTitle, gbc);

        // Username field
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        JLabel usernameLabel = new JLabel("Nom d'utilisateur");
        usernameLabel.setFont(poppinsRegular.deriveFont(14f));
        rightPanel.add(usernameLabel, gbc);

        gbc.gridy = 2;
        usernameField = createStyledTextField();
        rightPanel.add(usernameField, gbc);

        // Password field
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Mot de passe");
        passwordLabel.setFont(poppinsRegular.deriveFont(14f));
        rightPanel.add(passwordLabel, gbc);

        gbc.gridy = 4;
        passwordField = createStyledPasswordField();
        rightPanel.add(passwordField, gbc);

        // Login button
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 8, 8, 8);
        loginButton = createStyledButton("Se connecter");
        loginButton.addActionListener(e -> handleLogin());
        rightPanel.add(loginButton, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setPreferredSize(new Dimension(250, 35));
        field.setFont(poppinsRegular.deriveFont(14f));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setPreferredSize(new Dimension(250, 35));
        field.setFont(poppinsRegular.deriveFont(14f));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(250, 40));
        button.setFont(poppinsBold.deriveFont(14f));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(21, 115, 71));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private void handleLogin() {
        UserDao userDao = new UserDaoImpl();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Optional<User> user = userDao.findByUsername(username);

        if (user.isEmpty()) {
            showErrorMessage("Utilisateur non trouvé !");
        } else if (!password.equals(user.get().getPassword())) {
            showErrorMessage("Mot de passe incorrect !");
        } else {
            this.dispose();
            SwingUtilities.invokeLater(() -> new Root());
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Erreur d'authentification",
                JOptionPane.ERROR_MESSAGE
        );
    }
}