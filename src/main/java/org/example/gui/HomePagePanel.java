package org.example.gui;

import org.example.IMDB;
import org.example.interfaces.NameField;
import org.example.productions.Actor;
import org.example.productions.Production;
import org.example.utils.ManageLists;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePagePanel extends JPanel implements ListSelectionListener {
    private final HomePage homePage;

    public HomePagePanel(HomePage homePage) {
        this.homePage = homePage;
        setLayout(new GridBagLayout());
        setBackground(new Color(12, 180, 200, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel recommendedLabel = new JLabel("                                             Recommended For You" +
                "                                  ");
        recommendedLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(recommendedLabel, gbc);

        DefaultListModel<String> recommendedListModel = new DefaultListModel<>();
        List<Production> shuffleProduction = new ArrayList<>(IMDB.getInstance().getProductions());
        Collections.shuffle(shuffleProduction);
        List<Actor> shuffleActor = new ArrayList<>(IMDB.getInstance().getActors());
        Collections.shuffle(shuffleActor);
        for (int i = 0; i < 5; i++) {
            recommendedListModel.addElement(shuffleProduction.get(i).getComparableValue());
            recommendedListModel.addElement(shuffleActor.get(i).getComparableValue());
        }

        JList<String> recommendedList = new JList<>(recommendedListModel);
        recommendedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recommendedList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        recommendedList.setVisibleRowCount(5);
        recommendedList.setBackground(new Color(12, 180, 200, 255));
        recommendedList.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        recommendedList.addListSelectionListener(this);
        gbc.gridy = 1;
        gbc.weightx = 1;
        add(recommendedList, gbc);

        JLabel favoritesLabel = new JLabel("Your Favorites");
        favoritesLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 20));
        favoritesLabel.setBorder(BorderFactory.createEmptyBorder(75, 0, 0, 0));
        gbc.gridy = 2;
        gbc.weightx = 0;
        add(favoritesLabel, gbc);

        if (IMDB.loggedInUser.getFavorites().isEmpty()) {
            JLabel noLabel = new JLabel("You have nothing in your favorites");
            noLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
            gbc.gridy = 3;
            add(noLabel, gbc);

        } else {
            DefaultListModel<String> favoritesListModel = new DefaultListModel<>();
            for (NameField item : IMDB.loggedInUser.getFavorites()) {
                if (ManageLists.search(item.getComparableValue()) != null)
                    favoritesListModel.addElement(item.getComparableValue());
            }

            JList<String> favoritesList = new JList<>(favoritesListModel);
            favoritesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            favoritesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            favoritesList.setVisibleRowCount(10);
            favoritesList.setBackground(new Color(12, 180, 200, 255));
            favoritesList.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
            favoritesList.addListSelectionListener(this);
            gbc.gridy = 3;
            gbc.weightx = 1;
            add(favoritesList, gbc);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            JList<String> sourceList = (JList<String>) e.getSource();
            String selectedItem = sourceList.getSelectedValue();

            NameField item = ManageLists.search(selectedItem);

            if (item == null) JOptionPane.showMessageDialog(homePage
                    , "This is not the Name of an item from our System.");
            else {
                homePage.setCenterPanel(new ViewItemPanel(homePage, item));
            }
        }
    }
}