package main.java.view;

import main.java.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserView extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton detailButton;
    private JButton editButton;
    private JButton deleteButton;

    public UserView() {
        setLayout(new BorderLayout());
        setBackground(GlobalStyle.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Quản lý Người dùng");
        titleLabel.setFont(GlobalStyle.FONT_TITLE);
        titleLabel.setForeground(GlobalStyle.COLOR_TEXT_PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        String[] columnNames = {"Username", "Role", "Liên kết"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setFont(GlobalStyle.FONT_NORMAL);
        userTable.setRowHeight(30);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setFont(GlobalStyle.FONT_BOLD);
        userTable.getTableHeader().setBackground(GlobalStyle.COLOR_PRIMARY);
        userTable.getTableHeader().setForeground(Color.WHITE);
        TableStyleHelper.configureTableStyle(userTable);

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        addButton = GlobalStyle.createPrimaryButton("Thêm người dùng");
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

    public void loadUsers(List<User> users, java.util.Map<String, String> teacherNames, java.util.Map<String, String> studentNames) {
        tableModel.setRowCount(0);
        for (User user : users) {
            String link = "";
            if (user.getTeacherId() != null) {
                link = "Giáo viên: " + teacherNames.getOrDefault(user.getTeacherId(), user.getTeacherId());
            } else if (user.getStudentId() != null) {
                link = "Sinh viên: " + studentNames.getOrDefault(user.getStudentId(), user.getStudentId());
            }
            
            Object[] row = {
                user.getUsername(),
                user.getRole(),
                link
            };
            tableModel.addRow(row);
        }
    }

    public int getSelectedUserId() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            return -1;
        }
        // We need to get the actual user ID from the service
        return selectedRow;
    }

    public String getSelectedUsername() {
        int selectedRow = userTable.getSelectedRow();
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

