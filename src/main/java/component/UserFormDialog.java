package main.java.component;

import main.java.model.User;
import main.java.view.GlobalStyle;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserFormDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JComboBox<String> teacherComboBox;
    private JComboBox<String> studentComboBox;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean isEditMode;
    private User user;
    private List<main.java.model.Teacher> teachers;
    private List<main.java.model.Student> students;

    public UserFormDialog(JFrame parent, boolean isEditMode, User user,
                          List<main.java.model.Teacher> teachers, List<main.java.model.Student> students) {
        super(parent, isEditMode ? "Sửa người dùng" : "Thêm người dùng", true);
        this.isEditMode = isEditMode;
        this.user = user != null ? user : new User();
        this.teachers = teachers;
        this.students = students;

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
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = new JTextField(20);
        usernameField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && user.getUsername() != null) {
            usernameField.setText(user.getUsername());
            usernameField.setEditable(false);
        }
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel passwordLabel = new JLabel(isEditMode ? "Mật khẩu (để trống giữ nguyên):" : "Mật khẩu:");
        passwordLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setFont(GlobalStyle.FONT_NORMAL);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        roleComboBox = new JComboBox<>(new String[]{"ADMIN", "TEACHER", "STUDENT"});
        roleComboBox.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && user.getRole() != null) {
            roleComboBox.setSelectedItem(user.getRole());
        }
        roleComboBox.addActionListener(e -> updateLinkFields());
        formPanel.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel teacherLabel = new JLabel("Giáo viên:");
        teacherLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(teacherLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        DefaultComboBoxModel<String> teacherModel = new DefaultComboBoxModel<>();
        teacherModel.addElement("-- Chọn --");
        for (main.java.model.Teacher teacher : teachers) {
            teacherModel.addElement(teacher.getTeacherId() + " - " + teacher.getName());
        }
        teacherComboBox = new JComboBox<>(teacherModel);
        teacherComboBox.setFont(GlobalStyle.FONT_NORMAL);
        formPanel.add(teacherComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel studentLabel = new JLabel("Sinh viên:");
        studentLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(studentLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        DefaultComboBoxModel<String> studentModel = new DefaultComboBoxModel<>();
        studentModel.addElement("-- Chọn --");
        for (main.java.model.Student student : students) {
            studentModel.addElement(student.getStudentId() + " - " + student.getName());
        }
        studentComboBox = new JComboBox<>(studentModel);
        studentComboBox.setFont(GlobalStyle.FONT_NORMAL);
        formPanel.add(studentComboBox, gbc);

        updateLinkFields();
        if (isEditMode) {
            if (user.getTeacherId() != null) {
                for (int i = 0; i < teacherModel.getSize(); i++) {
                    if (teacherModel.getElementAt(i).startsWith(user.getTeacherId())) {
                        teacherComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
            if (user.getStudentId() != null) {
                for (int i = 0; i < studentModel.getSize(); i++) {
                    if (studentModel.getElementAt(i).startsWith(user.getStudentId())) {
                        studentComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }

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

    private void updateLinkFields() {
        String role = (String) roleComboBox.getSelectedItem();
        boolean isTeacher = "TEACHER".equals(role);
        boolean isStudent = "STUDENT".equals(role);

        teacherComboBox.setEnabled(isTeacher);
        studentComboBox.setEnabled(isStudent);

        if (!isTeacher) {
            teacherComboBox.setSelectedIndex(0);
        }
        if (!isStudent) {
            studentComboBox.setSelectedIndex(0);
        }
    }

    public User getUser() {
        user.setUsername(usernameField.getText().trim());
        
        String password = new String(passwordField.getPassword());
        if (!password.isEmpty() || !isEditMode) {
            user.setPasswordPlain(password);
        }
        
        user.setRole((String) roleComboBox.getSelectedItem());
        
        String selectedTeacher = (String) teacherComboBox.getSelectedItem();
        if (selectedTeacher != null && !selectedTeacher.equals("-- Chọn --")) {
            user.setTeacherId(selectedTeacher.split(" - ")[0]);
            user.setStudentId(null);
        } else {
            user.setTeacherId(null);
        }
        
        String selectedStudent = (String) studentComboBox.getSelectedItem();
        if (selectedStudent != null && !selectedStudent.equals("-- Chọn --")) {
            user.setStudentId(selectedStudent.split(" - ")[0]);
            if (user.getTeacherId() != null) {
                user.setTeacherId(null);
            }
        } else if (user.getTeacherId() == null) {
            user.setStudentId(null);
        }
        
        return user;
    }

    public JButton getSaveButton() {
        return saveButton;
    }
}

