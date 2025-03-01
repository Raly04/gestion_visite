package org.example.panels;


import org.example.implementations.UserDaoImpl;
import org.example.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserPanel extends CrudPanel<User> {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton editButton;    // Add these as class fields
    private JButton deleteButton;

    public UserPanel() {
        super(new UserDaoImpl());
    }

    @Override
    protected DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new String[]{"ID", "Username", "Password"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    }

    @Override
    protected JPanel createFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username field
        usernameField = new JTextField(20);
        addFormField(form, "Username:", usernameField, gbc, 0);

        // Password field
        passwordField = new JPasswordField(20);
        addFormField(form, "Password:", passwordField, gbc, 1);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            User user = new User();
            user.setUsername(usernameField.getText());
            user.setPassword(new String(passwordField.getPassword()));
            dao.save(user);
            showTable();
        });

        cancelButton.addActionListener(e -> showTable());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        form.add(buttonPanel, gbc);

        return form;
    }

    @Override
    protected void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
    }

    @Override
    protected void populateForm(User entity) {
        usernameField.setText(entity.getUsername());
        // Optionally set password field if needed
        passwordField.setText(entity.getPassword());
    }

    @Override
    protected User getEntityFromRow(int row) {
        User user = new User();
        user.setId((Integer) tableModel.getValueAt(row, 0));
        user.setUsername((String) tableModel.getValueAt(row, 1));
        // Get actual password from database if needed
        return user;
    }

    @Override
    protected void deleteEntity(User entity) {
        dao.delete(entity);
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        List<User> users = dao.findAll();

        for (User user : users) {
            tableModel.addRow(new Object[]{
                    user.getId(),
                    user.getUsername(),
                    "********" // Hide password in table
            });
        }
    }

    private void addFormField(JPanel form, String label, JComponent field,
                              GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        form.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        form.add(field, gbc);
    }
}