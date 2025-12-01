package main.java.view;

import main.java.model.Homeroom;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HomeroomView extends JPanel {
    private JTable homeroomTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton detailButton;
    private JButton editButton;
    private JButton deleteButton;

    public HomeroomView() {
        setLayout(new BorderLayout());
        setBackground(GlobalStyle.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Quản lý Lớp học");
        titleLabel.setFont(GlobalStyle.FONT_TITLE);
        titleLabel.setForeground(GlobalStyle.COLOR_TEXT_PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        String[] columnNames = {"Mã lớp", "Môn học", "Giáo viên", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        homeroomTable = new JTable(tableModel);
        homeroomTable.setFont(GlobalStyle.FONT_NORMAL);
        homeroomTable.setRowHeight(30);
        homeroomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        homeroomTable.getTableHeader().setFont(GlobalStyle.FONT_BOLD);
        homeroomTable.getTableHeader().setBackground(GlobalStyle.COLOR_PRIMARY);
        homeroomTable.getTableHeader().setForeground(Color.WHITE);
        TableStyleHelper.configureTableStyle(homeroomTable);

        JScrollPane scrollPane = new JScrollPane(homeroomTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        addButton = GlobalStyle.createPrimaryButton("Thêm lớp học");
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

    public void loadHomerooms(List<Homeroom> homerooms, java.util.Map<String, String> subjectNames, java.util.Map<String, String> teacherNames) {
        tableModel.setRowCount(0);
        for (Homeroom homeroom : homerooms) {
            Object[] row = {
                homeroom.getClassId(),
                subjectNames.getOrDefault(homeroom.getSubjectId(), homeroom.getSubjectId()),
                teacherNames.getOrDefault(homeroom.getTeacherId(), homeroom.getTeacherId()),
                homeroom.getStartDate() != null ? homeroom.getStartDate().toString() : "",
                homeroom.getEndDate() != null ? homeroom.getEndDate().toString() : "",
                homeroom.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    public String getSelectedClassId() {
        int selectedRow = homeroomTable.getSelectedRow();
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

