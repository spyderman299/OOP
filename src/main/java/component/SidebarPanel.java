package main.java.component;

import main.java.view.GlobalStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel {
    private JPanel userInfoPanel;
    private JLabel avatarLabel;
    private JLabel usernameLabel;
    private JLabel roleLabel;
    private JPanel menuPanel;
    private List<MenuItem> menuItems;
    private String currentRole;
    private MenuItem selectedItem;
    private MenuItemListener menuItemListener;

    public interface MenuItemListener {
        void onMenuItemSelected(String menuId);
    }

    private static class MenuItem {
        String id;
        String label;
        JPanel panel;
        JLabel labelComponent;

        MenuItem(String id, String label) {
            this.id = id;
            this.label = label;
        }
    }

    public SidebarPanel() {
        setLayout(new BorderLayout());
        setBackground(GlobalStyle.COLOR_SECONDARY);
        setPreferredSize(new Dimension(250, 0));
        menuItems = new ArrayList<>();

        // User info panel at top
        createUserInfoPanel();

        // Menu panel
        createMenuPanel();

        add(userInfoPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);
    }

    private void createUserInfoPanel() {
        userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(GlobalStyle.COLOR_SECONDARY);
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Avatar
        avatarLabel = new JLabel();
        avatarLabel.setFont(new Font("Arial", Font.BOLD, 40));
        avatarLabel.setForeground(GlobalStyle.COLOR_PRIMARY);
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setPreferredSize(new Dimension(60, 60));
        avatarLabel.setMinimumSize(new Dimension(60, 60));
        avatarLabel.setMaximumSize(new Dimension(60, 60));
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(Color.WHITE);
        avatarLabel.setBorder(BorderFactory.createLineBorder(GlobalStyle.COLOR_PRIMARY, 2));

        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        avatarPanel.setOpaque(false);
        avatarPanel.add(avatarLabel);

        // Username
        usernameLabel = new JLabel();
        usernameLabel.setFont(GlobalStyle.FONT_BOLD);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Role
        roleLabel = new JLabel();
        roleLabel.setFont(GlobalStyle.FONT_SMALL);
        roleLabel.setForeground(new Color(200, 200, 200));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userInfoPanel.add(avatarPanel);
        userInfoPanel.add(Box.createVerticalStrut(10));
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(Box.createVerticalStrut(5));
        userInfoPanel.add(roleLabel);
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(GlobalStyle.COLOR_SECONDARY);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    public void setUserInfo(String username, String role) {
        this.currentRole = role;
        if (username != null && !username.isEmpty()) {
            avatarLabel.setText(String.valueOf(username.charAt(0)).toUpperCase());
        }
        usernameLabel.setText(username);
        roleLabel.setText(role);
        updateMenuItems();
    }

    private void updateMenuItems() {
        menuPanel.removeAll();
        menuItems.clear();

        // Always show HOME
        addMenuItem("HOME", "HOME");

        // Role-based menu items
        if ("ADMIN".equals(currentRole)) {
            addMenuItem("STUDENTS", "Quản lý Sinh viên");
            addMenuItem("CLASSES", "Quản lý Lớp học");
            addMenuItem("TEACHERS", "Quản lý Giáo viên");
            addMenuItem("USERS", "Quản lý Người dùng");
            addMenuItem("SUBJECTS", "Quản lý Môn học");
            addMenuItem("ENROLLMENTS", "Quản lý Điểm");
        } else if ("TEACHER".equals(currentRole)) {
            addMenuItem("STUDENTS", "Quản lý Sinh viên");
            addMenuItem("CLASSES", "Quản lý Lớp học");
            addMenuItem("SUBJECTS", "Quản lý Môn học");
            addMenuItem("ENROLLMENTS", "Quản lý Điểm");
        } else if ("STUDENT".equals(currentRole)) {
            addMenuItem("ENROLLMENTS", "Quản lý Điểm");
        }

        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void addMenuItem(String id, String label) {
        MenuItem item = new MenuItem(id, label);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(250, 40));
        panel.setMaximumSize(new Dimension(250, 40));

        // Arrow icon (using simple character)
        JLabel iconLabel = new JLabel(">");
        iconLabel.setFont(new Font("Arial", Font.BOLD, 14));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setPreferredSize(new Dimension(15, 20));
        panel.add(iconLabel);

        // Menu label
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(GlobalStyle.FONT_NORMAL);
        labelComp.setForeground(Color.WHITE);
        panel.add(labelComp);

        item.panel = panel;
        item.labelComponent = labelComp;

        // Mouse listener
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(item);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedItem != item) {
                    panel.setBackground(new Color(52, 73, 94));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (selectedItem != item) {
                    panel.setBackground(GlobalStyle.COLOR_SECONDARY);
                }
            }
        });

        menuItems.add(item);
        menuPanel.add(panel);
    }

    private void selectMenuItem(MenuItem item) {
        // Deselect previous
        if (selectedItem != null) {
            selectedItem.panel.setBackground(GlobalStyle.COLOR_SECONDARY);
        }

        // Select new
        selectedItem = item;
        item.panel.setBackground(GlobalStyle.COLOR_PRIMARY);

        // Notify listener
        if (menuItemListener != null) {
            menuItemListener.onMenuItemSelected(item.id);
        }
    }

    public void setMenuItemListener(MenuItemListener listener) {
        this.menuItemListener = listener;
    }

    public void selectMenuItemById(String menuId) {
        for (MenuItem item : menuItems) {
            if (item.id.equals(menuId)) {
                selectMenuItem(item);
                break;
            }
        }
    }
}

