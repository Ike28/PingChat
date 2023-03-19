package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import com.map222.sfmc.socialnetworkfx.domain.business.User;

import java.util.Objects;

public class ReceivedReqitemController {
    private User displayedUser;
    private AddpageController rootController;

    @FXML
    private Button acceptButton;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView pfpView;

    @FXML
    private Button rejectButton;

    @FXML
    void acceptClicked() {
        if(!Objects.equals(acceptButton.getText(), "ACCEPTED") && acceptButton.isVisible()) {
            this.rootController.acceptRequest(this.displayedUser);
            acceptButton.setText("ACCEPTED");
            rejectButton.setVisible(false);
            rejectButton.setMouseTransparent(true);
        }
    }

    @FXML
    void rejectClicked() {
        if(!Objects.equals(rejectButton.getText(), "REJECTED") && rejectButton.isVisible()) {
            this.rootController.rejectRequest(this.displayedUser);
            rejectButton.setText("REJECTED");
            acceptButton.setVisible(false);
            acceptButton.setMouseTransparent(true);
        }
    }

    public void init(User displayedUser, AddpageController rootController) {
        this.displayedUser = displayedUser;
        this.rootController = rootController;
        this.nameLabel.setText(this.displayedUser.getFirstName() + " " + this.displayedUser.getLastName());
    }
}
