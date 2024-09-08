package soeldi.hub.soeldihub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import soeldi.hub.soeldihub.model.DatabaseService;
import soeldi.hub.soeldihub.model.entities.Session;
import soeldi.hub.soeldihub.model.entities.User;

public class SoeldiHubApplicationController {

    @FXML
    private VBox friendsVbox;
    @FXML
    private Label welcomeProfileLabel;
    @FXML
    private Label usernameProfileLabel;

    @FXML
    void initialize() {
        //TODO replace with real implementation
        final User soeldiUser = DatabaseService.getInstance().findUser("soeldi", "soeldispassword").get();
        final User sophieUser = DatabaseService.getInstance().findUser("sophie", "sophiespassword").get();
        friendsVbox.getChildren().add(createFriendsNode(soeldiUser));
        friendsVbox.getChildren().add(createFriendsNode(sophieUser));

        if(Session.getInstance().isPresent()){
            usernameProfileLabel.setText(Session.getInstance().get().getUsername());
        }
    }

    private Node createFriendsNode(final User user) {
        final Label usernameLabel = new Label(user.username());
        final VBox vBox = new VBox(usernameLabel);
        return new HBox(vBox);
    }

    @FXML
    private void onActionForYouButton(final ActionEvent actionEvent) {
        //TODO implement behaviour
    }

    @FXML
    private void onActionFollowedButton(final ActionEvent actionEvent) {
        //TODO implement behaviour
    }

    @FXML
    private void onActionSearchButton(final ActionEvent actionEvent) {
        //TODO implement behaviour
    }
}
