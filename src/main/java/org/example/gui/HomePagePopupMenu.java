package org.example.gui;

import org.example.IMDB;
import org.example.users.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePagePopupMenu implements ActionListener {
    private final HomePage homePage;
    private final JButton menuButton;

    public HomePagePopupMenu(HomePage homePage, JButton menuButton) {
        this.homePage = homePage;
        this.menuButton = menuButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        {
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem productionsMenu = new JMenuItem("Productions");
            productionsMenu.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
            productionsMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    homePage.setCenterPanel(new Productions(homePage));
                }
            });
            popupMenu.add(productionsMenu);


            JMenuItem actorsMenu = new JMenuItem("Actors");
            actorsMenu.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
            actorsMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    homePage.setCenterPanel(new Actors(homePage));
                }
            });
            popupMenu.add(actorsMenu);


            JMenuItem notificationsMenu = new JMenuItem("Notifications");
            notificationsMenu.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
            notificationsMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    homePage.setCenterPanel(new Notifications(homePage));
                }
            });
            popupMenu.add(notificationsMenu);


            JMenuItem requestsMenu = new JMenuItem("Requests");
            requestsMenu.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
            requestsMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    homePage.setCenterPanel(new ChooseRequest(homePage));
                }
            });
            popupMenu.add(requestsMenu);


            if (IMDB.loggedInUser instanceof Admin) {
                JMenuItem usersMenu = new JMenuItem("Users");
                usersMenu.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                usersMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        homePage.setCenterPanel(new ChooseUserAction(homePage));
                    }
                });
                popupMenu.add(usersMenu);
            }

            JMenuItem logoutMenu = new JMenuItem("Logout");
            logoutMenu.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
            logoutMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    IMDB.loggedInUser = null;
                    homePage.setVisible(false);
                    homePage.dispose();
                    new InitApp();
                }
            });
            popupMenu.add(logoutMenu);

            popupMenu.show(menuButton, 0, menuButton.getHeight());
        }
    }
}