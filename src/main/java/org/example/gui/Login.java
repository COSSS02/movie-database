package org.example.gui;

import org.example.IMDB;
import org.example.interfaces.NameField;
import org.example.users.User;
import org.example.utils.ManageLists;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    public Login() {
        setTitle("IMDB - Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(12, 180, 200, 255));

        JLabel titleLabel = new JLabel("Welcome back! Please enter your credentials:");
        titleLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.PAGE_START);

        JPanel credentialsPanel = new JPanel(new GridBagLayout());
        credentialsPanel.setBackground(new Color(12, 180, 200, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField emailField = new JTextField(30);
        emailField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        emailField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        credentialsPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        credentialsPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JPasswordField passwordField = new JPasswordField(30);
        passwordField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        passwordField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        credentialsPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        credentialsPanel.add(passwordField, gbc);

        add(credentialsPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(new Color(12, 180, 200, 255));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton loginButton = new JButton("Login");
        loginButton.setFocusable(false);
        loginButton.setBackground(new Color(46, 139, 87));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                User<NameField> user = ManageLists.loginUser(email, password);

                if (user == null) {
                    JOptionPane.showMessageDialog(Login.this,
                            "The entered credentials are wrong. Please try again.");
                    emailField.setText("");
                    passwordField.setText("");

                } else {
                    IMDB.loggedInUser = user;
                    JOptionPane.showMessageDialog(Login.this, "Login Successful!");
                    setVisible(false);
                    dispose();
                    new HomePage();
                }

            }
        });
        buttonsPanel.add(loginButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setFocusable(false);
        exitButton.setBackground(new Color(192, 57, 43));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buttonsPanel.add(exitButton);

        add(buttonsPanel, BorderLayout.PAGE_END);

        // Display the frame
        setVisible(true);
    }
}