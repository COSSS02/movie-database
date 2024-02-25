package org.example.requests;

import org.example.IMDB;
import org.example.enums.RequestType;
import org.example.interfaces.NameField;
import org.example.interfaces.Observer;
import org.example.interfaces.Subject;
import org.example.strategies.RequestStrategy;
import org.example.users.Admin;
import org.example.users.Contributor;
import org.example.users.Regular;
import org.example.users.User;
import org.example.utils.ManageLists;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Request implements Subject {
    private RequestType type;
    private LocalDateTime createdDate;
    private String movieTitle;
    private String actorName;
    private String description;
    private String username;
    private String to;
    private boolean solved;
    private final List<Observer> observers;

    public Request() {
        observers = new ArrayList<>();
    }

    public Request(RequestType type, LocalDateTime createdDate, String description, String username) {
        this();
        this.type = type;
        this.createdDate = createdDate;
        this.description = description;
        this.username = username;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;

        // adds experience to the user whose request was solved
        if (solved)
            if (type.equals(RequestType.ACTOR_ISSUE) || type.equals(RequestType.MOVIE_ISSUE))
                ((User<NameField>) ManageLists.searchInList(IMDB.getInstance().getUsers(), username))
                        .addExperience(new RequestStrategy());

        // removes the request solver so that he doesn't get notified anymore
        User<NameField> c = (User<NameField>) ManageLists.searchInList(IMDB.getInstance().getUsers(), to);
        if (c instanceof Contributor) observers.remove(c);

        observers.removeIf(observer -> observer instanceof Admin);

        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        User<NameField> c1 = (User<NameField>) ManageLists.searchInList(IMDB.getInstance().getUsers(), username);
        User<NameField> c2 = (User<NameField>) ManageLists.searchInList(IMDB.getInstance().getUsers(), to);

        // notifies Contributor user if it created the request
        if (c1 instanceof Contributor) {
            if (solved) {
                c1.update("Your request '" + description + "' has been resolved!");
            } else {
                c1.update("Your request '" + description + "' has been rejected.");
            }
        }

        // notifies Contributor user if he has to solve the request
        if (c2 instanceof Contributor) {
            c2.update("A new request has been submitted: " + description);
        }

        //  notifies the other user types
        for (Observer observer : observers)
            if (observer instanceof Regular) {
                if (solved) {
                    observer.update("Your request '" + description + "' has been resolved!");
                } else {
                    observer.update("Your request '" + description + "' has been rejected.");
                }

            } else if (observer instanceof Admin) {
                observer.update("A new request has been submitted: " + description);
            }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request Type: ").append(type).append("\n");
        sb.append("Created Date: ").append(createdDate).append("\n");
        sb.append("Username: ").append(username).append("\n");

        if (movieTitle != null) {
            sb.append("Movie Title: ").append(movieTitle).append("\n");
        }

        if (actorName != null) {
            sb.append("Actor Name: ").append(actorName).append("\n");
        }

        sb.append("Description: ").append(description).append("\n");

        return sb.toString();
    }
}