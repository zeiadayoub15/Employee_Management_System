package GUI;

import Entities.*;
import java.awt.BorderLayout;
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

    private JPanel formPanel ;
    private JTextField id_Field ;
    private JTextField nameDep_Field ;
    private JButton addButton ;
    private final String[] depStringsBasics = {"IT", "Human Resources", "Finance", "Marketing", "Sales"};
    private ArrayList<String> depStrings = new ArrayList<>(Arrays.asList(depStringsBasics));
    private Runnable onDepartmentAdded ;

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

        JLabel title = new JLabel("Add New Department", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        formPanel = new JPanel(new GridLayout(2, 2, 10, 12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        Font fieldFont = new Font("Arial", Font.BOLD, 15);

        formPanel.add(new JLabel("ID"));
        id_Field = new JTextField(18) ;
        id_Field.setFont(fieldFont);
        formPanel.add(id_Field);

        formPanel.add(new JLabel("Department name"));
        nameDep_Field = new JTextField(18) ;
        nameDep_Field.setFont(fieldFont);
        formPanel.add(nameDep_Field);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add");
        buttonPanel.add(addButton);
        add(buttonPanel, BorderLayout.SOUTH);
        addButton.addActionListener(this);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
