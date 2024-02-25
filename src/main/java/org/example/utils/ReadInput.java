package org.example.utils;

import org.example.IMDB;
import org.example.enums.*;
import org.example.interfaces.NameField;
import org.example.interfaces.Subject;
import org.example.productions.*;
import org.example.requests.*;
import org.example.users.*;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReadInput {

    // Parses a json file and returns a JSONArray object
    private static JSONArray readInput(String location) {
        JSONArray jsonArray;
        JSONParser parser = new JSONParser();

        try {
            jsonArray = (JSONArray) parser.parse(new FileReader(location));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        return jsonArray;
    }

    public static void readActors() {
        JSONArray actorsArray = readInput("src/main/resources/input/actors.json");

        for (Object actorObj : actorsArray) {
            JSONObject actorJson = (JSONObject) actorObj;

            String name = (String) actorJson.get("name");
            String biography = (String) actorJson.get("biography");
            JSONArray performancesArray = (JSONArray) actorJson.get("performances");

            Actor actor = new Actor(name, biography);

            for (Object performanceObj : performancesArray) {
                JSONObject performance = (JSONObject) performanceObj;

                String title = (String) performance.get("title");
                String type = (String) performance.get("type");

                actor.addPerformance(title, type);
            }

            IMDB.getInstance().getActors().add(actor);
        }
    }

    public static void readProductions() {
        JSONArray productionsArray = readInput("src/main/resources/input/production.json");

        for (Object productionObj : productionsArray) {
            Production production;
            JSONObject productionJson = (JSONObject) productionObj;

            String title = (String) productionJson.get("title");
            String type = (String) productionJson.get("type");

            JSONArray directorsArray = (JSONArray) productionJson.get("directors");
            JSONArray actorsArray = (JSONArray) productionJson.get("actors");
            JSONArray genresArray = (JSONArray) productionJson.get("genres");
            JSONArray ratingsArray = (JSONArray) productionJson.get("ratings");

            String plot = (String) productionJson.get("plot");
            double averageRating = (double) productionJson.get("averageRating");
            int releaseYear = productionJson.get("releaseYear") != null ?
                    (int) (long) productionJson.get("releaseYear") : 0;

            if (type.equals("Movie")) {
                String duration = (String) productionJson.get("duration");
                production = new Movie(title, plot, averageRating, releaseYear, duration);

            } else {
                int numSeasons = (int) (long) productionJson.get("numSeasons");
                production = new Series(title, plot, averageRating, releaseYear, numSeasons);
            }

            for (Object directorObj : directorsArray) {
                String director = (String) directorObj;
                production.getDirectors().add(director);
            }

            for (Object actorObj : actorsArray) {
                String actor = (String) actorObj;
                production.getActors().add(actor);
            }

            for (Object genreObj : genresArray) {
                Genre genre = Genre.valueOf((String) genreObj);
                production.getGenres().add(genre);
            }

            for (Object ratingObj : ratingsArray) {
                JSONObject ratingJson = (JSONObject) ratingObj;

                String username = (String) ratingJson.get("username");
                int rating = (int) (long) ratingJson.get("rating");
                String comment = (String) ratingJson.get("comment");

                production.getRatings().add(new Rating(username, rating, comment));
            }

            if (type.equals("Movie")) {
                IMDB.getInstance().getProductions().add(production);

            } else {
                Series series = (Series) production;

                JSONObject seasons = (JSONObject) productionJson.get("seasons");
                for (int i = 1; i <= series.getNumSeasons(); i++) {
                    String current = "Season " + i;
                    series.addSeason(current);

                    JSONArray seasonArray = (JSONArray) seasons.get(current);
                    for (Object episodeObj : seasonArray) {
                        JSONObject episodeJson = (JSONObject) episodeObj;

                        String episodeName = (String) episodeJson.get("episodeName");
                        String duration = (String) episodeJson.get("duration");

                        series.addEpisode(current, episodeName, duration);
                    }
                }

                IMDB.getInstance().getProductions().add(series);
            }
        }
    }

    public static void readRequests() {
        JSONArray requestsArray = readInput("src/main/resources/input/requests.json");

        for (Object requestObj : requestsArray) {
            JSONObject requestJson = (JSONObject) requestObj;

            RequestType type = RequestType.valueOf((String) requestJson.get("type"));
            String username = (String) requestJson.get("username");
            String to = (String) requestJson.get("to");
            String description = (String) requestJson.get("description");
            String date = (String) requestJson.get("createdDate");

            LocalDateTime createdDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);

            Request request = new Request(type, createdDate, description, username);
            request.setTo(to);

            if (type.equals(RequestType.ACTOR_ISSUE)) {
                String actorName = (String) requestJson.get("actorName");
                request.setActorName(actorName);

                Staff<NameField> contributor = ManageLists.findContributor(actorName);
                if (contributor != null) {
                    contributor.addRequest(request);
                }

                request.notifyObservers();
                request.addObserver(contributor);

            } else if (type.equals(RequestType.MOVIE_ISSUE)) {
                String movieTitle = (String) requestJson.get("movieTitle");
                request.setMovieTitle(movieTitle);

                Staff<NameField> contributor = ManageLists.findContributor(movieTitle);
                if (contributor != null) {
                    contributor.addRequest(request);
                }

                request.notifyObservers();
                request.addObserver(contributor);

            } else {
                RequestsHolder.addAdminRequest(request);
            }

            User<NameField> user = (User<NameField>) ManageLists.searchInList(IMDB.getInstance().getUsers(),
                    request.getUsername());
            ((Regular) user).getCreatedRequests().add(request);

            request.addObserver(user);

            IMDB.getInstance().getRequests().add(request);
        }
    }

    public static void readAccounts() {
        JSONArray accountsArray = readInput("src/main/resources/input/accounts.json");

        for (Object accountObj : accountsArray) {
            JSONObject accountJson = (JSONObject) accountObj;

            AccountType userType = AccountType.valueOf((String) accountJson.get("userType"));
            String username = (String) accountJson.get("username");
            String experience = (String) accountJson.get("experience");

            User<NameField> user = UserFactory.getUser(userType, username, experience);

            JSONObject information = (JSONObject) accountJson.get("information");

            JSONObject credentials = (JSONObject) information.get("credentials");
            String email = (String) credentials.get("email");
            String password = (String) credentials.get("password");

            String name = (String) information.get("name");
            String country = (String) information.get("country");
            int age = (int) (long) information.get("age");
            String gender = (String) information.get("gender");
            String date = (String) information.get("birthDate");

            LocalDate birthDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

            user.setInformation(new User.Information.InformationBuilder(email, password)
                    .name(name).country(country).age(age).gender(gender).birthDate(birthDate).build());

            if (accountJson.get("favoriteProductions") != null) {
                JSONArray favoriteProductions = (JSONArray) accountJson.get("favoriteProductions");

                for (Object favoriteProductionObj : favoriteProductions) {
                    String productionTitle = (String) favoriteProductionObj;

                    user.addFavorite(ManageLists.searchInList(IMDB.getInstance().getProductions(), productionTitle));
                }
            }

            if (accountJson.get("favoriteActors") != null) {
                JSONArray favoriteActors = (JSONArray) accountJson.get("favoriteActors");

                for (Object favoriteActorObj : favoriteActors) {
                    String ActorName = (String) favoriteActorObj;

                    user.addFavorite(ManageLists.searchInList(IMDB.getInstance().getActors(), ActorName));
                }
            }

            if (userType.equals(AccountType.Regular)) {
                IMDB.getInstance().getUsers().add(user);

            } else {
                Staff<NameField> staff = (Staff<NameField>) user;

                if (accountJson.get("productionsContribution") != null) {
                    JSONArray productionsContributions = (JSONArray) accountJson.get("productionsContribution");

                    for (Object productionsContributionObj : productionsContributions) {
                        String productionTitle = (String) productionsContributionObj;

                        staff.addContribution(ManageLists.searchInList(IMDB.getInstance().getProductions(),
                                productionTitle));

                        ((Subject) ManageLists.searchInList(IMDB.getInstance().getProductions(),
                                productionTitle)).addObserver(staff);
                    }
                }

                if (accountJson.get("actorsContribution") != null) {
                    JSONArray actorsContributions = (JSONArray) accountJson.get("actorsContribution");

                    for (Object actorsContributionObj : actorsContributions) {
                        String ActorName = (String) actorsContributionObj;

                        staff.addContribution(ManageLists.searchInList(IMDB.getInstance().getActors(), ActorName));

                        ((Subject) ManageLists.searchInList(IMDB.getInstance().getActors(),
                                ActorName)).addObserver(staff);
                    }
                }

                if (userType.equals(AccountType.Admin))
                    RequestsHolder.admins.add((Admin) staff);

                IMDB.getInstance().getUsers().add(staff);
            }
        }

        ManageLists.fixProductionRatingsList();
    }

    // Reads an integer from System.in between 1 and max, or with no bounds if max=-1
    public static int readOption(int max) {
        int number;
        while (true) {
            if (IMDB.scanner.hasNextInt()) {
                number = IMDB.scanner.nextInt();

                if ((number >= 1 && number <= max) || max == -1) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and " + max + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                IMDB.scanner.nextLine();
            }
        }

        IMDB.scanner.nextLine();
        return number;
    }

    // Reads a non-empty string from System.in
    public static String readString() {
        String input;
        do {
            input = IMDB.scanner.nextLine().trim();
        } while (input.isEmpty());

        return input;
    }
}