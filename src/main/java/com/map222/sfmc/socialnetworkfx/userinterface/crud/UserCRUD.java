package com.map222.sfmc.socialnetworkfx.userinterface.crud;


import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.utils.ManifestOption;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.DeletedEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.FriendshipService;
import com.map222.sfmc.socialnetworkfx.service.NetworkService;
import com.map222.sfmc.socialnetworkfx.service.UserService;
import com.map222.sfmc.socialnetworkfx.userinterface.main.MainUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class UserCRUD extends MainUI {
    /**
     * Creates a user CRUD UI instance
     * @param userService -a user service object for user operations
     * @param friendshipService -a friendship service object for friendship operations
     * @param networkService -a network service object for network operations
     */
    public UserCRUD(UserService userService, FriendshipService friendshipService, NetworkService networkService) {
        super(userService, friendshipService, networkService);
    }

    @Override
    public void printMenu() {
        System.out.println("\n/     USERS     /");
        System.out.println("1. Add user");
        System.out.println("2. Remove user");
        System.out.println("3. Update user");
        System.out.println("4. Show all users");
        System.out.println("b. Go back");
    }

    private void addHandler() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("\nEnter the username" +
                    "\n!! A username can only contain lowercase letters, digits and special characters -, _ \n" +
                    "\n!! Usernames must be between 4 and 20 characters: ");
            String username = reader.readLine().strip();
            if(!Pattern.matches("^[._a-z0-9\\-]+$", username))
                throw new ValidationException("\nInvalid username.");
            if(username.length() < 4 || username.length() > 20)
                throw new ValidationException("\nInvalid username length.");

            if(userService.getByID(username) != null)
                throw new ValidationException("\nUsername already in use.");

            System.out.println("\nEnter the user's first name: ");
            String firstName = reader.readLine().strip();
            System.out.println("\nEnter the user's surname: ");
            String lastName = reader.readLine().strip();

            User newUser = new User(firstName, lastName);
            newUser.setID(username);
            this.userService.add(newUser);

            System.out.println("\nSuccessfully added user!");
            this.networkService.addUserToNetwork(newUser);
        }
        catch (IOException ie) {
            System.out.println("\nAn error occurred:\n");
            ie.printStackTrace();
            System.out.println("\n-->This was most likely caused by bad data. Try again.");
        }
        catch (ValidationException ve) {
            System.out.println("\nError:\n" + ve.getMessage());
        }
    }

    private void deleteHandler() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\nEnter the username of the user to delete: ");
            String username = reader.readLine().strip();

            User deleted = userService.delete(username);
            for(String friend: deleted.getFriends()) {
                userService.deleteFriendship(friend, username);
                String username1 = (username.charAt(0) > friend.charAt(0)) ? friend : username;
                String friend1 = (username.charAt(0) > friend.charAt(0)) ? username : friend;
                friendshipService.delete(new Pair<>(username1, friend1));
                networkService.removeFriendshipFromNetwork(new Pair<>(friend, username));
            }
            networkService.removeUser(username);
            System.out.println("\nSuccessfully deleted user!");
        }
        catch (IOException ioe) {
            System.out.println("\nAn error occurred:\n");
            ioe.printStackTrace();
            System.out.println("\n-->This was most likely caused by bad data. Try again.");
        }
        catch (InvalidIDException | DeletedEntityException ide) {
            System.out.println("\nAn error occurred:" + ide.getMessage() + "\n-->Try again.");
        }
    }

    private void updateHandler() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\nEnter the username of the user to update: ");
            String username = reader.readLine().strip();

            System.out.println("\nEnter the user's first name: ");
            String firstName = reader.readLine().strip();
            System.out.println("\nEnter the user's last name: ");
            String lastName = reader.readLine().strip();

            User newUser = new User(firstName, lastName);
            newUser.setID(username);
            userService.update(newUser);
            System.out.println("\nSuccessfully updated user!");
        }
        catch (IOException ioe) {
            System.out.println("\nAn error occurred:\n");
            ioe.printStackTrace();
            System.out.println("\n-->This was most likely caused by bad data. Try again.");
        }
        catch (InvalidIDException | DeletedEntityException | ValidationException idve) {
            System.out.println("\nAn error occurred:" + idve.getMessage() + "\n-->Try again.");
        }
        catch (InvalidEntityException iee) {
            System.out.println("\nAn error occurred:" + iee.getMessage() + "\n-->INTERNAL ERROR.");
        }
    }

    private void showHandler() {
        Iterable<User> users = userService.getAll();
        int usersLength = 0;
        for(User user: users) {
            System.out.println(user + "\n");
            usersLength++;
        }
        if(usersLength == 0)
            System.out.println("\n--> No registered users");
    }

    @Override
    public ManifestOption optionHandler(String option) {
        switch(option) {
            case "1":
                addHandler();
                return ManifestOption.CRUDOP;
            case "2":
                deleteHandler();
                return ManifestOption.CRUDOP;
            case "3":
                updateHandler();
                return ManifestOption.CRUDOP;
            case "4":
                showHandler();
                return ManifestOption.CRUDOP;
            case "b":
                return ManifestOption.EXIT;
            default:
                System.out.println("\nAn error occurred:\nInvalid menu option.\n-->Try again.");
                break;
        }
        return ManifestOption.UNHANDLED;
    }
}
