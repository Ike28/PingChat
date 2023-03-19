package com.map222.sfmc.socialnetworkfx.service.database;

import com.map222.sfmc.socialnetworkfx.repository.database.RequestDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.service.RequestService;

public class RequestDBService extends RequestService {
    private RequestDatabaseRepo repoDB;

    public RequestDBService(RequestDatabaseRepo repo) {
        super(repo);
        this.repo = repo;
    }


}
