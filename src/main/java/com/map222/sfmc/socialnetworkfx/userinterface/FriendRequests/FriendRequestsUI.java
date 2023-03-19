package com.map222.sfmc.socialnetworkfx.userinterface.FriendRequests;


import com.map222.sfmc.socialnetworkfx.domain.business.Request;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.utils.ManifestOption;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.database.FriendshipDBService;
import com.map222.sfmc.socialnetworkfx.service.database.RequestDBService;
import com.map222.sfmc.socialnetworkfx.service.database.UserDBService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class FriendRequestsUI extends RequestsUI {

    private User currentUser;

    public FriendRequestsUI(UserDBService userService, FriendshipDBService friendshipService, RequestDBService requestService, User currentUser) {
        super(userService, friendshipService, requestService);
        this.currentUser = currentUser;
    }

    @Override
    public void printMenu() {
        System.out.println("\n/     Friend Requests     /");
        System.out.println("1. Send a friend request\n");
        System.out.println("2. Show friend requests\n");
        System.out.println("x. Disconnect");
    }

    @Override
    public ManifestOption optionHandler(String option) {
        switch(option) {
            case "1":
                sendOptionHandler();
                return ManifestOption.SUBMENU;
            case "2":
                showOptionHandler();
                return ManifestOption.SUBMENU;
            case "x":
                return ManifestOption.EXIT;
            default:
                System.out.println("\nAn error occurred:\nInvalid menu option.\n-->Try again.");
                break;
        }
        return ManifestOption.UNHANDLED;
    }

    private void sendOptionHandler() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\nEnter the username of the user you want to send a request to: ");

            String username = reader.readLine().strip();
            if(!Pattern.matches("^[._a-z0-9\\-]+$", username) || username.length() < 4 || username.length() > 20)
                throw new ValidationException("\nInvalid username.");

            if(this.userService.getByID(username) == null)
                throw new InvalidIDException("\nNo such user exists.");

            requestService.createRequest(currentUser.getID(),username);

        }
        catch (ValidationException | InvalidIDException idve) {
            System.out.println("\nError:\n" + idve.getMessage());
        }
        catch (IOException ie) {
            System.out.println("\nAn error occurred:\n");
            ie.printStackTrace();
            System.out.println("\n-->This was most likely caused by bad data. Try again.");
        }
    }


    private void showOptionHandler() {
        Iterable<Request> requests = requestService.getAll();
        int requestsLength = 0;
        for(Request request: requests){
            System.out.println(request + "\n");
            requestsLength++;
        }
        if(requestsLength == 0)
            System.out.println("\n--> There are no requests sent to this user");
        else {
            FriendRequestShowUI friendRequestShowUI = new FriendRequestShowUI(this.userService, this.friendshipService, this.requestService, currentUser, requests);
            friendRequestShowUI.runInterface();
        }
    }
}
