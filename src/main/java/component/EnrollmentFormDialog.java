package main.java.component;

import main.java.model.Enrollment;
import main.java.view.GlobalStyle;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class EnrollmentFormDialog extends JDialog {
    private JComboBox<String> classComboBox;
    private JComboBox<String> studentComboBox;
    private JSpinner attendanceSpinner;
    private JSpinner homeworkSpinner;
    private JSpinner midTermSpinner;
    private JSpinner endTermSpinner;
    private JLabel finalScoreLabel;
    private JLabel resultLabel;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean isEditMode;
    private Enrollment enrollment;

    public EnrollmentFormDialog(JFrame parent, boolean isEditMode, Enrollment enrollment,
                               List<String> classes, List<String> students) {
        super(parent, isEditMode ? "Sửa điểm số" : "Thêm đăng ký", true);
        this.isEditMode = isEditMode;
        this.enrollment = enrollment != null ? enrollment : new Enrollment();

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setResizable(false);

        createForm(classes, students);
    }

    private void createForm(List<String> classes, List<String> students) {
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
        JLabel classLabel = new JLabel("Lớp học:");
        classLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(classLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        DefaultComboBoxModel<String> classModel = new DefaultComboBoxModel<>();
        for (String className : classes) {
            classModel.addElement(className);
        }
        classComboBox = new JComboBox<>(classModel);
        classComboBox.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && enrollment.getClassId() != null) {
            classComboBox.setSelectedItem(enrollment.getClassId());
            classComboBox.setEnabled(false);
        }
        formPanel.add(classComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel studentLabel = new JLabel("Sinh viên:");
        studentLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(studentLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        DefaultComboBoxModel<String> studentModel = new DefaultComboBoxModel<>();
        for (String student : students) {
            studentModel.addElement(student);
        }
        studentComboBox = new JComboBox<>(studentModel);
        studentComboBox.setFont(GlobalStyle.FONT_NORMAL);
        if (isEditMode && enrollment.getStudentId() != null) {
            studentComboBox.setSelectedItem(enrollment.getStudentId());
            studentComboBox.setEnabled(false);
        }
        formPanel.add(studentComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel attendanceLabel = new JLabel("Chuyên cần (0-10):");
        attendanceLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(attendanceLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        attendanceSpinner = new JSpinner(new SpinnerNumberModel(
                enrollment.getAttendance() != null ? enrollment.getAttendance().doubleValue() : 0.0,
                0.0, 10.0, 0.1));
        attendanceSpinner.setFont(GlobalStyle.FONT_NORMAL);
        formPanel.add(attendanceSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel homeworkLabel = new JLabel("Bài tập (0-10):");
        homeworkLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(homeworkLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        homeworkSpinner = new JSpinner(new SpinnerNumberModel(
                enrollment.getHomework() != null ? enrollment.getHomework().doubleValue() : 0.0,
                0.0, 10.0, 0.1));
        homeworkSpinner.setFont(GlobalStyle.FONT_NORMAL);
        formPanel.add(homeworkSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel midTermLabel = new JLabel("Giữa kỳ (0-10):");
        midTermLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(midTermLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        midTermSpinner = new JSpinner(new SpinnerNumberModel(
                enrollment.getMidTerm() != null ? enrollment.getMidTerm().doubleValue() : 0.0,
                0.0, 10.0, 0.1));
        midTermSpinner.setFont(GlobalStyle.FONT_NORMAL);
        formPanel.add(midTermSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel endTermLabel = new JLabel("Cuối kỳ (0-10):");
        endTermLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(endTermLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        endTermSpinner = new JSpinner(new SpinnerNumberModel(
                enrollment.getEndTerm() != null ? enrollment.getEndTerm().doubleValue() : 0.0,
                0.0, 10.0, 0.1));
        endTermSpinner.setFont(GlobalStyle.FONT_NORMAL);
        formPanel.add(endTermSpinner, gbc);

        // Add listeners to calculate final score
        SpinnerNumberModel[] models = {
                (SpinnerNumberModel) attendanceSpinner.getModel(),
                (SpinnerNumberModel) homeworkSpinner.getModel(),
                (SpinnerNumberModel) midTermSpinner.getModel(),
                (SpinnerNumberModel) endTermSpinner.getModel()
        };
        for (SpinnerNumberModel model : models) {
            model.addChangeListener(e -> calculateFinalScore());
        }

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel finalScoreLabelText = new JLabel("Tổng kết:");
        finalScoreLabelText.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(finalScoreLabelText, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        finalScoreLabel = new JLabel("0.0");
        finalScoreLabel.setFont(GlobalStyle.FONT_BOLD);
        finalScoreLabel.setForeground(GlobalStyle.COLOR_PRIMARY);
        formPanel.add(finalScoreLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel resultLabelText = new JLabel("Kết quả:");
        resultLabelText.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(resultLabelText, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        resultLabel = new JLabel("Failed");
        resultLabel.setFont(GlobalStyle.FONT_BOLD);
        formPanel.add(resultLabel, gbc);

        calculateFinalScore();

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

    private void calculateFinalScore() {
        double attendance = ((Number) attendanceSpinner.getValue()).doubleValue();
        double homework = ((Number) homeworkSpinner.getValue()).doubleValue();
        double midTerm = ((Number) midTermSpinner.getValue()).doubleValue();
        double endTerm = ((Number) endTermSpinner.getValue()).doubleValue();

        double finalScore = 0.1 * attendance + 0.2 * homework + 0.3 * midTerm + 0.4 * endTerm;
        finalScore = Math.round(finalScore * 100.0) / 100.0;

        finalScoreLabel.setText(String.valueOf(finalScore));
        resultLabel.setText(finalScore >= 5.0 ? "Passed" : "Failed");
        resultLabel.setForeground(finalScore >= 5.0 ? GlobalStyle.COLOR_SUCCESS : GlobalStyle.COLOR_DANGER);
    }

    public Enrollment getEnrollment() {
        String selectedClass = (String) classComboBox.getSelectedItem();
        String selectedStudent = (String) studentComboBox.getSelectedItem();
        
        if (selectedClass != null) {
            enrollment.setClassId(selectedClass);
        }
        if (selectedStudent != null) {
            enrollment.setStudentId(selectedStudent);
        }
        
        enrollment.setAttendance(BigDecimal.valueOf(((Number) attendanceSpinner.getValue()).doubleValue()));
        enrollment.setHomework(BigDecimal.valueOf(((Number) homeworkSpinner.getValue()).doubleValue()));
        enrollment.setMidTerm(BigDecimal.valueOf(((Number) midTermSpinner.getValue()).doubleValue()));
        enrollment.setEndTerm(BigDecimal.valueOf(((Number) endTermSpinner.getValue()).doubleValue()));
        
        return enrollment;
    }

    public JButton getSaveButton() {
        return saveButton;
    }
}

