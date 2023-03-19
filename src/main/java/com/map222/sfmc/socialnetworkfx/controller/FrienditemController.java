package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.relational.FriendshipDTO;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.io.IOException;
import java.util.Objects;

public class FrienditemController {
    private FriendshipDTO currentFriend;
    private MasterService service;
    private FriendspageController rootController;

    @FXML
    private Button chatButton;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView pfpView;

    @FXML
    private Button removeButton;

    @FXML
    private void openChat() throws IOException {
        if(!Objects.equals(this.chatButton.getText(), "X")) {
            User thisUser = service.getUser(currentFriend.getUsername());
            this.rootController.openChat(thisUser);
        }
    }

    @FXML
    private void promptRemove() {
        this.rootController.removeFriend(this.currentFriend);
        this.chatButton.setText("X");
        this.chatButton.setStyle("-fx-background-color: #FB0A45");
        this.removeButton.setVisible(false);
        this.removeButton.setMouseTransparent(true);
    }

    public void init(FriendshipDTO currentFriend, MasterService service, FriendspageController rootController) {
        this.currentFriend = currentFriend;
        this.service = service;
        this.nameLabel.setText(currentFriend.getFirstname() + " " + currentFriend.getLastname());
        this.rootController = rootController;
        Image pfp = new Image(getClass().getResourceAsStream("/images/defaultpfp.png"));
        pfpView.setImage(pfp);
    }
}
