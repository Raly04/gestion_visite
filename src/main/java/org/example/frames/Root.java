package org.example.frames;

import org.example.panels.MedecinPanel;
import org.example.panels.PatientPanel;
import org.example.panels.UserPanel;
import org.example.panels.VisitePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Root extends JFrame {
    private JTabbedPane tabbedPane;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private Font poppinsBold;
    private Font poppinsRegular;

    public Root() {
        loadFonts();
        setTitle("Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        initComponents();
        setupLayout();
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

    private void initComponents() {
        // Initialize main components
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Style the tabbed pane
        tabbedPane.setFont(poppinsRegular.deriveFont(14f));
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // Create and add tab panels
        tabbedPane.addTab("Visites", createTabIcon("visite"), new VisitePanel());
        tabbedPane.addTab("Médecins", createTabIcon("medecin"), new MedecinPanel());
        tabbedPane.addTab("Patients", createTabIcon("patient"), new PatientPanel());
        tabbedPane.addTab("Utilisateurs", createTabIcon("utilisateur"), new UserPanel());

        // Apply custom font to tab labels
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            JLabel label = new JLabel(tabbedPane.getTitleAt(i));
            label.setFont(poppinsRegular.deriveFont(14f));
            label.setIcon(createTabIcon(tabbedPane.getTitleAt(i).toLowerCase()));
            tabbedPane.setTabComponentAt(i, label);
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(createHeader(), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 135, 84));
        header.setPreferredSize(new Dimension(getWidth(), 50));

        JLabel titleLabel = new JLabel("S.G Visites Médicale");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(poppinsBold.deriveFont(20f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        header.add(titleLabel, BorderLayout.WEST);

        // Add padding to header
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        return header;
    }

    private ImageIcon createTabIcon(String name) {
        String path = "/icons/" + name + ".png";
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return new ImageIcon(new byte[0]);
        }
    }
}