package soeldi.hub.soeldihub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import soeldi.hub.soeldihub.model.DatabaseService;
import soeldi.hub.soeldihub.model.entities.Flow;
import soeldi.hub.soeldihub.model.entities.Session;
import soeldi.hub.soeldihub.model.entities.User;

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

    private static StackPane createFlowMediaView(final Flow flow, final Region parentRegion) {
        final double parentHeightRatio = 0.75;
        final double nineToSixteenRatio = 9.0 / 16.0;

        final Media media = new Media(flow.source().toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        final StackPane parentStackPane = new StackPane();
        parentStackPane.setStyle("-fx-background-radius: 20; -fx-background-color: #212121");
        parentStackPane.maxWidthProperty()
                .bind(parentRegion.heightProperty().multiply(parentHeightRatio * nineToSixteenRatio));
        parentStackPane.maxHeightProperty()
                .bind(parentRegion.heightProperty().multiply(parentHeightRatio));

        final MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setPreserveRatio(false);
        mediaView.fitWidthProperty().bind(parentStackPane.maxWidthProperty());
        mediaView.fitHeightProperty().bind(parentStackPane.maxHeightProperty());

        final Rectangle rectangle = new Rectangle();
        rectangle.heightProperty().bind(parentRegion.heightProperty().multiply(parentHeightRatio));
        rectangle.widthProperty().bind(parentStackPane.widthProperty());
        rectangle.setArcWidth(40);
        rectangle.setArcHeight(40);

        parentStackPane.setClip(rectangle);
        parentStackPane.getChildren().add(mediaView);

        final int userId =  Session.getInstance().orElseThrow().getUserId();
        final boolean isLikedByUser = DatabaseService.getInstance().isLikedBy(userId, flow.id().orElseThrow());

        final VBox likeButtonVbox = createLikeButtonVbox(
                flow.id().orElseThrow(), flow.likes().orElse(0), isLikedByUser
        );

        final AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: transparent;");
        parentStackPane.getChildren().add(anchorPane);
        AnchorPane.setBottomAnchor(likeButtonVbox, 20.0);
        AnchorPane.setRightAnchor(likeButtonVbox, 20.0);
        anchorPane.getChildren().add(likeButtonVbox);

        anchorPane.setOnMouseClicked(e -> {
            if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            }
            else {
                mediaPlayer.play();
            }
        });
        return parentStackPane;
    }

    private static VBox createLikeButtonVbox(final int flowId, final int likes, final boolean isLikedByUser) {
        final Label label = new Label(String.valueOf(likes));
        label.getStyleClass().add("like-label");

        final Button button = new Button("like");
        button.getStyleClass().add("like-button");
        button.setOnAction(e -> addLikeToFlow(flowId, label, button));
        if(isLikedByUser) {
            button.setStyle("-fx-background-color: red");
        }

        final VBox vBox = new VBox(button, label);
        vBox.getStyleClass().add("like-vbox");

        return vBox;
    }

    private static void addLikeToFlow(final int flowId, final Label likeLabel, final Button likeButton) {
        Optional.of(DatabaseService.getInstance())
                .flatMap(service -> service.createLike(Session.getInstance().orElseThrow().getUserId(), flowId))
                .filter(bool -> bool)
                .ifPresent(
                        bool -> {
                            likeLabel.setText(String.valueOf(Integer.parseInt(likeLabel.getText()) + 1));
                            likeButton.setStyle("-fx-background-color: red;");
                        }
                );
    }

    private void fillContentWithLatestFlows() {
        DatabaseService.getInstance().findLatestFlows().ifPresent(
                flows -> flows.forEach(
                        optFlow -> optFlow.
                                map(flow -> flowsVbox.getChildren().add(createFlowMediaView(flow, contentVbox)))
                )
        );
    }
}
