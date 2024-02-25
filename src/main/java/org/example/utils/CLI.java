package org.example.utils;

import org.example.IMDB;
import org.example.enums.*;
import org.example.interfaces.NameField;
import org.example.interfaces.RequestsManager;
import org.example.productions.*;
import org.example.requests.*;
import org.example.users.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CLI {

    // Asks for a filter then prints the details of the chosen production
    public static void productionDetails() {
        System.out.println("Choose a filter:");
        System.out.println("1) No filter");
        System.out.println("2) Genre");
        System.out.println("3) Number of Ratings");
        System.out.println("4) Go back");

        int option = ReadInput.readOption(4);
        switch (option) {
            case 1 -> {
                for (int i = 0; i < IMDB.getInstance().getProductions().size(); i++) {
                    System.out.println((i + 1) + ") " + IMDB.getInstance().getProductions().get(i).getTitle());
                }

                System.out.println("Choose a Production to see all its Details.");
                option = ReadInput.readOption(IMDB.getInstance().getProductions().size());
                System.out.println(IMDB.getInstance().getProductions().get(option - 1).displayInfo());
            }

            case 2 -> {
                for (Genre genre : Genre.values()) {
                    System.out.println((genre.ordinal() + 1) + ") " + genre);
                }

                System.out.println("Choose a Genre.");
                option = ReadInput.readOption(Genre.values().length);
                Genre genre = Genre.values()[option - 1];

                List<Production> genreList = new ArrayList<>();

                for (Production production : IMDB.getInstance().getProductions()) {
                    if (production.getGenres().contains(genre)) genreList.add(production);
                }

                if (!genreList.isEmpty()) {
                    for (int i = 0; i < genreList.size(); i++) {
                        System.out.println((i + 1) + ") " + genreList.get(i).getTitle());
                    }

                    System.out.println("Choose a Production to see all its Details.");
                    option = ReadInput.readOption(genreList.size());
                    System.out.println(genreList.get(option - 1).displayInfo());
                }
            }
            case 3 -> {
                System.out.println("Choose the number of Ratings.");

                option = ReadInput.readOption(-1);

                List<Production> numRatingsList = new ArrayList<>();

                for (Production production : IMDB.getInstance().getProductions()) {
                    if (production.getRatings().size() == option) numRatingsList.add(production);
                }

                if (!numRatingsList.isEmpty()) {
                    for (int i = 0; i < numRatingsList.size(); i++) {
                        System.out.println((i + 1) + ") " + numRatingsList.get(i).getTitle());
                    }

                    System.out.println("Choose a Production to see all its Details.");
                    option = ReadInput.readOption(numRatingsList.size());
                    System.out.println(numRatingsList.get(option - 1).displayInfo());
                }
            }
        }
    }

    // Prints the details of an actor who is chosen from an alphabetical list
    public static void ActorDetails() {
        List<Actor> alphabeticalList = new ArrayList<>(IMDB.getInstance().getActors());
        Collections.sort(alphabeticalList);

        for (int i = 0; i < alphabeticalList.size(); i++) {
            System.out.println((i + 1) + ") " + alphabeticalList.get(i).getName());
        }

        System.out.println("Choose an Actor to see all its Details.");
        int option = ReadInput.readOption(alphabeticalList.size());
        System.out.println(alphabeticalList.get(option - 1).displayInfo());
    }

    public static void viewNotifications() {
        System.out.println(IMDB.loggedInUser.getNotifications().toString().replace("[", "")
                .replace("]", "").replace(",", "\n"));

        if (!IMDB.loggedInUser.getNotifications().isEmpty()) {
            System.out.println("Would you like to Delete all Notifications?");
            System.out.println("1) Yes");
            System.out.println("2) No");

            int option = ReadInput.readOption(2);

            if (option == 1) IMDB.loggedInUser.getNotifications().clear();
        }
    }

    // Method used to manage the list of favorites items for the logged-in user
    public static void manageFavorites() {
        System.out.println("Your Favorite items:");
        for (NameField item : IMDB.loggedInUser.getFavorites()) {
            if (ManageLists.search(item.getComparableValue()) != null)
                System.out.println(item.getComparableValue());
        }

        System.out.println("Choose what you want to do:");
        System.out.println("1) Add an item to my Favorites list");
        System.out.println("2) Remove an item from my Favorites list");
        System.out.println("3) Go back");

        int option = ReadInput.readOption(3);

        switch (option) {
            case 1 -> {
                int i, size = IMDB.getInstance().getProductions().size();

                for (i = 0; i < size; i++) {
                    System.out.println((i + 1) + ") " + IMDB.getInstance().getProductions().get(i).getTitle());
                }

                size += IMDB.getInstance().getActors().size();

                for (int j = i; j < size; j++) {
                    System.out.println((j + 1) + ") " + IMDB.getInstance().getActors().get(j - i).getName());
                }

                System.out.println((size + 1) + ") Go back");

                System.out.println("Choose an item to Add to your Favorites list.");

                option = ReadInput.readOption(size + 1);

                if (option != size + 1) {
                    NameField item;

                    if (IMDB.getInstance().getProductions().size() > option)
                        item = IMDB.getInstance().getProductions().get(option - 1);
                    else
                        item = IMDB.getInstance().getActors()
                                .get(option - IMDB.getInstance().getProductions().size() - 1);

                    if (!IMDB.loggedInUser.getFavorites().contains(item))
                        IMDB.loggedInUser.addFavorite(item);
                    else System.out.println("This item is already in your Favorites list.");
                }
            }

            case 2 -> {
                int index = 1;
                List<NameField> favorites = new ArrayList<>();

                for (NameField element : IMDB.loggedInUser.getFavorites()) {
                    System.out.println(index + ") " + element.getComparableValue());
                    favorites.add(element);
                    index++;
                }

                if (index != 1) {
                    System.out.println("Choose an item to Remove from your Favorites list.");
                    option = ReadInput.readOption(favorites.size());

                    IMDB.loggedInUser.removeFavorite(favorites.get(option - 1));
                }
            }
        }
    }

    // Used by regular or contributor users to create or delete a request
    public static void manageRequests() {
        RequestsManager user = (RequestsManager) IMDB.loggedInUser;

        System.out.println("Choose what you want to do:");
        System.out.println("1) Create a Request");
        System.out.println("2) Delete a Request");
        System.out.println("3) Go back");

        int option = ReadInput.readOption(3);

        switch (option) {
            case 1 -> {
                System.out.println("Choose a Request Type:");
                for (RequestType requestType : RequestType.values()) {
                    System.out.println((requestType.ordinal() + 1) + ") " + requestType);
                }

                option = ReadInput.readOption(RequestType.values().length);
                RequestType requestType = RequestType.values()[option - 1];

                System.out.println("Write a Description for your Request.");
                String description = ReadInput.readString();

                Request request = new Request(requestType, LocalDateTime.now(), description,
                        IMDB.loggedInUser.getUsername());

                if (requestType.equals(RequestType.MOVIE_ISSUE)) {
                    System.out.println("Type the Title of the Production.");
                    String movie = ReadInput.readString();

                    if (ManageLists.searchInList(IMDB.getInstance().getProductions(), movie) == null) {
                        System.out.println("This Production does not exist in our System.");
                        break;
                    }

                    if (ManageLists.findContributor(movie) != IMDB.loggedInUser)
                        request.setMovieTitle(movie);

                    else {
                        System.out.println("You can't make a Request for your own Contribution.");
                        break;
                    }

                } else if (requestType.equals(RequestType.ACTOR_ISSUE)) {
                    System.out.println("Type the Name of the Actor.");
                    String actor = ReadInput.readString();

                    if (ManageLists.searchInList(IMDB.getInstance().getActors(), actor) == null) {
                        System.out.println("This Actor does not exist in our System.");
                        break;
                    }

                    if (ManageLists.findContributor(actor) != IMDB.loggedInUser)
                        request.setActorName(actor);

                    else {
                        System.out.println("You can't make a Request for your own Contribution.");
                        break;
                    }
                }

                user.createRequest(request);
            }

            case 2 -> {
                if (!user.getCreatedRequests().isEmpty()) {
                    for (int i = 0; i < user.getCreatedRequests().size(); i++) {
                        System.out.println((i + 1) + ") " + user.getCreatedRequests().get(i).getType() + " - " +
                                user.getCreatedRequests().get(i).getDescription());
                    }

                    System.out.println("Choose a Request to Remove.");
                    option = ReadInput.readOption(user.getCreatedRequests().size());
                    user.removeRequest(user.getCreatedRequests().get(option - 1));
                }
            }
        }
    }

    // Used by regular users to rate an Actor/Movie/Series
    public static void rateItem() {
        int i, size = IMDB.getInstance().getProductions().size();

        for (i = 0; i < size; i++) {
            System.out.println((i + 1) + ") " + IMDB.getInstance().getProductions().get(i).getTitle());
        }

        size += IMDB.getInstance().getActors().size();

        for (int j = i; j < size; j++) {
            System.out.println((j + 1) + ") " + IMDB.getInstance().getActors().get(j - i).getName());
        }

        System.out.println((size + 1) + ") Go back");

        System.out.println("Choose an item to Rate.");

        int option = ReadInput.readOption(size + 1);

        if (option != size + 1) {
            NameField item;

            if (IMDB.getInstance().getProductions().size() > option)
                item = IMDB.getInstance().getProductions().get(option - 1);
            else
                item = IMDB.getInstance().getActors()
                        .get(option - IMDB.getInstance().getProductions().size() - 1);

            System.out.println("Choose a Rating.");
            int r = ReadInput.readOption(10);

            System.out.println("Write a Comment.");
            String comment = ReadInput.readString();

            Rating rating = new Rating(IMDB.loggedInUser.getUsername(), r, comment);

            if (item instanceof Production) ((Regular) IMDB.loggedInUser)
                    .addRatingToProduction(rating, (Production) item);
            else if (item instanceof Actor) ((Regular) IMDB.loggedInUser)
                    .addRatingToActor(rating, (Actor) item);
        }
    }

    // Used by Admins and Contributors to Add/Delete Actors/Movies/Series to the system
    public static void manageSystem() {
        System.out.println("Choose what you want to do:");
        System.out.println("1) Add a Production to the System");
        System.out.println("2) Add an Actor to the System");
        System.out.println("3) Remove a Production from the System");
        System.out.println("4) Remove an Actor from the System");
        System.out.println("5) Go back");

        int option = ReadInput.readOption(5);

        switch (option) {
            case 1 -> {
                System.out.println("Type the title of the Production to be Added.");
                String title = ReadInput.readString();

                System.out.println("Write the Plot of the Production.");
                String plot = ReadInput.readString();

                System.out.println("Type the Release Year.");
                int releaseYear = ReadInput.readOption(2024);

                System.out.println("Is this Production a Movie or a Series?");
                System.out.println("1) Movie");
                System.out.println("2) Series");

                option = ReadInput.readOption(2);

                Production p;

                if (option == 1) {
                    System.out.println("Type the Duration of the Movie.");
                    String duration = ReadInput.readString();

                    p = new Movie(title, plot, 0, releaseYear, duration);

                } else {
                    System.out.println("Type the number of Seasons of the Series.");
                    int numSeasons = ReadInput.readOption(-1);

                    p = new Series(title, plot, 0, releaseYear, numSeasons);
                }

                ((Staff<NameField>) IMDB.loggedInUser).addProductionSystem(p);
            }

            case 2 -> {
                System.out.println("Type the Name of the Actor to be Added.");
                String name = ReadInput.readString();

                System.out.println("Write the Biography of the Actor.");
                String biography = ReadInput.readString();

                Actor a = new Actor(name, biography);
                ((Staff<NameField>) IMDB.loggedInUser).addActorSystem(a);
            }

            case 3 -> {
                int index = 1;

                List<Production> productions = new ArrayList<>();

                for (NameField element : ((Staff<NameField>) IMDB.loggedInUser).getContributions()) {
                    if (element instanceof Production p) {
                        System.out.println(index + ") " + p.getTitle());
                        productions.add(p);
                        index++;
                    }
                }

                if (IMDB.loggedInUser instanceof Admin) {
                    for (NameField element : RequestsHolder.adminContributions) {
                        if (element instanceof Production p) {
                            System.out.println(index + ") " + p.getTitle());
                            productions.add(p);
                            index++;
                        }
                    }
                }

                if (index != 1) {
                    System.out.println("Choose a Production to Remove.");
                    int choice = ReadInput.readOption(index - 1);

                    ((Staff<NameField>) IMDB.loggedInUser).removeProductionSystem(productions
                            .get(choice - 1).getTitle());
                }
            }

            case 4 -> {
                int index = 1;

                List<Actor> actors = new ArrayList<>();

                for (NameField element : ((Staff<NameField>) IMDB.loggedInUser).getContributions()) {
                    if (element instanceof Actor a) {
                        System.out.println(index + ") " + a.getName());
                        actors.add(a);
                        index++;
                    }
                }

                if (IMDB.loggedInUser instanceof Admin) {
                    for (NameField element : RequestsHolder.adminContributions) {
                        if (element instanceof Actor a) {
                            System.out.println(index + ") " + a.getName());
                            actors.add(a);
                            index++;
                        }
                    }
                }

                if (index != 1) {
                    System.out.println("Choose an Actor to Remove.");
                    int choice = ReadInput.readOption(index - 1);

                    ((Staff<NameField>) IMDB.loggedInUser).removeActorSystem(actors
                            .get(choice - 1).getName());
                }
            }
        }
    }

    // Used by Admins and Contributors to view and manage the requests they have to solve/reject
    public static void solveRequest() {
        int i, size = ((Staff<NameField>) IMDB.loggedInUser).getRequests().size();

        for (i = 0; i < size; i++) {
            System.out.println((i + 1) + ") " + ((Staff<NameField>) IMDB.loggedInUser).getRequests().get(i));
        }

        if (IMDB.loggedInUser instanceof Admin) {
            size += RequestsHolder.adminRequests.size();

            for (int j = i; j < size; j++) {
                System.out.println((j + 1) + ") " + RequestsHolder.adminRequests.get(j - i));
            }
        }

        if (size != 0) {
            System.out.println(size + 1 + ") Go back");

            System.out.println("Choose a Request to mark as Solved/Rejected.");
            int choice = ReadInput.readOption(size + 1);

            if (choice != size + 1) {
                System.out.println("Choose what you want to do:");
                System.out.println("1) Mark the Request as Solved");
                System.out.println("2) Reject the Request");
                System.out.println("3) Go back");

                int option = ReadInput.readOption(3);

                if (((Staff<NameField>) IMDB.loggedInUser).getRequests().size() > choice) {
                    ((Staff<NameField>) IMDB.loggedInUser).getRequests().get(choice - 1).setSolved(option == 1);

                    ((Staff<NameField>) IMDB.loggedInUser).removeRequest
                            (((Staff<NameField>) IMDB.loggedInUser).getRequests().get(choice - 1));

                } else {
                    size -= ((Staff<NameField>) IMDB.loggedInUser).getRequests().size();

                    RequestsHolder.adminRequests.get(choice - size - 1).setSolved(option == 1);

                    RequestsHolder.removeAdminRequest(RequestsHolder.adminRequests.get(choice - size - 1));

                }
            }
        }
    }

    // Used to modify the details of a production
    public static void updateProduction() {
        int index = 1;

        List<Production> productions = new ArrayList<>();

        for (NameField element : ((Staff<NameField>) IMDB.loggedInUser).getContributions()) {
            if (element instanceof Production p) {
                System.out.println(index + ") " + p.getTitle());
                productions.add(p);
                index++;
            }
        }

        if (IMDB.loggedInUser instanceof Admin) {
            for (NameField element : RequestsHolder.adminContributions) {
                if (element instanceof Production p) {
                    System.out.println(index + ") " + p.getTitle());
                    productions.add(p);
                    index++;
                }
            }
        }

        System.out.println(index + ") Go back");

        System.out.println("Choose a Production to Update.");
        int choice = ReadInput.readOption(index);

        Production p = productions.get(choice - 1);

        System.out.println("Choose what you want to Update:");
        System.out.println(" 1) Update the Title");
        System.out.println(" 2) Add a Director");
        System.out.println(" 3) Remove a Director");
        System.out.println(" 4) Add an Actor");
        System.out.println(" 5) Remove an Actor");
        System.out.println(" 6) Add a Genre");
        System.out.println(" 7) Remove a Genre");
        System.out.println(" 8) Update the Plot");
        System.out.println(" 9) Update the Release Year");

        int option = ReadInput.readOption(9);

        switch (option) {
            case 1 -> {
                System.out.println("Write the new Title.");
                p.setTitle(ReadInput.readString());
            }
            case 2 -> {
                System.out.println("Write the Name of the new Director.");
                p.getDirectors().add(ReadInput.readString());
            }
            case 3 -> {
                for (int i = 0; i < p.getDirectors().size(); i++) {
                    System.out.println((i + 1) + ") " + p.getDirectors().get(i));
                }

                if (!p.getDirectors().isEmpty()) {
                    System.out.println("Choose a Director to Remove.");
                    option = ReadInput.readOption(p.getDirectors().size());
                    p.getDirectors().remove(option - 1);
                }
            }
            case 4 -> {
                System.out.println("Write the Name of the new Actor.");
                p.getActors().add(ReadInput.readString());
            }
            case 5 -> {
                for (int i = 0; i < p.getActors().size(); i++) {
                    System.out.println((i + 1) + ") " + p.getActors().get(i));
                }

                if (!p.getActors().isEmpty()) {
                    System.out.println("Choose an Actor to Remove.");
                    option = ReadInput.readOption(p.getActors().size());
                    p.getActors().remove(option - 1);
                }
            }
            case 6 -> {
                for (Genre genre : Genre.values()) {
                    System.out.println((genre.ordinal() + 1) + ") " + genre);
                }

                System.out.println("Choose a Genre to Add.");
                option = ReadInput.readOption(Genre.values().length);
                Genre genre = Genre.values()[option - 1];

                if (!p.getGenres().contains(genre)) p.getGenres().add(genre);
            }
            case 7 -> {
                for (int i = 0; i < p.getGenres().size(); i++) {
                    System.out.println((i + 1) + ") " + p.getGenres().get(i));
                }

                if (!p.getGenres().isEmpty()) {
                    System.out.println("Choose a Genre to Remove.");
                    option = ReadInput.readOption(p.getGenres().size());
                    p.getGenres().remove(option - 1);
                }
            }
            case 8 -> {
                System.out.println("Write the new Plot.");
                p.setPlot(ReadInput.readString());
            }
            case 9 -> {
                System.out.println("Type the new Release Year.");
                p.setReleaseYear(ReadInput.readOption(2024));
            }
        }
    }

    // Used to modify the details of an actor
    public static void updateActor() {
        int index = 1;

        List<Actor> actors = new ArrayList<>();

        for (NameField element : ((Staff<NameField>) IMDB.loggedInUser).getContributions()) {
            if (element instanceof Actor a) {
                System.out.println(index + ") " + a.getName());
                actors.add(a);
                index++;
            }
        }

        if (IMDB.loggedInUser instanceof Admin) {
            for (NameField element : RequestsHolder.adminContributions) {
                if (element instanceof Actor a) {
                    System.out.println(index + ") " + a.getName());
                    actors.add(a);
                    index++;
                }
            }
        }

        System.out.println(index + ") Go back");

        System.out.println("Choose an Actor to Update.");
        int choice = ReadInput.readOption(index);

        Actor a = actors.get(choice - 1);

        System.out.println("Choose what you want to update:");
        System.out.println(" 1) Update the Name");
        System.out.println(" 2) Add a Performance");
        System.out.println(" 3) Remove a Performance");
        System.out.println(" 4) Update the Biography");

        int option = ReadInput.readOption(4);

        switch (option) {
            case 1 -> {
                System.out.println("Write the new Name.");
                a.setName(ReadInput.readString());
            }
            case 2 -> {
                System.out.println("Write the Title of the Performance.");
                String title = ReadInput.readString();
                System.out.println("Is it a Movie or a Series?");
                System.out.println("1) Movie");
                System.out.println("2) Series");
                option = ReadInput.readOption(2);

                String type;
                if (option == 1) type = "Movie";
                else type = "Series";

                a.addPerformance(title, type);
            }
            case 3 -> {
                for (int i = 0; i < a.getPerformances().size(); i++) {
                    System.out.println((i + 1) + ") " + a.getPerformances().get(i).getTitle() + " - " +
                            a.getPerformances().get(i).getType());
                }

                if (!a.getPerformances().isEmpty()) {
                    System.out.println("Choose a Performance to Remove.");
                    option = ReadInput.readOption(a.getPerformances().size());
                    a.getPerformances().remove(option - 1);
                }
            }
            case 4 -> {
                System.out.println("Write the new Biography.");
                a.setBiography(ReadInput.readString());
            }
        }
    }

    // Used by admins to add and delete users from the system
    public static void manageUsers() {
        System.out.println("Choose what you want to do:");
        System.out.println(" 1) Add a new User");
        System.out.println(" 2) Remove a User");
        System.out.println("3) Go back");

        int option = ReadInput.readOption(3);

        switch (option) {
            case 1 -> {
                for (AccountType type : AccountType.values()) {
                    System.out.println((type.ordinal() + 1) + ") " + type);
                }

                System.out.println("Choose the Account Type.");
                option = ReadInput.readOption(AccountType.values().length);
                AccountType accountType = AccountType.values()[option - 1];

                System.out.println("Enter the First and Last Name of the User.");
                String name = ReadInput.readString();

                String experience = null;
                if (!accountType.equals(AccountType.Admin)) experience = "0";

                User<NameField> user = UserFactory.getUser(accountType, ManageLists.
                        generateUsername(name), experience);

                System.out.println("Enter the Email.");
                String email = ReadInput.readString();

                System.out.println("Enter the Country.");
                String country = ReadInput.readString();

                System.out.println("Enter the Age.");
                int age = ReadInput.readOption(120);

                System.out.println("Enter the Gender.");
                String gender = ReadInput.readString();

                System.out.println("Enter the Birth Date (format: yyyy-mm-dd).");
                String date = ReadInput.readString();

                LocalDate birthDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

                user.setInformation(new User.Information.InformationBuilder(email, ManageLists.generatePassword())
                        .name(name).country(country).age(age).gender(gender).birthDate(birthDate).build());

                IMDB.getInstance().getUsers().add(user);

                if (accountType.equals(AccountType.Admin)) RequestsHolder.admins.add((Admin) user);
            }

            case 2 -> {
                for (int i = 0; i < IMDB.getInstance().getUsers().size(); i++) {
                    System.out.println((i + 1) + ") " + IMDB.getInstance().getUsers().get(i).getUsername());
                }

                System.out.println("Choose a User to Remove from the System.");
                option = ReadInput.readOption(IMDB.getInstance().getUsers().size());
                User<NameField> to_remove = IMDB.getInstance().getUsers().get(option - 1);

                if (to_remove instanceof Regular r) {
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

                } else if (to_remove instanceof Staff s) {
                    RequestsHolder.adminRequests.addAll(s.getRequests());
                    RequestsHolder.adminContributions.addAll(s.getContributions());

                    if (to_remove instanceof Contributor c) {
                        for (Request request : c.getCreatedRequests()) {
                            if (request.getTo().equalsIgnoreCase("ADMIN"))
                                RequestsHolder.adminRequests.remove(request);

                            else ((Staff<NameField>) ManageLists.searchInList(IMDB.getInstance().getUsers(),
                                    request.getTo())).getRequests().remove(request);
                        }

                    } else RequestsHolder.admins.remove(to_remove);
                }

                IMDB.getInstance().getUsers().remove(to_remove);
            }
        }
    }
}