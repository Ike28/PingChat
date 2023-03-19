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
import com.map222.sfmc.socialnetworkfx.domain.business.Event;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventspageController {
    private MasterService service;
    private User currentUser;
    private ObservableList<Event> events;
    private Stage currentStage;
    private Stage returnStage;
    private Stage backStage;

    private List<UUID> addParticipations;
    private List<UUID> removeParticipations;

    @FXML
    private ImageView backOption;

    @FXML
    private ImageView chatOption;

    @FXML
    private ImageView connectOption;

    @FXML
    private GridPane eventsGrid;

    @FXML
    private Label firstnameLabel;

    @FXML
    private ImageView friendsOption;

    @FXML
    private ScrollPane eventsScroll;

    @FXML
    private Label lastnameLabel;

    @FXML
    private ImageView moreOption;

    @FXML
    private Button createButton;

    @FXML
    private DatePicker datePick;

    @FXML
    private TextField titleField;

    @FXML
    private ScrollPane notifScroll;

    @FXML
    private GridPane notifGrid;

    private void showNotifications() throws IOException {
        List<Pair<UUID, String>> participations = this.service.getParticipationsOfUser(this.currentUser.getID());
        int rowIndex = 0;
        for(Pair<UUID, String> p : participations) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/notif-item.fxml"));
            AnchorPane root = loader.load();
            NotifController controller = loader.getController();
            controller.init(this.service.getEventByID(p.getFirstOfPair()));

            notifGrid.add(root, 0, rowIndex++);
            GridPane.setMargin(root, new Insets(20));
        }
        setDisplayPref(notifGrid);
    }

    @FXML
    private void createClicked() {
        try {
            LocalDateTime eventDate = datePick.getValue().atTime(LocalTime.NOON);
            if (titleField.getText().isEmpty())
                throw new ValidationException("\nEnter the event's title");
            String title = titleField.getText();
            Event newEvent = new Event(title, eventDate);
            this.service.addEvent(newEvent);
            AlertController.showAlert(null, Alert.AlertType.CONFIRMATION, "SUCCESS", "Blizz Event \"" + title + "\" created");
            this.titleField.clear();
            displayEvents();
        }
        catch (ValidationException | IOException | InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }

    @FXML
    void addClicked() throws IOException {
        logParticipations();
        EdgemenuController.instantiate().openExplore(service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    void chatClicked() throws IOException {
        logParticipations();
        EdgemenuController.instantiate().openChat(null, service, currentUser, currentStage, returnStage, currentStage);
    }

    @FXML
    void disconnectClicked() {
        logParticipations();
        EdgemenuController.instantiate().openDisconnect(currentStage, returnStage);
    }

    @FXML
    void friendsClicked() throws IOException {
        logParticipations();
        EdgemenuController.instantiate().openFriends(service, this.currentUser, this.currentStage, this.returnStage, this.currentStage);
    }

    @FXML
    void leaveClicked() {
        logParticipations();
        EdgemenuController.instantiate().openLeave(currentStage, backStage);
    }

    private void logParticipations() {
        for(UUID id: this.addParticipations)
            this.service.addParticipation(this.currentUser.getID(), id);
        for(UUID id: this.removeParticipations)
            this.service.removeParticipation(this.currentUser.getID(), id);
    }

    public void addParticipation(UUID eventID) {
        if(this.removeParticipations.contains(eventID))
            this.removeParticipations.remove(eventID);
        else this.addParticipations.add(eventID);
    }

    public void removeParticipation(UUID eventID) {
        if(this.addParticipations.contains(eventID))
            this.addParticipations.remove(eventID);
        else this.removeParticipations.add(eventID);
    }

    private void setDisplayPref(GridPane gridPane) {
        gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxWidth(Region.USE_PREF_SIZE);

        gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxHeight(Region.USE_PREF_SIZE);
    }

    private void displayEvents() throws IOException {
        events.setAll(this.service.getAllEvents());
        int rowIndex = 1, columnIndex = 0;
        List<Pair<UUID, String>> participations = this.service.getParticipationsOfUser(this.currentUser.getID());

        for(Event e: this.events) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/event-item.fxml"));
            AnchorPane root = loader.load();
            EventitemController controller = loader.getController();

            controller.init(e, this, participations.contains(new Pair<>(e.getID(), this.currentUser.getID())));
            if(columnIndex == 2) {
                columnIndex = 0;
                rowIndex++;
            }
            eventsGrid.add(root, columnIndex++, rowIndex);
            GridPane.setMargin(root, new Insets(20));
        }
        this.setDisplayPref(eventsGrid);
        showNotifications();
    }

    public void init(MasterService service, User currentUser, Stage currentStage, Stage returnStage, Stage backStage) throws IOException {
        this.service = service;
        this.currentUser = currentUser;
        this.currentStage = currentStage;
        this.returnStage = returnStage;
        this.backStage = backStage;

        this.firstnameLabel.setText(this.currentUser.getFirstName());
        this.lastnameLabel.setText(this.currentUser.getLastName());
        events = FXCollections.observableArrayList();
        this.addParticipations = new ArrayList<>();
        this.removeParticipations = new ArrayList<>();
        this.displayEvents();
    }
}
