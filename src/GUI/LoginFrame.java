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
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(AppTheme.BG_MAIN);

        JLabel title = new JLabel("LOGIN", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(AppTheme.TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        this.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 15));
        form.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        AppTheme.stylePanel(form);

        JLabel userLabel = new JLabel("Username:");
        AppTheme.styleLabel(userLabel);
        Username_field = new JTextField() ;
        AppTheme.styleTextField(Username_field);
        Username_field.setFont(new Font(null , Font.BOLD , 15));
        form.add(userLabel);
        form.add(Username_field);

        JLabel passLabel = new JLabel("Password:");
        AppTheme.styleLabel(passLabel);
        pass_field = new JPasswordField() ;
        pass_field.setFont(AppTheme.FONT_INPUT);
        pass_field.setBackground(Color.WHITE);
        pass_field.setForeground(AppTheme.TEXT_DARK);
        pass_field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        form.add(passLabel);
        form.add(pass_field);
        this.add(form, BorderLayout.CENTER);

        buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        AppTheme.stylePanel(buttons);

        loginButton = new JButton("Login");
        AppTheme.styleButton(loginButton, AppTheme.BTN_UPDATE, AppTheme.TEXT_LIGHT);
        loginButton.addActionListener(this);
        buttons.add(loginButton);
        this.add(buttons, BorderLayout.SOUTH);

        JButton exitButton = new JButton("Exit");
        AppTheme.styleButton(exitButton, AppTheme.BTN_DELETE, AppTheme.TEXT_LIGHT);
        exitButton.addActionListener(e -> System.exit(0));
        buttons.add(exitButton);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==loginButton){
            String user = Username_field.getText().trim();
            String pass = pass_field.getText().trim() ;
            if (user.equals("admin") && pass.equals("admin123")) {
                this.dispose();
                new MainFrame();
            }
            else {
                JOptionPane.showMessageDialog(this, "invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}