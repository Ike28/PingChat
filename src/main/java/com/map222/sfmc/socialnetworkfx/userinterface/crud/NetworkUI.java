package com.map222.sfmc.socialnetworkfx.userinterface.crud;

import com.map222.sfmc.socialnetworkfx.domain.utils.ManifestOption;
import com.map222.sfmc.socialnetworkfx.service.FriendshipService;
import com.map222.sfmc.socialnetworkfx.service.NetworkService;
import com.map222.sfmc.socialnetworkfx.service.UserService;
import com.map222.sfmc.socialnetworkfx.userinterface.main.MainUI;

import java.util.HashMap;
import java.util.List;

public class NetworkUI extends MainUI {
    /**
     * Creates a network UI instance
     * @param userService -a user service for user operations
     * @param friendshipService -a friendship service for friendship operations
     * @param networkService -a network service for network operations
     */
    public NetworkUI(UserService userService, FriendshipService friendshipService, NetworkService networkService) {
        super(userService, friendshipService, networkService);
    }

    @Override
    public void printMenu() {
        System.out.println("\n/     NETWORK     /");
        System.out.println("1. Show the full network");
        System.out.println("2. Get the number of communities");
        //System.out.println("3. Get most sociable community"); //not implemented
        System.out.println("b. Go back");
    }

    private void showHandler() {
        HashMap<String, List<String>> tree = networkService.getNetworkTree();
        List<String> friendsList;
        for(String key: tree.keySet()) {
            System.out.print(key + "'s friends: ");
            friendsList = tree.get(key);
            for(String f: friendsList)
                System.out.print(f + "; ");
            System.out.println("\n");
        }
    }

    private void communityHandler() {
        int communities = networkService.communities();
        System.out.println("\nThere are " + communities + " communities currently in the network.");
    }

    @Override
    public ManifestOption optionHandler(String option) {
        switch(option) {
            case "1":
                showHandler();
                return ManifestOption.STATOP;
            case "2":
                communityHandler();
                return ManifestOption.STATOP;
            case "b":
                return ManifestOption.EXIT;
            default:
                System.out.println("\nAn error occurred:\nInvalid menu option.\n-->Try again.");
                break;
        }
        return ManifestOption.UNHANDLED;
    }

}
