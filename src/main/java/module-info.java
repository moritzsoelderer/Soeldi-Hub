module soeldi.hub.soeldihub {
    requires javafx.controls;
    requires javafx.fxml;


    opens soeldi.hub.soeldihub to javafx.fxml;
    exports soeldi.hub.soeldihub;
}