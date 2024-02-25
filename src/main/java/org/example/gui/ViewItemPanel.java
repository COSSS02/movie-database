package org.example.gui;

import org.example.IMDB;
import org.example.enums.RequestType;
import org.example.interfaces.NameField;
import org.example.productions.Actor;
import org.example.productions.Production;
import org.example.requests.RequestsHolder;
import org.example.users.Admin;
import org.example.users.Contributor;
import org.example.users.Regular;
import org.example.users.Staff;
import org.example.utils.ManageLists;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewItemPanel extends JPanel {

    public ViewItemPanel(HomePage homePage, NameField item) {
        setLayout(new BorderLayout());
        setBackground(new Color(12, 180, 200, 255));

        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(new Color(12, 180, 200, 255));
        textArea.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        if (item instanceof Production p) textArea.append(p.displayInfo());
        else if (item instanceof Actor a)
            textArea.append(a.displayInfo());
        textArea.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(100, 0, 0, 0),
                textArea.getBorder()
        ));

        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setPreferredSize(new Dimension(500, 300));
        textScrollPane.setBackground(new Color(12, 180, 200, 255));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(12, 180, 200, 255));


        JButton addFavorites = new JButton("Add to favorites");
        addFavorites.setBackground(Color.LIGHT_GRAY);
        addFavorites.setAlignmentX(Component.CENTER_ALIGNMENT);
        addFavorites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!IMDB.loggedInUser.getFavorites().contains(item))
                    IMDB.loggedInUser.addFavorite(item);
                else JOptionPane.showMessageDialog(homePage,
                        "This item is already in your Favorites list.");
            }
        });
        buttonPanel.add(addFavorites);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));


        JButton removeFavorites = new JButton("Remove from favorites");
        removeFavorites.setBackground(Color.LIGHT_GRAY);
        removeFavorites.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeFavorites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (IMDB.loggedInUser.getFavorites().contains(item))
                    IMDB.loggedInUser.removeFavorite(item);
                else JOptionPane.showMessageDialog(homePage,
                        "This item is not in your Favorites list.");
            }
        });
        buttonPanel.add(removeFavorites);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));


        if (IMDB.loggedInUser instanceof Regular) {
            JButton createRequest = new JButton("Create a request");
            createRequest.setAlignmentX(Component.CENTER_ALIGNMENT);
            createRequest.setBackground(Color.LIGHT_GRAY);
            createRequest.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (item instanceof Production)
                        homePage.setCenterPanel(new Requests(homePage, RequestType.MOVIE_ISSUE,
                                item.getComparableValue()));
                    else if (item instanceof Actor)
                        homePage.setCenterPanel(new Requests(homePage, RequestType.ACTOR_ISSUE,
                                item.getComparableValue()));
                }
            });
            buttonPanel.add(createRequest);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));


            JButton rate = new JButton("Add a rating");
            rate.setAlignmentX(Component.CENTER_ALIGNMENT);
            rate.setBackground(Color.LIGHT_GRAY);
            rate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    homePage.setCenterPanel(new Ratings(homePage, item));
                }
            });
            buttonPanel.add(rate);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));


        } else if (IMDB.loggedInUser instanceof Contributor) {
            JButton createRequest = new JButton("Create a request");
            createRequest.setAlignmentX(Component.CENTER_ALIGNMENT);
            createRequest.setBackground(Color.LIGHT_GRAY);
            createRequest.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (item instanceof Production) {
                        if (ManageLists.findContributor(item.getComparableValue()) != IMDB.loggedInUser)
                            homePage.setCenterPanel(new Requests(homePage, RequestType.MOVIE_ISSUE,
                                    item.getComparableValue()));
                        else JOptionPane.showMessageDialog(homePage, "You can't make a Request " +
                                "for your own Contribution.");

                    } else if (item instanceof Actor) {
                        if (ManageLists.findContributor(item.getComparableValue()) != IMDB.loggedInUser)
                            homePage.setCenterPanel(new Requests(homePage, RequestType.ACTOR_ISSUE,
                                    item.getComparableValue()));
                        else JOptionPane.showMessageDialog(homePage, "You can't make a Request " +
                                "for your own Contribution.");
                    }
                }
            });
            buttonPanel.add(createRequest);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));


            JButton delete = new JButton("Delete from system");
            delete.setAlignmentX(Component.CENTER_ALIGNMENT);
            delete.setBackground(Color.LIGHT_GRAY);
            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Staff<NameField> contributor = ManageLists.findContributor(item.getComparableValue());

                    if (IMDB.loggedInUser.equals(contributor)) {
                        if (item instanceof Production) contributor.removeProductionSystem
                                (item.getComparableValue());
                        else if (item instanceof Actor) contributor.removeActorSystem
                                (item.getComparableValue());
                    } else {
                        JOptionPane.showMessageDialog(homePage, "You can only remove your own contributions");
                    }
                }
            });
            buttonPanel.add(delete);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));


        } else if (IMDB.loggedInUser instanceof Admin) {
            JButton delete = new JButton("Delete from system");
            delete.setAlignmentX(Component.CENTER_ALIGNMENT);
            delete.setBackground(Color.LIGHT_GRAY);
            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Staff<NameField> contributor = ManageLists.findContributor(item.getComparableValue());

                    if (contributor == null) {
                        boolean found = false;
                        for (NameField element : RequestsHolder.adminContributions) {
                            if (element.equals(item)) {
                                if (item instanceof Production) ((Admin) IMDB.loggedInUser).removeProductionSystem
                                        (item.getComparableValue());
                                else if (item instanceof Actor) ((Admin) IMDB.loggedInUser).removeActorSystem
                                        (item.getComparableValue());
                                found = true;
                                break;
                            }
                        }

                        if (!found) JOptionPane.showMessageDialog(homePage, "You can only remove " +
                                "your own contributions or Admin Contributions");

                    } else if (IMDB.loggedInUser.equals(contributor)) {
                        if (item instanceof Production) contributor.removeProductionSystem
                                (item.getComparableValue());
                        else if (item instanceof Actor) contributor.removeActorSystem
                                (item.getComparableValue());

                    } else {
                        JOptionPane.showMessageDialog(homePage, "You can only remove " +
                                "your own contributions or Admin Contributions");
                    }
                }
            });
            buttonPanel.add(delete);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }


        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 0, 10));
        
        add(textScrollPane, BorderLayout.LINE_START);
        add(buttonPanel, BorderLayout.CENTER);
    }
}