package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.io.IOException;
import java.util.Locale;

public class MessagesController {
    private MasterService service;
    private User currentUser;
    private Stage currentStage;
    private Stage returnStage;

    @FXML
    private Label firstnameLabel;

    @FXML
    private Label lastnameLabel;

    @FXML
    private TextField usnField;

    @FXML
    private Button openchatButton;

    public void init(MasterService service, User currentUser, Stage currentStage, Stage returnStage) {
        this.service = service;
        this.currentUser = currentUser;
        this.currentStage = currentStage;
        this.returnStage = returnStage;
    }

    @FXML
    private void openClicked() {
        try {
            if(usnField.getText().isEmpty())
                throw new ValidationException("\nEnter a friend's username");
            String username = usnField.getText();
            User friend = this.service.getUser(username);
            if(friend == null)
                throw new ValidationException("\nUser not found :(");
            if(!this.service.isFriendsWith(this.currentUser.getID(), username))
                throw new ValidationException("\nYou and " + username + " are not friends");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/views/chat-page.fxml"));
            AnchorPane root = fxmlLoader.load();
            ChatController controller = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.setTitle("PingChat | CHAT WITH " + username.toUpperCase(Locale.ROOT));
            controller.init(service, currentUser, stage, returnStage, stage);
            currentStage.close();
            stage.show();
        }
        catch (ValidationException | IOException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }
}
