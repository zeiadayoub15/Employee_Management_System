package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AppTheme
{
    public static final Color BG_MAIN = new Color(245, 247, 250);
    public static final Color BG_PANEL = new Color(255, 255, 255);
    public static final Color BG_HEADER = new Color(255, 138, 101);
    public static final Color TEXT_DARK = new Color(33, 37, 41);
    public static final Color TEXT_LIGHT = Color.WHITE;
    public static final Color BORDER_COLOR = new Color(206, 212, 218);

    public static final Color BTN_SEARCH = new Color(255, 112, 67);
    public static final Color BTN_UPDATE = new Color(66, 133, 244);
    public static final Color BTN_DELETE = new Color(220, 53, 69);
    public static final Color BTN_RESET = new Color(255, 193, 7);

    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);

    public static void stylePanel(JPanel panel)
    {
        panel.setBackground(BG_PANEL);
    }

    public static void styleLabel(JLabel label)
    {
        label.setFont(FONT_LABEL);
        label.setForeground(TEXT_DARK);
    }

    public static void styleTextField(JTextField field)
    {
        field.setFont(FONT_INPUT);
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_DARK);
        Border line = BorderFactory.createLineBorder(BORDER_COLOR, 1);
        Border pad = BorderFactory.createEmptyBorder(4, 6, 4, 6);
        field.setBorder(BorderFactory.createCompoundBorder(line, pad));
    }

    public static void styleComboBox(JComboBox<?> comboBox)
    {
        comboBox.setFont(FONT_INPUT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_DARK);
    }

    public static void styleButton(JButton button, Color bgColor, Color fgColor)
    {
        button.setFont(FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleTable(JTable table, JScrollPane scrollPane)
    {
        table.setFont(FONT_TABLE);
        table.setForeground(TEXT_DARK);
        table.setBackground(Color.WHITE);
        table.setRowHeight(26);
        table.setGridColor(new Color(230, 232, 235));
        table.setSelectionBackground(new Color(210, 227, 252));
        table.setSelectionForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_HEADER);
        header.setBackground(BG_HEADER);
        header.setForeground(TEXT_LIGHT);
        header.setOpaque(true);

        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
    }
}
