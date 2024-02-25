package org.example.gui;

import org.example.IMDB;
import org.example.enums.AccountType;
import org.example.interfaces.NameField;
import org.example.productions.Actor;
import org.example.productions.Production;
import org.example.requests.Request;
import org.example.requests.RequestsHolder;
import org.example.users.*;
import org.example.utils.ManageLists;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Users extends JPanel {

    Users(HomePage homePage, AccountType type) {
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

        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField emailField = new JTextField(30);
        emailField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        emailField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(emailLabel, gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        JLabel countryLabel = new JLabel("Country: ");
        countryLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField countryField = new JTextField(30);
        countryField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        countryField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(countryLabel, gbc);
        gbc.gridx = 1;
        add(countryField, gbc);

        JLabel ageLabel = new JLabel("Age: ");
        ageLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField ageField = new JTextField(30);
        ageField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        ageField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(ageLabel, gbc);
        gbc.gridx = 1;
        add(ageField, gbc);


        JLabel genderLabel = new JLabel("Gender: ");
        genderLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField genderField = new JTextField(30);
        genderField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        genderField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(genderLabel, gbc);
        gbc.gridx = 1;
        add(genderField, gbc);

        JLabel dateLabel = new JLabel("Birth Date (format: yyyy-mm-dd): ");
        dateLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        JTextField dateField = new JTextField(30);
        dateField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        dateField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(dateLabel, gbc);
        gbc.gridx = 1;
        add(dateField, gbc);

        JButton button = new JButton("Add User");
        button.setFocusable(false);
        button.setBackground(new Color(192, 57, 43));
        button.setForeground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String country = countryField.getText();
                String age = ageField.getText();
                String gender = genderField.getText();
                String date = dateField.getText();

                if (name.isEmpty() || email.isEmpty() || country.isEmpty()
                        || age.isEmpty() || gender.isEmpty() || date.isEmpty())
                    JOptionPane.showMessageDialog(homePage, "Please complete all the fields");

                if (Ratings.isInteger(age) && Integer.parseInt(age) > 0 && Integer.parseInt(age) < 121) {
                    String experience = null;
                    if (!type.equals(AccountType.Admin)) experience = "0";

                    User<NameField> user = UserFactory.getUser(type, ManageLists.
                            generateUsername(name), experience);

                    LocalDate birthDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

                    user.setInformation(new User.Information.InformationBuilder(email, ManageLists.generatePassword())
                            .name(name).country(country).age(Integer.parseInt(age)).gender(gender)
                            .birthDate(birthDate).build());

                    IMDB.getInstance().getUsers().add(user);

                    if (type.equals(AccountType.Admin)) RequestsHolder.admins.add((Admin) user);

                    homePage.setCenterPanel(new HomePagePanel(homePage));

                } else {
                    JOptionPane.showMessageDialog(homePage, "Please enter a valid age (<121)");
                }
            }
        });

        gbc.gridy = 6;
        add(button, gbc);
    }
}

class ChooseUserAction extends JPanel {

    public ChooseUserAction(HomePage homePage) {
        setBackground(new Color(12, 180, 200, 255));

        JButton add = new JButton("Add a user");
        add.setFocusable(false);
        add.setBackground(new Color(192, 57, 43));
        add.setForeground(Color.WHITE);

        JButton remove = new JButton("Remove a user");
        remove.setFocusable(false);
        remove.setBackground(new Color(192, 57, 43));
        remove.setForeground(Color.WHITE);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePage.setCenterPanel(new ChooseUserType(homePage));
            }
        });

        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePage.setCenterPanel(new RemoveUser(homePage));
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(Box.createHorizontalGlue());
        add(add);
        add(Box.createHorizontalGlue());
        add(remove);
        add(Box.createHorizontalGlue());
    }
}

class RemoveUser extends JPanel implements ListSelectionListener {
    private HomePage homePage;

