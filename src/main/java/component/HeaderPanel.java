package main.java.component;

import main.java.view.GlobalStyle;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private JLabel titleLabel;
    private JLabel userInfoLabel;
    private JButton logoutButton;
    private Runnable onLogout;

    public HeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(GlobalStyle.COLOR_PRIMARY);
        setPreferredSize(new Dimension(0, 60));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left: Title
        titleLabel = new JLabel("Hệ thống Quản lý Sinh viên");
        titleLabel.setFont(GlobalStyle.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.WEST);

        // Right: User info and logout button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        userInfoLabel = new JLabel();
        userInfoLabel.setFont(GlobalStyle.FONT_BOLD);
        userInfoLabel.setForeground(Color.WHITE);
        rightPanel.add(userInfoLabel);

        logoutButton = GlobalStyle.createPrimaryButton("Đăng xuất");
        logoutButton.setBackground(new Color(231, 76, 60)); // Red color
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });
        rightPanel.add(logoutButton);

        add(rightPanel, BorderLayout.EAST);
    }

    public void setUserInfo(String username, String role) {
        userInfoLabel.setText("Xin chào, " + username + " (" + role + ")");
    }

    public void setOnLogout(Runnable onLogout) {
        this.onLogout = onLogout;
    }
}

