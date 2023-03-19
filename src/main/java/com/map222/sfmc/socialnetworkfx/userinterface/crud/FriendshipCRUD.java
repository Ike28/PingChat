package com.map222.sfmc.socialnetworkfx.userinterface.crud;

import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.relational.Friendship;
import com.map222.sfmc.socialnetworkfx.domain.utils.ManifestOption;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.DeletedEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.service.FriendshipService;
import com.map222.sfmc.socialnetworkfx.service.NetworkService;
import com.map222.sfmc.socialnetworkfx.service.UserService;
import com.map222.sfmc.socialnetworkfx.userinterface.main.MainUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FriendshipCRUD extends MainUI {
    /**
     * Creates a friendship CRUD UI instance
     * @param userService -a user service for user operations
     * @param friendshipService -a friendship service for friendship operations
     * @param networkService -a network service for network operations
     */
    public FriendshipCRUD(UserService userService, FriendshipService friendshipService, NetworkService networkService) {
        super(userService, friendshipService, networkService);
    }

    @Override
    public void printMenu() {
        System.out.println("\n/     FRIENDSHIPS     /");
        System.out.println("1. Add friendship");
        System.out.println("2. Remove friendship");
        System.out.println("3. Show all friendships");
        System.out.println("b. Go back");
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

    private void addHandler() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("\nEnter the first user's username: ");
            String user1 = reader.readLine().strip();

            System.out.println("\nEnter the second user's username: ");
            String user2 = reader.readLine().strip();

            String usernameErrors = "";
            User userEnt1 = userService.getByID(user1);
            User userEnt2 = userService.getByID(user2);

            if(userEnt1 == null)
                usernameErrors += "\nNo user exists with username '" + user1 + "'.";
            if(userEnt2 == null)
                usernameErrors += "\nNo user exists with username '" + user2 + "'.";
            if(usernameErrors.length() > 0)
                throw new InvalidIDException(usernameErrors);

            if(userEnt1.isDeleted() || userEnt2.isDeleted())
                throw new DeletedEntityException("\nAttempting to link one or more deleted users.");

            if(user1.charAt(0) > user2.charAt(0)) {
                String aux = user1;
                user1 = user2;
                user2 = aux;
            }
            Friendship friendship = new Friendship(user1, user2);
            friendshipService.add(friendship);
            userService.addFriendshipToUser(user1, user2);
            userService.addFriendshipToUser(user2, user1);

            System.out.println("\nSuccessfully added friendship!");
            networkService.addFriendshipToNetwork(friendship);
        }
        catch (IOException ioe) {
            System.out.println("\nAn error occurred:\n");
            ioe.printStackTrace();
            System.out.println("\n-->This was most likely caused by bad data. Try again.");
        }
        catch(InvalidIDException ide) {
            System.out.println("\nAn error occurred:" + ide.getMessage());
            System.out.println("\n-->Check the above data for typos. Try again.");
        }
        catch (DeletedEntityException dee) {
            System.out.println("\nAn error occurred:" + dee.getMessage() + "\n-->try again.");
        }
    }

    private void deleteHandler() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("\nEnter the first user's username: ");
            String user1 = reader.readLine().strip();

            System.out.println("\nEnter the second user's username: ");
            String user2 = reader.readLine().strip();

            String usernameErrors = "";
            User userEnt1 = userService.getByID(user1);
            User userEnt2 = userService.getByID(user2);

            if(userEnt1 == null)
                usernameErrors += "\nNo user exists with username '" + user1 + "'.";
            if(userEnt2 == null)
                usernameErrors += "\nNo user exists with username '" + user2 + "'.";
            if(usernameErrors.length() > 0)
                throw new InvalidIDException(usernameErrors);

            if(userEnt1.isDeleted() || userEnt2.isDeleted())
                throw new DeletedEntityException("\nAttempting to link one or more deleted users.");

            if(user1.charAt(0) > user2.charAt(0)) {
                String aux = user1;
                user1 = user2;
                user2 = aux;
            }
            friendshipService.delete(new Pair<>(user1, user2));
            userService.deleteFriendship(user1, user2);
            userService.deleteFriendship(user2, user1);

            System.out.println("\nSuccessfully deleted friendship!");
            networkService.removeFriendshipFromNetwork(new Pair<>(user1, user2));
        }
        catch (IOException ioe) {
            System.out.println("\nAn error occurred:\n");
            ioe.printStackTrace();
            System.out.println("\n-->This was most likely caused by bad data. Try again.");
        }
        catch(InvalidIDException ide) {
            System.out.println("\nAn error occurred:" + ide.getMessage());
            System.out.println("\n-->Check the above data for typos. Try again.");
        }
        catch (DeletedEntityException dee) {
            System.out.println("\nAn error occurred:" + dee.getMessage() + "\n-->try again.");
        }
    }

    private void showHandler() {
        Iterable<Friendship> friendships = friendshipService.getAll();
        int friendshipsLength = 0;
        for(Friendship friendship: friendships) {
            System.out.println(friendship + "\n");
            friendshipsLength++;
        }
        if(friendshipsLength == 0)
            System.out.println("\n--> There are no friendships yet");
    }
}
