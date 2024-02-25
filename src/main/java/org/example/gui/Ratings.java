package org.example.gui;

import org.example.IMDB;
import org.example.interfaces.NameField;
import org.example.productions.Actor;
import org.example.productions.Production;
import org.example.productions.Rating;
import org.example.users.Regular;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ratings extends JPanel {

    public Ratings(HomePage homePage, NameField item) {
        setLayout(new GridBagLayout());
        setBackground(new Color(12, 180, 200, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel ratingLabel = new JLabel("Rating: ");
        ratingLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField ratingField = new JTextField(30);
        ratingField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        ratingField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(ratingLabel, gbc);
        gbc.gridx = 1;
        add(ratingField, gbc);

        JLabel commentLabel = new JLabel("Comment: ");
        commentLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField commentField = new JTextField(30);
        commentField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        commentField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(commentLabel, gbc);
        gbc.gridx = 1;
        add(commentField, gbc);

        JButton button = new JButton("Add Rating");
        button.setFocusable(false);
        button.setBackground(new Color(192, 57, 43));
        button.setForeground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rating = ratingField.getText();
                String comment = commentField.getText();

                if (rating.isEmpty() || comment.isEmpty())
                    JOptionPane.showMessageDialog(homePage, "Please complete all the fields");

                if (isInteger(rating) && Integer.parseInt(rating) > 0 && Integer.parseInt(rating) < 11) {
                    Rating r = new Rating(IMDB.loggedInUser.getUsername(), Integer.parseInt(rating), comment);

                    if (item instanceof Production) ((Regular) IMDB.loggedInUser)
                            .addRatingToProduction(r, (Production) item);
                    else if (item instanceof Actor) ((Regular) IMDB.loggedInUser)
                            .addRatingToActor(r, (Actor) item);

                    homePage.setCenterPanel(new HomePagePanel(homePage));

                } else {
                    JOptionPane.showMessageDialog(homePage, "Please enter a valid integer (1-10)");
                }
            }
        });

        gbc.gridy = 2;
        add(button, gbc);
    }

    static boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}