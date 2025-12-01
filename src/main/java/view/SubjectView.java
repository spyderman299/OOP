package main.java.view;

import main.java.model.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SubjectView extends JPanel {
    private JTable subjectTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton detailButton;
    private JButton editButton;
    private JButton deleteButton;

    public SubjectView() {
        setLayout(new BorderLayout());
        setBackground(GlobalStyle.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Quản lý Môn học");
        titleLabel.setFont(GlobalStyle.FONT_TITLE);
        titleLabel.setForeground(GlobalStyle.COLOR_TEXT_PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        String[] columnNames = {"Mã môn", "Tên môn", "Tổng số buổi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        subjectTable = new JTable(tableModel);
        subjectTable.setFont(GlobalStyle.FONT_NORMAL);
        subjectTable.setRowHeight(30);
        subjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectTable.getTableHeader().setFont(GlobalStyle.FONT_BOLD);
        subjectTable.getTableHeader().setBackground(GlobalStyle.COLOR_PRIMARY);
        subjectTable.getTableHeader().setForeground(Color.WHITE);
        TableStyleHelper.configureTableStyle(subjectTable);

        JScrollPane scrollPane = new JScrollPane(subjectTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        addButton = GlobalStyle.createPrimaryButton("Thêm môn học");
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

    public void loadSubjects(List<Subject> subjects) {
        tableModel.setRowCount(0);
        for (Subject subject : subjects) {
            Object[] row = {
                subject.getSubjectId(),
                subject.getName(),
                subject.getTotalSessions()
            };
            tableModel.addRow(row);
        }
    }

    public String getSelectedSubjectId() {
        int selectedRow = subjectTable.getSelectedRow();
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

