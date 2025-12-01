package main.java.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GlobalStyle {
    // Colors
    public static final Color COLOR_PRIMARY = new Color(108, 92, 231); // Purple #6C5CE7
    public static final Color COLOR_SECONDARY = new Color(44, 62, 80); // Dark blue #2C3E50
    public static final Color COLOR_BACKGROUND = new Color(245, 246, 250); // Light gray
    public static final Color COLOR_CARD = Color.WHITE;
    public static final Color COLOR_TEXT_PRIMARY = new Color(44, 62, 80);
    public static final Color COLOR_TEXT_SECONDARY = new Color(127, 140, 141);
    public static final Color COLOR_SUCCESS = new Color(46, 213, 115);
    public static final Color COLOR_WARNING = new Color(255, 159, 67);
    public static final Color COLOR_DANGER = new Color(231, 76, 60);
    public static final Color COLOR_INFO = new Color(52, 152, 219);
    public static final Color COLOR_FOOTER = new Color(52, 73, 94);

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    // Scale factor for responsive design
    private static final double SCALE_FACTOR = 1.0;

    /**
     * Scale a dimension value
     */
    public static int scale(int value) {
        return (int) (value * SCALE_FACTOR);
    }

    /**
     * Scale a font
     */
    public static Font scaleFont(Font font) {
        return font.deriveFont((float) (font.getSize() * SCALE_FACTOR));
    }

    /**
     * Configure UIManager for consistent look and feel
     */
    public static void configureUIManager() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel: " + e.getMessage());
        }

        // Configure common UI components
        UIManager.put("Button.font", FONT_NORMAL);
        UIManager.put("Label.font", FONT_NORMAL);
        UIManager.put("TextField.font", FONT_NORMAL);
        UIManager.put("TextArea.font", FONT_NORMAL);
        UIManager.put("ComboBox.font", FONT_NORMAL);
        UIManager.put("Table.font", FONT_NORMAL);
        UIManager.put("TableHeader.font", FONT_BOLD);
        
        // Configure table colors - always black text on white background
        UIManager.put("Table.foreground", Color.BLACK);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.selectionForeground", Color.BLACK);
        UIManager.put("Table.selectionBackground", new Color(200, 220, 255));
        UIManager.put("Table.focusCellBackground", new Color(200, 220, 255));
        UIManager.put("Table.focusCellForeground", Color.BLACK);
        
        // Disable rollover effects
        UIManager.put("Table.rolloverEnabled", false);
    }

    /**
     * Create a styled button
     */
    public static JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFont(FONT_BOLD);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Create a primary button
     */
    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, COLOR_PRIMARY, Color.WHITE);
    }

    /**
     * Create a danger button
     */
    public static JButton createDangerButton(String text) {
        return createStyledButton(text, COLOR_DANGER, Color.WHITE);
    }

    /**
     * Create a warning button
     */
    public static JButton createWarningButton(String text) {
        return createStyledButton(text, COLOR_WARNING, Color.WHITE);
    }

    /**
     * Create an info button
     */
    public static JButton createInfoButton(String text) {
        return createStyledButton(text, COLOR_INFO, Color.WHITE);
    }

    /**
     * Create a card panel with shadow effect
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }
}

