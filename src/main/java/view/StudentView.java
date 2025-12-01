package main.java.view;

import main.java.component.HeaderPanel;
import main.java.component.SidebarPanel;
import main.java.component.FooterPanel;
import main.java.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class StudentView extends JPanel {
    private JTextField searchField;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton detailButton;
    private JButton editButton;
    private JButton deleteButton;

    public StudentView() {
        setLayout(new BorderLayout());
        setBackground(GlobalStyle.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Quản lý Sinh viên");
        titleLabel.setFont(GlobalStyle.FONT_TITLE);
        titleLabel.setForeground(GlobalStyle.COLOR_TEXT_PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchLabel.setFont(GlobalStyle.FONT_BOLD);
        searchPanel.add(searchLabel);

        searchField = new JTextField(20);
        searchField.setFont(GlobalStyle.FONT_NORMAL);
        searchPanel.add(searchField);

        JButton searchButton = GlobalStyle.createPrimaryButton("Tìm kiếm");
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchPanel.add(searchButton);

        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Mã SV", "Họ tên", "Ngày sinh", "SĐT", "Tuổi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setFont(GlobalStyle.FONT_NORMAL);
        studentTable.setRowHeight(30);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setFont(GlobalStyle.FONT_BOLD);
        studentTable.getTableHeader().setBackground(GlobalStyle.COLOR_PRIMARY);
        studentTable.getTableHeader().setForeground(Color.WHITE);
        TableStyleHelper.configureTableStyle(studentTable);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        addButton = GlobalStyle.createPrimaryButton("Thêm sinh viên");
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

        add(centerPanel, BorderLayout.CENTER);
    }

    public void loadStudents(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            Object[] row = {
                student.getStudentId(),
                student.getName(),
                student.getDob() != null ? student.getDob().toString() : "",
                student.getPhone() != null ? student.getPhone() : "",
                student.getAge()
            };
            tableModel.addRow(row);
        }
    }

    public Student getSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }

        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        // This will be handled by controller
        return new Student();
    }

    public String getSelectedStudentId() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return (String) tableModel.getValueAt(selectedRow, 0);
    }

    public String getSearchText() {
        return searchField.getText().trim();
    }

    public JTextField getSearchField() {
        return searchField;
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

    public JTable getStudentTable() {
        return studentTable;
    }
}

