package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import com.map222.sfmc.socialnetworkfx.domain.business.User;

import java.util.Objects;

public class UseritemController {
    private User currentUser;
    private User displayedUser;
    private AddpageController rootController;

    @FXML
    private Button addButton;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView pfpView;

    @FXML
    private void addClicked() {
        if(!Objects.equals(addButton.getText(), "REQUESTED")) {
            this.rootController.sendRequest(this.displayedUser);
            addButton.setStyle("-fx-background-color: #24284c;");
            addButton.setText("REQUESTED");
        }
    }

    public void init(User currentUser, User displayedUser, AddpageController rootController) {
        this.currentUser = currentUser;
        this.displayedUser = displayedUser;
        this.rootController = rootController;
        this.nameLabel.setText(displayedUser.getFirstName() +  " " + displayedUser.getLastName());
    }
}
