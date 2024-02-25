package org.example.productions;

import org.example.interfaces.NameField;
import org.example.users.*;
import org.example.enums.Genre;
import org.example.interfaces.Observer;
import org.example.interfaces.Subject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Production implements NameField, Subject {
    private String title;
    private final List<String> directors;
    private final List<String> actors;
    private final List<Genre> genres;
    private final List<Rating> ratings;
    private String plot;
    private double averageRating;
    private int releaseYear;
    private final List<Observer> observers;


    public Production() {
        directors = new ArrayList<>();
        actors = new ArrayList<>();
        genres = new ArrayList<>();
        ratings = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public Production(String title, String plot, double averageRating, int releaseYear) {
        this();
        this.title = title;
        this.plot = plot;
        this.averageRating = averageRating;
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void calculateAverageRating() {
        if (ratings != null) {
            double sum = 0;
            for (Rating value : ratings) {
                sum += value.getRating();
            }

            averageRating = sum / ratings.size();
        }
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public String getComparableValue() {
        return getTitle();
    }

    @Override
    public int compareTo(@NotNull NameField o) {
        return title.compareTo(o.getComparableValue());
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
        Rating rating = ratings.get(ratings.size() - 1);

        for (Observer observer : observers)
            observer.update(title + " just received a rating from " + rating.getUsername() +
                    ": \"" + rating.getComment() + "\" -> " + rating.getRating());
    }

    public abstract String displayInfo();
}