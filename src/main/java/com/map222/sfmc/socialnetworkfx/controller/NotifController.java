package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.map222.sfmc.socialnetworkfx.domain.business.Event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class NotifController {
    @FXML
    private Label timeLabel;

    @FXML
    private Label titleLabel;

    public void init(Event currentEvent) {
        this.titleLabel.setText(currentEvent.getTitle());
        this.timeLabel.setText("in " + ChronoUnit.DAYS.between(LocalDateTime.now(), currentEvent.getDate()) + " days");
    }
}
