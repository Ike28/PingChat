package com.map222.sfmc.socialnetworkfx.service;


import com.map222.sfmc.socialnetworkfx.domain.business.Network;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.relational.Friendship;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;

import java.util.HashMap;
import java.util.List;

public class NetworkService {
    private Network network;
    private boolean networkInitialized;

    /**
     * Creates an uninitialized network service instance
     */
    public NetworkService() {
        network = new Network();
        networkInitialized = false;
    }

    /**
     * Creates an unitialized network service instance
     * @param network -the unitialized network instance
     */
    public NetworkService(Network network) {
        this.network = network;
        networkInitialized = false;
    }

    /**
     *
     * @param user -the user to be added
     */
    public void addUserToNetwork(User user) {
        network.addUserToNetwork(user);
    }

    /**
     *
     * @param username -username of the user to remove from the network
     */
    public void removeUser(String username ) {
        network.removeUser(username);
    }

    /**
     * Initializez the network
     * ! Method can only be run once per instance. Any subsequent calls after the first will have no effect.
     * @param users -an iterable containing the users of the network
     */
    public void initializeNetwork(Iterable<User> users) {
        if(!networkInitialized) {
            for(User u: users)
                network.addUserToNetwork(u);
            networkInitialized = true;
        }
    }

    /**
     *
     * @param friendship -the friendship to add
     */
    public void addFriendshipToNetwork(Friendship friendship) {
        network.addFriendshipToNetwork(friendship);
    }

    /**
     *
     * @param id -id of the friendship
     */
    public void removeFriendshipFromNetwork(Pair<String, String> id) {
        network.removeFriendship(id);
    }

    /**
     * Returns each user in the network and their friends list
     * @return -a hashmap containing the users as keys and their friends list as value
     */
    public HashMap<String, List<String>> getNetworkTree() {
        return network.getTree();
    }

    /**
     * Computes the number of connected communities in the network
     * @return -a positive integer value representing the number of connected communities
     */
    public int communities () {
        return network.connectedComponents();
    }
}
