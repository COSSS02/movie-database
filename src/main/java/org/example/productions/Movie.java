package org.example.productions;

public class Movie extends Production {
    private String duration;

    public Movie(String title, String plot, double averageRating, int releaseYear, String duration) {
        super(title, plot, averageRating, releaseYear);
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String displayInfo() {
        StringBuilder sb = new StringBuilder();

        if (getTitle() != null) {
            sb.append("Movie Title: ").append(getTitle()).append("\n");
        }

        if (!getDirectors().isEmpty()) {
            sb.append("Directors: ").append(getDirectors().toString()
                    .replace("[", "").replace("]", "")).append("\n");
        }


        if (!getActors().isEmpty()) {
            sb.append("Actors: ").append(getActors().toString()
                    .replace("[", "").replace("]", "")).append("\n");
        }


        if (!getGenres().isEmpty()) {
            sb.append("Genres: ").append(getGenres().toString()
                    .replace("[", "").replace("]", "")).append("\n");
        }


        if (!getRatings().isEmpty()) {
            sb.append("Ratings: ").append(getRatings().toString().replace("[", "")
                    .replace("]", "").replace(",", "\n")).append("\n");
        }


        if (getPlot() != null) {
            sb.append("Plot: ").append(getPlot()).append("\n");
        }

        if (getAverageRating() != 0) {
            sb.append("Average Rating: ").append(getAverageRating()).append("\n");
        }

        if (getReleaseYear() != 0) {
            sb.append("Release Year: ").append(getReleaseYear()).append("\n");
        }

        if (getDuration() != null) {
            sb.append("Duration: ").append(getDuration()).append("\n");
        }

        return sb.toString();
    }
}