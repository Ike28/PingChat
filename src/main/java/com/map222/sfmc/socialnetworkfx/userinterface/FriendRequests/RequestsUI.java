package com.map222.sfmc.socialnetworkfx.userinterface.FriendRequests;


import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.utils.ManifestOption;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.service.database.FriendshipDBService;
import com.map222.sfmc.socialnetworkfx.service.database.RequestDBService;
import com.map222.sfmc.socialnetworkfx.service.database.UserDBService;
import com.map222.sfmc.socialnetworkfx.userinterface.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RequestsUI implements UI {
    protected UserDBService userService;
    protected FriendshipDBService friendshipService;
    protected RequestDBService requestService;

    public RequestsUI(UserDBService userService, FriendshipDBService friendshipService, RequestDBService requestService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.requestService = requestService;
    }

    @Override
    public void printMenu() {
        System.out.println("\n/     Friend Requests     /");
        System.out.println("1. Open Friend Requests");
        System.out.println("b. back");
    }

    @Override
    public ManifestOption optionHandler(String option) {
        switch(option) {
            case "1":
                authenticationHandler();
                return ManifestOption.SUBMENU;
            case "b":
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

    private void authenticationHandler() {
        try {
            System.out.println("\n**********\nEnter your username:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String username = reader.readLine().strip();

            User currentUser = this.userService.getByID(username);
            if(currentUser == null)
                throw new InvalidIDException("\nUsername not found.");

            FriendRequestsUI friendRequestsUI = new FriendRequestsUI(this.userService, this.friendshipService, this.requestService, currentUser);
            friendRequestsUI.runInterface();

        }
        catch (IOException ie) {
            System.out.println("\nAn error occurred:\n");
            ie.printStackTrace();
            System.out.println("\n-->This was most likely caused by bad data. Try again.");
        }
        catch (InvalidIDException ide) {
            System.out.println("\nAn error occurred:" + ide.getMessage() + "\n-->Try again.");
        }
    }
}
