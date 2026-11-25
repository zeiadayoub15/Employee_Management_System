package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewEmployeesPanel extends JPanel
{
    private JTextField searchField;
    private JComboBox<String> departmentFilterCombo;
    private JButton searchButton;
    private JButton resetButton;

    private JTable employeesTable;
    private DefaultTableModel tableModel;

    private JButton updateButton;
    private JButton deleteButton;

    public ViewEmployeesPanel()
    {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        initTopPanel();
        initCenterTable();
        initBottomPanel();
        loadTestData();
    }

    private void initTopPanel()
    {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchField = new JTextField(18);
        departmentFilterCombo = new JComboBox<>(new String[]{"All Departments"});
        searchButton = new JButton("Search");
        resetButton = new JButton("Reset");

        topPanel.add(new JLabel("Search (ID):"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        topPanel.add(new JLabel("Department:"));
        topPanel.add(departmentFilterCombo);
        topPanel.add(resetButton);
        add(topPanel, BorderLayout.NORTH);
    }

    private void initCenterTable()
    {
        String[] columns = {
                "ID",
                "Full Name",
                "Age",
                "Department",
                "Position",
                "Type",
                "Calculated Salary"
        };
        tableModel = new DefaultTableModel(columns, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        employeesTable = new JTable(tableModel);
        employeesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeesTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(employeesTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // testing data
    public void loadTestData() { // دالة مؤقتة لإضافة صفوف تجريبية للجدول قبل ربط الـ Backend
        tableModel.setRowCount(0); // تفريغ أي صفوف قديمة في الجدول قبل إضافة البيانات الجديدة

        double fullTimeSalary1 = 15000 + 2000; // حساب مرتب Full-Time: الأساسي (15000) + البونص (2000)[cite: 3]
        tableModel.addRow(new Object[]{ // إضافة صف الموظف الأول إلى الجدول
                101, // ID
                "Ahmed Hassan", // Full Name
                28, // Age
                "IT", // Department
                "Software Engineer", // Position
                "Full-Time", // Type
                fullTimeSalary1 // Calculated Salary
        });

        double partTimeSalary1 = 100 * 80; // حساب مرتب Part-Time: سعر الساعة (100) * عدد الساعات (80)[cite: 4]
        tableModel.addRow(new Object[]{ // إضافة صف الموظف الثاني إلى الجدول
                102, // ID
                "Mona Adel", // Full Name
                24, // Age
                "Marketing", // Department
                "Content Specialist", // Position
                "Part-Time", // Type
                partTimeSalary1 // Calculated Salary
        });

        double fullTimeSalary2 = 18000 + 1500; // حساب مرتب Full-Time آخر: (18000 + 1500)[cite: 3]
        tableModel.addRow(new Object[]{ // إضافة صف الموظف الثالث إلى الجدول
                103, // ID
                "Karim Samir", // Full Name
                32, // Age
                "Finance", // Department
                "Accountant", // Position
                "Full-Time", // Type
                fullTimeSalary2 // Calculated Salary
        });

        double partTimeSalary2 = 120 * 60; // حساب مرتب Part-Time آخر: (120 * 60)[cite: 4]
        tableModel.addRow(new Object[]{ // إضافة صف الموظف الرابع إلى الجدول
                104, // ID
                "Nourhan Tarek", // Full Name
                26, // Age
                "HR", // Department
                "Recruiter", // Position
                "Part-Time", // Type
                partTimeSalary2 // Calculated Salary
        });
    }
}

