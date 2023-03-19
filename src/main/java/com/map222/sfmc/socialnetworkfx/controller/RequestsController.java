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

public class RequestsController {
    private Stage currentStage;
    private Stage returnStage;
    private User currentUser;
    private MasterService service;

    @FXML
    private Label firstnameLabel;

    @FXML
    private Label lastnameLabel;

    @FXML
    private Button acceptButton;

    @FXML
    private Button rejectButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField usnField;

    public void init(MasterService serviceStage, User currentUser, Stage currentStage, Stage returnStage) {
        this.currentStage = currentStage;
        this.returnStage = returnStage;
        this.currentUser = currentUser;
        this.service = new MasterService();
    }

    @FXML
    private void acceptClicked() {
        try {
            if(usnField.getText().isEmpty())
                throw new ValidationException("\nEnter someone's username");
            String otherUser = usnField.getText();
            this.service.acceptRequest(otherUser, this.currentUser.getID());
            AlertController.showAlert(null, Alert.AlertType.INFORMATION, "SUCCESS", "You and " + otherUser + " are now friends! ^_^");
        }
        catch (ValidationException | InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }

    @FXML
    private void rejectClicked() {
        try {
            if(usnField.getText().isEmpty())
                throw new ValidationException("\nEnter someone's username");
            String otherUser = usnField.getText();
            this.service.rejectRequest(otherUser, this.currentUser.getID());
            AlertController.showAlert(null, Alert.AlertType.INFORMATION, "SUCCESS", "You rejected " + otherUser + "'s friend request");
        }
        catch (ValidationException | InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }

    @FXML
    private void cancelClicked() {
        try {
            if(usnField.getText().isEmpty())
                throw new ValidationException("\nEnter someone's username");
            String otherUser = usnField.getText();
            this.service.cancelRequest(this.currentUser.getID(), otherUser);
            AlertController.showAlert(null, Alert.AlertType.INFORMATION, "SUCCESS", "Friend request to " + otherUser + " cancelled");
        }
        catch (ValidationException | InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }
}
