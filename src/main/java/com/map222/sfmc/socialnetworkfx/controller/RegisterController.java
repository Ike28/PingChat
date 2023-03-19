package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.util.Objects;

public class RegisterController {
    private Stage currentStage;
    private MasterService service;
    private Stage returnStage;

    @FXML
    private AnchorPane registerPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmField;

    @FXML
    private TextField firstnameField;

    @FXML
    private TextField lastnameField;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    @FXML
    private Label usnOverheadLabel;

    @FXML
    private Label passOverheadLabel;

    @FXML
    private Label confirmOverheadLabel;

    @FXML
    private Label firstnmOverheadLabel;

    @FXML
    private Label lastnmOverheadLabel;

    public void init(Stage currentStage, Stage returnStage, MasterService service) {
        this.currentStage = currentStage;
        this.returnStage = returnStage;
        this.service = service;
    }

    @FXML
    private void registerClicked() {
        try {
            String validationErrors = "";
            if (usernameField.getText().isEmpty()) {
                validationErrors += "Create a username";
            }
            if (passwordField.getText().isEmpty()) {
                validationErrors += "Create a password";
            }
            if (confirmField.getText().isEmpty()) {
                validationErrors += "You need to enter a password and confirm it";
            }
            if (firstnameField.getText().isEmpty()) {
                validationErrors += "Enter your firstname";
            }
            if (lastnameField.getText().isEmpty()) {
                validationErrors += "Enter your lastname";
            }

            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmField.getText();
            String firstname = firstnameField.getText();
            String lastname = lastnameField.getText();

            if(!Objects.equals(password, confirmPassword)) {
                validationErrors += "\nPasswords do not match";
            }

            if(!validationErrors.equals(""))
                throw new ValidationException(validationErrors);

            if(this.service.getUser(username) != null) {
                throw new InvalidIDException("This username is already taken");
            }

            this.service.addUser(username, firstname, lastname);
            this.service.addPasswordEntry(username, password);

            AlertController.showAlert(null, Alert.AlertType.INFORMATION, "Account creation successful!", "Welcome aboard, " + firstname + "!");

            this.backClicked();
        }
        catch (ValidationException | InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }

    @FXML
    private void backClicked() {
        this.currentStage.close();
        this.returnStage.show();
    }

    @FXML
    private void defaultLabels() {
        usnOverheadLabel.setVisible(false);
        usnOverheadLabel.setMouseTransparent(true);

        passOverheadLabel.setVisible(false);
        passOverheadLabel.setMouseTransparent(true);

        confirmOverheadLabel.setVisible(false);
        confirmOverheadLabel.setMouseTransparent(true);

        firstnmOverheadLabel.setVisible(false);
        firstnmOverheadLabel.setMouseTransparent(true);

        lastnmOverheadLabel.setVisible(false);
        lastnmOverheadLabel.setMouseTransparent(true);
    }

    @FXML
    private void backgroundOnClick() {
        defaultLabels();
        registerPane.requestFocus();
    }

    @FXML
    private void usnOnClick() {
        defaultLabels();
        usnOverheadLabel.setVisible(true);
        usnOverheadLabel.setMouseTransparent(false);
    }

    @FXML
    private void passOnClick() {
        defaultLabels();
        passOverheadLabel.setVisible(true);
        passOverheadLabel.setMouseTransparent(false);
    }

    @FXML
    private void confirmOnClick() {
        defaultLabels();
        confirmOverheadLabel.setVisible(true);
        confirmOverheadLabel.setMouseTransparent(false);
    }

    @FXML
    private void firstnmOnClick() {
        defaultLabels();
        firstnmOverheadLabel.setVisible(true);
        firstnmOverheadLabel.setMouseTransparent(false);
    }

    @FXML
    private void lastnmOnClick() {
        defaultLabels();
        lastnmOverheadLabel.setVisible(true);
        lastnmOverheadLabel.setMouseTransparent(false);
    }
}
