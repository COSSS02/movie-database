package org.example;

import org.example.gui.InitApp;
import org.example.interfaces.NameField;
import org.example.productions.Actor;
import org.example.productions.Production;
import org.example.requests.Request;
import org.example.users.Admin;
import org.example.users.Contributor;
import org.example.users.Regular;
import org.example.users.User;
import org.example.utils.CLI;
import org.example.utils.ManageLists;
import org.example.utils.ReadInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class IMDB {
    private final List<User<NameField>> users;
    private final List<Actor> actors;
    private final List<Request> requests;
    private final List<Production> productions;

    public static Scanner scanner;
    public static User<NameField> loggedInUser;
    private static IMDB imdb;


    private IMDB() {
        users = new ArrayList<>();
        actors = new ArrayList<>();
        requests = new ArrayList<>();
        productions = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public static IMDB getInstance() {
        if (imdb == null) {
            imdb = new IMDB();
        }
        return imdb;
    }

    public List<User<NameField>> getUsers() {
        return users;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public List<Production> getProductions() {
        return productions;
    }


    public void run() {
        ReadInput.readActors();
        ReadInput.readProductions();
        ReadInput.readAccounts();
        ReadInput.readRequests();
        new InitApp();
    }

    public static void main(String[] args) {
        imdb = getInstance();
        imdb.run();
    }

    public void startCLI() {
        System.out.println("Welcome back! Please enter your credentials\n");

        while (true) {
            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            User<NameField> user = ManageLists.loginUser(email, password);

            if (user == null) {
                System.out.println("The entered credentials are wrong. Please try again.\n");
            } else {
                loggedInUser = user;
                break;
            }
        }

        printOptions();

        if (loggedInUser.getClass().equals(Regular.class)) {
            RegularUser();
        } else if (loggedInUser.getClass().equals(Contributor.class)) {
            ContributorUser();
        } else if (loggedInUser.getClass().equals(Admin.class)) {
            AdminUser();
        }
    }

    private void printOptions() {
        System.out.println("\n 1) View Production Details");
        System.out.println(" 2) View Actor Details");
        System.out.println(" 3) View Notifications");
        System.out.println(" 4) Search for Actor/Movie/Series");
        System.out.println(" 5) Add/Delete Actor/Movie/Series to/from Favorites");
    }

    private void printRegularOptions() {
        System.out.println(" 6) Create/Delete a Request");
        System.out.println(" 7) Rate an Actor/Movie/Series");
        System.out.println(" 8) Logout");
        System.out.println(" 9) Exit");
    }

    private void printContributorOptions() {
        System.out.println(" 6) Create/Delete a Request");
        System.out.println(" 7) Add/Delete Actor/Movie/Series to/from System");
        System.out.println(" 8) Solve a Request");
        System.out.println(" 9) Update Production Details");
        System.out.println("10) Update Actor Details");
        System.out.println("11) Logout");
        System.out.println("12) Exit");
    }

    private void printAdminOptions() {
        System.out.println(" 6) Add/Delete Actor/Movie/Series to/from System");
        System.out.println(" 7) Solve a Request");
        System.out.println(" 8) Update Production Details");
        System.out.println(" 9) Update Actor Details");
        System.out.println("10) Add/Delete User to/from System");
        System.out.println("11) Logout");
        System.out.println("12) Exit");
    }

    private void RegularUser() {
        printRegularOptions();

        int option = ReadInput.readOption(9);

        while (option < 8) {

            switch (option) {
                case 1 -> CLI.productionDetails();

                case 2 -> CLI.ActorDetails();

                case 3 -> CLI.viewNotifications();

                case 4 -> {
                    System.out.println("Type the Name/Title of an Actor/Movie/Series.");
                    NameField item = ManageLists.search(ReadInput.readString());

                    if (item == null)
                        System.out.println("This is not the Name of an item from our System.");
                    else if (item instanceof Production p) System.out.println(p.displayInfo());
                    else if (item instanceof Actor a) System.out.println(a.displayInfo());
                }

                case 5 -> CLI.manageFavorites();

                case 6 -> CLI.manageRequests();

                case 7 -> CLI.rateItem();
            }

            printOptions();
            printRegularOptions();
            option = ReadInput.readOption(9);
        }

        if (option == 8) {
            loggedInUser = null;
            new InitApp();
        }
    }

    private void ContributorUser() {
        printContributorOptions();

        int option = ReadInput.readOption(12);

        while (option < 11) {

            switch (option) {
                case 1 -> CLI.productionDetails();

                case 2 -> CLI.ActorDetails();

                case 3 -> CLI.viewNotifications();

                case 4 -> {
                    System.out.println("Type the Name/Title of an Actor/Movie/Series.");
                    NameField item = ManageLists.search(ReadInput.readString());

                    if (item == null)
                        System.out.println("This is not the Name of an item from our System.");
                    else if (item instanceof Production p) System.out.println(p.displayInfo());
                    else if (item instanceof Actor a) System.out.println(a.displayInfo());
                }

                case 5 -> CLI.manageFavorites();

                case 6 -> CLI.manageRequests();

                case 7 -> CLI.manageSystem();

                case 8 -> CLI.solveRequest();

                case 9 -> CLI.updateProduction();

                case 10 -> CLI.updateActor();
            }

            printOptions();
            printContributorOptions();
            option = ReadInput.readOption(12);
        }

        if (option == 11) {
            loggedInUser = null;
            new InitApp();
        }
    }

    private void AdminUser() {
        printAdminOptions();

        int option = ReadInput.readOption(12);

        while (option < 11) {

            switch (option) {
                case 1 -> CLI.productionDetails();

                case 2 -> CLI.ActorDetails();

                case 3 -> CLI.viewNotifications();

                case 4 -> {
                    System.out.println("Type the Name/Title of an Actor/Movie/Series.");
                    NameField item = ManageLists.search(ReadInput.readString());

                    if (item == null)
                        System.out.println("This is not the Name of an item from our System.");
                    else if (item instanceof Production p) System.out.println(p.displayInfo());
                    else if (item instanceof Actor a) System.out.println(a.displayInfo());
                }

                case 5 -> CLI.manageFavorites();

                case 6 -> CLI.manageSystem();

                case 7 -> CLI.solveRequest();

                case 8 -> CLI.updateProduction();

                case 9 -> CLI.updateActor();

                case 10 -> CLI.manageUsers();
            }

            printOptions();
            printAdminOptions();
            option = ReadInput.readOption(12);
        }

        if (option == 11) {
            loggedInUser = null;
            new InitApp();
        }
    }

}