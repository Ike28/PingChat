package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

public class RemovefriendController {
    private Stage currentStage;
    private Stage returnStage;
    private User currentUser;
    private MasterService service;

    @FXML
    private Label firstnameLabel;

    @FXML
    private Label lastnameLabel;

    @FXML
    private TextField usnField;

    @FXML
    private Button removeButton;

    public void init(MasterService service, User currentUser, Stage currentStage, Stage returnStage) {
        this.service = service;
        this.currentUser = currentUser;
        this.currentStage = currentStage;
        this.returnStage = returnStage;
    }

    @FXML
    private void removeClicked() {
        try {
            if(usnField.getText().isEmpty())
                throw new ValidationException("\nEnter a username");
            String deletedFriend = usnField.getText();
            if(this.service.getUser(deletedFriend) == null)
                throw new InvalidIDException("\nUser not found :(");
            if(!this.service.isFriendsWith(currentUser.getID(), deletedFriend))
                throw new ValidationException("\nYou are not friends with " + deletedFriend);
            this.service.deleteFriendship(this.currentUser.getID(), deletedFriend);
            AlertController.showAlert(null, Alert.AlertType.INFORMATION, "SUCCESS",
                    "You and " + deletedFriend + " are no longer friends\nYou can add them back any time if you change your mind");
        }
        catch (ValidationException | InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }
}
