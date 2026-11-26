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
        add(buttonPanel, BorderLayout.SOUTH);
        addButton.addActionListener(this);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        AppTheme.stylePanel(centerWrapper);
        centerWrapper.add(formPanel);
        add(centerWrapper, BorderLayout.CENTER);

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
                if (SharedData.findDepartmentById(ID) != null || SharedData.findDepartmentByName(deptName) != null) {
                    JOptionPane.showMessageDialog(this, "Department ID or Name already exists!", "Addition Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Department newDept = new Department(ID, deptName);
                SharedData.addDepartment(newDept);
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