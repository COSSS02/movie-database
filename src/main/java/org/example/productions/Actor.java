package org.example.productions;

import org.example.interfaces.NameField;
import org.example.interfaces.Observer;
import org.example.interfaces.Subject;
import org.example.users.Admin;
import org.example.users.Contributor;
import org.example.users.Regular;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Actor implements NameField, Subject {
    private String name;
    private final List<Pair> performances;
    private String biography;
    private final List<Rating> ratings;
    private double averageRating;
    private final List<Observer> observers;

    public Actor() {
        performances = new ArrayList<>();
        ratings = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public Actor(String name, String biography) {
        this();
        this.name = name;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<Pair> getPerformances() {
        return performances;
    }

    public void addPerformance(String title, String type) {
        performances.add(new Pair(title, type));
    }

    public List<Rating> getRatings() {
        return ratings;
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

    @Override
    public String getComparableValue() {
        return getName();
    }

    @Override
    public int compareTo(@NotNull NameField o) {
        return name.compareTo(o.getComparableValue());
    }

    public String displayInfo() {
        StringBuilder sb = new StringBuilder();

        if (name != null) {
            sb.append("Name: ").append(name).append("\n");
        }

        if (!performances.isEmpty()) {
            sb.append("Performances:\n");
            for (Pair performance : performances) {
                if (performance.getTitle() != null && performance.getType() != null) {
                    sb.append(performance.getTitle()).append(" - ").append(performance.getType()).append("\n");
                }
            }
        }

        if (biography != null) {
            sb.append("Biography: ").append(biography).append("\n");
        }

        if (!ratings.isEmpty()) {
            sb.append("Ratings: ").append(getRatings().toString().replace("[", "")
                    .replace("]", "").replace(",", "\n")).append("\n");
        }

        if (averageRating != 0) {
            sb.append("Average Rating: ").append(averageRating).append("\n");
        }

        return sb.toString();
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
            observer.update(name + " just received a rating from " + rating.getUsername() +
                    ": \"" + rating.getComment() + "\" -> " + rating.getRating());
    }

    public static class Pair {
        private String title;
        private String type;

        public Pair(String title, String type) {
            this.title = title;
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}