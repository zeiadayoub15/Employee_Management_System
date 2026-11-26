package GUI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // Names of the "cards" (screens)
    public static final String HOME = "HOME";
    public static final String ADD = "ADD";
    public static final String VIEW = "VIEW";
    public static final String DEPT = "DEPT";

    private CardLayout cardLayout = new CardLayout();
    private JPanel contentPanel = new JPanel(cardLayout);

    public MainFrame() {
        setTitle("Employee Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ---- WEST: navigation menu ----
        add(buildMenu(), BorderLayout.WEST);

        // ---- CENTER: all screens stacked, one visible at a time ----
        contentPanel.add(buildHomePanel(), HOME);
        contentPanel.add(new AddPannel(), ADD);
        contentPanel.add(new ViewEmployeesPanel(), VIEW);
        contentPanel.add(new DepartmentPanel(), DEPT);
        add(contentPanel, BorderLayout.CENTER);

        showCard(HOME);
        this.setVisible(true);
    }

    private JPanel buildMenu() {
        JPanel menu = new JPanel(new GridLayout(0, 1, 5, 5)); // any rows, 1 column
        menu.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        menu.setPreferredSize(new Dimension(190, 0));

        JButton homeBtn = new JButton("Dashboard");
        JButton addBtn = new JButton("Add Employee");
        JButton viewBtn = new JButton("View Employees");
        JButton deptBtn = new JButton("Departments");
        JButton logoutBtn = new JButton("Log out");

        homeBtn.addActionListener(e -> showCard(HOME));
        addBtn.addActionListener(e -> showCard(ADD));
        viewBtn.addActionListener(e -> showCard(VIEW));
        deptBtn.addActionListener(e -> showCard(DEPT));
        logoutBtn.addActionListener(e -> logout());

        menu.add(homeBtn);
        menu.add(addBtn);
        menu.add(viewBtn);
        menu.add(deptBtn);
        menu.add(logoutBtn);
        return menu;
    }

    private JPanel buildHomePanel() {
        JPanel p = new JPanel(new GridBagLayout()); // centers its content
        JLabel welcome = new JLabel("Welcome, Admin!");
        welcome.setFont(new Font("Arial", Font.BOLD, 28));
        p.add(welcome);
        return p;
    }

    // Temporary screen until the real panel is ready
    private JPanel placeholder(String text) {
        JPanel p = new JPanel(new GridBagLayout());
        p.add(new JLabel(text));
        return p;
    }

    public void showCard(String name) {
        cardLayout.show(contentPanel, name);
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out?",
                "Log out",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}

