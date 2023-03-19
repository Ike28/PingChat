package com.map222.sfmc.socialnetworkfx.service.database;

import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.repository.database.ParticipationDBRepo;

import java.util.List;
import java.util.UUID;

public class ParticipationDBService {
    private ParticipationDBRepo repo;

    public ParticipationDBService() {
        this.repo = new ParticipationDBRepo();
    }

    public List<Pair<UUID, String>> getAllParticipations() {
        return this.repo.getAll();
    }


    public List<Pair<UUID, String>> getAllParticipationsOfUser(String username) {
        return this.repo.getAllOfUser(username);
    }

    public void add(String username, UUID eventID) {
        this.repo.addEventParticipation(username, eventID);
    }

    public void remove(String username, UUID eventID) {
        this.repo.removeEventParticipation(username, eventID);
    }
}
