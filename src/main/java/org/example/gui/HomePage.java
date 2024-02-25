package org.example.gui;

import org.example.interfaces.NameField;
import org.example.utils.ManageLists;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomePage extends JFrame {
    private JPanel centerPanel;

    public void setCenterPanel(JPanel centerPanel) {
        if (this.centerPanel != null) {
            this.centerPanel.removeAll();
            this.centerPanel.add(centerPanel);

        } else this.centerPanel = centerPanel;

        revalidate();
        repaint();
    }

    public HomePage() {
        setTitle("Imdb - Home Page");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(12, 180, 200, 255));

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.PAGE_START;

        JButton menuButton = new JButton("Menu");
        menuButton.setFocusable(false);
        menuButton.setBackground(Color.LIGHT_GRAY);
        menuButton.addActionListener(new HomePagePopupMenu(HomePage.this, menuButton));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(menuButton, gbc);

        JLabel titleLabel = new JLabel("IMDB");
        titleLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 26));
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setCenterPanel(new HomePagePanel(HomePage.this));
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        add(titleLabel, gbc);

        JTextField searchField = new JTextField();
        searchField.setMinimumSize(new Dimension(200, 28));
        searchField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        searchField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        add(searchField, gbc);

        JButton searchButton = new JButton("Search");
        searchButton.setFocusable(false);
        searchButton.setBackground(Color.LIGHT_GRAY);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = searchField.getText();
                searchField.setText("");

                NameField item = ManageLists.search(text);

                if (item == null) JOptionPane.showMessageDialog(HomePage.this
                        , "This is not the Name of an item from our System.");
                else {
                    setCenterPanel(new ViewItemPanel(HomePage.this, item));
                }
            }
        });
        gbc.gridx = 3;
        gbc.gridy = 0;
        add(searchButton, gbc);

        setCenterPanel(new HomePagePanel(HomePage.this));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 1;
        add(centerPanel, gbc);

        setVisible(true);
    }
}