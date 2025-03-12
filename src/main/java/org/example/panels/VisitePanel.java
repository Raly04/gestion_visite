package org.example.panels;

import org.example.implementations.MedecinDaoImpl;
import org.example.implementations.PatientDaoImpl;
import org.example.implementations.VisiteDaoIpml;
import org.example.models.Medecin;
import org.example.models.Patient;
import org.example.models.Visite;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VisitePanel extends CrudPanel<Visite, Integer> {
    private JTextField idField;
    private JComboBox<Patient> patientComboBox;
    private JComboBox<Medecin> medecinComboBox;
    private JXDatePicker datePicker;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton advancedSearchButton;

    private static final PatientDaoImpl patientDao = new PatientDaoImpl();
    private static final MedecinDaoImpl medecinDao = new MedecinDaoImpl();
    private static final VisiteDaoIpml visiteDaoIpml = new VisiteDaoIpml();

    public VisitePanel() {
        super(visiteDaoIpml);
        addAdvancedSearchButton();
    }

    private void addAdvancedSearchButton() {
        advancedSearchButton = new JButton("Recherche avancée");
        advancedSearchButton.addActionListener(e -> showAdvancedSearchDialog());
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                Component[] subComponents = ((JPanel) component).getComponents();
                for (Component subComponent : subComponents) {
                    if (subComponent instanceof JPanel) {
                        Component[] topPanelComponents = ((JPanel) subComponent).getComponents();
                        for (Component topComponent : topPanelComponents) {
                            if (topComponent instanceof JPanel &&
                                    ((JPanel) topComponent).getComponentCount() > 0 &&
                                    ((JPanel) topComponent).getComponent(0) instanceof JPanel) {
                                JPanel searchPanel = (JPanel) ((JPanel) topComponent).getComponent(1);
                                searchPanel.add(advancedSearchButton);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Code Patient", "Code Médecin", "Date"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void updateComboBoxes() {
        // Sauvegarder les sélections actuelles
        Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
        Medecin selectedMedecin = (Medecin) medecinComboBox.getSelectedItem();

        // Mettre à jour les listes
        patientComboBox.removeAllItems();
        medecinComboBox.removeAllItems();

        // Recharger les données depuis la base
        for (Patient patient : patientDao.findAll()) {
            patientComboBox.addItem(patient);
        }

        for (Medecin medecin : medecinDao.findAll()) {
            medecinComboBox.addItem(medecin);
        }

        // Restaurer les sélections si possible
        if (selectedPatient != null) {
            patientComboBox.setSelectedItem(selectedPatient);
        }
        if (selectedMedecin != null) {
            medecinComboBox.setSelectedItem(selectedMedecin);
        }
    }

    @Override
    protected JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Initialize components
        idField = new JTextField(20);
        idField.setEnabled(false);
        patientComboBox = new JComboBox<>();
        medecinComboBox = new JComboBox<>();
        updateComboBoxes();
        datePicker = new JXDatePicker();
        datePicker.setDate(new Date());
        datePicker.setFormats(new SimpleDateFormat("dd/MM/yyyy"));
        datePicker.getEditor().setEditable(false);
        datePicker.setLocale(Locale.FRANCE); // Pour avoir le calendrier en français

        // Personnalisation de l'apparence
        datePicker.setBackground(Color.WHITE);
        datePicker.getEditor().setPreferredSize(new Dimension(200, 35));

        // Add ID field (disabled)
        addFormField(formPanel, "ID:", idField, gbc, 0);

        // Customize comboboxes
        patientComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Patient) {
                    Patient patient = (Patient) value;
                    value = patient.getCodepat() + " - " + patient.getNom() + " " + patient.getPrenom();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        medecinComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Medecin) {
                    Medecin medecin = (Medecin) value;
                    value = medecin.getCodemed() + " - " + medecin.getNom() + " " + medecin.getPrenom();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        // Add components
        addFormField(formPanel, "Patient:", patientComboBox, gbc, 1);
        addFormField(formPanel, "Médecin:", medecinComboBox, gbc, 2);
        addFormField(formPanel, "Date:", datePicker, gbc, 3);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Enregistrer");
        cancelButton = new JButton("Annuler");

        styleButton(saveButton, new Color(25, 135, 84));
        styleButton(cancelButton, new Color(108, 117, 125));

        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> showTable());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addFormField(JPanel panel, String label, JComponent field,
                              GridBagConstraints gbc, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void handleSave() {
        Patient patient = (Patient) patientComboBox.getSelectedItem();
        Medecin medecin = (Medecin) medecinComboBox.getSelectedItem();
        Date date = datePicker.getDate();

        if (patient == null || medecin == null || date == null) {
            JOptionPane.showMessageDialog(this,
                    "Le patient, le médecin et la date sont obligatoires.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Visite visite = new Visite();
        if (!idField.getText().isEmpty()) {
            visite.setId(Long.parseLong(idField.getText()));
        }
        visite.setPatient(patient);
        visite.setMedecin(medecin);
        visite.setDate(date);

        try {
            if (idField.getText().isEmpty()) {
                // Vérifier s'il existe déjà une visite pour ce patient/médecin à cette date
                List<Visite> existingVisites = dao.findAll();
                for (Visite existingVisite : existingVisites) {
                    if (existingVisite.getPatient().equals(patient) &&
                        existingVisite.getMedecin().equals(medecin) &&
                        isSameDay(existingVisite.getDate(), date)) {
                        JOptionPane.showMessageDialog(this,
                                "Une visite existe déjà pour ce patient avec ce médecin à cette date.",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                dao.save(visite);
            } else {
                dao.update(visite);
            }
            loadData();
            showTable();
            clearForm();
        } catch (Exception e) {
            String message = "Erreur lors de l'enregistrement de la visite : ";
            if (e.getMessage().contains("foreign key constraint")) {
                message += "Le patient ou le médecin n'existe plus dans la base de données.";
            } else if (e.getMessage().contains("duplicate")) {
                message += "Cette visite existe déjà.";
            } else {
                message += e.getMessage();
            }
            JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
               cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void deleteEntity(Visite visite) {
        try {
            dao.delete(visite);
        } catch (Exception e) {
            String message = "Impossible de supprimer cette visite : ";
            if (e.getMessage().contains("foreign key constraint")) {
                message += "La visite est liée à d'autres enregistrements.";
            } else {
                message += e.getMessage();
            }
            JOptionPane.showMessageDialog(this,
                    message,
                    "Erreur de suppression",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAdvancedSearchDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Recherche avancée", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JSpinner dateDebut = new JSpinner(new SpinnerDateModel());
        JSpinner dateFin = new JSpinner(new SpinnerDateModel());

        addFormField(formPanel, "Date début:", dateDebut, gbc, 0);
        addFormField(formPanel, "Date fin:", dateFin, gbc, 1);

        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> {
            performAdvancedSearch(null, null, (Date) dateDebut.getValue(), (Date) dateFin.getValue());
            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(searchButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void performAdvancedSearch(Medecin medecin, Patient patient, Date dateDebut, Date dateFin) {
        tableModel.setRowCount(0);
        List<Visite> visites = dao.findAll();

        for (Visite visite : visites) {
            boolean matches = true;

            if (medecin != null && !visite.getMedecin().equals(medecin)) {
                matches = false;
            }
            if (patient != null && !visite.getPatient().equals(patient)) {
                matches = false;
            }
            if (dateDebut != null && visite.getDate().before(dateDebut)) {
                matches = false;
            }
            if (dateFin != null && visite.getDate().after(dateFin)) {
                matches = false;
            }

            if (matches) {
                addVisiteToTable(visite);
            }
        }
    }

    private void addVisiteToTable(Visite visite) {
        tableModel.addRow(new Object[]{
                visite.getId(),
                visite.getPatient().getCodepat(),
                visite.getMedecin().getCodemed(),
                visite.getDate()
        });
    }

    @Override
    protected void clearForm() {
        idField.setText("");
        patientComboBox.setSelectedIndex(0);
        medecinComboBox.setSelectedIndex(0);
        datePicker.setDate(new Date());
    }

    @Override
    protected void populateForm(Visite visite) {
        idField.setText(String.valueOf(visite.getId()));
        patientComboBox.setSelectedItem(visite.getPatient());
        medecinComboBox.setSelectedItem(visite.getMedecin());
        datePicker.setDate(visite.getDate());
    }

    @Override
    protected Visite getEntityFromRow(int row) {
        int modelRow = dataTable.convertRowIndexToModel(row);
        Visite visite = new Visite();
        visite.setId((Long) tableModel.getValueAt(modelRow, 0));

        String codepat = (String) tableModel.getValueAt(modelRow, 1);
        String codemed = (String) tableModel.getValueAt(modelRow, 2);

        visite.setPatient(patientDao.findById(codepat).orElse(null));
        visite.setMedecin(medecinDao.findById(codemed).orElse(null));
        visite.setDate((Date) tableModel.getValueAt(modelRow, 3));

        return visite;
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        List<Visite> visites = dao.findAll();
        for (Visite visite : visites) {
            addVisiteToTable(visite);
        }
    }

    @Override
    protected void showForm(Visite entity) {
        // Mettre à jour les combobox avant d'afficher le formulaire
        updateComboBoxes();
        clearForm();
        if (entity != null) {
            populateForm(entity);
        }
        cardLayout.show(contentPanel, "FORM");
    }
}