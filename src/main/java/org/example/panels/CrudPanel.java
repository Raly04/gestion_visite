package org.example.panels;

import org.example.daos.GenericDao;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public abstract class CrudPanel<T> extends JPanel {
    protected JPanel tablePanel;
    protected JPanel formPanel;
    protected JTable dataTable;
    protected DefaultTableModel tableModel;
    protected JButton addButton;
    protected JButton editButton;
    protected JButton deleteButton;
    protected CardLayout cardLayout;
    protected JPanel contentPanel;
    protected GenericDao<T, Integer> dao;

    public CrudPanel(GenericDao<T, Integer> dao) {
        this.dao = dao;
        initComponents();
        setupLayout();
        loadData();
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Table Panel
        tablePanel = new JPanel(new BorderLayout(0, 15));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        setupTable();

        // Action buttons panel at the top
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        // Style Add button
        addButton = new JButton("Ajouter");
        addButton.setIcon(UIManager.getIcon("Tree.addIcon"));
        styleButton(addButton, new Color(25, 135, 84));

        // Style Edit button
        editButton = new JButton("Modifier");
        editButton.setIcon(UIManager.getIcon("Tree.editIcon"));
        styleButton(editButton, new Color(13, 110, 253));
        editButton.setEnabled(false);

        // Style Delete button
        deleteButton = new JButton("Supprimer");
        deleteButton.setIcon(UIManager.getIcon("Tree.removeIcon"));
        styleButton(deleteButton, new Color(220, 53, 69));
        deleteButton.setEnabled(false);

        // Add action listeners
        addButton.addActionListener(e -> showForm(null));
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());

        // Add buttons to panels
        leftButtons.add(addButton);
        rightButtons.add(editButton);
        rightButtons.add(deleteButton);

        // Setup action panel
        actionPanel.add(leftButtons, BorderLayout.WEST);
        actionPanel.add(rightButtons, BorderLayout.EAST);

        // Add scroll pane with styled table
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Add panels to table panel
        tablePanel.add(actionPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Form Panel
        formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add panels to card layout
        contentPanel.add(tablePanel, "TABLE");
        contentPanel.add(formPanel, "FORM");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    protected void setupTable() {
        tableModel = createTableModel();
        dataTable = new JTable(tableModel);
        styleTable();

        // Add selection listener
        dataTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = dataTable.getSelectedRow() != -1;
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });
    }

    protected void styleTable() {
        dataTable.setShowGrid(true);
        dataTable.setGridColor(new Color(233, 236, 239));
        dataTable.setRowHeight(40);
        dataTable.setFont(dataTable.getFont().deriveFont(12f));
        dataTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        dataTable.getTableHeader().setFont(dataTable.getFont().deriveFont(Font.BOLD));
        dataTable.getTableHeader().setBackground(new Color(248, 249, 250));
        dataTable.getTableHeader().setForeground(new Color(33, 37, 41));
        dataTable.setSelectionBackground(new Color(13, 110, 253, 25));
        dataTable.setSelectionForeground(Color.BLACK);

        // Center align table header
        ((DefaultTableCellRenderer)dataTable.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(button.getFont().deriveFont(12f));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }

    private void handleEdit() {
        int row = dataTable.getSelectedRow();
        if (row != -1) {
            T entity = getEntityFromRow(row);
            showForm(entity);
        }
    }

    private void handleDelete() {
        int row = dataTable.getSelectedRow();
        if (row != -1 && confirmDelete()) {
            T entity = getEntityFromRow(row);
            deleteEntity(entity);
            loadData();
        }
    }

    protected boolean confirmDelete() {
        return JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this item?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    protected void showForm(T entity) {
        clearForm();
        if (entity != null) {
            populateForm(entity);
        }
        cardLayout.show(contentPanel, "FORM");
    }

    protected void showTable() {
        cardLayout.show(contentPanel, "TABLE");
        loadData();
    }

    // Abstract methods to be implemented by specific panels
    protected abstract DefaultTableModel createTableModel();
    protected abstract JPanel createFormPanel();
    protected abstract void clearForm();
    protected abstract void populateForm(T entity);
    protected abstract T getEntityFromRow(int row);
    protected abstract void deleteEntity(T entity);
    protected abstract void loadData();
}