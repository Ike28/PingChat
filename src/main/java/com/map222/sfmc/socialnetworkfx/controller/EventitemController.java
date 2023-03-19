package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import com.map222.sfmc.socialnetworkfx.domain.business.Event;

import java.util.Objects;

public class EventitemController {
    private Event currentEvent;
    private EventspageController rootController;

    @FXML
    private Button cancelButton;

    @FXML
    private Button joinButton;

    @FXML
    private ImageView pfpView;

    @FXML
    private Label titleLabel;

    @FXML
    private void joinClicked() {
        if(Objects.equals(this.joinButton.getText(), "JOIN")) {
            this.rootController.addParticipation(this.currentEvent.getID());
            this.joinButton.setText("✅ GOING");
            this.joinButton.setStyle("-fx-background-color: #00ffb0");
            this.cancelButton.setVisible(true);
            this.cancelButton.setMouseTransparent(false);
        }
    }

    @FXML
    private void cancelClicked() {
        if(!Objects.equals(this.joinButton.getText(), "JOIN")) {
            this.rootController.removeParticipation(this.currentEvent.getID());
            this.joinButton.setText("JOIN");
            this.joinButton.setStyle("-fx-background-color: #1d9198");
            this.cancelButton.setVisible(false);
            this.cancelButton.setMouseTransparent(true);
        }
    }

    public void init(Event currentEvent, EventspageController rootController, boolean participating) {
        this.currentEvent = currentEvent;
        this.rootController = rootController;

        this.titleLabel.setText(this.currentEvent.getTitle());

        if(!participating) {
            this.cancelButton.setVisible(false);
            this.cancelButton.setMouseTransparent(true);
        }
        else {
            this.joinButton.setText("✅ GOING");
            this.joinButton.setStyle("-fx-background-color: #00ffb0");
        }
    }
}
