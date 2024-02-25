package org.example.requests;

import org.example.IMDB;
import org.example.interfaces.NameField;
import org.example.users.Admin;
import org.example.users.Contributor;
import org.example.users.Regular;
import org.example.utils.ManageLists;

import java.util.List;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;


public class RequestsHolder {
    public static final List<Admin> admins = new ArrayList<>();
    public static final List<Request> adminRequests = new ArrayList<>();
    public static final SortedSet<NameField> adminContributions = new TreeSet<>();

    public static void addAdminRequest(Request request) {
        request.setTo("ADMIN");
        RequestsHolder.adminRequests.add(request);

        for (Admin admin : RequestsHolder.admins) {
            request.addObserver(admin);
        }
        request.notifyObservers();
    }

    public static void removeAdminRequest(Request request) {
        adminRequests.remove(request);

        NameField user = ManageLists.searchInList(IMDB.getInstance().getUsers(), request.getUsername());

        if (user instanceof Regular r) {
            r.removeRequest(request);
        } else if (user instanceof Contributor c) {
            c.removeRequest(request);
        }
    }

    public static void addAdminContribution(NameField contribution) {
        adminContributions.add(contribution);
    }

    public static void removeAdminContribution(NameField contribution) {
        adminContributions.add(contribution);
    }
}