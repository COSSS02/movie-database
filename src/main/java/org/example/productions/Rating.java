package org.example.productions;

import org.example.interfaces.NameField;
import org.jetbrains.annotations.NotNull;

public class Rating implements NameField {
    private String username;
    private int rating;
    private String comment;

    public Rating(String username, int rating, String comment) {
        this.username = username;
        setRating(rating);
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 10)
            this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return username + ": '" + comment + "' -> " + rating;
    }

    @Override
    public String getComparableValue() {
        return username;
    }

    @Override
    public int compareTo(@NotNull NameField o) {
        return username.compareTo(o.getComparableValue());
    }
}