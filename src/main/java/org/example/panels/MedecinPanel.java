package org.example.panels;

import org.example.daos.MedecinDao;
import org.example.implementations.MedecinDaoImpl;
import org.example.models.Medecin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class MedecinPanel extends CrudPanel<Medecin , String> {
    private JTextField codeField;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField gradeField;
    private JButton saveButton;
    private JButton cancelButton;

    public MedecinPanel() {
        super(new MedecinDaoImpl());
    }

    @Override
    protected DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[][]{},
                new String[]{"Code", "Nom", "Prénom", "Grade"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    @Override
    protected JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Form fields panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Initialize form fields
        codeField = new JTextField(20);
        nomField = new JTextField(20);
        prenomField = new JTextField(20);
        gradeField = new JTextField(20);

        // Add form components
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Code:"), gbc);
        gbc.gridx = 1;
        formPanel.add(codeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nomField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1;
        formPanel.add(prenomField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Grade:"), gbc);
        gbc.gridx = 1;
        formPanel.add(gradeField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Enregistrer");
        cancelButton = new JButton("Annuler");

        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> showTable());

        // Style buttons
        styleButton(saveButton, new Color(25, 135, 84));
        styleButton(cancelButton, new Color(108, 117, 125));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add panels to main form panel
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void handleSave() {
        String code = codeField.getText().trim();
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String grade = gradeField.getText().trim();

        // Validate required fields
        if (code.isEmpty() || nom.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Les champs Code et Nom sont obligatoires.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if medecin code already exists when adding new medecin
        if (codeField.isEnabled()) {
            Optional<Medecin> existingMedecin = dao.findById(code);
            if (existingMedecin.isPresent()) {
                JOptionPane.showMessageDialog(this,
                        "Un médecin avec le code " + code + " existe déjà.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Medecin medecin = new Medecin();
        medecin.setCodemed(code);
        medecin.setNom(nom);
        medecin.setPrenom(prenom);
        medecin.setGrade(grade);

        try {
            if (codeField.isEnabled()) {
                dao.save(medecin);
            } else {
                dao.update(medecin);
            }
            loadData();
            showTable();
            clearForm();
        } catch (Exception e) {
            String message = "Erreur lors de l'enregistrement : ";
            if (e.getMessage().contains("duplicate")) {
                message += "Un médecin avec ce code existe déjà.";
            } else {
                message += e.getMessage();
            }
            JOptionPane.showMessageDialog(this,
                    message,
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void clearForm() {
        codeField.setText("");
        nomField.setText("");
        prenomField.setText("");
        gradeField.setText("");
        codeField.setEnabled(true);
    }

    @Override
    protected void populateForm(Medecin medecin) {
        codeField.setText(medecin.getCodemed());
        nomField.setText(medecin.getNom());
        prenomField.setText(medecin.getPrenom());
        gradeField.setText(medecin.getGrade());
        codeField.setEnabled(false);
    }

    @Override
    protected Medecin getEntityFromRow(int row) {
        int modelRow = dataTable.convertRowIndexToModel(row);
        Medecin medecin = new Medecin();
        medecin.setCodemed((String) tableModel.getValueAt(modelRow, 0));
        medecin.setNom((String) tableModel.getValueAt(modelRow, 1));
        medecin.setPrenom((String) tableModel.getValueAt(modelRow, 2));
        medecin.setGrade((String) tableModel.getValueAt(modelRow, 3));
        return medecin;
    }

    @Override
    protected void deleteEntity(Medecin medecin) {
        try {
            dao.delete(medecin);
        } catch (Exception e) {
            String message = "Impossible de supprimer ce médecin : ";
            if (e.getMessage().contains("foreign key constraint")) {
                message += "Ce médecin a des visites associées.";
            } else {
                message += e.getMessage();
            }
            JOptionPane.showMessageDialog(this,
                    message,
                    "Erreur de suppression",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        List<Medecin> medecins = dao.findAll();
        for (Medecin medecin : medecins) {
            tableModel.addRow(new Object[]{
                    medecin.getCodemed(),
                    medecin.getNom(),
                    medecin.getPrenom(),
                    medecin.getGrade()
            });
        }
    }

    @Override
    protected void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        dataTable.setRowSorter(sorter);

        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    boolean matches = false;
                    for (int i = 0; i < entry.getValueCount(); i++) {
                        Object value = entry.getValue(i);
                        if (value != null) {
                            String stringValue = value.toString().toLowerCase();
                            if (stringValue.contains(searchText)) {
                                matches = true;
                                break;
                            }
                        }
                    }
                    return matches;
                }
            });
        }
    }
}