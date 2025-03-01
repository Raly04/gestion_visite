package org.example.frames;

import org.example.panels.MedecinPanel;
import org.example.panels.PatientPanel;
import org.example.panels.UserPanel;

import javax.swing.*;
import java.awt.*;

public class Root extends JFrame {
    private JTabbedPane tabbedPane;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public Root() {
        setTitle("Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        initComponents();
        setupLayout();
        setVisible(true);
    }

    private void initComponents() {
        // Initialize main components
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Create and add tab panels
        tabbedPane.addTab("Utilisateurs", createTabIcon("user"), new UserPanel());
        tabbedPane.addTab("Médecins", createTabIcon("medecin"), new MedecinPanel());
        tabbedPane.addTab("Patients", createTabIcon("patient"), new PatientPanel());

        // Style the tabbed pane
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(createHeader(), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(51, 153, 255));
        header.setPreferredSize(new Dimension(getWidth(), 50));

        JLabel titleLabel = new JLabel("Management System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        header.add(titleLabel, BorderLayout.WEST);

        return header;
    }

    private ImageIcon createTabIcon(String name) {
        // Replace with actual icon loading
        return new ImageIcon(new byte[0]);
    }
}