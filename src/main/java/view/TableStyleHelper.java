package main.java.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TableStyleHelper {
    /**
     * Configure table to have black text on white background for all cells
     * AND white text on purple background for headers
     */
    public static void configureTableStyle(JTable table) {
        // ========== 1. HEADER RENDERER - FIX WHITE TEXT ON PURPLE BACKGROUND ==========
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // FORCE white text on purple background for header
                label.setBackground(GlobalStyle.COLOR_PRIMARY);
                label.setForeground(Color.WHITE);
                label.setFont(GlobalStyle.FONT_BOLD);
                label.setOpaque(true);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(90, 80, 200)));
                
                return label;
            }
        };
        
        // Apply header renderer to ALL columns
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(headerRenderer);
        header.setBackground(GlobalStyle.COLOR_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(GlobalStyle.FONT_BOLD);
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        
        // ========== 2. CELL RENDERER - FIX BLACK TEXT ON WHITE BACKGROUND ==========
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // FORCE black text
                label.setForeground(Color.BLACK);
                label.setOpaque(true);
                
                // Set background based on selection
                if (isSelected) {
                    label.setBackground(new Color(200, 220, 255));
                } else {
                    label.setBackground(Color.WHITE);
                }
                
                return label;
            }
        };
        
        // Set as default renderer for the entire table
        table.setDefaultRenderer(Object.class, cellRenderer);
        table.setDefaultRenderer(String.class, cellRenderer);
        table.setDefaultRenderer(Integer.class, cellRenderer);
        table.setDefaultRenderer(Double.class, cellRenderer);
        table.setDefaultRenderer(Float.class, cellRenderer);
        table.setDefaultRenderer(Long.class, cellRenderer);
        table.setDefaultRenderer(Number.class, cellRenderer);
        
        // Apply to all existing columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        // ========== 3. TABLE DEFAULTS ==========
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setSelectionForeground(Color.BLACK);
        table.setSelectionBackground(new Color(200, 220, 255));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Disable rollover effects
        table.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
        
        // Force repaint
        table.repaint();
        header.repaint();
    }
}

