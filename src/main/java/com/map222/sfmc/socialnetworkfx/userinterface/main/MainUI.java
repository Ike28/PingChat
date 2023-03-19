package com.map222.sfmc.socialnetworkfx.userinterface.main;

import com.map222.sfmc.socialnetworkfx.domain.utils.ManifestOption;
import com.map222.sfmc.socialnetworkfx.domain.validators.UserValidator;
import com.map222.sfmc.socialnetworkfx.repository.file.UserFileRepository;
import com.map222.sfmc.socialnetworkfx.service.FriendshipService;
import com.map222.sfmc.socialnetworkfx.service.NetworkService;
import com.map222.sfmc.socialnetworkfx.service.UserService;
import com.map222.sfmc.socialnetworkfx.userinterface.FriendshipStatistics;
import com.map222.sfmc.socialnetworkfx.userinterface.UI;
import com.map222.sfmc.socialnetworkfx.userinterface.crud.FriendshipCRUD;
import com.map222.sfmc.socialnetworkfx.userinterface.crud.NetworkUI;
import com.map222.sfmc.socialnetworkfx.userinterface.crud.UserCRUD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainUI implements UI {
    protected UserService userService;
    protected FriendshipService friendshipService;
    protected NetworkService networkService;

    /**
     * Creates a main UI instance
     * @param userService -a user service for user operations
     * @param friendshipService -a friendship service for friendship operations
     * @param networkService -a network service for network operations
     */
    public MainUI(UserService userService, FriendshipService friendshipService, NetworkService networkService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.networkService = networkService;
    }

    public MainUI(String filename) {
        this.userService = new UserService(new UserFileRepository(filename, new UserValidator()));
    }

    @Override
    public void printMenu() {
        System.out.println("\n/     MAIN MENU     /");
        System.out.println("1. C.R.U.D. Users");
        System.out.println("2. C.R.U.D. Friendships");
        System.out.println("3. Network statistics");
        System.out.println("4. Friendship statistics");
        System.out.println("x. Exit");
    }

    @Override
    public ManifestOption optionHandler(String option) {
        switch(option) {
            case "1":
                UserCRUD userUI = new UserCRUD(this.userService, this.friendshipService, this.networkService);
                userUI.runInterface();
                return ManifestOption.SUBMENU;
            case "2":
                FriendshipCRUD friendshipUI = new FriendshipCRUD(this.userService, this.friendshipService, networkService);
                friendshipUI.runInterface();
                return ManifestOption.SUBMENU;
            case "3":
                NetworkUI networkUI = new NetworkUI(this.userService, this.friendshipService, this.networkService);
                networkUI.runInterface();
                return ManifestOption.SUBMENU;
            case "4":
                FriendshipStatistics statisticsUI = new FriendshipStatistics(this.userService, this.friendshipService, null);
                statisticsUI.runInterface();
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
