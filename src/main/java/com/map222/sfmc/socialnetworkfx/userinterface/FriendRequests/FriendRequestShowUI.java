package com.map222.sfmc.socialnetworkfx.userinterface.FriendRequests;

import com.map222.sfmc.socialnetworkfx.domain.business.Request;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.utils.ManifestOption;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.database.FriendshipDBService;
import com.map222.sfmc.socialnetworkfx.service.database.RequestDBService;
import com.map222.sfmc.socialnetworkfx.service.database.UserDBService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class FriendRequestShowUI extends FriendRequestsUI {

    private Iterable<Request> requests;
    private User currentUser;

    public FriendRequestShowUI(UserDBService userService, FriendshipDBService friendshipService, RequestDBService requestService, User currentUser, Iterable<Request> requests) {
        super(userService, friendshipService, requestService, currentUser);
        this.requests = requests;
        this.currentUser = currentUser;
    }

    @Override
    public void printMenu() {
        System.out.println("\n/     Friend Requests     /");
        System.out.println("1. Accept a friend request\n");
        System.out.println("2. Reject a friend requests\n");
        System.out.println("x. Disconnect");
    }

    @Override
    public ManifestOption optionHandler(String option) {
        switch(option) {
            case "1":
                acceptOptionHandler();
                return ManifestOption.SUBMENU;
            case "2":
                rejectOptionHandler();
                return ManifestOption.SUBMENU;
            case "x":
                return ManifestOption.EXIT;
            default:
                System.out.println("\nAn error occurred:\nInvalid menu option.\n-->Try again.");
                break;
        }
        return ManifestOption.UNHANDLED;
    }

    private void rejectOptionHandler() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\nEnter the username of the user from whom you want to accept the friend request: ");

            String username = reader.readLine().strip();
            if(!Pattern.matches("^[._a-z0-9\\-]+$", username) || username.length() < 4 || username.length() > 20)
                throw new ValidationException("\nInvalid username.");

            if(this.userService.getByID(username) == null)
                throw new InvalidIDException("\nNo such user exists.");


            Request request = new Request(currentUser.getID(), username);
            requestService.rejectRequest(request);

        }
        catch (ValidationException | InvalidIDException idve) {
            System.out.println("\nError:\n" + idve.getMessage());
        }
        catch (IOException ie) {
            System.out.println("\nAn error occurred:\n");
            ie.printStackTrace();
            System.out.println("\n-->This was most likely caused by bad data. Try again.");
        } catch (InvalidEntityException iee) {
            System.out.println("\nError:\n" + iee.getMessage());
        }
    }


    private void acceptOptionHandler() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\nEnter the username of the user from whom you want to accept the friend request: ");

            String username = reader.readLine().strip();
            if (!Pattern.matches("^[._a-z0-9\\-]+$", username) || username.length() < 4 || username.length() > 20)
                throw new ValidationException("\nInvalid username.");

            if (this.userService.getByID(username) == null)
                throw new InvalidIDException("\nNo such user exists.");


            Request request = new Request(username, currentUser.getID());
            requestService.acceptRequest(request);

        } catch (ValidationException | InvalidIDException idve) {
            System.out.println("\nError:\n" + idve.getMessage());
        } catch (IOException ie) {
            System.out.println("\nAn error occurred:\n");
            ie.printStackTrace();
            System.out.println("\n-->This was most likely caused by bad data. Try again.");
        } catch (InvalidEntityException iee) {
            System.out.println("\nError:\n" + iee.getMessage());
        }

    }
}
