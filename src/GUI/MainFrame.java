package GUI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final String HOME = "HOME";
    public static final String ADD = "ADD";
    public static final String VIEW = "VIEW";
    public static final String DEPT = "DEPT";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private final AddPannel addPanel;
    private final ViewEmployeesPanel viewEmployeesPanel;
    private final DepartmentPanel departmentPanel;

    public MainFrame() {
        setTitle("Employee Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(AppTheme.BG_MAIN);

        add(buildMenu(), BorderLayout.WEST);

        viewEmployeesPanel = new ViewEmployeesPanel();
        addPanel = new AddPannel(() -> viewEmployeesPanel.refreshData());
        departmentPanel = new DepartmentPanel(() -> {
            addPanel.refreshDepartments();
            viewEmployeesPanel.refreshData();
        });

        contentPanel.setBackground(AppTheme.BG_MAIN);
        contentPanel.add(buildHomePanel(), HOME);
        contentPanel.add(addPanel, ADD);
        contentPanel.add(viewEmployeesPanel, VIEW);
        contentPanel.add(departmentPanel, DEPT);
        add(contentPanel, BorderLayout.CENTER);

        showCard(HOME);
        this.setVisible(true);
    }

    private JPanel buildMenu() {
        JPanel menu = new JPanel(new GridLayout(0, 1, 8, 8));
        menu.setBackground(AppTheme.BG_PANEL);
        menu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        menu.setPreferredSize(new Dimension(195, 0));

        JButton homeBtn = new JButton("Dashboard");
        JButton addBtn = new JButton("Add Employee");
        JButton viewBtn = new JButton("View Employees");
        JButton deptBtn = new JButton("Departments");
        JButton logoutBtn = new JButton("Log out");

        AppTheme.styleButton(homeBtn, AppTheme.BTN_UPDATE, AppTheme.TEXT_LIGHT);
        AppTheme.styleButton(addBtn, new Color(40, 167, 69), AppTheme.TEXT_LIGHT);
        AppTheme.styleButton(viewBtn, AppTheme.BTN_SEARCH, AppTheme.TEXT_LIGHT);
        AppTheme.styleButton(deptBtn, AppTheme.BTN_RESET, AppTheme.TEXT_DARK);
        AppTheme.styleButton(logoutBtn, AppTheme.BTN_DELETE, AppTheme.TEXT_LIGHT);

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
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(AppTheme.BG_MAIN);
        JLabel welcome = new JLabel("Welcome, Admin!");
        welcome.setFont(new Font("Arial", Font.BOLD, 28));
        welcome.setForeground(AppTheme.TEXT_DARK);
        p.add(welcome);
        return p;
    }

    private JPanel placeholder(String text) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(AppTheme.BG_MAIN);
        JLabel label = new JLabel(text);
        AppTheme.styleLabel(label);
        p.add(label);
        return p;
    }

    public void showCard(String name) {
        if (VIEW.equals(name)) {
            viewEmployeesPanel.refreshData();
        } else if (ADD.equals(name)) {
            addPanel.refreshDepartments();
        }
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