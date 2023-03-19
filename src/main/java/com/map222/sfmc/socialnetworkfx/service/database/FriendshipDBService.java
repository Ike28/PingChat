package com.map222.sfmc.socialnetworkfx.service.database;


import com.map222.sfmc.socialnetworkfx.domain.relational.Friendship;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.repository.database.FriendshipDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.service.FriendshipService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipDBService extends FriendshipService {
    private FriendshipDatabaseRepo repoDB;

    public FriendshipDBService(FriendshipDatabaseRepo repo) {
        super(repo);
        this.repoDB = repo;
    }

    public List<String> usersFriendList(String username) throws InvalidIDException {
        return this.repoDB.getUserFriendList(username);
    }

    public List<Friendship> getUsersFriendships(String username) {
        return StreamSupport.stream(this.repoDB.getAll().spliterator(), false)
                .filter( x-> Objects.equals(x.getID().getFirstOfPair(), username)
                        || Objects.equals(x.getID().getSecondOfPair(), username))
                .collect(Collectors.toList());
    }

    public boolean isFriendsWith(String username, String friend) {
        String firstInID = username, secondInID = friend;
        if (username.compareTo(friend) > 0) {
            firstInID = friend;
            secondInID = username;
        }
        return this.repo.getByID(new Pair<>(firstInID, secondInID)) != null || this.repo.getByID(new Pair<>(secondInID, firstInID)) != null;
    }
}
