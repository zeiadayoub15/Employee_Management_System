package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginFrame extends JFrame implements ActionListener {

    JPanel buttons ;
    JTextField Username_field ;
    JPasswordField pass_field ;
    JButton loginButton;
    public LoginFrame() {

        this.setTitle("Employee system management");
        this.setSize(591, 246);  
        this.setResizable(false);                  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);         // set window in middle screen
        this.setLayout(new BorderLayout());


        JLabel title = new JLabel("LOGIN", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        this.add(title, BorderLayout.NORTH);

        
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 15));
        form.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        Username_field = new JTextField() ;
        Username_field.setFont(new Font(null , Font.BOLD , 15));
        form.add(new JLabel("Username:"));
        form.add(Username_field);
        pass_field = new JPasswordField() ;
        form.add(new JLabel("Password:"));
        form.add(pass_field);
        this.add(form, BorderLayout.CENTER);

        
        buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);     
        buttons.add(loginButton);
        this.add(buttons, BorderLayout.SOUTH);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        buttons.add(exitButton);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==loginButton){
            String user = Username_field.getText().trim();   // trim for spaces
            String pass = pass_field.getText().trim() ;
            if (user.equals("admin") && pass.equals("admin123")) {
                System.out.println("welcome admin");
                this.dispose();
                new MainFrame();
            }
            else {
                JOptionPane.showMessageDialog(this, "invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}