    public RemoveUser(HomePage homePage) {
        this.homePage = homePage;

        setLayout(new GridBagLayout());
        setBackground(new Color(12, 180, 200, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Users");
        label.setFont(new Font("DejaVu Sans", Font.BOLD, 20));
        add(label, gbc);

        if (IMDB.getInstance().getUsers().isEmpty()) {
            JLabel noLabel = new JLabel("There are no users in the system");
            noLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
            gbc.gridy = 1;
            add(noLabel, gbc);

        } else {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (User<NameField> user : IMDB.getInstance().getUsers())
                listModel.addElement(user.getUsername());

            JList<String> list = new JList<>(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setLayoutOrientation(JList.VERTICAL);
            list.setVisibleRowCount(10);
            list.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
            list.setBackground(new Color(12, 180, 200, 255));
            list.addListSelectionListener(this);

            JScrollPane scrollPane = new JScrollPane(list);
            scrollPane.setPreferredSize(new Dimension(500, 200));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            gbc.gridy = 1;
            gbc.weighty = 1;
            gbc.weightx = 1;
            add(scrollPane, gbc);
        }
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            JList<String> sourceList = (JList<String>) e.getSource();
            String selectedItem = sourceList.getSelectedValue();

            User<NameField> u = (User<NameField>)
                    ManageLists.searchInList(IMDB.getInstance().getUsers(), selectedItem);

            if (u instanceof Regular r) {
                for (Request request : r.getCreatedRequests()) {
                    if (request.getTo().equalsIgnoreCase("ADMIN"))
                        RequestsHolder.adminRequests.remove(request);

                    else ((Staff<NameField>) ManageLists.searchInList(IMDB.getInstance().getUsers(),
                            request.getTo())).getRequests().remove(request);
                }

                for (NameField item : r.getCreatedRatings()) {
                    if (item instanceof Production p)
                        p.getRatings().remove(ManageLists.searchInList(p.getRatings(), r.getUsername()));
                    else if (item instanceof Actor a)
                        a.getRatings().remove(ManageLists.searchInList(a.getRatings(), r.getUsername()));
                }

            } else if (u instanceof Staff s) {
                RequestsHolder.adminRequests.addAll(s.getRequests());
                RequestsHolder.adminContributions.addAll(s.getContributions());

                if (u instanceof Contributor c) {
                    for (Request request : c.getCreatedRequests()) {
                        if (request.getTo().equalsIgnoreCase("ADMIN"))
                            RequestsHolder.adminRequests.remove(request);

                        else ((Staff<NameField>) ManageLists.searchInList(IMDB.getInstance().getUsers(),
                                request.getTo())).getRequests().remove(request);
                    }

                } else RequestsHolder.admins.remove(u);
            }

            IMDB.getInstance().getUsers().remove(u);

            homePage.setCenterPanel(new HomePagePanel(homePage));
        }
    }
}

class ChooseUserType extends JPanel {

    public ChooseUserType(HomePage homePage) {
        setBackground(new Color(12, 180, 200, 255));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JButton regular = new JButton("Regular");
        regular.setFocusable(false);
        regular.setBackground(new Color(192, 57, 43));
        regular.setForeground(Color.WHITE);
        regular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePage.setCenterPanel(new Users(homePage, AccountType.Regular));
            }
        });
        add(Box.createHorizontalGlue());
        add(regular);

        JButton contributor = new JButton("Contributor");
        contributor.setFocusable(false);
        contributor.setBackground(new Color(192, 57, 43));
        contributor.setForeground(Color.WHITE);
        contributor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePage.setCenterPanel(new Users(homePage, AccountType.Contributor));
            }
        });
        add(Box.createHorizontalGlue());
        add(contributor);

        JButton admin = new JButton("Admin");
        admin.setFocusable(false);
        admin.setBackground(new Color(192, 57, 43));
        admin.setForeground(Color.WHITE);
        admin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePage.setCenterPanel(new Users(homePage, AccountType.Admin));
            }
        });
        add(Box.createHorizontalGlue());
        add(admin);

        add(Box.createHorizontalGlue());
    }
}