package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ViewEmployeesPanel extends JPanel
{
    // temp until linking with backend
    private static class TestEmployeeRecord
    {
        int id;
        String fullName;
        int age;
        String department;
        String position;
        String type;
        double calculatedSalary;

        TestEmployeeRecord(int id, String fullName, int age, String department,
                           String position, String type, double calculatedSalary) {
            this.id = id;
            this.fullName = fullName;
            this.age = age;
            this.department = department;
            this.position = position;
            this.type = type;
            this.calculatedSalary = calculatedSalary;
        }
    }
    private final List<TestEmployeeRecord> testEmployees = new ArrayList<>();
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
        initListeners();
        loadTestData();
    }

    private void initTopPanel()
    {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchField = new JTextField(18);
        departmentFilterCombo = new JComboBox<>(new String[]{
                "All Departments", "IT", "Human Resources", "Finance", "Marketing", "Sales"
        });
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

    private void initBottomPanel()
    {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // testing data
   /* public void loadTestData() { // دالة مؤقتة لإضافة صفوف تجريبية للجدول قبل ربط الـ Backend
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
*/
    private void initListeners()
    {
        updateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                handleUpdateEmployee();
            }
        });

        deleteButton.addActionListener(new ActionListener()
        {
         @Override
         public void actionPerformed(ActionEvent e)
         {
             handleDeleteEmployee();
         }
        });

        searchButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                handleSearchById();
            }
        });

        departmentFilterCombo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                handleFilterByDepartment();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                departmentFilterCombo.setSelectedIndex(0);
                renderEmployeesTable(testEmployees);
            }
        });
    }

    private void handleUpdateEmployee() {
        int selectedRow = employeesTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an employee from the table first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int empId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        TestEmployeeRecord target = findEmployeeById(empId);

        if (target == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selected employee record was not found.",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Update Employee (ID: " + target.id + ")", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 270);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));

        JTextField nameField = new JTextField(target.fullName);
        JTextField ageField = new JTextField(String.valueOf(target.age));
        JComboBox<String> deptCombo = new JComboBox<>(new String[]{
                "IT", "Human Resources", "Finance", "Marketing", "Sales"
        });
        deptCombo.setSelectedItem(target.department);

        JTextField positionField = new JTextField(target.position);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Full-Time", "Part-Time"});
        typeCombo.setSelectedItem(target.type);

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(deptCombo);
        formPanel.add(new JLabel("Position:"));
        formPanel.add(positionField);
        formPanel.add(new JLabel("Type:"));
        formPanel.add(typeCombo);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        buttonsPanel.add(saveBtn);
        buttonsPanel.add(cancelBtn);

        cancelBtn.addActionListener(e -> dialog.dispose());

        saveBtn.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String rawAge = ageField.getText().trim();
            String newDept = (String) deptCombo.getSelectedItem();
            String newPosition = positionField.getText().trim();
            String newType = (String) typeCombo.getSelectedItem();

            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Full Name is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int newAge;
            try {
                newAge = Integer.parseInt(rawAge);
                if (newAge < 18 || newAge > 70) {
                    JOptionPane.showMessageDialog(dialog, "Age must be between 18 and 70.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Age must be a valid number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (newPosition.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Position is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            target.fullName = newName;
            target.age = newAge;
            target.department = newDept;
            target.position = newPosition;
            target.type = newType;
            target.calculatedSalary = "Full-Time".equalsIgnoreCase(newType) ? (15000 + 2000) : (100 * 80);

            handleFilterByDepartment();
            dialog.dispose();

            JOptionPane.showMessageDialog(
                    this,
                    "Employee updated successfully!",
                    "Updated",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private TestEmployeeRecord findEmployeeById(int id)
    {
        for (TestEmployeeRecord emp : testEmployees) {
            if (emp.id == id) {
                return emp;
            }
        }
        return null;
    }
    // temp until linking with backend
    private void handleDeleteEmployee()
    {
        int selectedRow = employeesTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an employee from the table first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Object empId = tableModel.getValueAt(selectedRow, 0);
        Object empName = tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete employee [" + empId + " - " + empName + "]?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(
                    this,
                    "Employee deleted successfully!",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void handleSearchById()
    {
        String rawId = searchField.getText().trim();

        if (rawId.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter an Employee ID to search.",
                    "Empty Search Field",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int targetId;
        try {
            targetId = Integer.parseInt(rawId);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Employee ID must be a valid integer.",
                    "Invalid ID",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        List<TestEmployeeRecord> matches = new ArrayList<>();
        for (TestEmployeeRecord emp : testEmployees) {
            if (emp.id == targetId) {
                matches.add(emp);
            }
        }

        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No employee found with ID: " + targetId,
                    "No Match",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        renderEmployeesTable(matches);
    }

    private void renderEmployeesTable(List<TestEmployeeRecord> employeesToShow)
    {
        tableModel.setRowCount(0);
        for (TestEmployeeRecord emp : employeesToShow) {
            tableModel.addRow(new Object[]{
                    emp.id,
                    emp.fullName,
                    emp.age,
                    emp.department,
                    emp.position,
                    emp.type,
                    emp.calculatedSalary
            });
        }
    }

    private void handleFilterByDepartment()
    {
        String selectedDept = (String) departmentFilterCombo.getSelectedItem();
        if (selectedDept == null || selectedDept.equalsIgnoreCase("All Departments")) {
            renderEmployeesTable(testEmployees);
            return;
        }

        List<TestEmployeeRecord> matches = new ArrayList<>();
        for (TestEmployeeRecord emp : testEmployees) {
            if (emp.department != null && emp.department.equalsIgnoreCase(selectedDept)) {
                matches.add(emp);
            }
        }

        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No employees found in department: " + selectedDept,
                    "No Match",
                    JOptionPane.INFORMATION_MESSAGE
            );
            renderEmployeesTable(matches);
            return;
        }
        renderEmployeesTable(matches);
    }

    public void loadTestData()
    {
        testEmployees.clear();

        testEmployees.add(new TestEmployeeRecord(
                101, "Ahmed Hassan", 28, "IT", "Software Engineer", "Full-Time", 15000 + 2000
        ));
        testEmployees.add(new TestEmployeeRecord(
                102, "Mona Adel", 24, "Marketing", "Content Specialist", "Part-Time", 100 * 80
        ));
        testEmployees.add(new TestEmployeeRecord(
                103, "Karim Samir", 32, "Finance", "Accountant", "Full-Time", 18000 + 1500
        ));
        testEmployees.add(new TestEmployeeRecord(
                104, "Nourhan Tarek", 26, "Human Resources", "Recruiter", "Part-Time", 120 * 60
        ));

        renderEmployeesTable(testEmployees);
    }
}

