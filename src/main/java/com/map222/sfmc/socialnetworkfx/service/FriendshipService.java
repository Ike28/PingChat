package com.map222.sfmc.socialnetworkfx.service;

import com.map222.sfmc.socialnetworkfx.domain.relational.Friendship;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipService extends Service<Pair<String, String>, Friendship> {

    /**
     * Creates a friendship service instance
     * @param repo -a repository for storing the friendships (may or may not be empty)
     */
    public FriendshipService(Repository<Pair<String, String>, Friendship> repo) {
        super(repo);

    }

    public List<Pair<Friendship, LocalDateTime>> getFriendshipsForMonth(String username, String month) {
        Predicate<Friendship> userFriendshipsInMonth =
                x -> (Objects.equals(x.getID().getFirstOfPair(), username) || Objects.equals(x.getID().getSecondOfPair(), username))
                    && Objects.equals(x.getDate().getMonth().name(), month);
        return getPairs(username, userFriendshipsInMonth);
    }

    public List<Pair<Friendship, LocalDateTime>> getFriendshipsForUser(String username){
        Predicate<Friendship> userFriendshipsOfUser =
                x -> (Objects.equals(x.getID().getFirstOfPair(), username) || Objects.equals(x.getID().getSecondOfPair(), username));
        return getPairs(username, userFriendshipsOfUser);
    }

    private List<Pair<Friendship, LocalDateTime>> getPairs(String username, Predicate<Friendship> userFriendshipsOfUser) {
        return StreamSupport.stream(this.repo.getAll().spliterator(), false)
                .filter(userFriendshipsOfUser)
                .map( x-> {
                    if(Objects.equals(x.getID().getSecondOfPair(), username))
                        x.setID(new Pair<>(username, x.getID().getFirstOfPair()));
                    return new Pair<>(x, x.getDate());
                })
                .collect(Collectors.toList());
    }

    public void createFriendship(String user1, String user2){
        Friendship newfriendship = new Friendship(user1, user2);
        this.repo.add(newfriendship);
    }

}
