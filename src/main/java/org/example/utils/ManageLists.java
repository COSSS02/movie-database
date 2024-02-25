package org.example.utils;

import org.example.IMDB;
import org.example.interfaces.NameField;
import org.example.productions.Production;
import org.example.productions.Rating;
import org.example.users.*;

import java.util.List;
import java.util.Random;
import java.security.SecureRandom;

public class ManageLists {

    // Returns the user with the provided Credentials
    public static User<NameField> loginUser(String email, String password) {
        for (User<NameField> user : IMDB.getInstance().getUsers()) {
            if (user.getInformation().getCredentials().getEmail().equals(email) &&
                    user.getInformation().getCredentials().getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // Searches for a Production or an Actor in the System
    public static NameField search(String name) {
        NameField nameField = ManageLists.searchInList(IMDB.getInstance().getProductions(), name);
        if (nameField == null) {
            nameField = ManageLists.searchInList(IMDB.getInstance().getActors(), name);
        }
        return nameField;
    }

    // Searches for an item in a list that implements the NameField interface
    public static NameField searchInList(List<? extends NameField> list, String field) {
        for (NameField element : list) {
            if (element.getComparableValue().equalsIgnoreCase(field)) {
                return element;
            }
        }
        return null;
    }

    // Finds the Contributor that added a production/actor to the system
    public static Staff<NameField> findContributor(String contribution) {
        NameField nameField = searchInList(IMDB.getInstance().getProductions(), contribution);
        if (nameField == null) {
            nameField = searchInList(IMDB.getInstance().getActors(), contribution);
        }

        for (User<NameField> user : IMDB.getInstance().getUsers()) {
            if (user.getClass().equals(Contributor.class) ||
                    user.getClass().equals(Admin.class)) {

                Staff<NameField> staff = (Staff<NameField>) user;

                if (staff.getContributions().contains(nameField))
                    return staff;
            }
        }
        return null;
    }

    // Generates a username from the name provided
    public static String generateUsername(String name) {
        String[] names = name.split(" ");
        String firstName = names[0].toLowerCase();
        String lastName = names[names.length - 1].toLowerCase();

        Random random = new Random();
        int randomNumber = random.nextInt(9999);

        return firstName + "_" + lastName + "_" + randomNumber;
    }

    // Generates a secure password
    public static String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }

    // Adds the ratings from every production to the user lists. This method should be called after
    // the users have been added to the system
    public static void fixProductionRatingsList() {
        for (Production production : IMDB.getInstance().getProductions()) {
            production.getRatings().sort(new RatingsComparator());

            for (Rating rating : production.getRatings()) {
                User<NameField> user = (User<NameField>) searchInList(IMDB.getInstance().getUsers(),
                        rating.getUsername());
                if (user instanceof Regular) {
                    ((Regular) user).getCreatedRatings().add(production);
                    production.addObserver(user);
                }
            }
        }
    }
}