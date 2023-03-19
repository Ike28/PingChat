package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.io.IOException;

public class EdgemenuController {
    private static EdgemenuController active;

    private EdgemenuController() {

    }

    public static EdgemenuController instantiate() {
        if(active == null)
            active = new EdgemenuController();
        return active;
    }

    public void openChat(User chatfriend, MasterService service, User currentUser, Stage currentStage, Stage returnStage, Stage backStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/chat-page.fxml"));
        AnchorPane root = fxmlLoader.load();
        ChatController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.setTitle("PingChat | MY CONVERSATIONS");
        controller.init(service, currentUser, stage, returnStage, backStage);
        currentStage.close();
        stage.show();
        if(chatfriend != null)
            controller.displayMessages(chatfriend);
    }

    public void openFriends(MasterService service, User currentUser, Stage currentStage, Stage returnStage, Stage backStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/friendspage.fxml"));
        AnchorPane root = fxmlLoader.load();
        FriendspageController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.setTitle("PingChat | MY PINGUS");
        controller.init(service, currentUser, stage, returnStage, backStage);
        currentStage.close();
        stage.show();
    }

    public void openLeave(Stage currentStage, Stage previousStage) {
        currentStage.close();
        previousStage.show();
    }

    public void openDisconnect(Stage currentStage, Stage returnStage) {
        currentStage.close();
        returnStage.show();
    }

    public void openExplore(MasterService service, User currentUser, Stage currentStage, Stage returnStage, Stage backStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/addpage.fxml"));
        AnchorPane root = fxmlLoader.load();
        AddpageController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.setTitle("PingChat | CONNECT");
        controller.init(service, currentUser, stage, returnStage, backStage);
        currentStage.close();
        stage.show();
    }

    public void openBlizz(MasterService service, User currentUser, Stage currentStage, Stage returnStage, Stage backStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/events-page.fxml"));
        AnchorPane root = fxmlLoader.load();
        EventspageController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.setTitle("PingChat | BLIZZ");
        controller.init(service, currentUser, stage, returnStage, backStage);
        currentStage.close();
        stage.show();
    }
}
