package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.io.IOException;

public class LaunchpageController {
    private Stage currentStage;
    private Stage returnStage;
    private User currentUser;
    private MasterService service;


    @FXML
    private Label firstnameLabel;

    @FXML
    private Label lastnameLabel;

    @FXML
    private Button friendsButton;

    @FXML
    private Button requestsButton;

    @FXML
    private Button chatButton;


    public void init(MasterService service, User currentUser, Stage currentStage, Stage returnStage) {
        this.currentStage = currentStage;
        this.returnStage = returnStage;
        this.currentUser = currentUser;
        this.service = service;

        this.firstnameLabel.setText(this.currentUser.getFirstName());
        this.lastnameLabel.setText(this.currentUser.getLastName());
    }

    @FXML
    private void friendsClicked() throws IOException {
        EdgemenuController.instantiate().openFriends(service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    private void requestsClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/requests-page.fxml"));
        AnchorPane root = fxmlLoader.load();
        RequestsController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.setTitle("PingChat | MY REQUESTS");
        controller.init(service, currentUser, stage, returnStage);
        currentStage.close();
        stage.show();
    }

    @FXML
    private void addClicked() throws IOException {
        EdgemenuController.instantiate().openExplore(service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    private void eventsClicked() throws IOException {
        EdgemenuController.instantiate().openBlizz(service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    private void disconnectClicked() {
        EdgemenuController.instantiate().openDisconnect(currentStage, returnStage);
    }


    @FXML
    private void chatClicked() throws IOException {
        EdgemenuController.instantiate().openChat(null, service, currentUser, currentStage, returnStage, currentStage);
    }
}
