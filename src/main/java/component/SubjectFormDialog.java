package main.java.component;

import main.java.model.Subject;
import main.java.view.GlobalStyle;

import javax.swing.*;
import java.awt.*;

public class SubjectFormDialog extends JDialog {
    private JTextField subjectIdField;
    private JTextField nameField;
    private JSpinner totalSessionsSpinner;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean isEditMode;
    private Subject subject;

    public SubjectFormDialog(JFrame parent, boolean isEditMode, Subject subject) {
        super(parent, isEditMode ? "Sửa môn học" : "Thêm môn học", true);
        this.isEditMode = isEditMode;
        this.subject = subject != null ? subject : new Subject();

        setSize(400, 250);
        setLocationRelativeTo(parent);
        setResizable(false);

        createForm();
    }

    private void createForm() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(GlobalStyle.COLOR_BACKGROUND);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel subjectIdLabel = new JLabel("Mã môn:");
        subjectIdLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(subjectIdLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        subjectIdField = new JTextField(20);
        subjectIdField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode) {
            subjectIdField.setEditable(false);
            subjectIdField.setText(subject.getSubjectId());
        }
        formPanel.add(subjectIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel nameLabel = new JLabel("Tên môn:");
        nameLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        nameField = new JTextField(20);
        nameField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && subject.getName() != null) {
            nameField.setText(subject.getName());
        }
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel totalSessionsLabel = new JLabel("Tổng số buổi:");
        totalSessionsLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(totalSessionsLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        totalSessionsSpinner = new JSpinner(new SpinnerNumberModel(isEditMode ? subject.getTotalSessions() : 0, 0, 999, 1));
        totalSessionsSpinner.setFont(GlobalStyle.FONT_NORMAL);
        formPanel.add(totalSessionsSpinner, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        saveButton = GlobalStyle.createPrimaryButton("Lưu");
        saveButton.setPreferredSize(new Dimension(100, 35));
        buttonPanel.add(saveButton);

        cancelButton = new JButton("Hủy");
        cancelButton.setFont(GlobalStyle.FONT_NORMAL);
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    public Subject getSubject() {
        subject.setSubjectId(subjectIdField.getText().trim());
        subject.setName(nameField.getText().trim());
        subject.setTotalSessions((Integer) totalSessionsSpinner.getValue());
        return subject;
    }

    public JButton getSaveButton() {
        return saveButton;
    }
}

