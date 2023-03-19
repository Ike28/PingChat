package com.map222.sfmc.socialnetworkfx.userinterface;

import com.map222.sfmc.socialnetworkfx.domain.relational.Friendship;
import com.map222.sfmc.socialnetworkfx.domain.utils.DateConstants;
import com.map222.sfmc.socialnetworkfx.domain.utils.ManifestOption;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.FriendshipService;
import com.map222.sfmc.socialnetworkfx.service.NetworkService;
import com.map222.sfmc.socialnetworkfx.service.UserService;
import com.map222.sfmc.socialnetworkfx.userinterface.main.MainUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class FriendshipStatistics extends MainUI {
    public FriendshipStatistics(UserService userService, FriendshipService friendshipService, NetworkService networkService) {
        super(userService, friendshipService, networkService);
    }

    @Override
    public void printMenu() {
        System.out.println("\n/     ABOUT FRIENDSHIPS     /");
        System.out.println("1. Show all friendships of a user");
        System.out.println("2. Show all friendships of a user in a specific month");
        System.out.println("x. Exit");
    }

    @Override
    public ManifestOption optionHandler(String option) {
        switch(option) {
            case "1":
                nameFriendshipHandler();
                return ManifestOption.SUBMENU;
            case "2":
                monthFriendshipsHandler();
                return ManifestOption.SUBMENU;
            case "x":
                return ManifestOption.EXIT;
            default:
                System.out.println("\nAn error occurred:\nInvalid menu option.\n-->Try again.");
                break;
        }
        return ManifestOption.UNHANDLED;
    }

    private void nameFriendshipHandler(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\nEnter the username: ");

            String username = reader.readLine().strip();
            if(!Pattern.matches("^[._a-z0-9\\-]+$", username) || username.length() < 4 || username.length() > 20)
                throw new ValidationException("\nInvalid username.");

            if(this.userService.getByID(username) == null)
                throw new InvalidIDException("\nNo such user exists.");

            List<Pair<Friendship, LocalDateTime>> userFriendships
                    = this.friendshipService.getFriendshipsForUser(username);
            userFriendships.forEach( x-> {
                System.out.println(x.getFirstOfPair().getID().getFirstOfPair()
                        + "is friends with "
                        + x.getFirstOfPair().getID().getSecondOfPair()
                        + " since "
                        + x.getSecondOfPair().format(DateTimeFormatter.ofPattern("E, MMM dd yyy")));
            });
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

    private void monthFriendshipsHandler() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\nEnter the username: ");

            String username = reader.readLine().strip();
            if(!Pattern.matches("^[._a-z0-9\\-]+$", username) || username.length() < 4 || username.length() > 20)
                throw new ValidationException("\nInvalid username.");

            if(this.userService.getByID(username) == null)
                throw new InvalidIDException("\nNo such user exists.");

            System.out.println("\nEnter the month's full name (e.g. january): ");
            String month = reader.readLine().strip().toUpperCase();
            if(!DateConstants.Months().contains(month))
                throw new ValidationException("\nInvalid month");


            System.out.println("\nFriendship anniversaries in " + month + ":");
            List<Pair<Friendship, LocalDateTime>> userMonthFriendships
                    = this.friendshipService.getFriendshipsForMonth(username, month);
            userMonthFriendships.forEach( x-> {
                System.out.println(x.getFirstOfPair().getID().getFirstOfPair()
                        + " is friends with "
                        + x.getFirstOfPair().getID().getSecondOfPair()
                        + " since "
                        + x.getSecondOfPair().format(DateTimeFormatter.ofPattern("E, MMM dd yyyy")));
            });

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
}
