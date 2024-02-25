package org.example.users;

import org.example.IMDB;
import org.example.interfaces.NameField;
import org.example.interfaces.StaffInterface;
import org.example.productions.*;
import org.example.requests.Request;
import org.example.utils.ManageLists;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Staff<T extends NameField> extends User<NameField> implements StaffInterface {
    private final List<Request> requests;
    private final SortedSet<T> contributions;

    public Staff(String username, String experience) {
        super(username, experience);
        requests = new ArrayList<>();
        contributions = new TreeSet<>();
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void removeRequest(Request request) {
        requests.remove(request);

        NameField user = ManageLists.searchInList(IMDB.getInstance().getUsers(), request.getUsername());

        if (user instanceof Regular r) {
            r.removeRequest(request);
        } else if (user instanceof Contributor c) {
            c.removeRequest(request);
        }
    }

    public SortedSet<T> getContributions() {
        return contributions;
    }

    public void addContribution(T contribution) {
        contributions.add(contribution);
    }

    @Override
    abstract public void addProductionSystem(Production p);

    @Override
    abstract public void addActorSystem(Actor a);

    abstract public void removeProductionSystem(String name);

    abstract public void removeActorSystem(String name);
}