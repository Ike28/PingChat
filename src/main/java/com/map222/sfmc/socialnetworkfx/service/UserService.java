package com.map222.sfmc.socialnetworkfx.service;


import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.repository.Repository;

public class UserService extends Service<String, User> {
    /**
     * Creates a user service instance
     * @param repo -a repository for storing the users (may or may not be empty)
     */
    public UserService(Repository<String, User> repo) {
        super(repo);
    }

    /**
     * Adds a new friendship to an existing user
     * @param username -username of the existing user
     * @param friendname -username of the friend
     */
    public void addFriendshipToUser (String username, String friendname) {
        User user = repo.getByID(username);
        user.addFriend(friendname);
        repo.update(user);
    }

    /**
     * Deletes a friendship from an existing user
     * @param username -username of the existing user
     * @param friendname -username of the friend to unfriend
     */
    public void deleteFriendship (String username, String friendname) {
        User user = repo.getByID(username);
        user.removeFriend(friendname);
        repo.update(user);
    }
}
