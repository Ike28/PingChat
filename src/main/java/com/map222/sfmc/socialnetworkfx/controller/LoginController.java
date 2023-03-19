package com.map222.sfmc.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.io.IOException;

public class LoginController {
    private Stage currentStage;
    private MasterService service;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label usnOverheadLabel;

    @FXML
    private Label passOverheadLabel;

    @FXML
    private AnchorPane loginPane;

    public void init(Stage currentStage, MasterService service) {
        this.currentStage = currentStage;
        this.service = service;
        usnDefault();
        passDefault();
    }

    @FXML
    private void loginClicked() throws IOException{
        try {
            if(usernameField.getText().isEmpty())
                throw new ValidationException("\nEnter your username");
            if(passwordField.getText().isEmpty())
                throw new ValidationException("\nEnter your password");

            String username = usernameField.getText();
            String password = passwordField.getText();
            if(this.service.authorizeConnect(username, password)) {
                User user = this.service.getUser(username);
                //this.currentStage.close();
                this.switchToLaunchPage(user);
            }
            else AlertController.showError(null, "Invalid username and password combination");
        }
        catch (ValidationException | InvalidIDException exception) {
            AlertController.showError(null, exception.getMessage());
        }
    }

    @FXML
    private void registerClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/register-page.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage stage = new Stage();
            stage.setResizable(false);

            RegisterController controller = loader.getController();
            stage.setScene(new Scene(root));
            stage.setTitle("PingChat | JOIN THE COMMUNITY");
            controller.init(stage, currentStage, service);
            currentStage.close();
            stage.show();
        }
        catch (IOException e) {
            AlertController.showError(null, e.getMessage());
        }
    }

    private void switchToLaunchPage(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/launch-page.fxml"));
        AnchorPane root = loader.load();

        LaunchpageController controller = loader.getController();
        currentStage.setScene(new Scene(root));
        currentStage.setTitle("PingChat | HOME");
        currentStage.show();
        controller.init(service, user, currentStage, currentStage);
    }

    @FXML
    private void backgroundOnClick() {
        usnDefault();
        passDefault();
        loginPane.requestFocus();
    }

    @FXML
    private void usnOnClick() {
        passDefault();
        usnOverheadLabel.setVisible(true);
        usnOverheadLabel.setMouseTransparent(false);
    }

    @FXML
    private void usnDefault() {
        usnOverheadLabel.setVisible(false);
        usnOverheadLabel.setMouseTransparent(true);
    }

    @FXML
    private void passOnClick() {
        usnDefault();
        passOverheadLabel.setVisible(true);
        passOverheadLabel.setMouseTransparent(false);
    }

    @FXML
    private void passDefault() {
        passOverheadLabel.setVisible(false);
        passOverheadLabel.setMouseTransparent(true);
    }
}
