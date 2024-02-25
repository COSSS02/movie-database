package org.example.gui;

import org.example.IMDB;
import org.example.interfaces.NameField;
import org.example.interfaces.RequestsManager;
import org.example.requests.Request;
import org.example.requests.RequestsHolder;
import org.example.users.Admin;
import org.example.users.Regular;
import org.example.enums.RequestType;
import org.example.users.Staff;

import javax.lang.model.element.Name;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Iterator;

public class Requests extends JPanel {

    public Requests(HomePage homePage, RequestType requestType, String name) {
        setBackground(new Color(12, 180, 200, 255));
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel label = new JLabel("Description:");
        label.setFont(new Font("DejaVu Sans", Font.BOLD, 16));

        JTextField textField = new JTextField(30);
        textField.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        textField.setBackground(Color.LIGHT_GRAY);

        JButton button = new JButton("Add Request");
        button.setFocusable(false);
        button.setBackground(new Color(192, 57, 43));
        button.setForeground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = textField.getText();
                if (description.isEmpty()) JOptionPane.showMessageDialog(homePage, "Add a description");
                else {
                    Request request = new Request(requestType, LocalDateTime.now(), description,
                            IMDB.loggedInUser.getUsername());

                    if (requestType.equals(RequestType.MOVIE_ISSUE)) {
                        request.setMovieTitle(name);

                    } else if (requestType.equals(RequestType.ACTOR_ISSUE)) {
                        request.setActorName(name);
                    }
                    ((RequestsManager) IMDB.loggedInUser).createRequest(request);

                    homePage.setCenterPanel(new HomePagePanel(homePage));
                }
            }
        });

        add(label);
        add(textField);
        add(button);
    }
}

class ChooseRequest extends JPanel {

    public ChooseRequest(HomePage homePage) {
        setBackground(new Color(12, 180, 200, 255));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JButton createButton = new JButton("Create a Request");
        createButton.setFocusable(false);
        createButton.setBackground(new Color(192, 57, 43));
        createButton.setForeground(Color.WHITE);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePage.setCenterPanel(new ChooseRequestType(homePage));
            }
        });
        add(Box.createHorizontalGlue());
        add(createButton);

        if (!(IMDB.loggedInUser instanceof Admin)) {
            JButton deleteButton = new JButton("Delete a Request");
            deleteButton.setFocusable(false);
            deleteButton.setBackground(new Color(192, 57, 43));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    homePage.setCenterPanel(new DeleteRequest(homePage));
                }
            });
            add(Box.createHorizontalGlue());
            add(deleteButton);
        }

        if (!(IMDB.loggedInUser instanceof Regular)) {
            JButton solveButton = new JButton("Solve a Request");
            solveButton.setFocusable(false);
            solveButton.setBackground(new Color(192, 57, 43));
            solveButton.setForeground(Color.WHITE);
            solveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    homePage.setCenterPanel(new SolveRequest(homePage));
                }
            });
            add(Box.createHorizontalGlue());
            add(solveButton);
        }

        add(Box.createHorizontalGlue());
    }
}

class ChooseRequestType extends JPanel {

    public ChooseRequestType(HomePage homePage) {
        setBackground(new Color(12, 180, 200, 255));

        JButton deleteAccount = new JButton("Delete Account");
        deleteAccount.setFocusable(false);
        deleteAccount.setBackground(new Color(192, 57, 43));
        deleteAccount.setForeground(Color.WHITE);

        JButton otherButton = new JButton("Other");
        otherButton.setFocusable(false);
        otherButton.setBackground(new Color(192, 57, 43));
        otherButton.setForeground(Color.WHITE);

        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePage.setCenterPanel(new Requests(homePage, RequestType.DELETE_ACCOUNT, null));
            }
        });

        otherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePage.setCenterPanel(new Requests(homePage, RequestType.OTHERS, null));
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(Box.createHorizontalGlue());
        add(deleteAccount);
        add(Box.createHorizontalGlue());
        add(otherButton);
        add(Box.createHorizontalGlue());
    }
}

class DeleteRequest extends JPanel implements ListSelectionListener {
    private HomePage homePage;

