package org.example.gui;

import org.example.IMDB;
import org.example.enums.RequestType;
import org.example.interfaces.NameField;
import org.example.interfaces.RequestsManager;
import org.example.productions.*;
import org.example.requests.Request;
import org.example.users.Regular;
import org.example.users.Staff;
import org.example.utils.ManageLists;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class Productions extends JPanel implements ListSelectionListener {
    private final HomePage homepage;

    public Productions(HomePage homePage) {
        this.homepage = homePage;

        setLayout(new GridBagLayout());
        setBackground(new Color(12, 180, 200, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Productions");
        label.setFont(new Font("DejaVu Sans", Font.BOLD, 20));
        add(label, gbc);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Production production : IMDB.getInstance().getProductions())
            listModel.addElement(production.getComparableValue());

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
            JButton addButton = new JButton("Add a production");
            addButton.setBackground(Color.LIGHT_GRAY);
            addButton.addActionListener(e -> {
                homepage.setCenterPanel(new ChooseProductionType(homePage));
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

            NameField item = ManageLists.searchInList(IMDB.getInstance().getProductions(), selectedItem);

            if (item == null) JOptionPane.showMessageDialog(homepage
                    , "This is not the Name of a production from our System.");
            else {
                homepage.setCenterPanel(new ViewItemPanel(homepage, item));
            }
        }
    }
}

class ChooseProductionType extends JPanel {

    public ChooseProductionType(HomePage homePage) {
        setBackground(new Color(12, 180, 200, 255));

        JButton movie = new JButton("Movie");
        movie.setFocusable(false);
        movie.setBackground(new Color(192, 57, 43));
        movie.setForeground(Color.WHITE);

        JButton series = new JButton("Series");
        series.setFocusable(false);
        series.setBackground(new Color(192, 57, 43));
        series.setForeground(Color.WHITE);

        movie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePage.setCenterPanel(new AddProduction(homePage, "Movie"));
            }
        });

        series.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePage.setCenterPanel(new AddProduction(homePage, "Series"));
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));


        add(Box.createHorizontalGlue());
        add(movie);
        add(Box.createHorizontalGlue());
        add(series);
        add(Box.createHorizontalGlue());
    }
}

class AddProduction extends JPanel {

    public AddProduction(HomePage homePage, String type) {
        setLayout(new GridBagLayout());
        setBackground(new Color(12, 180, 200, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Title: ");
        titleLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField titleField = new JTextField(30);
        titleField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        titleField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);
        gbc.gridx = 1;
        add(titleField, gbc);

        JLabel plotLabel = new JLabel("Plot: ");
        plotLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField plotField = new JTextField(30);
        plotField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        plotField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(plotLabel, gbc);
        gbc.gridx = 1;
        add(plotField, gbc);

        JLabel yearLabel = new JLabel("Release Year: ");
        yearLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField yearField = new JTextField(30);
        yearField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        yearField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(yearLabel, gbc);
        gbc.gridx = 1;
        add(yearField, gbc);

        JLabel extraLabel;
        if (type.equals("Movie")) extraLabel = new JLabel("Duration: ");
        else extraLabel = new JLabel("Number of Seasons: ");
        extraLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField extraField = new JTextField(30);
        extraField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        extraField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(extraLabel, gbc);
        gbc.gridx = 1;
        add(extraField, gbc);

        JButton button = new JButton("Add Production");
        button.setFocusable(false);
        button.setBackground(new Color(192, 57, 43));
        button.setForeground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String plot = plotField.getText();
                String year = yearField.getText();
                String extra = extraField.getText();

                if (title.isEmpty() || plot.isEmpty() || year.isEmpty() || extra.isEmpty())
                    JOptionPane.showMessageDialog(homePage, "Please complete all the fields");

                boolean enter = true;
                if (type.equals("Series"))
                    if (!(Ratings.isInteger(extra) && Integer.parseInt(extra) > 0)) {
                        JOptionPane.showMessageDialog(homePage, "Please enter a valid number of Seasons");
                        enter = false;
                    }

                if (enter)
                    if (Ratings.isInteger(year) && Integer.parseInt(year) > 0 && Integer.parseInt(year) < 2025) {
                        Production p;

                        if (type.equals("Movie")) p = new Movie(title, plot, 0,
                                Integer.parseInt(year), extra);
                        else p = new Series(title, plot, 0,
                                Integer.parseInt(year), Integer.parseInt(extra));

                        ((Staff<NameField>) IMDB.loggedInUser).addProductionSystem(p);

                        homePage.setCenterPanel(new HomePagePanel(homePage));

                    } else {
                        JOptionPane.showMessageDialog(homePage, "Please enter a valid year (<2025)");
                    }
            }
        });

        gbc.gridy = 4;
        add(button, gbc);
    }
}