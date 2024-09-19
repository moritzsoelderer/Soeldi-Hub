package soeldi.hub.soeldihub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import soeldi.hub.soeldihub.model.DatabaseService;
import soeldi.hub.soeldihub.model.entities.Session;
import soeldi.hub.soeldihub.model.entities.User;

import java.net.URL;
import java.util.Optional;

import static soeldi.hub.soeldihub.utils.FlowPaneCreator.addStyleClassAndToChildren;
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
    private HBox refreshButtonHBox;

    @FXML
    void initialize() {
        //TODO replace with real implementation
        DatabaseService.getInstance()
                .findUser("soeldi", "soeldispassword")
                .flatMap(this::createFriendsNode)
                .ifPresent(friendsVbox.getChildren()::add);
        DatabaseService.getInstance()
                .findUser("sophie", "sophiespassword")
                .flatMap(this::createFriendsNode)
                .ifPresent(friendsVbox.getChildren()::add);

        initializeRefreshButton();
        Session.getInstance().map(Session::getUsername).ifPresent(usernameProfileLabel::setText);
        fillContentWithLatestFlows();
    }

    private void initializeRefreshButton() {
        final URL imageUrl = SoeldiHubApplication.class.getResource("icons/RefreshIcon.png");
        Optional.ofNullable(imageUrl)
                .map(URL::toString)
                .map(Image::new)
                .map(ImageView::new)
                .map(iv -> {
                    iv.setFitWidth(20);
                    iv.setFitHeight(20);
                    return iv;
                })
                .map(StackPane::new)
                .flatMap(sp -> addStyleClassAndToChildren(sp, refreshButtonHBox, "refresh-button"))
                .ifPresent(sp -> sp.setOnMouseClicked(this::reloadFlows));
    }

    private Optional<HBox> createFriendsNode(final User user) {
        return Optional.ofNullable(user)
                .map(User::username)
                .map(Label::new)
                .map(VBox::new)
                .map(HBox::new);
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
        DatabaseService.getInstance()
                .findLatestFlows()
                .ifPresent(
                      list -> list.forEach(flow -> flowsVbox.getChildren().add(createFlowView(flow, contentVbox)))
                );
    }

    @FXML
    private void reloadFlows(final MouseEvent mouseEvent) {
        flowsVbox.getChildren().clear();
        fillContentWithLatestFlows();
    }
}
