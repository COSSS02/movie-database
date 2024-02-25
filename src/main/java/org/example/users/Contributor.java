package org.example.users;

import org.example.IMDB;
import org.example.enums.RequestType;
import org.example.interfaces.NameField;
import org.example.interfaces.Observer;
import org.example.interfaces.RequestsManager;
import org.example.productions.Actor;
import org.example.productions.Production;
import org.example.requests.Request;
import org.example.requests.RequestsHolder;
import org.example.strategies.ContributionStrategy;
import org.example.utils.ManageLists;

import java.util.ArrayList;
import java.util.List;

public class Contributor extends Staff<NameField> implements RequestsManager {
    private final List<Request> createdRequests;

    public Contributor(String username, String experience) {
        super(username, experience);
        createdRequests = new ArrayList<>();
    }

    @Override
    public List<Request> getCreatedRequests() {
        return createdRequests;
    }

    @Override
    public void createRequest(Request r) {
        boolean toWasSet = false;

        switch (r.getType()) {
            case ACTOR_ISSUE -> {
                Staff<NameField> contributor = ManageLists.findContributor(r.getActorName());
                if (contributor != null && contributor != this) {
                    r.setTo(contributor.getUsername());
                    toWasSet = true;
                    contributor.addRequest(r);

                    r.addObserver(contributor);
                    r.notifyObservers();
                }
            }

            case MOVIE_ISSUE -> {
                Staff<NameField> contributor = ManageLists.findContributor(r.getMovieTitle());
                if (contributor != null && contributor != this) {
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

    @Override
    public void addProductionSystem(Production p) {
        IMDB.getInstance().getProductions().add(p);
        addContribution(p);
        IMDB.loggedInUser.addExperience(new ContributionStrategy());
    }

    @Override
    public void addActorSystem(Actor a) {
        IMDB.getInstance().getActors().add(a);
        addContribution(a);
        IMDB.loggedInUser.addExperience(new ContributionStrategy());
    }

    @Override
    public void removeProductionSystem(String name) {
        NameField item = ManageLists.searchInList(IMDB.getInstance().getProductions(), name);

        if (getContributions().contains(item)) {
            IMDB.getInstance().getProductions().remove(item);
            getContributions().remove(item);
        }
    }

    @Override
    public void removeActorSystem(String name) {
        NameField item = ManageLists.searchInList(IMDB.getInstance().getActors(), name);

        if (getContributions().contains(item)) {
            IMDB.getInstance().getActors().remove(item);
            getContributions().remove(item);
        }
    }
}