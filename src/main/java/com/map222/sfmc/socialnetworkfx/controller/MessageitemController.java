package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import com.map222.sfmc.socialnetworkfx.domain.business.Message;
import com.map222.sfmc.socialnetworkfx.domain.business.User;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MessageitemController {
    private User currentUser;
    private Message currentMessage;
    private Message reply;
    private ChatController rootController;

    @FXML
    private Label dateLabel;

    @FXML
    private Pane msgBubble;

    @FXML
    private AnchorPane msgContainer;

    @FXML
    private Label msgLabel;

    @FXML
    private Label replyLabel;

    @FXML
    private void clearReply() {
        this.rootController.updateReplyRoot(null);

    }

    @FXML
    private void replyClicked() {
        this.rootController.updateReplyRoot(this.currentMessage.getID());
    }

    private void setMessage() {
        if(this.reply == null)
            this.replyLabel.setText("");
        else {
            String replyAddon = "Replied to ";
            if(Objects.equals(this.reply.getSender().getID(), this.currentMessage.getSender().getID())) {
                if(Objects.equals(this.reply.getSender().getID(), currentUser.getID()))
                    replyAddon += "yourself";
                else replyAddon += "themselves";
            }
            else {
                if(Objects.equals(this.reply.getSender().getID(), currentUser.getID()))
                    replyAddon += "you";
                else replyAddon += this.reply.getSender().getFirstName();
            }
            replyAddon += ": ";
            this.replyLabel.setText(replyAddon + this.reply.getMessage());
        }

        this.msgLabel.setWrapText(true);
        this.msgLabel.setText(this.currentMessage.getMessage());
        this.dateLabel.setText(this.currentMessage.getDate().format(DateTimeFormatter.ofPattern("MMM dd h:mm a")));
    }

    public void init(Message message, Message reply, User currentUser, ChatController rootController) {
        this.currentUser = currentUser;
        this.currentMessage = message;
        this.reply = reply;
        this.rootController = rootController;
        setMessage();
    }
}
