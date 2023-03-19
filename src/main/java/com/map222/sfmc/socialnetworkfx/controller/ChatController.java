package com.map222.sfmc.socialnetworkfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import com.map222.sfmc.socialnetworkfx.domain.business.Message;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.relational.FriendshipDTO;
import com.map222.sfmc.socialnetworkfx.domain.utils.Comparators;
import com.map222.sfmc.socialnetworkfx.domain.utils.Constants;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatController {
    private MasterService service;
    private User currentUser;
    private User currentFriend;
    private Stage currentStage;
    private Stage returnStage;
    private Stage backStage;
    private Map<User, Pair<Message, List<Message>>> chatMapper;
    private UUID currentReplyRoot;
    private List<User> receiverSelection;

    @FXML
    private Label firstnameLabel;

    @FXML
    private Label lastnameLabel;

    @FXML
    private TextArea msgArea;

    @FXML
    private ImageView sendButton;

    @FXML
    private ScrollPane chatScroll;

    @FXML
    private ScrollPane msgScroll;

    @FXML
    private GridPane friendsGrid;

    @FXML
    private Label chatnameLabel;

    @FXML
    private GridPane messageGrid;

    @FXML
    private Label replyLabel;

    @FXML
    private void searchClicked() {

    }

    @FXML
    private void manysendClicked() throws IOException {
        this.receiverSelection = new ArrayList<>();
        this.messageGrid.getChildren().clear();
        this.chatnameLabel.setText("Chat with your Pingus at once");
        this.messageGrid.setPrefHeight(100);

        int columnIndex = 1;
        for(User u: chatMapper.keySet()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/selectsend-item.fxml"));
            AnchorPane root = loader.load();
            SendController controller = loader.getController();
            controller.init(u, this);

            this.messageGrid.add(root, columnIndex++, 0);
            GridPane.setMargin(root, new Insets(10));
        }
        this.setDisplayPrefs(messageGrid);
    }

    @FXML
    private void sendClicked() throws IOException {
        if(msgArea.getText().isEmpty())
            throw new ValidationException("\nEnter your message");
        String msg = msgArea.getText();

        if(this.receiverSelection == null) {
            Message message = new Message(this.currentUser, this.currentFriend, msg);
            message.setReplyRoot(this.currentReplyRoot);
            this.currentReplyRoot = null;
            this.msgArea.clear();

            this.service.sendMessageToOne(message);
            List<Message> olderMessages = this.chatMapper.get(message.getReceiver()).getSecondOfPair();
            olderMessages.add(0, message);
            this.chatMapper.put(message.getReceiver(), new Pair<>(message, olderMessages));
            this.displayChats();
            this.positionMessage(message, messageGrid.getRowCount());
            msgScroll.setVvalue(1D);
            chatScroll.setVvalue(0D);
        }
        else {
            for(User u: this.receiverSelection) {
                Message newMessage = new Message(this.currentUser, u, msg);
                this.service.sendMessageToOne(newMessage);
                List<Message> olderMessages = this.chatMapper.get(u).getSecondOfPair();
                olderMessages.add(0, newMessage);
                this.chatMapper.put(u, new Pair<>(newMessage, olderMessages));
                this.displayChats();
                this.chatScroll.setVvalue(0D);
            }
            this.msgArea.clear();
            this.manysendClicked();
        }
    }

    public void switchSelection(User user) {
        if(this.receiverSelection.contains(user))
            this.receiverSelection.remove(user);
        else this.receiverSelection.add(user);
    }

    private void positionMessage(Message m, int rowIndex) throws IOException {
        FXMLLoader loader;
        if (Objects.equals(m.getSender().getID(), this.currentUser.getID())) {
            loader = new FXMLLoader(getClass().getResource("/views/user-chatitem.fxml"));
        } else loader = new FXMLLoader(getClass().getResource("/views/friend-chatitem.fxml"));

        AnchorPane root = loader.load();
        MessageitemController controller = loader.getController();
        if (m.getReplyRoot() == null)
            controller.init(m, null, this.currentUser, this);
        else controller.init(m, service.getMessage(m.getReplyRoot()), this.currentUser, this);
        messageGrid.add(root, 0, rowIndex);
    }

    public void updateReplyRoot(UUID rootID) {
        if(rootID == null) {
            this.replyLabel.setText("");
        }
        else this.replyLabel.setText("Reply to: " + this.service.getMessage(rootID).getMessage());
        this.currentReplyRoot = rootID;
    }

    private void setDisplayPrefs(GridPane gridPane) {
        gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxWidth(Region.USE_PREF_SIZE);

        gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxHeight(Region.USE_PREF_SIZE);
    }

    private void setChatsScrollSpeed(double coef) {
        friendsGrid.setOnScroll(event -> {
            double deltaY = event.getDeltaY()*coef; // to make the scrolling a bit faster
            double width = chatScroll.getContent().getBoundsInLocal().getWidth();
            double vvalue = chatScroll.getVvalue();
            chatScroll.setVvalue(vvalue + -deltaY/width); // deltaY/width to make the scrolling equally fast regardless of the actual width of the component
        });
    }

    public void displayMessages(User friend) throws IOException {
        this.messageGrid.setPrefHeight(387);
        this.receiverSelection = null;
        this.messageGrid.getChildren().clear();

        int rowIndex = 0;
        List<Message> chatHistory = this.chatMapper.get(friend).getSecondOfPair();
        this.currentFriend = friend;
        this.chatnameLabel.setText(friend.getFirstName() + " " + friend.getLastName());

            this.msgArea.setVisible(true);
            this.msgArea.setMouseTransparent(false);
            this.sendButton.setVisible(true);
            this.sendButton.setMouseTransparent(false);

            for (int i = chatHistory.size() - 1; i >= 0; i--) {
                Message m = chatHistory.get(i);
                this.positionMessage(m, rowIndex);
                rowIndex++;
            }
            currentReplyRoot = null;
            this.setDisplayPrefs(messageGrid);
            msgScroll.setVvalue(1D);
    }

    private void displayChats() throws IOException {
        friendsGrid.getChildren().clear();
        if(chatMapper.size() == 0)
            this.currentFriend = null;
        else {
            int rowIndex = 0;
            List<Map.Entry<User, Pair<Message, List<Message>>>> orderedChats = chatMapper.entrySet().stream()
                    .sorted(Comparators.dateComparatorChat.reversed())
                    .collect(Collectors.toList());

            for (Map.Entry<User, Pair<Message, List<Message>>> e : orderedChats) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/conversation-item.fxml"));
                AnchorPane root = loader.load();
                ChatitemController controller = loader.getController();

                if (e.getValue().getSecondOfPair().size() > 0)
                    controller.init(service, e.getKey(), e.getValue().getFirstOfPair(), this);
                else controller.init(service, e.getKey(), null, this);

                friendsGrid.add(root, 0, rowIndex++);
                GridPane.setMargin(root, new Insets(10));
            }

            this.currentFriend = orderedChats.get(0).getKey();
        }
    }

    public void init(MasterService service, User currentUser, Stage currentStage, Stage returnStage, Stage backStage) throws IOException {
        try {
            this.service = service;
            this.currentUser = currentUser;
            this.currentFriend = null;
            this.currentStage = currentStage;
            this.returnStage = returnStage;
            this.firstnameLabel.setText(this.currentUser.getFirstName());
            this.lastnameLabel.setText(this.currentUser.getLastName());
            this.chatScroll.requestFocus();
            this.chatMapper = new HashMap<>();
            this.backStage = backStage;

            this.msgArea.setVisible(false);
            this.msgArea.setMouseTransparent(true);
            this.sendButton.setVisible(false);
            this.sendButton.setMouseTransparent(true);

            for (FriendshipDTO f : this.service.getFriendshipsDTO(this.currentUser.getID())) {

                List<Message> friendConversation = service.getChat(this.currentUser.getID(), f.getUsername());
                if(friendConversation.size() > 0)
                    this.chatMapper.put(friendConversation.get(0).getFriend(this.currentUser),
                            new Pair<>(friendConversation.get(0), friendConversation));
                else
                    this.chatMapper.put(this.service.getUser(f.getUsername()),
                            new Pair<>(new Message(null, null, "",
                                    LocalDateTime.ofInstant(Instant.ofEpochMilli(Constants.earliestTimestamp),
                                    TimeZone.getDefault().toZoneId())), friendConversation));
            }
            this.displayChats();
            this.setDisplayPrefs(this.friendsGrid);
            this.setChatsScrollSpeed(15);

            if (currentFriend != null) {
                this.chatnameLabel.setText(currentFriend.getFirstName() + " " + currentFriend.getLastName());
                this.displayMessages(this.currentFriend);
            }
            else this.chatnameLabel.setText("");
        }
        catch (InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }

    @FXML
    public void friendsClicked() throws IOException {
        EdgemenuController.instantiate().openFriends(service, currentUser, currentStage, returnStage, backStage);
    }

    @FXML
    public void addClicked() throws IOException {
        EdgemenuController.instantiate().openExplore(service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    public void eventsClicked() throws IOException {
        EdgemenuController.instantiate().openBlizz(service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    private void disconnectClicked() {
        EdgemenuController.instantiate().openDisconnect(currentStage, returnStage);
    }

    @FXML
    public void leaveClicked() {
        EdgemenuController.instantiate().openLeave(currentStage, backStage);
    }
}
