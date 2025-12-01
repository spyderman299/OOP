package main.java.component;

import main.java.view.GlobalStyle;

import javax.swing.*;
import java.awt.*;

public class FooterPanel extends JPanel {
    public FooterPanel() {
        setLayout(new BorderLayout());
        setBackground(GlobalStyle.COLOR_FOOTER);
        setPreferredSize(new Dimension(0, 40));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel copyrightLabel = new JLabel("© 2025 Hệ thống Quản lý Sinh viên. Tất cả quyền được bảo lưu.");
        copyrightLabel.setFont(GlobalStyle.FONT_SMALL);
        copyrightLabel.setForeground(Color.WHITE);
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(copyrightLabel, BorderLayout.CENTER);
    }
}

