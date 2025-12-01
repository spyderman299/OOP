package main.java.view;

import main.java.model.Enrollment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class EnrollmentView extends JPanel {
    private JComboBox<String> classFilterComboBox;
    private JComboBox<String> studentFilterComboBox;
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton detailButton;
    private JButton editButton;
    private JButton deleteButton;
    public boolean isStudentView;

    public EnrollmentView() {
        this(false);
    }

    public EnrollmentView(boolean isStudentView) {
        this.isStudentView = isStudentView;
        setLayout(new BorderLayout());
        setBackground(GlobalStyle.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String title = isStudentView ? "Điểm số của tôi" : "Quản lý Điểm số";
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(GlobalStyle.FONT_TITLE);
        titleLabel.setForeground(GlobalStyle.COLOR_TEXT_PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        // Filter panel (hidden for students)
        if (!isStudentView) {
            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            filterPanel.setOpaque(false);
            filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            JLabel classLabel = new JLabel("Lớp học:");
            classLabel.setFont(GlobalStyle.FONT_BOLD);
            filterPanel.add(classLabel);

            classFilterComboBox = new JComboBox<>();
            classFilterComboBox.setFont(GlobalStyle.FONT_NORMAL);
            classFilterComboBox.addItem("Tất cả");
            classFilterComboBox.setPreferredSize(new Dimension(200, 30));
            filterPanel.add(classFilterComboBox);

            JLabel studentLabel = new JLabel("Sinh viên:");
            studentLabel.setFont(GlobalStyle.FONT_BOLD);
            filterPanel.add(studentLabel);

            studentFilterComboBox = new JComboBox<>();
            studentFilterComboBox.setFont(GlobalStyle.FONT_NORMAL);
            studentFilterComboBox.addItem("Tất cả");
            studentFilterComboBox.setPreferredSize(new Dimension(200, 30));
            filterPanel.add(studentFilterComboBox);

            centerPanel.add(filterPanel, BorderLayout.NORTH);
        }

        // Table
        String[] columnNames = {"Mã lớp", "Mã SV", "Họ tên SV", "Chuyên cần", "Bài tập", "Giữa kỳ", "Cuối kỳ", "Tổng kết", "Kết quả"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        enrollmentTable = new JTable(tableModel);
        enrollmentTable.setFont(GlobalStyle.FONT_NORMAL);
        enrollmentTable.setRowHeight(30);
        enrollmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrollmentTable.getTableHeader().setFont(GlobalStyle.FONT_BOLD);
        enrollmentTable.getTableHeader().setBackground(GlobalStyle.COLOR_PRIMARY);
        enrollmentTable.getTableHeader().setForeground(Color.WHITE);
        TableStyleHelper.configureTableStyle(enrollmentTable);

        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel (hidden for students)
        if (!isStudentView) {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            buttonPanel.setOpaque(false);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            addButton = GlobalStyle.createPrimaryButton("Thêm đăng ký");
            addButton.setPreferredSize(new Dimension(140, 35));
            buttonPanel.add(addButton);

            detailButton = GlobalStyle.createInfoButton("Chi tiết");
            detailButton.setPreferredSize(new Dimension(100, 35));
            buttonPanel.add(detailButton);

            editButton = GlobalStyle.createWarningButton("Sửa");
            editButton.setPreferredSize(new Dimension(100, 35));
            buttonPanel.add(editButton);

            deleteButton = GlobalStyle.createDangerButton("Xóa");
            deleteButton.setPreferredSize(new Dimension(100, 35));
            buttonPanel.add(deleteButton);

            centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        add(centerPanel, BorderLayout.CENTER);
    }

    public void loadEnrollments(List<Enrollment> enrollments, java.util.Map<String, String> studentNames) {
        tableModel.setRowCount(0);
        for (Enrollment enrollment : enrollments) {
            Object[] row = {
                enrollment.getClassId(),
                enrollment.getStudentId(),
                studentNames.getOrDefault(enrollment.getStudentId(), enrollment.getStudentId()),
                enrollment.getAttendance() != null ? enrollment.getAttendance().toString() : "0.0",
                enrollment.getHomework() != null ? enrollment.getHomework().toString() : "0.0",
                enrollment.getMidTerm() != null ? enrollment.getMidTerm().toString() : "0.0",
                enrollment.getEndTerm() != null ? enrollment.getEndTerm().toString() : "0.0",
                enrollment.getFinalScore() != null ? enrollment.getFinalScore().toString() : "0.0",
                enrollment.getResult() != null ? enrollment.getResult() : "Failed"
            };
            tableModel.addRow(row);
        }
    }

    public void setClassFilterItems(List<String> classes) {
        if (classFilterComboBox != null) {
            classFilterComboBox.removeAllItems();
            classFilterComboBox.addItem("Tất cả");
            for (String className : classes) {
                classFilterComboBox.addItem(className);
            }
        }
    }

    public void setStudentFilterItems(List<String> students) {
        if (studentFilterComboBox != null) {
            studentFilterComboBox.removeAllItems();
            studentFilterComboBox.addItem("Tất cả");
            for (String student : students) {
                studentFilterComboBox.addItem(student);
            }
        }
    }

    public String getSelectedClassId() {
        int selectedRow = enrollmentTable.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return (String) tableModel.getValueAt(selectedRow, 0);
    }

    public String getSelectedStudentId() {
        int selectedRow = enrollmentTable.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return (String) tableModel.getValueAt(selectedRow, 1);
    }

    public String getSelectedClassFilter() {
        if (classFilterComboBox == null || classFilterComboBox.getSelectedItem() == null) {
            return null;
        }
        String selected = classFilterComboBox.getSelectedItem().toString();
        return "Tất cả".equals(selected) ? null : selected;
    }

    public String getSelectedStudentFilter() {
        if (studentFilterComboBox == null || studentFilterComboBox.getSelectedItem() == null) {
            return null;
        }
        String selected = studentFilterComboBox.getSelectedItem().toString();
        return "Tất cả".equals(selected) ? null : selected;
    }

    public JComboBox<String> getClassFilterComboBox() {
        return classFilterComboBox;
    }

    public JComboBox<String> getStudentFilterComboBox() {
        return studentFilterComboBox;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getDetailButton() {
        return detailButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }
}

