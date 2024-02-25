package org.example.users;

import org.example.IMDB;
import org.example.enums.RequestType;
import org.example.interfaces.NameField;
import org.example.interfaces.Observer;
import org.example.interfaces.RequestsManager;
import org.example.productions.Actor;
import org.example.productions.Production;
import org.example.productions.Rating;
import org.example.requests.Request;
import org.example.requests.RequestsHolder;
import org.example.strategies.RatingStrategy;
import org.example.utils.ManageLists;
import org.example.utils.RatingsComparator;

import java.util.ArrayList;
import java.util.List;

public class Regular extends User<NameField> implements RequestsManager {
    private final List<Request> createdRequests;
    private final List<NameField> createdRatings;

    public Regular(String username, String experience) {
        super(username, experience);
        createdRequests = new ArrayList<>();
        createdRatings = new ArrayList<>();
    }


    @Override
    public List<Request> getCreatedRequests() {
        return createdRequests;
    }

    public List<NameField> getCreatedRatings() {
        return createdRatings;
    }

    @Override
    public void createRequest(Request r) {
        boolean toWasSet = false;

        switch (r.getType()) {
            case ACTOR_ISSUE -> {
                Staff<NameField> contributor = ManageLists.findContributor(r.getActorName());
                if (contributor != null) {
                    r.setTo(contributor.getUsername());
                    toWasSet = true;
                    contributor.addRequest(r);

                    r.addObserver(contributor);
                    r.notifyObservers();
                }
            }

            case MOVIE_ISSUE -> {
                Staff<NameField> contributor = ManageLists.findContributor(r.getMovieTitle());
                if (contributor != null) {
                    r.setTo(contributor.getUsername());
                    toWasSet = true;
                    contributor.addRequest(r);

                    r.addObserver(contributor);
                    r.notifyObservers();
                }
            }
        }

        if (!toWasSet) {
            RequestsHolder.addAdminRequest(r);
        }

        r.addObserver((Observer) ManageLists.searchInList(IMDB.getInstance().getUsers(), r.getUsername()));

        createdRequests.add(r);
        IMDB.getInstance().getRequests().add(r);
    }

    @Override
    public void removeRequest(Request r) {
        if (r.getType().equals(RequestType.DELETE_ACCOUNT) || r.getType().equals(RequestType.OTHERS)) {
            RequestsHolder.adminRequests.remove(r);

        } else {
            NameField nameField = ManageLists.searchInList(IMDB.getInstance().getUsers(), r.getTo());
            if (nameField != null) {
                Staff<NameField> staff = (Staff<NameField>) nameField;
                staff.getRequests().remove(r);
            }
        }
        createdRequests.remove(r);
    }

    public void addRatingToProduction(Rating rating, Production production) {
        production.getRatings().add(rating);
        production.getRatings().sort(new RatingsComparator());
        production.calculateAverageRating();

        if (!createdRatings.contains(production)) {
            createdRatings.add(production);
            addExperience(new RatingStrategy());
        }

        production.notifyObservers();
        production.addObserver(this);
    }

    public void addRatingToActor(Rating rating, Actor actor) {
        actor.getRatings().add(rating);
        actor.getRatings().sort(new RatingsComparator());
        actor.calculateAverageRating();

        if (!createdRatings.contains(actor)) {
            createdRatings.add(actor);
            addExperience(new RatingStrategy());
        }

        actor.notifyObservers();
        actor.addObserver(this);
    }
}