package com.map222.sfmc.socialnetworkfx.service;

import com.map222.sfmc.socialnetworkfx.domain.business.Event;
import com.map222.sfmc.socialnetworkfx.domain.business.Message;
import com.map222.sfmc.socialnetworkfx.domain.business.Request;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.relational.Friendship;
import com.map222.sfmc.socialnetworkfx.domain.relational.FriendshipDTO;
import com.map222.sfmc.socialnetworkfx.domain.utils.Comparators;
import com.map222.sfmc.socialnetworkfx.domain.utils.DatabaseCredentials;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.FriendshipValidator;
import com.map222.sfmc.socialnetworkfx.domain.validators.MessageValidator;
import com.map222.sfmc.socialnetworkfx.domain.validators.RequestValidator;
import com.map222.sfmc.socialnetworkfx.domain.validators.UserValidator;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.SQLQueryException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.repository.database.FriendshipDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.repository.database.MessageDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.repository.database.RequestDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.repository.database.UserDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.service.database.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MasterService {
    private UserDBService userService;
    private FriendshipDBService friendshipService;
    private MessageDBService messageService;
    private RequestDBService requestService;
    private PasswordDBService passwordService;
    private EventDBService eventService;
    private ParticipationDBService participationService;

    public MasterService() {
        FriendshipDatabaseRepo friendshipRepo = new FriendshipDatabaseRepo(
                DatabaseCredentials.getUsername(),
                DatabaseCredentials.getPassword(),
                new FriendshipValidator());
        UserDatabaseRepo userRepo = new UserDatabaseRepo(
                DatabaseCredentials.getUsername(),
                DatabaseCredentials.getPassword(),
                new UserValidator(),
                friendshipRepo);
        this.userService = new UserDBService(userRepo);
        this.friendshipService = new FriendshipDBService(friendshipRepo);
        this.messageService = new MessageDBService(new MessageDatabaseRepo(
                DatabaseCredentials.getUsername(),
                DatabaseCredentials.getPassword(),
                new MessageValidator(),
                userRepo
        ));
        this.requestService = new RequestDBService(new RequestDatabaseRepo(
                DatabaseCredentials.getUsername(),
                DatabaseCredentials.getPassword(),
                new RequestValidator()
        ));
        this.passwordService = new PasswordDBService(new PasswordDBRepo());
        this.eventService = new EventDBService();
        this.participationService = new ParticipationDBService();
    }

    // USERS

    public User addUser(String username, String firstname, String lastname) {
        User newUser = new User(firstname, lastname);
        newUser.setID(username);
        return this.userService.add(newUser);
    }

    public User deleteUser(String username) {
        return this.userService.delete(username);
    }

    public User updateUser(String username, String firstname, String lastname) {
        User newUser = new User(firstname, lastname);
        newUser.setID(username);
        return this.userService.update(newUser);
    }

    public User getUser(String username) {
        return this.userService.getByID(username);
    }

    // FRIENDSHIPS

    public Friendship addFriendship(String user1, String user2) {
        if(user2.compareTo(user1) < 0) {
            String aux = user1;
            user1 = user2;
            user2 = aux;
        }
        Friendship newFriendship = new Friendship(user1, user2);
        return this.friendshipService.add(newFriendship);
    }

    public Friendship deleteFriendship(String user1, String user2) {
        if(user2.compareTo(user1) < 0) {
            String aux = user1;
            user1 = user2;
            user2 = aux;
        }
        return this.friendshipService.delete(new Pair<>(user1, user2));
    }

    public List<String> getUsersFriendslist(String username) {
        return this.friendshipService.usersFriendList(username);
    }

    public List<FriendshipDTO> getFriendshipsDTO(String username) {
        return this.friendshipService.getUsersFriendships(username)
                .stream().map(
                        x -> new FriendshipDTO( x.getOther(username), this.userService.getByID(x.getOther(username)).getFirstName(),
                                this.userService.getByID(x.getOther(username)).getLastName(),
                                x.getDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
                ).collect(Collectors.toList());

    }

    public List<User> getUsersNotFriends(String username) {
        return StreamSupport.stream(this.userService.getAll().spliterator(), false)
                .filter( x-> !this.friendshipService.isFriendsWith(username, x.getID()) && !Objects.equals(x.getID(), username)
                && !this.userHasRequestTo(username, x.getID())
                && !this.userHasRequestTo(x.getID(), username))
                .collect(Collectors.toList());
    }

    public boolean isFriendsWith(String user, String friend) {
        return this.friendshipService.isFriendsWith(user, friend);
    }

    // PASSWORDS

    public Pair<String, String> getPassword(String username) throws SQLQueryException {
        return this.passwordService.get(username);
    }

    public void addPasswordEntry(String username, String password) throws SQLQueryException, InvalidIDException {
        this.passwordService.add(username, password);
    }

    public void updatePassword(String username, String currentPassword, String newPassword) throws InvalidIDException {
        this.passwordService.update(username, currentPassword, newPassword);
    }

    public boolean authorizeConnect(String username, String password) throws InvalidIDException {
        return this.passwordService.authorizeConnect(username, password);
    }

    // REQUESTS

    public List<Request> getReceivedRequests(String receiver) {
        return StreamSupport.stream(this.requestService.getRequestsForUser(receiver).spliterator(), false)
                .filter( x-> Objects.equals(x.get_status(), "pending"))
                .collect(Collectors.toList());
    }

    public void sendRequest(String sender, String receiver) {
        if(this.userService.getByID(receiver) == null)
            throw new InvalidIDException("\nUser not found :(");
        Request activeSent = this.requestService.getActiveRequest(sender, receiver);
        if(activeSent != null)
            throw new InvalidIDException("\nThis user already has a pending request from you");
        Request activeReceived = this.requestService.getActiveRequest(sender, receiver);
        if(activeReceived != null)
            throw new InvalidIDException("\nYou already have a pending request from this user");
        this.requestService.createRequest(sender, receiver);
    }

    public void acceptRequest(String sender, String receiver) {
        if(this.userService.getByID(receiver) == null)
            throw new InvalidIDException("\nUser not found :(");
        Request active = this.requestService.getActiveRequest(sender, receiver);
        if(active == null)
            throw new InvalidIDException("\nYou do not have a pending request to this user");
        this.requestService.acceptRequest(active);
        if(sender.compareTo(receiver) > 0) {
            String aux = sender;
            sender = receiver;
            receiver = aux;
        }
        this.friendshipService.add(new Friendship(sender, receiver));
    }

    public void rejectRequest(String sender, String receiver) {
        if(this.userService.getByID(receiver) == null)
            throw new InvalidIDException("\nUser not found :(");
        Request active = this.requestService.getActiveRequest(sender, receiver);
        if(active == null)
            throw new InvalidIDException("\nYou do not have a pending request from this user");
        this.requestService.rejectRequest(active);
    }

    public void cancelRequest(String sender, String receiver) {
        if(this.userService.getByID(receiver) == null)
            throw new InvalidIDException("\nUser not found :(");
        Request active = this.requestService.getActiveRequest(sender, receiver);
        if(active == null)
            throw new InvalidIDException("\nYou do not have a pending request to this user");
        active.set_status("cancelled");
        this.requestService.updateRequestStatus(active);
    }

    public boolean userHasRequestTo(String user, String other) {
        if(this.requestService.getActiveRequest(user, other) == null)
            return false;
        return true;
    }

    // MESSAGES

    public List<Message> getChat(String user1, String user2) throws InvalidIDException {
        User userObj1 = userService.getByID(user1);
        User userObj2 = userService.getByID(user2);
        if(userObj1 == null)
            throw new InvalidIDException("\nUser not found");
        if(userObj2 == null)
            throw new InvalidIDException("\nUser not found");

        if (user2.compareTo(user1) < 0) {
            String aux = user1;
            user1 = user2;
            user2 = aux;

        }
        return StreamSupport.stream(this.messageService.getAll(userObj1, userObj2).spliterator(), false)
                .sorted(Comparators.dateComparatorMsg.reversed())
                .collect(Collectors.toList());
    }

    public void sendMessageToOne(String sender, String receiver, String message) throws ValidationException {
        User senderObj = userService.getByID(sender);
        User receiverObj = userService.getByID(receiver);
        if(senderObj == null)
            throw new InvalidIDException("\nUser not found");
        if(receiverObj == null)
            throw new InvalidIDException("\nUser not found");
        Message newMsg = new Message(senderObj, receiverObj, message);
        this.messageService.add(newMsg);
    }

    public void sendMessageToMany(User sender, List<User> receivers, String message) {
        for(User u: receivers) {
            Message newMessage = new Message(sender, u, message);
            this.messageService.add(newMessage);
        }
    }

    public void sendMessageToOne(Message newMessage) {
        this.messageService.add(newMessage);
    }

    public Message getMessage(UUID messageID) {
        return this.messageService.getByID(messageID);
    }

    // EVENTS

    public Event addEvent(Event newEvent) {
        return this.eventService.add(newEvent);
    }

    public Event deleteEvent(UUID eventID) {
        return this.eventService.delete(eventID);
    }

    public Event getEventByID(UUID eventID) {
        return this.eventService.getByID(eventID);
    }

    public List<Event> getAllEvents() {
        return StreamSupport.stream(this.eventService.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Pair<UUID, String>> getAllEventParticipations() {
        return this.participationService.getAllParticipations();
    }

    public List<Pair<UUID, String>> getParticipationsOfUser(String username) {
        return this.participationService.getAllParticipationsOfUser(username);
    }

    public void addParticipation(String username, UUID eventID) {
        this.participationService.add(username, eventID);
    }

    public void removeParticipation(String username, UUID eventID) {
        this.participationService.remove(username, eventID);
    }

}
