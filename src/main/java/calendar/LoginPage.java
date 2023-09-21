package calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage{

    //----
    public static JFrame frame;
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    //----

    public static void main(String[] args){
        DataBase.generateTestUsers();
        createUI();
    }

    LoginPage(){
        final JPanel loginPanel= new JPanel();

        JButton loginButton;
        JLabel titleLabel = new JLabel("Authorization");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.TOP_ALIGNMENT);

        usernameField = new JTextField(20);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        passwordField = new JPasswordField(20);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateCredentials(usernameField.getText(), new String(passwordField.getPassword()))) {
                    frame.remove(loginPanel);
                    new CalendarPage(frame, usernameField.getText());
                    frame.setSize(700, 550);
                } else {
                    showWarning("Error", "Please check your username and password");
                }
            }
        });
        loginPanel.setLayout(new BoxLayout(loginPanel,BoxLayout.PAGE_AXIS));

        JPanel textPanel = new JPanel();
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalGlue());
        textPanel.setBackground(Color.white);
        loginPanel.add(textPanel);

        loginPanel.add(Box.createRigidArea(new Dimension(5,0)));

        JPanel inputPanel = new JPanel();
        inputPanel.add(usernameField);
        inputPanel.add(Box.createRigidArea(new Dimension(5,0)));
        inputPanel.add(Box.createHorizontalGlue());
        inputPanel.add(passwordField);
            JPanel buttPanel = new JPanel();
            buttPanel.add(loginButton);
            buttPanel.setBackground(Color.WHITE);
        inputPanel.add(buttPanel);
        inputPanel.setBackground(Color.WHITE);

        loginPanel.add(inputPanel);
        loginPanel.setBackground(Color.WHITE);

        frame.setSize(500,200);
        frame.add(loginPanel);
    }

    private static boolean validateCredentials(String username, String password) {
        return DataBase.userHM.containsKey(username) && password.equals(DataBase.userHM.get(username).password);
    }

    private static void showWarning (String title, String message) {
        JOptionPane.showMessageDialog(frame,message,title,JOptionPane.ERROR_MESSAGE);
    }

    private static void createUI() {

        frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        new LoginPage();
        frame.setVisible(true);
    }
}
