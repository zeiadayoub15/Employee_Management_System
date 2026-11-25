package GUI;

import javax.swing.*;

public class TestViewApp
{
    public static void main(String[] args) { // نقطة بداية تشغيل البرنامج
        SwingUtilities.invokeLater(() -> { // تشغيل الواجهة على Event Dispatch Thread الخاص بـ Swing
            JFrame frame = new JFrame("Test - View Employees"); // إنشاء نافذة رئيسية بعنوان تجريبي
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // إغلاق البرنامج بالكامل عند إغلاق النافذة
            frame.setSize(850, 450); // تحديد أبعاد النافذة (العرض 850، الارتفاع 450)
            frame.setLocationRelativeTo(null); // توسيط النافذة في منتصف الشاشة
            frame.add(new ViewEmployeesPanel()); // إضافة ViewEmployeesPanel داخل النافذة
            frame.setVisible(true); // إظهار النافذة للمستخدم
        }); // نهاية بلوك SwingUtilities.invokeLater
    }
}

