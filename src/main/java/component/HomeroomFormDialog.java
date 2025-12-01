package main.java.component;

import main.java.model.Homeroom;
import main.java.view.GlobalStyle;
import main.java.utility.Helper;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class HomeroomFormDialog extends JDialog {
    private JTextField classIdField;
    private JComboBox<String> subjectComboBox;
    private JComboBox<String> teacherComboBox;
    private JTextField startDateField;
    private JTextField endDateField;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean isEditMode;
    private Homeroom homeroom;
    private List<main.java.model.Subject> subjects;
    private List<main.java.model.Teacher> teachers;

    public HomeroomFormDialog(JFrame parent, boolean isEditMode, Homeroom homeroom,
                             List<main.java.model.Subject> subjects, List<main.java.model.Teacher> teachers) {
        super(parent, isEditMode ? "Sửa lớp học" : "Thêm lớp học", true);
        this.isEditMode = isEditMode;
        this.homeroom = homeroom != null ? homeroom : new Homeroom();
        this.subjects = subjects;
        this.teachers = teachers;

        setSize(450, 380);
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
        JLabel classIdLabel = new JLabel("Mã lớp:");
        classIdLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(classIdLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        classIdField = new JTextField(20);
        classIdField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode) {
            classIdField.setEditable(false);
            classIdField.setText(homeroom.getClassId());
        }
        formPanel.add(classIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel subjectLabel = new JLabel("Môn học:");
        subjectLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(subjectLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        DefaultComboBoxModel<String> subjectModel = new DefaultComboBoxModel<>();
        for (main.java.model.Subject subject : subjects) {
            subjectModel.addElement(subject.getSubjectId() + " - " + subject.getName());
        }
        subjectComboBox = new JComboBox<>(subjectModel);
        subjectComboBox.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && homeroom.getSubjectId() != null) {
            for (int i = 0; i < subjectModel.getSize(); i++) {
                if (subjectModel.getElementAt(i).startsWith(homeroom.getSubjectId())) {
                    subjectComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        formPanel.add(subjectComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel teacherLabel = new JLabel("Giáo viên:");
        teacherLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(teacherLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        DefaultComboBoxModel<String> teacherModel = new DefaultComboBoxModel<>();
        for (main.java.model.Teacher teacher : teachers) {
            teacherModel.addElement(teacher.getTeacherId() + " - " + teacher.getName());
        }
        teacherComboBox = new JComboBox<>(teacherModel);
        teacherComboBox.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && homeroom.getTeacherId() != null) {
            for (int i = 0; i < teacherModel.getSize(); i++) {
                if (teacherModel.getElementAt(i).startsWith(homeroom.getTeacherId())) {
                    teacherComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        formPanel.add(teacherComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel startDateLabel = new JLabel("Ngày bắt đầu (yyyy-MM-dd):");
        startDateLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(startDateLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        startDateField = new JTextField(20);
        startDateField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && homeroom.getStartDate() != null) {
            startDateField.setText(homeroom.getStartDate().toString());
        }
        formPanel.add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel endDateLabel = new JLabel("Ngày kết thúc (yyyy-MM-dd):");
        endDateLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(endDateLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        endDateField = new JTextField(20);
        endDateField.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && homeroom.getEndDate() != null) {
            endDateField.setText(homeroom.getEndDate().toString());
        }
        formPanel.add(endDateField, gbc);

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

    public Homeroom getHomeroom() {
        homeroom.setClassId(classIdField.getText().trim());
        
        String selectedSubject = (String) subjectComboBox.getSelectedItem();
        if (selectedSubject != null) {
            homeroom.setSubjectId(selectedSubject.split(" - ")[0]);
        }
        
        String selectedTeacher = (String) teacherComboBox.getSelectedItem();
        if (selectedTeacher != null) {
            homeroom.setTeacherId(selectedTeacher.split(" - ")[0]);
        }
        
        String startDateText = startDateField.getText().trim();
        if (!startDateText.isEmpty()) {
            homeroom.setStartDate(Helper.parseDate(startDateText));
        }
        
        String endDateText = endDateField.getText().trim();
        if (!endDateText.isEmpty()) {
            homeroom.setEndDate(Helper.parseDate(endDateText));
        }
        
        return homeroom;
    }

    public JButton getSaveButton() {
        return saveButton;
    }
}

