module soeldi.hub.soeldihub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens soeldi.hub.soeldihub to javafx.fxml;
    exports soeldi.hub.soeldihub;
}