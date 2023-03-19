package com.map222.sfmc.socialnetworkfx.domain.business;


import com.map222.sfmc.socialnetworkfx.domain.relational.Friendship;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Network {
    private HashMap<String, List<String>> networkTree;

    public Network() {
        networkTree = new HashMap<>();
    }

    /**
     * Adds a new user and their friends to the network
     * @param user -the new user
     */
    public void addUserToNetwork(User user) {
        List<String> userFriends = new ArrayList<>();
        if(networkTree.containsKey(user.getID()))
            userFriends = networkTree.get(user.getID());

        for(String friend: user.getFriends()) {
            if(!userFriends.contains(friend))
                userFriends.add(friend);
        }
        networkTree.put(user.getID(), userFriends);

        List<String> friendsList = new ArrayList<>();
        for(String friend: user.getFriends()) {
            if(networkTree.containsKey(friend))
                friendsList = networkTree.get(friend);
            if(!friendsList.contains(user.getID()))
                friendsList.add(user.getID());
            networkTree.put(friend, friendsList);
        }
    }

    /**
     * Removes a user and their friendships from the network
     * @param ID -username of the user to remove
     */
    public void removeUser(String ID) {
        networkTree.remove(ID);
    }

    /**
     * Adds a friendship to the network
     * @param friendship -the friendship to add
     */
    public void addFriendshipToNetwork(Friendship friendship) {
        List<String> user1Friends = networkTree.get(friendship.getID().getFirstOfPair());
        List<String> user2Friends = networkTree.get(friendship.getID().getSecondOfPair());
        if(!user1Friends.contains(friendship.getID().getSecondOfPair()))
            user1Friends.add(friendship.getID().getSecondOfPair());
        if(!user2Friends.contains(friendship.getID().getFirstOfPair()))
            user2Friends.add(friendship.getID().getFirstOfPair());

        networkTree.put(friendship.getID().getFirstOfPair(), user1Friends);
        networkTree.put(friendship.getID().getSecondOfPair(), user2Friends);
    }

    /**
     * Removes a friendship from the network (has no effect if the friendship doesn't exist)
     * @param ID -a tuple containing the usernames of the users to unfriend
     */
    public void removeFriendship(Pair<String, String> ID) {
        List<String> user1Friends = networkTree.get(ID.getFirstOfPair());
        List<String> user2Friends = networkTree.get(ID.getSecondOfPair());
        user1Friends.remove(ID.getSecondOfPair());
        user2Friends.remove(ID.getFirstOfPair());

        networkTree.put(ID.getFirstOfPair(), user1Friends);
        networkTree.put(ID.getSecondOfPair(), user2Friends);
    }

    /**
     *
     * @return a hashmap containing the users' usernames as keys and their friends list as value
     */
    public HashMap<String, List<String>> getTree() {
        return networkTree;
    }

    private void DFSUtil(String key, HashMap<String, Boolean> visited)
    {
        visited.put(key, Boolean.TRUE);
        for (String friend: networkTree.get(key)) {
            if (visited.get(friend) == Boolean.FALSE)
                DFSUtil(friend, visited);
        }
    }

    /**
     * Computes the number of connected communities in the network
     * @return a positive integer value representing the number of communities
     */
    public int connectedComponents()
    {
        int components = 0;
        HashMap<String, Boolean> visited = new HashMap<>();
        for(String key: networkTree.keySet())
            visited.put(key, Boolean.FALSE);
        for (String key: visited.keySet()) {
            if (visited.get(key) == Boolean.FALSE) {
                DFSUtil(key, visited);
                components++;
            }
        }
        return components;
    }
}
