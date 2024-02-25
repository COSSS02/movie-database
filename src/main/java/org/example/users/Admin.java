package org.example.users;

import org.example.IMDB;
import org.example.interfaces.NameField;
import org.example.productions.Actor;
import org.example.productions.Production;
import org.example.requests.RequestsHolder;
import org.example.utils.ManageLists;

public class Admin extends Staff<NameField> {

    public Admin(String username, String experience) {
        super(username, experience);
    }

    @Override
    public void addProductionSystem(Production p) {
        IMDB.getInstance().getProductions().add(p);
        addContribution(p);
    }

    @Override
    public void addActorSystem(Actor a) {
        IMDB.getInstance().getActors().add(a);
        addContribution(a);
    }

    @Override
    public void removeProductionSystem(String name) {
        NameField item = ManageLists.searchInList(IMDB.getInstance().getProductions(), name);

        if (getContributions().contains(item)) {
            IMDB.getInstance().getProductions().remove(item);
            getContributions().remove(item);

        } else if (RequestsHolder.adminContributions.contains(item)) {
            IMDB.getInstance().getProductions().remove(item);
            RequestsHolder.removeAdminContribution(item);
        }
    }

    @Override
    public void removeActorSystem(String name) {
        NameField item = ManageLists.searchInList(IMDB.getInstance().getActors(), name);

        if (getContributions().contains(item)) {
            IMDB.getInstance().getActors().remove(item);
            getContributions().remove(item);

        } else if (RequestsHolder.adminContributions.contains(item)) {
            IMDB.getInstance().getActors().remove(item);
            RequestsHolder.removeAdminContribution(item);
        }
    }
}