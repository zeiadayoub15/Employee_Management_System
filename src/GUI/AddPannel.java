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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class AddPannel extends JPanel implements ActionListener {
    private JTextField name_field ;
    private JTextField age_field ;
    private JComboBox departmentComboBox ;
    private final String depStringsBasics[] = {"IT" , "Human Resources" , "Finance" , "Marketing" , "Sales"} ;
    public ArrayList<String> depStrings = new ArrayList<>(Arrays.asList(depStringsBasics));
    
    private JTextField position_Field ;
    private JComboBox TypeComboBox ;
    private final String typeStrings[] = {"Full time","Part time"} ; 
    private JButton addButton ;
    private JLabel l1 ;
    private JLabel l2 ;
    private JTextField field1 ;
    private JTextField field2 ;
    private Runnable onEmployeeAdded ;
    
    JPanel formPanel ;
    public AddPannel(){
        this(null);
    }

    public AddPannel(Runnable onEmployeeAdded){
        this.onEmployeeAdded = onEmployeeAdded;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Add New Employee", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        formPanel = new JPanel(new GridLayout(8, 2, 10, 12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        formPanel.add(new JLabel("Full name"));
        name_field = new JTextField() ;
        name_field.setFont(new Font("Arial" , Font.BOLD , 15));
        formPanel.add(name_field);

        formPanel.add(new JLabel("Age"));
        age_field  = new JTextField() ;
        age_field.setFont(new Font("Arial" , Font.BOLD , 15));
        formPanel.add(age_field);

        formPanel.add(new JLabel("Department"));
        departmentComboBox = new JComboBox<>(depStrings.toArray());
        refreshDepartments();
        formPanel.add(departmentComboBox);

        formPanel.add(new JLabel("Position"));
        position_Field = new JTextField() ;
        position_Field.setFont(new Font("Arial" , Font.BOLD , 15));
        formPanel.add(position_Field);

        formPanel.add(new JLabel("Type"));
        TypeComboBox = new JComboBox<>(typeStrings) ;
        formPanel.add(TypeComboBox);
        TypeComboBox.addActionListener(this) ;
                l1 = new JLabel("Basic Salary") ;
                formPanel.add(l1);
                field1 = new JTextField() ;
                field1.setFont(new Font("Arial" , Font.BOLD , 15));
                formPanel.add(field1);
                l2 = new JLabel("Bonus") ;
                formPanel.add(l2);
                field2 = new JTextField() ;
                field2.setFont(new Font("Arial" , Font.BOLD , 15));
                formPanel.add(field2);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add");
        buttonPanel.add(addButton);
        add(buttonPanel, BorderLayout.SOUTH);
        addButton.addActionListener(this);

        add(formPanel, BorderLayout.CENTER);


    }

    public void refreshDepartments()
    {
        Object selected = departmentComboBox.getSelectedItem();
        depStrings.clear();
        departmentComboBox.removeAllItems();
        for (Department d : SharedData.getDepartments()) {
            depStrings.add(d.getName());
            departmentComboBox.addItem(d.getName());
        }
        if (selected != null) {
            departmentComboBox.setSelectedItem(selected);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==TypeComboBox){
            if (TypeComboBox.getSelectedItem().equals("Full time")){
                l1.setText("Basic Salary");
                l2.setText("Bonus");
            }
            else {
                l1.setText("hourly rate");
                l2.setText("working hours");
            }
        }
        else if (e.getSource()==addButton){
            String name = name_field.getText().trim() ;
            String department = (String) departmentComboBox.getSelectedItem();
            String position = position_Field.getText().trim() ;
            String Type = (String) TypeComboBox.getSelectedItem() ;
            
            if (name.isEmpty() || age_field.getText().trim().isEmpty() ||
                position.isEmpty() || field1.getText().trim().isEmpty() || 
                field2.getText().trim().isEmpty() || department == null){
                JOptionPane.showMessageDialog(this, "missed information", "Addition Failed", JOptionPane.ERROR_MESSAGE);
                return ;
            }
            if (!name.matches("[a-zA-Z\\s]+")) {
                JOptionPane.showMessageDialog(this, "Name must contain letters only!", "Addition Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!position.matches("[a-zA-Z\\s-]+")) {
                JOptionPane.showMessageDialog(this, "Position must contain letters only!", "Addition Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int age = Integer.parseInt(age_field.getText().trim());
                double F1 = Double.parseDouble(field1.getText().trim());
                double F2 = Double.parseDouble(field2.getText().trim());

                if (age <= 0 || F1 < 0 || F2 < 0) {
                    JOptionPane.showMessageDialog(this, "invalid numeric values", "Addition Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    int newId = SharedData.generateNextEmployeeId();
                    Employee newEmp;
                    if (Type.equals("Full time")) {
                        newEmp = new FullTimeEmployee(newId, name, age, department, position, F1, F2);
                    } else {
                        newEmp = new PartTimeEmployee(newId, name, age, department, position, F1, F2);
                    }
                    SharedData.addEmployee(newEmp);
                    if (onEmployeeAdded != null) {
                        onEmployeeAdded.run();
                    }

                    JOptionPane.showMessageDialog(this, "Employee added successfully!");
                    name_field.setText("");
                    age_field.setText("");
                    position_Field.setText("");
                    field1.setText("");
                    field2.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age and salary fields must be numbers!", "Addition Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
