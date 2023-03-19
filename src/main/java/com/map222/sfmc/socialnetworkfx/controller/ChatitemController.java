package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import com.map222.sfmc.socialnetworkfx.domain.business.Message;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ChatitemController {
    private MasterService service;
    private User currentUser;
    private Message lastMessage;
    private ChatController returnController;

    @FXML
    private Label dateLabel;

    @FXML
    private Label msgLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView userView;

    @FXML
    private void openChat() throws IOException {
        this.returnController.displayMessages(currentUser);
    }

    private void setMessage() {
        if(this.lastMessage == null) {
            this.msgLabel.setText("");
            this.dateLabel.setText("Say Hi!");
        }
        else {
            String msg = this.lastMessage.getMessage();
            if (Objects.equals(this.lastMessage.getSender().getID(), currentUser.getID()))
                msg = "You: " + msg;
            this.msgLabel.setText(msg);
            this.dateLabel.setText(this.lastMessage.getDate().format(DateTimeFormatter.ofPattern("MMM dd h:mm a")));
        }
    }

    public void init(MasterService service, User currentUser, Message lastMessage, ChatController returnController) {
        this.currentUser = currentUser;
        this.returnController = returnController;
        this.service = service;
        this.nameLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        this.lastMessage = lastMessage;
        this.setMessage();
    }

    public void update(Message lastMessage) {
        this.lastMessage = lastMessage;
        this.setMessage();
    }
}
