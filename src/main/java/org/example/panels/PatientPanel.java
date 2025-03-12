package org.example.panels;

import org.example.implementations.PatientDaoImpl;
import org.example.models.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class PatientPanel extends CrudPanel<Patient, String> {
    private JTextField codeField;
    private JTextField nomField;
    private JTextField prenomField;
    private JComboBox<String> sexeComboBox;
    private JTextField adresseField;
    private JButton saveButton;
    private JButton cancelButton;

    public PatientPanel() {
        super(new PatientDaoImpl());
    }

    @Override
    protected DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[][]{},
                new String[]{"Code", "Nom", "Prénom", "Sexe", "Adresse"}
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
        sexeComboBox = new JComboBox<>(new String[]{"Homme", "Femme"});
        adresseField = new JTextField(20);

        // Style the combo box
        sexeComboBox.setPreferredSize(new Dimension(200, 35));
        sexeComboBox.setBackground(Color.WHITE);

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
        formPanel.add(new JLabel("Sexe:"), gbc);
        gbc.gridx = 1;
        formPanel.add(sexeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Adresse:"), gbc);
        gbc.gridx = 1;
        formPanel.add(adresseField, gbc);

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
        String sexe = (String) sexeComboBox.getSelectedItem();
        String adresse = adresseField.getText().trim();

        // Validate required fields
        if (code.isEmpty() || nom.isEmpty() || sexe == null) {
            JOptionPane.showMessageDialog(this,
                    "Les champs Code, Nom et Sexe sont obligatoires.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if patient code already exists when adding new patient
        if (codeField.isEnabled()) {
            Optional<Patient> existingPatient = dao.findById(code);
            if (existingPatient.isPresent()) {
                JOptionPane.showMessageDialog(this,
                        "Un patient avec le code " + code + " existe déjà.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Patient patient = new Patient();
        patient.setCodepat(code);
        patient.setNom(nom);
        patient.setPrenom(prenom);
        patient.setSexe(sexe);
        patient.setAdresse(adresse);

        try {
            if (codeField.isEnabled()) {
                dao.save(patient);
            } else {
                dao.update(patient);
            }
            loadData();
            showTable();
            clearForm();
        } catch (Exception e) {
            String message = "Erreur lors de l'enregistrement : ";
            if (e.getMessage().contains("duplicate")) {
                message += "Un patient avec ce code existe déjà.";
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
        sexeComboBox.setSelectedIndex(0);
        adresseField.setText("");
        codeField.setEnabled(true);
    }

    @Override
    protected void populateForm(Patient patient) {
        codeField.setText(patient.getCodepat());
        nomField.setText(patient.getNom());
        prenomField.setText(patient.getPrenom());
        sexeComboBox.setSelectedItem(patient.getSexe());
        adresseField.setText(patient.getAdresse());
        codeField.setEnabled(false);
    }

    @Override
    protected Patient getEntityFromRow(int row) {
        int modelRow = dataTable.convertRowIndexToModel(row);
        Patient patient = new Patient();
        patient.setCodepat((String) tableModel.getValueAt(modelRow, 0));
        patient.setNom((String) tableModel.getValueAt(modelRow, 1));
        patient.setPrenom((String) tableModel.getValueAt(modelRow, 2));
        patient.setSexe((String) tableModel.getValueAt(modelRow, 3));
        patient.setAdresse((String) tableModel.getValueAt(modelRow, 4));
        return patient;
    }

    @Override
    protected void deleteEntity(Patient patient) {
        try {
            dao.delete(patient);
        } catch (Exception e) {
            String message = "Impossible de supprimer ce patient : ";
            if (e.getMessage().contains("foreign key constraint")) {
                message += "Ce patient a des visites associées.";
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
        List<Patient> patients = dao.findAll();
        for (Patient patient : patients) {
            tableModel.addRow(new Object[]{
                    patient.getCodepat(),
                    patient.getNom(),
                    patient.getPrenom(),
                    patient.getSexe(),
                    patient.getAdresse()
            });
        }
    }
}