package org.example.gui;

import org.example.IMDB;

import javax.swing.*;
import java.awt.*;

public class Notifications extends JPanel {

    public Notifications(HomePage homePage) {
        setLayout(new GridBagLayout());
        setBackground(new Color(12, 180, 200, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Notifications");
        label.setFont(new Font("DejaVu Sans", Font.BOLD, 20));
        add(label, gbc);

        if (IMDB.loggedInUser.getNotifications().isEmpty()) {
            JLabel noLabel = new JLabel("You have no new notifications");
            noLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
            gbc.gridy = 1;
            add(noLabel, gbc);

        } else {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (String notification : IMDB.loggedInUser.getNotifications())
                listModel.addElement(notification);

            JList<String> list = new JList<>(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setLayoutOrientation(JList.VERTICAL);
            list.setVisibleRowCount(10);
            list.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
            list.setBackground(new Color(12, 180, 200, 255));

            JScrollPane scrollPane = new JScrollPane(list);
            scrollPane.setPreferredSize(new Dimension(500, 200));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            gbc.gridy = 1;
            gbc.weighty = 1;
            gbc.weightx = 1;
            add(scrollPane, gbc);

        }

        JButton removeButton = new JButton("Delete all notifications");
        removeButton.setBackground(Color.LIGHT_GRAY);
        removeButton.addActionListener(e -> {
            IMDB.loggedInUser.getNotifications().clear();
            homePage.setCenterPanel(new HomePagePanel(homePage));
        });

        gbc.gridy = 5;
        add(removeButton, gbc);
    }
}