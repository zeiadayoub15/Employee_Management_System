package GUI;

import Entities.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
        initListeners();
        refreshData(); // [MODIFIED] تحميل البيانات والاقسام من SharedData
    }

    private String[] getDepartmentNames(boolean includeAllOption)
    {
        List<String> names = new ArrayList<>();
        if (includeAllOption) {
            names.add("All Departments");
        }
        for (Department dept : SharedData.getDepartments()) {
            if (dept != null && Helpers.isValidString(dept.getName())) {
                names.add(dept.getName());
            }
        }
        return names.toArray(new String[0]);
    }

    private void reloadDepartmentsCombo()
    {
        String previousSelection = (String) departmentFilterCombo.getSelectedItem();
        departmentFilterCombo.removeAllItems();
        for (String name : getDepartmentNames(true)) {
            departmentFilterCombo.addItem(name);
        }
        if (previousSelection != null) {
            departmentFilterCombo.setSelectedItem(previousSelection);
        }
        if (departmentFilterCombo.getSelectedItem() == null && departmentFilterCombo.getItemCount() > 0) {
            departmentFilterCombo.setSelectedIndex(0);
        }
    }

    public void refreshData() {
        reloadDepartmentsCombo();
        renderEmployeesTable(SharedData.getEmployees());
    }

    private void initTopPanel()
    {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchField = new JTextField(18);
        departmentFilterCombo = new JComboBox<>(getDepartmentNames(true));
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
                if (departmentFilterCombo.getSelectedItem() != null) {
                    handleFilterByDepartment();
                } // [ADDED]
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                if (departmentFilterCombo.getItemCount() > 0) {
                    departmentFilterCombo.setSelectedIndex(0);
                } // [ADDED]
                renderEmployeesTable(SharedData.getEmployees());
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
        int targetIndex = findEmployeeIndexById(empId);

        if (targetIndex == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selected employee record was not found.",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Employee currentEmp = SharedData.getEmployees().get(targetIndex);

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Update Employee (ID: " + currentEmp.getId() + ")", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));

        JTextField nameField = new JTextField(currentEmp.getName());
        JTextField ageField = new JTextField(String.valueOf(currentEmp.getAge()));
        JComboBox<String> deptCombo = new JComboBox<>(getDepartmentNames(false));
        deptCombo.setSelectedItem(currentEmp.getDepartment());

        JTextField positionField = new JTextField(currentEmp.getPosition());
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Full-Time", "Part-Time"});
        typeCombo.setSelectedItem(currentEmp.getType());

        JLabel payField1Label = new JLabel();
        JTextField payField1 = new JTextField();
        JLabel payField2Label = new JLabel();
        JTextField payField2 = new JTextField();

        Runnable syncPayFields = () -> {
            String selectedType = (String) typeCombo.getSelectedItem();
            if ("Full-Time".equals(selectedType)) {
                payField1Label.setText("Basic Salary:");
                payField2Label.setText("Bonus:");
                if (currentEmp instanceof FullTimeEmployee ft) {
                    payField1.setText(String.valueOf(ft.getBasicSalary()));
                    payField2.setText(String.valueOf(ft.getBonus()));
                } else {
                    payField1.setText("15000.0");
                    payField2.setText("2000.0");
                }
            } else {
                payField1Label.setText("Hourly Rate:");
                payField2Label.setText("Hours Worked:");
                if (currentEmp instanceof PartTimeEmployee pt) {
                    payField1.setText(String.valueOf(pt.getHourlyRate()));
                    payField2.setText(String.valueOf(pt.getHoursWorked()));
                } else {
                    payField1.setText("100.0");
                    payField2.setText("80.0");
                }
            }
        };

        syncPayFields.run();
        typeCombo.addActionListener(e -> syncPayFields.run());

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
        formPanel.add(payField1Label);
        formPanel.add(payField1);
        formPanel.add(payField2Label);
        formPanel.add(payField2);

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

            if (!Helpers.isValidString(newName) || !newName.matches("[a-zA-Z\\s]+")) {
                JOptionPane.showMessageDialog(dialog, "Full Name must contain letters only.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int newAge;
            try {
                newAge = Integer.parseInt(rawAge);
                if (!Helpers.isPositiveInt(newAge) || newAge < 18 || newAge > 70) {
                    JOptionPane.showMessageDialog(dialog, "Age must be between 18 and 70.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Age must be a valid number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!Helpers.isValidString(newPosition) || !newPosition.matches("[a-zA-Z\\s-]+")) {
                JOptionPane.showMessageDialog(dialog, "Position must contain letters only.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double val1;
            double val2;
            try {
                val1 = Double.parseDouble(payField1.getText().trim());
                val2 = Double.parseDouble(payField2.getText().trim());
                if (!Helpers.isNonNegativeDouble(val1) || !Helpers.isNonNegativeDouble(val2)) {
                    JOptionPane.showMessageDialog(dialog, "Salary values must be non-negative numbers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numeric salary values.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Employee updatedEmployee;
            if ("Full-Time".equals(newType)) {
                updatedEmployee = new FullTimeEmployee(currentEmp.getId(), newName, newAge, newDept, newPosition, val1, val2);
            } else {
                updatedEmployee = new PartTimeEmployee(currentEmp.getId(), newName, newAge, newDept, newPosition, val1, val2);
            }

            SharedData.getEmployees().set(targetIndex, updatedEmployee);
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

    private int findEmployeeIndexById(int id)
    {
        List<Employee> list = SharedData.getEmployees();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

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

        int empId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        Object empName = tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete employee [" + empId + " - " + empName + "]?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            SharedData.getEmployees().removeIf(emp -> emp.getId() == empId);
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
            if (!Helpers.isPositiveInt(targetId)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Employee ID must be a positive integer.",
                        "Invalid ID",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Employee ID must be a valid integer.",
                    "Invalid ID",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        List<Employee> matches = new ArrayList<>();
        for (Employee emp : SharedData.getEmployees()) {
            if (emp.getId() == targetId) {
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

    private void renderEmployeesTable(List<Employee> employeesToShow)
    {
        tableModel.setRowCount(0);
        for (Employee emp : employeesToShow) {
            tableModel.addRow(new Object[]{
                    emp.getId(),
                    emp.getName(),
                    emp.getAge(),
                    emp.getDepartment(),
                    emp.getPosition(),
                    emp.getType(),
                    emp.calculateSalary()
            });
        }
    }

    private void handleFilterByDepartment()
    {
        String selectedDept = (String) departmentFilterCombo.getSelectedItem();
        if (selectedDept == null || selectedDept.equalsIgnoreCase("All Departments")) {
            renderEmployeesTable(SharedData.getEmployees());
            return;
        }

        List<Employee> matches = new ArrayList<>();
        for (Employee emp : SharedData.getEmployees()) {
            if (emp.getDepartment() != null && emp.getDepartment().equalsIgnoreCase(selectedDept)) {
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
}