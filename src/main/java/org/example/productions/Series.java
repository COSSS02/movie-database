package org.example.productions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Series extends Production {
    private int numSeasons;
    private final Map<String, List<Episode>> episodes;

    public Series(String title, String plot, double averageRating, int releaseYear, int numSeasons) {
        super(title, plot, averageRating, releaseYear);
        this.numSeasons = numSeasons;
        episodes = new HashMap<>(numSeasons);
    }

    public int getNumSeasons() {
        return numSeasons;
    }

    public void setNumSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }

    public Map<String, List<Episode>> getEpisodes() {
        return episodes;
    }

    public void addSeason(String season) {
        episodes.put(season, new ArrayList<>());
    }

    public void addEpisode(String season, String episodeName, String duration) {
        Episode episode = new Episode(episodeName, duration);

        if (episodes.containsKey(season)) episodes.get(season).add(episode);
    }

    @Override
    public String displayInfo() {
        StringBuilder sb = new StringBuilder();

        if (getTitle() != null) {
            sb.append("Series Title: ").append(getTitle()).append("\n");
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

        if (getNumSeasons() != 0) {
            sb.append("Number of seasons: ").append(getNumSeasons()).append("\n");

            for (int i = 1; i <= getNumSeasons(); i++) {
                sb.append("Season ").append(i).append(": ");
                sb.append(getEpisodes().get("Season " + i).toString()
                        .replace("[", "").replace("]", "")).append("\n");
            }
        }

        return sb.toString();
    }
}