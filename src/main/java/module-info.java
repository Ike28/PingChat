module com.map222.sfmc.socialnetworkfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;


    opens com.map222.sfmc.socialnetworkfx to javafx.fxml;
    exports com.map222.sfmc.socialnetworkfx;
    exports com.map222.sfmc.socialnetworkfx.controller to javafx.fxml;
    opens com.map222.sfmc.socialnetworkfx.controller to javafx.fxml;
}