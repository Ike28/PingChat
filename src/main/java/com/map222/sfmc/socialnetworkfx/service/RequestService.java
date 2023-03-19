package com.map222.sfmc.socialnetworkfx.service;


import com.map222.sfmc.socialnetworkfx.domain.business.Request;
import com.map222.sfmc.socialnetworkfx.domain.utils.DatabaseCredentials;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.RequestValidator;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidEntityException;
import com.map222.sfmc.socialnetworkfx.repository.Repository;
import com.map222.sfmc.socialnetworkfx.repository.database.RequestDatabaseRepo;

import java.util.HashSet;
import java.util.Set;

public class RequestService extends Service<Pair<String, String>, Request>{
    private RequestDatabaseRepo requestRepo;

    /**
     * Creates a request service instance
     * @param repo - a repository for storing requests
     */
    public RequestService(Repository<Pair<String, String>, Request> repo) {
        super(repo);
    }

    /**
     * updates the status of an existing request
     * @param request - the request to update
     */
    public void updateRequestStatus(Request request){
        Request req = repo.getByID(request.getID());
        req.set_status(request.get_status());
        repo.update(req);
    }

    /**
     * updates the status of an existing request to accepted
     * @param request - the request to update
     */
    public void acceptRequest(Request request){
        Request req = repo.getByID(request.getID());
        if(req.get_status().equals("pending")){
        String accepted = "accepted";
        req.set_status(accepted);
        repo.update(req);}
        else throw new InvalidEntityException("\nRequest is not pending.");
    }

    /**
     * updates the status of an existing request to rejected
     * @param request - the request to update
     */
    public void rejectRequest(Request request){
        Request req = repo.getByID(request.getID());
        if(req.get_status().equals("pending"))
        {    String rejected = "rejected";
             req.set_status(rejected);
             repo.update(req);}
        else throw new InvalidEntityException("\nRequest is not pending.");
    }

    /**
     * creates a request from the username of 2 users
     * @param user1 - username of the sender
     * @param user2 - username of the receiver
     */
    public void createRequest(String user1, String user2){
        Request req = new Request(user1, user2);
        repo.add(req);
    }

    /**
     * creates a set of requests with the given receiver
     * @param username - string, receiver of the requests
     * @return results - set of requests
     */
    public Set<Request> getRequestsForUser(String username){
        Iterable<Request> requests = repo.getAll();
        Set<Request> results = new HashSet<>();
        for(Request request : requests)
        {
            if(request.getID().getSecondOfPair().equals(username))
                results.add(request);
        }
        return results;
    }

    public Request getActiveRequest(String sender, String receiver) {
        RequestDatabaseRepo requestRepo = new RequestDatabaseRepo(DatabaseCredentials.getUsername(), DatabaseCredentials.getPassword(),
                new RequestValidator());
        return requestRepo.getActiveRequest(sender, receiver);
    }

}
