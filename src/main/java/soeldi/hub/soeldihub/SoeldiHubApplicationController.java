package soeldi.hub.soeldihub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import soeldi.hub.soeldihub.model.DatabaseService;
import soeldi.hub.soeldihub.model.entities.Flow;
import soeldi.hub.soeldihub.model.entities.Session;
import soeldi.hub.soeldihub.model.entities.User;

import java.util.List;
import java.util.Optional;

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

    private VBox createFlowMediaView(final Flow flow) {
        final double parentHeightRatio = 0.7;

        final Media media = new Media(flow.source().toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        final MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.fitHeightProperty().bind(contentVbox.heightProperty().multiply(parentHeightRatio));

        final VBox parentVbox = new VBox(mediaView);
        parentVbox.setAlignment(Pos.TOP_CENTER);
        parentVbox.setStyle("-fx-background-radius: 20; -fx-background-color: #212121");

        mediaView.setOnMouseClicked(e -> {
            if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            }
            else {
                mediaPlayer.play();
            }
        });
        return parentVbox;
    }

    private void fillContentWithLatestFlows() {
        Optional<List<Optional<Flow>>> optFlows = DatabaseService.getInstance().findLatestFlows();
        if(optFlows.isEmpty()){
            return;
        }

        optFlows.get().forEach(
                optFlow -> optFlow.map(flow -> flowsVbox.getChildren().add(createFlowMediaView(flow)))
        );
    }
}
