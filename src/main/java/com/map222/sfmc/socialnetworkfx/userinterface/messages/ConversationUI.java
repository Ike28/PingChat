package com.map222.sfmc.socialnetworkfx.userinterface.messages;

import com.map222.sfmc.socialnetworkfx.domain.business.Message;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.utils.Comparators;
import com.map222.sfmc.socialnetworkfx.domain.utils.ManifestOption;
import com.map222.sfmc.socialnetworkfx.domain.utils.StringGens;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.database.FriendshipDBService;
import com.map222.sfmc.socialnetworkfx.service.database.MessageDBService;
import com.map222.sfmc.socialnetworkfx.service.database.UserDBService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class ConversationUI extends MessageUI {

    private User currentUser;
    private Map<String, Iterable<Message>> friendsChat;
    private List<Map.Entry<UUID, Message>> currentMessageHistory;
    private MessageDBService messageService;

    public ConversationUI(UserDBService userService, FriendshipDBService friendshipService, User currentUser) {
        super(userService, friendshipService);
        this.currentUser = currentUser;
        this.friendsChat = new HashMap<>();
        for(String friend: this.currentUser.getFriends()) {
            friendsChat.put(friend, null);
        }
        this.messageService = new MessageDBService(userService);
    }

    @Override
    public void printMenu() {
        System.out.println("\n/     CHAT     /");
        System.out.println("Friends");
        if(this.currentUser.getFriends().size() == 0)
            System.out.println("    - No friends yet: try adding some! :)");
        for(String friend: this.currentUser.getFriends())
            System.out.println("    * " + friend);

        System.out.println("____________________");
        System.out.println("x. Disconnect");
    }

    private Map<UUID, Message> chatHistory(String friend_username) {
        Map<UUID, Message> chatList = new HashMap<>();
        User friend = this.userService.getByID(friend_username);
        Iterable<Message> friendMessages = this.messageService.getAll(currentUser, friend);
        friendMessages.forEach(x -> chatList.put(x.getID(), x));
        this.friendsChat.put(friend_username, friendMessages);
        return chatList;
    }

    private List<Map.Entry<UUID, Message>> chatHistoryForDisplay (Map<UUID, Message> chatHistory) {
        return chatHistory.entrySet().stream()
                .sorted(Comparators.dateComparator)
                .collect(Collectors.toList());
    }

    private void showChatHistory(String friend_username) {
        System.out.println(StringGens.chatHeader(friend_username));
        Map<UUID, Message> chatHistory = this.chatHistory(friend_username);
        List<Map.Entry<UUID, Message>> chatHistoryForDisplay = chatHistoryForDisplay(chatHistory);

        if(chatHistory.size() == 0)
            System.out.println(StringGens.noMessages);
        else {

            for(int i=0; i<chatHistoryForDisplay.size(); i++) {
                String message = chatHistoryForDisplay.get(i).getValue().getMessage();

                if(chatHistoryForDisplay.get(i).getValue().getReplyRoot() != null) {
                    message = StringGens.addReplyToMessage(message,
                            chatHistory.get(chatHistoryForDisplay.get(i).getValue().getReplyRoot()).getMessage());
                }

                String timestamp = StringGens.getDisplayTimestamp(chatHistoryForDisplay.get(i).getValue().getDate());

                message = StringGens.messageToDisplayLayout(chatHistoryForDisplay.get(i).getValue().getSender().getID(),
                        currentUser.getID(), message, "#" + i, timestamp);

                System.out.println(message);

            }
        }
        this.currentMessageHistory = chatHistoryForDisplay;
        System.out.println(StringGens.chatFooter);
    }

    private String sendMessageHandler(User friendEntity) throws IOException, NumberFormatException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("> ");
        String messageText = reader.readLine().strip();

        if(messageText.length() > 2 && Objects.equals(messageText.substring(0, 2), "/r")
                && Objects.equals(messageText.substring(2, 4), " #")) {
            List<String> msgAttributes = Arrays.stream(messageText.substring(4).split(" ")).toList();
            Integer identifier = Integer.parseInt(msgAttributes.get(0));
            String messageTextParsed = String.join(" ", msgAttributes.subList(1, msgAttributes.size())).strip();
            if(0 <= identifier && identifier < currentMessageHistory.size() && !Objects.equals(messageTextParsed, "")) {
                Message newMessage = new Message(currentUser, friendEntity, messageTextParsed);
                newMessage.setReplyRoot(currentMessageHistory.get(identifier).getValue().getID());

                this.messageService.add(newMessage);
                System.out.println("    Sent reply \u2713");
            }
        }
        else if(!Objects.equals(messageText, "/e")
                && !Objects.equals(messageText, "")
                && !Objects.equals(messageText, "/s")) {
            Message newMessage = new Message(currentUser, friendEntity, messageText);

            this.messageService.add(newMessage);

            System.out.println("    Sent \u2713");
        }


        return messageText;
    }

    private void chatOpenHandler(String friend) {
        try {
            showChatHistory(friend);
            User friendEntity = this.userService.getByID(friend);
            String msgResult = this.sendMessageHandler(friendEntity);
            while(!Objects.equals(msgResult, "/e")) {

                if(Objects.equals(msgResult, "/s"))
                    showChatHistory(friend);
                msgResult = this.sendMessageHandler(friendEntity);
            }
        }
        catch (IOException ie) {
            System.out.println("\n\033[5;31mError:\n" + ie.getMessage() + "\nmost likely caused by bad data. Try again.\033[0m");
        }
        catch (NumberFormatException nfe) {
            System.out.println("\n\033[5;93mIncorrect reply syntax. Check the format: /r <message_code> <message>\033[0m");
        }
        catch (ValidationException vee) {
            System.out.println(vee.getMessage());
        }
    }

    @Override
    public ManifestOption optionHandler(String option) {

        if(Objects.equals(option, "x"))
            return ManifestOption.EXIT;

        if(this.friendsChat.containsKey(option)) {
            if(this.friendsChat.get(option) == null)
                this.friendsChat.put(option, messageService.getAll(currentUser, this.userService.getByID(option)));
            chatOpenHandler(option);
            return ManifestOption.SUBMENU;
        }

        System.out.println("\nWe couldn't find " + option + " in your friends list :(\n-->Try again.");
        return ManifestOption.UNHANDLED;
    }

}
