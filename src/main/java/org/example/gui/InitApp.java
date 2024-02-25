package org.example.gui;

import org.example.IMDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitApp extends JFrame {

    public InitApp() {
        setTitle("IMDB");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(12, 180, 200, 255));

        JButton cliButton = new JButton("CLI");
        cliButton.setFocusable(false);
        cliButton.setBackground(new Color(192, 57, 43));
        cliButton.setForeground(Color.WHITE);

        JButton guiButton = new JButton("GUI");
        guiButton.setFocusable(false);
        guiButton.setBackground(new Color(192, 57, 43));
        guiButton.setForeground(Color.WHITE);

        Dimension buttonSize = new Dimension(100, 100);
        cliButton.setPreferredSize(buttonSize);
        guiButton.setPreferredSize(buttonSize);

        cliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action for CLI mode
                setVisible(false);
                dispose();
                IMDB.getInstance().startCLI();
            }
        });

        guiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
                new Login();
            }
        });

        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        add(Box.createHorizontalGlue());
        add(cliButton);
        add(Box.createHorizontalGlue());
        add(guiButton);
        add(Box.createHorizontalGlue());

        setVisible(true);
    }
}