package com.map222.sfmc.socialnetworkfx.userinterface;

import com.map222.sfmc.socialnetworkfx.domain.utils.ManifestOption;
import com.map222.sfmc.socialnetworkfx.service.NetworkService;
import com.map222.sfmc.socialnetworkfx.service.database.FriendshipDBService;
import com.map222.sfmc.socialnetworkfx.service.database.RequestDBService;
import com.map222.sfmc.socialnetworkfx.service.database.UserDBService;
import com.map222.sfmc.socialnetworkfx.userinterface.FriendRequests.RequestsUI;
import com.map222.sfmc.socialnetworkfx.userinterface.main.MainUI;
import com.map222.sfmc.socialnetworkfx.userinterface.messages.MessageUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartUI implements UI {

    protected UserDBService userService;
    protected FriendshipDBService friendshipService;
    protected NetworkService networkService;
    protected RequestDBService requestService;

    /**
     * Creates a main UI instance
     * @param userService -a user service for user operations
     * @param friendshipService -a friendship service for friendship operations
     * @param networkService -a network service for network operations
     */
    public StartUI(UserDBService userService, FriendshipDBService friendshipService, NetworkService networkService, RequestDBService requestService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.networkService = networkService;
        this.requestService = requestService;
    }

    @Override
    public void printMenu() {
        System.out.println("\n/     START     /");
        System.out.println("1. Operational Interface");
        System.out.println("2. Messenger");
        System.out.println("3. Friend Requests");
        System.out.println("\nx. Exit");
    }

    @Override
    public ManifestOption optionHandler(String option) {
        switch(option) {
            case "1":
                MainUI mainUI = new MainUI(userService, friendshipService, networkService);
                mainUI.runInterface();
                return ManifestOption.SUBMENU;
            case "2":
                MessageUI messageUI = new MessageUI(userService, friendshipService);
                messageUI.runInterface();
                return ManifestOption.SUBMENU;
            case "3":
                RequestsUI requestsUI = new RequestsUI(userService, friendshipService, requestService);
                requestsUI.runInterface();
                return ManifestOption.SUBMENU;
            case "x":
                return ManifestOption.EXIT;
            default:
                System.out.println("\nAn error occurred:\nInvalid menu option.\n-->Try again.");
                break;
        }
        return ManifestOption.UNHANDLED;
    }

    @Override
    public void runInterface() {
        while(true) {
            printMenu();
            try {
                System.out.println("\nChoose an option: ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String line = reader.readLine();
                String option = line.strip().split(" ")[0];

                ManifestOption userResult = optionHandler(option);

                if(userResult == ManifestOption.EXIT)
                    return;
            }
            catch (IOException ie) {
                System.out.println("\nAn error occurred:\n");
                ie.printStackTrace();
                System.out.println("\n-->This was most likely caused by bad data. Try again.");
            }
        }
    }
}
