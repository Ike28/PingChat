package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import com.map222.sfmc.socialnetworkfx.domain.business.User;

public class SendController {
    private User displayedUser;
    private ChatController rootController;

    @FXML
    private CheckBox check;

    @FXML
    private void checkClicked() {
        this.rootController.switchSelection(this.displayedUser);
    }

    public void init(User displayedUser, ChatController rootController) {
        this.displayedUser = displayedUser;
        this.rootController = rootController;
        this.check.setText(this.displayedUser.getFirstName() + " " + this.displayedUser.getLastName());
    }
}
