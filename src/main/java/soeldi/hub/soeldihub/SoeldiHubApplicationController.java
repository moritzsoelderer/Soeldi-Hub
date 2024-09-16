package soeldi.hub.soeldihub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import soeldi.hub.soeldihub.model.DatabaseService;
import soeldi.hub.soeldihub.model.entities.Session;
import soeldi.hub.soeldihub.model.entities.User;

import static soeldi.hub.soeldihub.utils.FlowPaneCreator.createFlowView;

public class SoeldiHubApplicationController {

    @FXML
    private VBox friendsVbox;
    @FXML
    private Label welcomeProfileLabel;
    @FXML
    private Label usernameProfileLabel;
    @FXML
    private ScrollPane flowContainer;
    @FXML
    private VBox flowsVbox;
    @FXML
    private VBox contentVbox;

    @FXML
    void initialize() {
        //TODO replace with real implementation
        DatabaseService.getInstance()
                .findUser("soeldi", "soeldispassword")
                .map(this::createFriendsNode)
                .ifPresent(friendsVbox.getChildren()::add);
        DatabaseService.getInstance()
                .findUser("sophie", "sophiespassword")
                .map(this::createFriendsNode)
                .ifPresent(friendsVbox.getChildren()::add);

        Session.getInstance().map(Session::getUsername).ifPresent(usernameProfileLabel::setText);
        fillContentWithLatestFlows();
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

    private void fillContentWithLatestFlows() {
        DatabaseService.getInstance().findLatestFlows().ifPresent(
                flows -> flows.forEach(
                        optFlow -> optFlow.
                                map(flow -> flowsVbox.getChildren().add(createFlowView(flow, contentVbox)))
                )
        );
    }
}
