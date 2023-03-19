package com.map222.sfmc.socialnetworkfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.relational.FriendshipDTO;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.io.IOException;

public class FriendspageController {
    private MasterService service;
    private User currentUser;
    private Stage currentStage;
    private Stage returnStage;
    private Stage backStage;
    private ObservableList<FriendshipDTO> prietenii;
    private User chatUser;

    @FXML
    private Label firstnameLabel;

    @FXML
    private Label lastnameLabel;

    @FXML
    private TextField searchField;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ScrollPane friendsScroll;

    @FXML
    private GridPane friendsGrid;

    private void setFriendslistScrollSpeed(double coef) {
        friendsGrid.setOnScroll(event -> {
            double deltaY = event.getDeltaY()*coef; // to make the scrolling a bit faster
            double width = friendsScroll.getContent().getBoundsInLocal().getWidth();
            double vvalue = friendsScroll.getVvalue();
            friendsScroll.setVvalue(vvalue + -deltaY/width); // deltaY/width to make the scrolling equally fast regardless of the actual width of the component
        });
    }


    public void init(MasterService service, User currentUser, Stage currentStage, Stage returnStage, Stage backStage) {
        try {
            this.service = service;
            this.currentStage = currentStage;
            this.currentUser = currentUser;
            this.returnStage = returnStage;
            this.backStage = backStage;
            this.prietenii = FXCollections.observableArrayList();

            this.firstnameLabel.setText(this.currentUser.getFirstName());
            this.lastnameLabel.setText(this.currentUser.getLastName());

            int columnIndex = 0, rowIndex = 1;

            this.setFriendslistScrollSpeed(15);

            prietenii.setAll(this.service.getFriendshipsDTO(this.currentUser.getID()));

            for (FriendshipDTO f : prietenii) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/friend-item.fxml"));
                AnchorPane root = loader.load();
                FrienditemController controller = loader.getController();
                controller.init(f, service, this);
                if(columnIndex == 3) {
                    columnIndex = 0;
                    rowIndex++;
                }
                friendsGrid.add(root, columnIndex++, rowIndex);

                friendsGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                friendsGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                friendsGrid.setMaxWidth(Region.USE_PREF_SIZE);

                friendsGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                friendsGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                friendsGrid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(root, new Insets(15));
            }

        }
        catch (IOException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }

    @FXML
    private void addClicked() throws IOException {
        EdgemenuController.instantiate().openExplore(service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    private void deleteClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/deletefriend-page.fxml"));
        AnchorPane root = fxmlLoader.load();
        RemovefriendController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.setTitle("PingChat | TAKE A BREAK FROM YOUR PINGS");
        controller.init(service, currentUser, stage, returnStage);
        currentStage.close();
        stage.show();
    }

    @FXML
    private void eventsClicked() throws IOException {
        EdgemenuController.instantiate().openBlizz(service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    private void leaveClicked() {
        EdgemenuController.instantiate().openLeave(currentStage, backStage);
    }

    @FXML
    private void disconnectClicked() {
        EdgemenuController.instantiate().openDisconnect(currentStage, returnStage);
    }

    @FXML
    private void chatClicked() throws IOException {
        EdgemenuController.instantiate().openChat(this.chatUser, service, currentUser, currentStage, returnStage, currentStage);
    }

    public void openChat(User chatUser) throws IOException {
        this.chatUser = chatUser;
        this.chatClicked();
    }

    public void removeFriend(FriendshipDTO friend) {
        try {
            this.service.deleteFriendship(this.currentUser.getID(), friend.getUsername());
        }
        catch (InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }
}
