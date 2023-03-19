package com.map222.sfmc.socialnetworkfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import com.map222.sfmc.socialnetworkfx.domain.business.Request;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.io.IOException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AddpageController {
    private MasterService service;
    private User currentUser;
    private Stage currentStage;
    private Stage returnStage;
    private Stage backStage;
    private ObservableList<User> users;
    private ObservableList<Request> receivedRequests;

    @FXML
    private ImageView backOption;

    @FXML
    private ImageView chatOption;

    @FXML
    private ImageView connectOption;

    @FXML
    private Label firstnameLabel;

    @FXML
    private ImageView friendsOption;

    @FXML
    private Label lastnameLabel;

    @FXML
    private ImageView moreOption;

    @FXML
    private GridPane requestsGrid;

    @FXML
    private ScrollPane requestsScroll;

    @FXML
    private GridPane usersGrid;

    @FXML
    private ScrollPane usersScroll;

    @FXML
    private void chatClicked() throws IOException {
        EdgemenuController.instantiate().openChat(null, service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    private void disconnectClicked() {
        EdgemenuController.instantiate().openDisconnect(currentStage, returnStage);
    }

    @FXML
    private void eventsClicked() throws IOException {
        EdgemenuController.instantiate().openBlizz(service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    private void friendsClicked() throws IOException {
        EdgemenuController.instantiate().openFriends(service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    private void leaveClicked() {
        EdgemenuController.instantiate().openLeave(currentStage, backStage);
    }

    public void sendRequest(User other) {
        try {
            this.service.sendRequest(this.currentUser.getID(), other.getID());
        }
        catch (InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }

    public void acceptRequest(User other) {
        try {
            this.service.acceptRequest(other.getID(), this.currentUser.getID());
        }
        catch (InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }

    public void rejectRequest(User other) {
        try {
            this.service.rejectRequest(other.getID(), this.currentUser.getID());
        }
        catch (InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }

    private void setUserslistScrollSpeed(double coef) {
        usersGrid.setOnScroll(event -> {
            double deltaY = event.getDeltaY()*coef; // to make the scrolling a bit faster
            double width = usersScroll.getContent().getBoundsInLocal().getWidth();
            double vvalue = usersScroll.getVvalue();
            usersScroll.setVvalue(vvalue + -deltaY/width); // deltaY/width to make the scrolling equally fast regardless of the actual width of the component
        });
    }

    private void setRequestslistScrollSpeed(double coef) {
        requestsGrid.setOnScroll(event -> {
            double deltaY = event.getDeltaY()*coef; // to make the scrolling a bit faster
            double width = requestsScroll.getContent().getBoundsInLocal().getWidth();
            double vvalue = requestsScroll.getVvalue();
            requestsScroll.setVvalue(vvalue + -deltaY/width); // deltaY/width to make the scrolling equally fast regardless of the actual width of the component
        });
    }

    private void setDisplayPrefs(GridPane gridPane) {
        gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxWidth(Region.USE_PREF_SIZE);

        gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxHeight(Region.USE_PREF_SIZE);
    }


    public void init(MasterService service, User currentUser, Stage currentStage, Stage returnStage, Stage backStage) throws IOException {
        this.service = service;
        this.currentUser = currentUser;
        this.currentStage = currentStage;
        this.returnStage = returnStage;
        this.backStage  = backStage;

        this.users = FXCollections.observableArrayList();
        this.receivedRequests = FXCollections.observableArrayList();
        this.firstnameLabel.setText(this.currentUser.getFirstName());
        this.lastnameLabel.setText(this.currentUser.getLastName());

        this.setRequestslistScrollSpeed(5);
        this.setUserslistScrollSpeed(15);

        users.setAll(this.service.getUsersNotFriends(this.currentUser.getID()));
        receivedRequests.setAll(this.service.getReceivedRequests(this.currentUser.getID()));

        int columnIndex = 0, rowIndex = 1;
        for(User u: users) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/userscroll-item.fxml"));
            AnchorPane root = loader.load();
            UseritemController controller = loader.getController();
            controller.init(currentUser, u, this);
            if(columnIndex == 3) {
                columnIndex = 0;
                rowIndex++;
            }
            usersGrid.add(root, columnIndex++, rowIndex);
            setDisplayPrefs(usersGrid);

            GridPane.setMargin(root, new Insets(15));
        }

        int columnIndexReq = 0, rowIndexReq = 1;
        for(Request r: receivedRequests) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/requestscroll-item.fxml"));
            AnchorPane root = loader.load();
            ReceivedReqitemController controller = loader.getController();
            controller.init(this.service.getUser(r.get_sender()), this);
            if(columnIndexReq == 1) {
                columnIndexReq = 0;
                rowIndexReq++;
            }
            requestsGrid.add(root, columnIndexReq++, rowIndexReq);
            setDisplayPrefs(requestsGrid);
            GridPane.setMargin(root, new Insets(30));
        }
    }

}