    public DeleteRequest(HomePage homePage) {
        this.homePage = homePage;

        setLayout(new GridBagLayout());
        setBackground(new Color(12, 180, 200, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Requests");
        label.setFont(new Font("DejaVu Sans", Font.BOLD, 20));
        add(label, gbc);

        if (((RequestsManager) IMDB.loggedInUser).getCreatedRequests().isEmpty()) {
            JLabel noLabel = new JLabel("You have not created any requests");
            noLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
            gbc.gridy = 1;
            add(noLabel, gbc);

        } else {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Request request : ((RequestsManager) IMDB.loggedInUser).getCreatedRequests())
                listModel.addElement(request.getType() + " - " + request.getDescription());

            JList<String> list = new JList<>(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setLayoutOrientation(JList.VERTICAL);
            list.setVisibleRowCount(10);
            list.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
            list.setBackground(new Color(12, 180, 200, 255));
            list.addListSelectionListener(this);
            gbc.gridy = 1;
            add(new JScrollPane(list), gbc);
        }
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            JList<String> sourceList = (JList<String>) e.getSource();
            String selectedItem = sourceList.getSelectedValue();

            Iterator<Request> it = ((RequestsManager) IMDB.loggedInUser).getCreatedRequests().iterator();

            while (it.hasNext()) {
                Request request = it.next();
                if ((request.getType() + " - " + request.getDescription()).equals(selectedItem))
                    it.remove();
            }

            homePage.setCenterPanel(new DeleteRequest(homePage));
        }
    }
}

class SolveRequest extends JPanel implements ListSelectionListener {
    private HomePage homePage;

    public SolveRequest(HomePage homePage) {
        this.homePage = homePage;
        setLayout(new GridBagLayout());
        setBackground(new Color(12, 180, 200, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Requests");
        label.setFont(new Font("DejaVu Sans", Font.BOLD, 20));
        add(label, gbc);

        if (((Staff<NameField>) IMDB.loggedInUser).getRequests().isEmpty()) {
            JLabel noLabel = new JLabel("You have no requests to resolve");
            noLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
            gbc.gridy = 1;
            add(noLabel, gbc);

        } else {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Request request : ((Staff<NameField>) IMDB.loggedInUser).getRequests()) {
                listModel.addElement(request.getType() + " - " + request.getDescription());
            }

            if (IMDB.loggedInUser instanceof Admin)
                for (Request request : RequestsHolder.adminRequests) {
                    listModel.addElement(request.getType() + " - " + request.getDescription());
                }

            JList<String> list = new JList<>(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setLayoutOrientation(JList.VERTICAL);
            list.setVisibleRowCount(10);
            list.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
            list.setBackground(new Color(12, 180, 200, 255));
            list.addListSelectionListener(this);

            gbc.gridy = 1;
            add(new JScrollPane(list), gbc);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            JList<String> sourceList = (JList<String>) e.getSource();
            String selectedItem = sourceList.getSelectedValue();

            for (Request r : ((Staff<NameField>) IMDB.loggedInUser).getRequests())
                if ((r.getType() + " - " + r.getDescription()).equals(selectedItem))
                    homePage.setCenterPanel(new SolveOrReject(homePage, r));

            if (IMDB.loggedInUser instanceof Admin) {
                for (Request r : RequestsHolder.adminRequests)
                    if ((r.getType() + " - " + r.getDescription()).equals(selectedItem))
                        homePage.setCenterPanel(new SolveOrReject(homePage, r));
            }
        }
    }
}


class SolveOrReject extends JPanel {

    SolveOrReject(HomePage homePage, Request request) {
        setBackground(new Color(12, 180, 200, 255));

        JButton solve = new JButton("Mark the request as solved");
        solve.setFocusable(false);
        solve.setBackground(new Color(192, 57, 43));
        solve.setForeground(Color.WHITE);

        JButton reject = new JButton("Reject the request");
        reject.setFocusable(false);
        reject.setBackground(new Color(192, 57, 43));
        reject.setForeground(Color.WHITE);

        solve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                request.setSolved(true);

                if (((Staff<NameField>) IMDB.loggedInUser).getRequests().contains(request))
                    ((Staff<NameField>) IMDB.loggedInUser).removeRequest(request);

                else RequestsHolder.removeAdminRequest(request);

                homePage.setCenterPanel(new HomePagePanel(homePage));
            }
        });

        reject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                request.setSolved(false);

                if (((Staff<NameField>) IMDB.loggedInUser).getRequests().contains(request))
                    ((Staff<NameField>) IMDB.loggedInUser).removeRequest(request);

                else RequestsHolder.removeAdminRequest(request);

                homePage.setCenterPanel(new HomePagePanel(homePage));
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(Box.createHorizontalGlue());
        add(solve);
        add(Box.createHorizontalGlue());
        add(reject);
        add(Box.createHorizontalGlue());
    }
}