package com.map222.sfmc.socialnetworkfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.map222.sfmc.socialnetworkfx.controller.LoginController;
import com.map222.sfmc.socialnetworkfx.domain.utils.DatabaseCredentials;
import com.map222.sfmc.socialnetworkfx.domain.validators.FriendshipValidator;
import com.map222.sfmc.socialnetworkfx.domain.validators.RequestValidator;
import com.map222.sfmc.socialnetworkfx.domain.validators.UserValidator;
import com.map222.sfmc.socialnetworkfx.repository.database.FriendshipDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.repository.database.RequestDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.repository.database.UserDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.service.MasterService;
import com.map222.sfmc.socialnetworkfx.service.NetworkService;
import com.map222.sfmc.socialnetworkfx.service.database.FriendshipDBService;
import com.map222.sfmc.socialnetworkfx.service.database.RequestDBService;
import com.map222.sfmc.socialnetworkfx.service.database.UserDBService;
import com.map222.sfmc.socialnetworkfx.userinterface.StartUI;

import java.io.IOException;

public class Run extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/homepage.fxml"));

        AnchorPane root = loader.load();

        LoginController controller = loader.getController();

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("PingChat | LOG IN");
        primaryStage.setResizable(false);
        controller.init(primaryStage, new MasterService());
        primaryStage.show();
    }
}
