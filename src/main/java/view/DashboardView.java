package main.java.view;

import main.java.component.HeaderPanel;
import main.java.component.SidebarPanel;
import main.java.component.FooterPanel;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JFrame {
    private HeaderPanel headerPanel;
    private SidebarPanel sidebarPanel;
    private FooterPanel footerPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel statisticsPanel;

    public DashboardView() {
        setTitle("Hệ thống Quản lý Sinh viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        GlobalStyle.configureUIManager();

        setLayout(new BorderLayout());

        // Header
        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        sidebarPanel = new SidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);

        // Content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(GlobalStyle.COLOR_BACKGROUND);

        // Statistics panel (Dashboard)
        statisticsPanel = createStatisticsPanel();
        contentPanel.add(statisticsPanel, "DASHBOARD");

        add(contentPanel, BorderLayout.CENTER);

        // Footer
        footerPanel = new FooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(GlobalStyle.COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Trang chủ");
        titleLabel.setFont(GlobalStyle.FONT_TITLE);
        titleLabel.setForeground(GlobalStyle.COLOR_TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Statistics cards
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(2, 2, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Card 1: Students
        JPanel studentCard = createStatCard("Số lượng sinh viên", "0", "SV");
        cardsPanel.add(studentCard);

        // Card 2: Teachers
        JPanel teacherCard = createStatCard("Số lượng giáo viên", "0", "GV");
        cardsPanel.add(teacherCard);

        // Card 3: Subjects
        JPanel subjectCard = createStatCard("Số lượng môn học", "0", "MH");
        cardsPanel.add(subjectCard);

        // Card 4: Classes
        JPanel classCard = createStatCard("Số lượng lớp học", "0", "LH");
        cardsPanel.add(classCard);

        panel.add(cardsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatCard(String title, String value, String icon) {
        JPanel card = GlobalStyle.createCardPanel();
        card.setLayout(new BorderLayout());

        // Icon and value
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setOpaque(false);
        
        // Icon as text badge
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 16));
        iconLabel.setForeground(GlobalStyle.COLOR_PRIMARY);
        iconLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GlobalStyle.COLOR_PRIMARY, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        iconLabel.setOpaque(false);
        topPanel.add(iconLabel);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(GlobalStyle.COLOR_PRIMARY);
        topPanel.add(valueLabel);

        card.add(topPanel, BorderLayout.CENTER);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(GlobalStyle.FONT_BOLD);
        titleLabel.setForeground(GlobalStyle.COLOR_TEXT_SECONDARY);
        card.add(titleLabel, BorderLayout.SOUTH);

        return card;
    }

    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }

    public SidebarPanel getSidebarPanel() {
        return sidebarPanel;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getStatisticsPanel() {
        return statisticsPanel;
    }

    public void updateStatistic(String cardTitle, String value) {
        // Find the cards panel
        Component[] components = statisticsPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel cardsPanel = (JPanel) comp;
                if (cardsPanel.getLayout() instanceof GridLayout) {
                    // Iterate through cards
                    for (Component card : cardsPanel.getComponents()) {
                        if (card instanceof JPanel) {
                            JPanel cardPanel = (JPanel) card;
                            // Check if this card matches the title
                            boolean found = false;
                            for (Component cardComp : cardPanel.getComponents()) {
                                if (cardComp instanceof JLabel) {
                                    JLabel label = (JLabel) cardComp;
                                    if (label.getText().equals(cardTitle)) {
                                        found = true;
                                        break;
                                    }
                                }
                            }
                            if (found) {
                                // Update the value label in this card
                                for (Component cardComp : cardPanel.getComponents()) {
                                    if (cardComp instanceof JPanel) {
                                        JPanel topPanel = (JPanel) cardComp;
                                        for (Component topComp : topPanel.getComponents()) {
                                            if (topComp instanceof JLabel) {
                                                JLabel valueLabel = (JLabel) topComp;
                                                if (valueLabel.getFont().getSize() == 36) {
                                                    valueLabel.setText(value);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

