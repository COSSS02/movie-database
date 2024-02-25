package org.example.gui;

import org.example.IMDB;
import org.example.interfaces.NameField;
import org.example.productions.Actor;
import org.example.users.Regular;
import org.example.users.Staff;
import org.example.utils.ManageLists;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Actors extends JPanel implements ListSelectionListener {
    private final HomePage homepage;

    public Actors(HomePage homePage) {
        this.homepage = homePage;

        setLayout(new GridBagLayout());
        setBackground(new Color(12, 180, 200, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Actors");
        label.setFont(new Font("DejaVu Sans", Font.BOLD, 20));
        add(label, gbc);

        List<Actor> alphabeticalList = new ArrayList<>(IMDB.getInstance().getActors());
        Collections.sort(alphabeticalList);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Actor actor : alphabeticalList)
            listModel.addElement(actor.getComparableValue());

        JList<String> list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(10);
        list.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        list.setBackground(new Color(12, 180, 200, 255));
        list.addListSelectionListener(this);
        gbc.gridy = 1;
        add(new JScrollPane(list), gbc);

        if (!(IMDB.loggedInUser instanceof Regular)) {
            JButton addButton = new JButton("Add an actor");
            addButton.setBackground(Color.LIGHT_GRAY);
            addButton.addActionListener(e -> {
                homePage.setCenterPanel(new AddActor(homePage));
            });

            gbc.gridy = 2;
            add(addButton, gbc);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            JList<String> sourceList = (JList<String>) e.getSource();
            String selectedItem = sourceList.getSelectedValue();

            NameField item = ManageLists.searchInList(IMDB.getInstance().getActors(), selectedItem);

            if (item == null) JOptionPane.showMessageDialog(homepage
                    , "This is not the Name of an actor from our System.");
            else {
                homepage.setCenterPanel(new ViewItemPanel(homepage, item));
            }
        }
    }
}

class AddActor extends JPanel {

    public AddActor(HomePage homePage) {
        setLayout(new GridBagLayout());
        setBackground(new Color(12, 180, 200, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField nameField = new JTextField(30);
        nameField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        nameField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        // Password Label and Field
        JLabel biographyLabel = new JLabel("Biography: ");
        biographyLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField BiographyField = new JTextField(30);
        BiographyField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        BiographyField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(biographyLabel, gbc);
        gbc.gridx = 1;
        add(BiographyField, gbc);

        JButton button = new JButton("Add Actor");
        button.setFocusable(false);
        button.setBackground(new Color(192, 57, 43));
        button.setForeground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String biography = BiographyField.getText();

                if (name.isEmpty() || biography.isEmpty())
                    JOptionPane.showMessageDialog(homePage, "Please complete all the fields");
                else {
                    ((Staff<NameField>) IMDB.loggedInUser).addActorSystem(new Actor(name, biography));
                    homePage.setCenterPanel(new HomePagePanel(homePage));
                }
            }
        });

        gbc.gridy = 2;
        add(button, gbc);
    }
}