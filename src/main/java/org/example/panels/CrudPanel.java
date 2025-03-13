package org.example.panels;

import org.example.daos.GenericDao;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public abstract class CrudPanel<T , ID> extends JPanel {
    protected JPanel tablePanel;
    protected JPanel formPanel;
    protected JTable dataTable;
    protected DefaultTableModel tableModel;
    protected JButton addButton;
    protected JButton editButton;
    protected JButton deleteButton;
    protected CardLayout cardLayout;
    protected JPanel contentPanel;
    protected GenericDao<T, ID> dao;
    protected JTextField searchField;

    public CrudPanel(GenericDao<T, ID> dao) {
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

        // Top Panel containing search and buttons
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Buttons panels
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create and style search field
        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(250, 35));
        searchField.putClientProperty("JTextField.placeholderText", "Rechercher...");

        // Create search container with icon
        JPanel searchContainer = new JPanel();
        searchContainer.setLayout(new BoxLayout(searchContainer, BoxLayout.X_AXIS));
        searchContainer.setBackground(Color.WHITE);
        searchContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)
        ));

        // Add search icon
        JLabel searchIcon = new JLabel(UIManager.getIcon("Tree.searchIcon"));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        searchContainer.add(searchIcon);
        searchContainer.add(searchField);
        searchPanel.add(searchContainer);

        // Add search listener
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        // Style buttons
        addButton = new JButton("Ajouter");
        addButton.setIcon(UIManager.getIcon("Tree.addIcon"));
        styleButton(addButton, new Color(25, 135, 84));

        editButton = new JButton("Modifier");
        editButton.setIcon(UIManager.getIcon("Tree.editIcon"));
        styleButton(editButton, new Color(13, 110, 253));
        editButton.setEnabled(false);

        deleteButton = new JButton("Supprimer");
        deleteButton.setIcon(UIManager.getIcon("Tree.removeIcon"));
        styleButton(deleteButton, new Color(220, 53, 69));
        deleteButton.setEnabled(false);

        // Add action listeners
        addButton.addActionListener(e -> showForm(null));
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());

        // Assemble button panels
        leftButtons.add(addButton);
        rightButtons.add(editButton);
        rightButtons.add(deleteButton);

        // Assemble top panel
        topPanel.add(leftButtons, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(rightButtons, BorderLayout.EAST);

        // Add scroll pane with styled table
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Assemble table panel
        tablePanel.add(topPanel, BorderLayout.NORTH);
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

        // Enable sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        dataTable.setRowSorter(sorter);

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

    void styleButton(JButton button, Color backgroundColor) {
        button.setFont(button.getFont().deriveFont(12f));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }

    protected void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        dataTable.setRowSorter(sorter);

        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
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
                "Etes vous sûr de supprimmer cette enregistrement ?",
                "Confirmation",
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

    // Abstract methods
    protected abstract DefaultTableModel createTableModel();
    protected abstract JPanel createFormPanel();
    protected abstract void clearForm();
    protected abstract void populateForm(T entity);
    protected abstract T getEntityFromRow(int row);
    protected abstract void deleteEntity(T entity);
    protected abstract void loadData();
}