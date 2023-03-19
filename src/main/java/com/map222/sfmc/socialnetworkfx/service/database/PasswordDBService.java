package com.map222.sfmc.socialnetworkfx.service.database;

import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.utils.PassGen;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.SQLQueryException;

public class PasswordDBService {
    private PasswordDBRepo passwordRepo;

    public PasswordDBService(PasswordDBRepo repo) {
        this.passwordRepo = repo;
    }

    public PasswordDBService() {
        this.passwordRepo = new PasswordDBRepo();
    }

    public Pair<String, String> get(String username) throws SQLQueryException {
        String usernameHash = PassGen.convertPassword(username);
        return this.passwordRepo.getByID(usernameHash);
    }

    public void add(String username, String password) throws SQLQueryException, InvalidIDException {
        String usernameHash = PassGen.convertPassword(username);
        String passwordHash = PassGen.convertPassword(password);
        this.passwordRepo.add(usernameHash, passwordHash);
    }

    public void update(String username, String currentPassword, String newPassword) throws InvalidIDException {
        String usernameHash = PassGen.convertPassword(username);
        String passwordHash = PassGen.convertPassword(currentPassword);
        String newPassHash = PassGen.convertPassword(newPassword);
        this.passwordRepo.changePassword(usernameHash, passwordHash, newPassHash);
    }

    public boolean authorizeConnect(String username, String password) throws InvalidIDException {
        String usernameHash = PassGen.convertPassword(username);
        String passwordHash = PassGen.convertPassword(password);
        return this.passwordRepo.matchPass(usernameHash, passwordHash);
    }
}
