package main.java.component;

import main.java.model.Teacher;
import main.java.view.GlobalStyle;
import main.java.utility.Helper;

import javax.swing.*;
import java.awt.*;

public class TeacherFormDialog extends JDialog {
    private JTextField teacherIdField;
    private JTextField nameField;
    private JTextField dobField;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean isEditMode;
    private Teacher teacher;

    public TeacherFormDialog(JFrame parent, boolean isEditMode, Teacher teacher) {
        super(parent, isEditMode ? "Sửa giáo viên" : "Thêm giáo viên", true);
        this.isEditMode = isEditMode;
        this.teacher = teacher != null ? teacher : new Teacher();

        setSize(400, 350);
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
        JLabel teacherIdLabel = new JLabel("Mã GV:");
        teacherIdLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(teacherIdLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        teacherIdField = new JTextField(20);
        teacherIdField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode) {
            teacherIdField.setEditable(false);
            teacherIdField.setText(teacher.getTeacherId());
        }
        formPanel.add(teacherIdField, gbc);

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
        if (isEditMode && teacher.getName() != null) {
            nameField.setText(teacher.getName());
        }
        formPanel.add(nameField, gbc);

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
        if (isEditMode && teacher.getDob() != null) {
            dobField.setText(teacher.getDob().toString());
        }
        formPanel.add(dobField, gbc);

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
        if (isEditMode && teacher.getPhone() != null) {
            phoneField.setText(teacher.getPhone());
        }
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        emailField = new JTextField(20);
        emailField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && teacher.getEmail() != null) {
            emailField.setText(teacher.getEmail());
        }
        formPanel.add(emailField, gbc);

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

    public Teacher getTeacher() {
        teacher.setTeacherId(teacherIdField.getText().trim());
        teacher.setName(nameField.getText().trim());
        
        String dobText = dobField.getText().trim();
        if (!dobText.isEmpty()) {
            teacher.setDob(Helper.parseDate(dobText));
        } else {
            teacher.setDob(null);
        }
        
        teacher.setPhone(phoneField.getText().trim());
        teacher.setEmail(emailField.getText().trim());
        return teacher;
    }

    public JButton getSaveButton() {
        return saveButton;
    }
}

