package soeldi.hub.soeldihub;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SoeldiHubController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}