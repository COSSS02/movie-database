package org.example.interfaces;

import org.example.requests.Request;

import java.util.ArrayList;
import java.util.List;

public interface RequestsManager {
    public List<Request> getCreatedRequests();

    public void createRequest(Request r);

    public void removeRequest(Request r);
}