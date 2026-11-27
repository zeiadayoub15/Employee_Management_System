package GUI;

import Entities.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DepartmentPanel extends JPanel implements ActionListener {

    private final JPanel formPanel ;
    private final JTextField id_Field ;
    private final JTextField nameDep_Field ;
    private final JButton addButton ;
    private final String[] depStringsBasics = {"IT", "Human Resources", "Finance", "Marketing", "Sales"};
    private final ArrayList<String> depStrings = new ArrayList<>(Arrays.asList(depStringsBasics));
    private final Runnable onDepartmentAdded ;
    private final DefaultTableModel tableModel ;

    public ArrayList<String> getDepartments() {
        depStrings.clear();
        for (Department d : SharedData.getDepartments()) {
            depStrings.add(d.getName());
        }
        return depStrings;
    }

    public DepartmentPanel()
    {
        this(null);
    }

    public DepartmentPanel(Runnable onDepartmentAdded){
        this.onDepartmentAdded = onDepartmentAdded ;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setBackground(AppTheme.BG_MAIN);

        JLabel title = new JLabel("Add New Department", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(AppTheme.TEXT_DARK);
        add(title, BorderLayout.NORTH);

        formPanel = new JPanel(new GridLayout(2, 2, 10, 12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        AppTheme.stylePanel(formPanel);

        Font fieldFont = new Font("Arial", Font.BOLD, 15);

        JLabel idLabel = new JLabel("ID");
        AppTheme.styleLabel(idLabel);
        formPanel.add(idLabel);
        id_Field = new JTextField(18) ;
        AppTheme.styleTextField(id_Field);
        id_Field.setFont(fieldFont);
        formPanel.add(id_Field);

        JLabel nameDepLabel = new JLabel("Department name");
        AppTheme.styleLabel(nameDepLabel);
        formPanel.add(nameDepLabel);
        nameDep_Field = new JTextField(18) ;
        AppTheme.styleTextField(nameDep_Field);
        nameDep_Field.setFont(fieldFont);
        formPanel.add(nameDep_Field);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        AppTheme.stylePanel(buttonPanel);
        addButton = new JButton("Add");
        AppTheme.styleButton(addButton, new Color(40, 167, 69), AppTheme.TEXT_LIGHT);
        buttonPanel.add(addButton);
        addButton.addActionListener(this);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        AppTheme.stylePanel(centerWrapper);
        centerWrapper.add(formPanel);

        // الفورم + زرار Add فوق بعض
        JPanel topPanel = new JPanel(new BorderLayout(0, 5));
        AppTheme.stylePanel(topPanel);
        topPanel.add(centerWrapper, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // جدول الأقسام
        tableModel = new DefaultTableModel(new String[]{"ID", "Department name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable departmentsTable = new JTable(tableModel);
        departmentsTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(departmentsTable);
        AppTheme.styleTable(departmentsTable, scrollPane);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        refreshTable();

    }

    public void refreshTable() {
    tableModel.setRowCount(0);
    for (Department d : SharedData.getDepartments()) {
        tableModel.addRow(new Object[]{d.getId(), d.getName()});
    }
}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==addButton){
            if (nameDep_Field.getText().trim().isEmpty() || id_Field.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "missed information", "Addition Failed", JOptionPane.ERROR_MESSAGE);
                return ;
            }
            if (!nameDep_Field.getText().trim().matches("[a-zA-Z\\s]+")) {
                JOptionPane.showMessageDialog(this, "Name must contain letters only!", "Addition Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int ID = Integer.parseInt(id_Field.getText().trim());

                String deptName = nameDep_Field.getText().trim();

                if (!Helpers.isPositiveInt(ID)) {
                    JOptionPane.showMessageDialog(this, "ID must be positive!", "Addition Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    SharedData.departments().addDepartment(new Department(ID, deptName));
                } catch (IllegalArgumentException | java.io.IOException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Addition Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                refreshTable();

                if (onDepartmentAdded != null) {
                    onDepartmentAdded.run();
                }

                JOptionPane.showMessageDialog(this, "Department added successfully!");
                id_Field.setText("");
                nameDep_Field.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID field must be numbers!", "Addition Failed", JOptionPane.ERROR_MESSAGE);
            }


        }
    }
}