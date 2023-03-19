package com.map222.sfmc.socialnetworkfx.service.database;


import com.map222.sfmc.socialnetworkfx.repository.database.UserDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.service.UserService;

public class UserDBService extends UserService {
    private UserDatabaseRepo repoDB;

    public UserDBService (UserDatabaseRepo repo) {
        super(repo);
        this.repoDB = repo;
    }

    public UserDatabaseRepo getRepo() {
        return this.repoDB;
    }
}
