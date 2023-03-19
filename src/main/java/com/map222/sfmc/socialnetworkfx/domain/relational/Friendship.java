package com.map222.sfmc.socialnetworkfx.domain.relational;


import com.map222.sfmc.socialnetworkfx.domain.business.Entity;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Friendship extends Entity<Pair<String, String>> {

    LocalDateTime date;

    public Friendship() {
        date = LocalDateTime.now();
        this.amendDelete();
    }

    public Friendship(String user1, String user2, LocalDateTime date) {
        this.setID(new Pair<>(user1, user2));
        this.date = date;
    }

    public Friendship(String user1, String user2) {
        this.date = LocalDateTime.now();
        this.setID(new Pair<>(user1, user2));
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return this.getID().getFirstOfPair() + " is friends with " + this.getID().getSecondOfPair() + " since " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getOther(String thisOne) {
        if(Objects.equals(this.getID().getSecondOfPair(), thisOne))
            return this.getID().getFirstOfPair();
        if(Objects.equals(this.getID().getFirstOfPair(), thisOne))
            return this.getID().getSecondOfPair();
        return null;
    }
}
