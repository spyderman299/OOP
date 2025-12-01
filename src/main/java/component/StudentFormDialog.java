package main.java.component;

import main.java.model.Student;
import main.java.view.GlobalStyle;
import main.java.utility.Helper;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class StudentFormDialog extends JDialog {
    private JTextField studentIdField;
    private JTextField nameField;
    private JTextField dobField;
    private JTextField phoneField;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean isEditMode;
    private Student student;

    public StudentFormDialog(JFrame parent, boolean isEditMode, Student student) {
        super(parent, isEditMode ? "Sửa sinh viên" : "Thêm sinh viên", true);
        this.isEditMode = isEditMode;
        this.student = student != null ? student : new Student();

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);

        createForm();
    }

    private void createForm() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(GlobalStyle.COLOR_BACKGROUND);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Student ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel studentIdLabel = new JLabel("Mã SV:");
        studentIdLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(studentIdLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        studentIdField = new JTextField(20);
        studentIdField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode) {
            studentIdField.setEditable(false);
            studentIdField.setText(student.getStudentId());
        }
        formPanel.add(studentIdField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel nameLabel = new JLabel("Họ tên:");
        nameLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        nameField = new JTextField(20);
        nameField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && student.getName() != null) {
            nameField.setText(student.getName());
        }
        formPanel.add(nameField, gbc);

        // Date of Birth
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel dobLabel = new JLabel("Ngày sinh (yyyy-MM-dd):");
        dobLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(dobLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        dobField = new JTextField(20);
        dobField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && student.getDob() != null) {
            dobField.setText(student.getDob().toString());
        }
        formPanel.add(dobField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel phoneLabel = new JLabel("SĐT:");
        phoneLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        phoneField = new JTextField(20);
        phoneField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && student.getPhone() != null) {
            phoneField.setText(student.getPhone());
        }
        formPanel.add(phoneField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
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

    public Student getStudent() {
        student.setStudentId(studentIdField.getText().trim());
        student.setName(nameField.getText().trim());
        
        String dobText = dobField.getText().trim();
        if (!dobText.isEmpty()) {
            student.setDob(Helper.parseDate(dobText));
        } else {
            student.setDob(null);
        }
        
        student.setPhone(phoneField.getText().trim());
        return student;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}

