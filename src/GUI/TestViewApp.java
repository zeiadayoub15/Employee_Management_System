package GUI;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestViewApp
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            JFrame frame = new JFrame("Employee Management System - Update & View Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(980, 540);
            frame.setLocationRelativeTo(null);

            ViewEmployeesPanel viewPanel = new ViewEmployeesPanel();
            frame.add(viewPanel);

            frame.setVisible(true);
        });
    }
}

