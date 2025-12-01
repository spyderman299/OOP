package main.java.view;

import main.java.model.Teacher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeacherView extends JPanel {
    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton detailButton;
    private JButton editButton;
    private JButton deleteButton;

    public TeacherView() {
        setLayout(new BorderLayout());
        setBackground(GlobalStyle.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Quản lý Giáo viên");
        titleLabel.setFont(GlobalStyle.FONT_TITLE);
        titleLabel.setForeground(GlobalStyle.COLOR_TEXT_PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        String[] columnNames = {"Mã GV", "Họ tên", "Ngày sinh", "SĐT", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        teacherTable = new JTable(tableModel);
        teacherTable.setFont(GlobalStyle.FONT_NORMAL);
        teacherTable.setRowHeight(30);
        teacherTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teacherTable.getTableHeader().setFont(GlobalStyle.FONT_BOLD);
        teacherTable.getTableHeader().setBackground(GlobalStyle.COLOR_PRIMARY);
        teacherTable.getTableHeader().setForeground(Color.WHITE);
        TableStyleHelper.configureTableStyle(teacherTable);

        JScrollPane scrollPane = new JScrollPane(teacherTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        addButton = GlobalStyle.createPrimaryButton("Thêm giáo viên");
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

    public void loadTeachers(List<Teacher> teachers) {
        tableModel.setRowCount(0);
        for (Teacher teacher : teachers) {
            Object[] row = {
                teacher.getTeacherId(),
                teacher.getName(),
                teacher.getDob() != null ? teacher.getDob().toString() : "",
                teacher.getPhone() != null ? teacher.getPhone() : "",
                teacher.getEmail() != null ? teacher.getEmail() : ""
            };
            tableModel.addRow(row);
        }
    }

    public String getSelectedTeacherId() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return (String) tableModel.getValueAt(selectedRow, 0);
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

