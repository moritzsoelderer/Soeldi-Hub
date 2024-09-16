package soeldi.hub.soeldihub.utils;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import soeldi.hub.soeldihub.SoeldiHubApplication;
import soeldi.hub.soeldihub.model.DatabaseService;
import soeldi.hub.soeldihub.model.entities.Flow;
import soeldi.hub.soeldihub.model.entities.Session;

import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

public class FlowPaneCreator {

    public static StackPane createFlowView(final Flow flow, final Region parentRegion) {
        final double parentHeightRatio = 0.75;
        final double nineToSixteenRatio = 9.0 / 16.0;
        final int userId =  Session.getInstance().orElseThrow().getUserId();
        final boolean isLikedByUser = DatabaseService.getInstance().isLikedBy(userId, flow.id().orElseThrow());
        final String username = DatabaseService.getInstance().findUser(flow.uploadedBy()).orElseThrow().username();

        final StackPane parentStackPane = new StackPane();
        final Optional<MediaView> mediaView = createFlowMediaView(flow, parentStackPane);
        final Optional<VBox> likeButtonVbox = Optional.of(flow)
                .flatMap(Flow::id)
                .map(id -> createLikeButtonVbox(id, flow.likes().orElse(0), isLikedByUser));
        final Optional<VBox> flowInfoVbox = Optional.of(flow)
                .flatMap(Flow::uploadedAt)
                .map(uploadedAt -> createFlowInfoVbox(username, flow.title(), flow.caption(), uploadedAt));
        final Rectangle rectangle = new Rectangle();
        final HBox flowHbox = new HBox();
        final VBox flowVbox = new VBox();

        rectangle.setArcHeight(40);
        rectangle.setArcWidth(40);
        flowHbox.getStyleClass().add("flow-container-hbox");
        flowVbox.getStyleClass().add("flow-container-vbox");

        rectangle.heightProperty().bind(parentRegion.heightProperty().multiply(parentHeightRatio));
        rectangle.widthProperty().bind(parentStackPane.widthProperty());
        parentStackPane.maxWidthProperty()
                .bind(parentRegion.heightProperty().multiply(parentHeightRatio * nineToSixteenRatio));
        parentStackPane.maxHeightProperty()
                .bind(parentRegion.heightProperty().multiply(parentHeightRatio));
        mediaView.ifPresent(v -> flowVbox.setOnMouseClicked(e -> playAndPauseOnFlowClicked(e, flowVbox, v.getMediaPlayer())));

        flowInfoVbox.ifPresent(infobox -> {
            flowHbox.getChildren().add(infobox);
            HBox.setHgrow(infobox, Priority.SOMETIMES);
        });
        likeButtonVbox.ifPresent(likebox -> {
            flowHbox.getChildren().add(likebox);
            HBox.setHgrow(likebox, Priority.NEVER);
        });

        mediaView.ifPresent(view -> parentStackPane.getChildren().add(view));
        parentStackPane.getChildren().add(flowVbox);
        flowVbox.getChildren().add(flowHbox);
        parentStackPane.setClip(rectangle);
        return parentStackPane;
    }

    public static Optional<MediaView> createFlowMediaView(final Flow flow, final Pane paneToBindSizeTo) {
        return Optional.of(flow)
                .map(Flow::source)
                .map(URL::toString)
                .map(Media::new)
                .map(MediaPlayer::new)
                .map(player -> {player.setCycleCount(MediaPlayer.INDEFINITE); return player;})
                .map(MediaView::new)
                .map(
                        mediaView -> {
                            mediaView.setPreserveRatio(false);
                            mediaView.fitWidthProperty().bind(paneToBindSizeTo.maxWidthProperty());
                            mediaView.fitHeightProperty().bind(paneToBindSizeTo.maxHeightProperty());
                            return mediaView;
                        }
                );
    }

    public static VBox createLikeButtonVbox(final int flowId, final int likes, final boolean isLikedByUser) {
        final Label label = new Label(String.valueOf(likes));
        label.getStyleClass().add("like-label");

        final InputStream likeIcon = SoeldiHubApplication.class.getResourceAsStream("icons/LikeIcon.png");
        final ImageView imageView = new ImageView(new Image(likeIcon));
        final StackPane imageViewParent = new StackPane(imageView);
        imageViewParent.setMaxHeight(30);
        imageViewParent.setMaxWidth(30);
        imageViewParent.setStyle("-fx-background-color: transparent");
        imageView.fitHeightProperty().bind(imageViewParent.maxHeightProperty());
        imageView.fitWidthProperty().bind(imageViewParent.maxWidthProperty());

        imageViewParent.getStyleClass().add("like-button");
        imageViewParent.setOnMouseClicked(e -> addLikeToFlow(e, flowId, label, imageView));
        if(isLikedByUser) {
            final InputStream likedLike = SoeldiHubApplication.class.getResourceAsStream("icons/LikeIconLiked.png");
            imageView.setImage(new Image(likedLike));
        }

        final VBox vBox = new VBox(imageViewParent, label);
        vBox.getStyleClass().add("like-vbox");
        return vBox;
    }

    public static VBox createFlowInfoVbox(final String username, final String title, final String caption, final Instant createdAt) {
        final VBox vBox =  new VBox();
        vBox.getStyleClass().add("flow-info-vbox");

        Optional.ofNullable(username)
                .map(Label::new)
                .ifPresent(l -> addStyleClassAndToChildren(l, vBox, "flow-username-label"));
        Optional.ofNullable(title)
                .map(Label::new)
                .ifPresent(l -> addStyleClassAndToChildren(l, vBox, "flow-title-label"));
        Optional.ofNullable(caption)
                .flatMap(FlowPaneCreator::createFlowCaption)
                .ifPresent(l -> vBox.getChildren().add(l));
        Optional.ofNullable(createdAt)
                .map(c -> LocalDate.ofInstant(c, ZoneId.systemDefault()))
                .map(LocalDate::toString)
                .map(Label::new)
                .ifPresent(l -> addStyleClassAndToChildren(l, vBox, "flow-createdAt-label"));
        return vBox;
    }

    public static Optional<Label> createFlowCaption(final String caption) {
        final String captionStyle = "flow-caption-label";
        return Optional.ofNullable(caption)
                .map(Label::new)
                .map(label -> {
                        label.getStyleClass().add(captionStyle);
                        label.setOnMouseClicked( e -> wrapUnwrapLabel(e, label, captionStyle, "flow-caption-label-unwrap"));
                        return label;
                    }
                );
    }

    //TODO maybe implement a idempotent styleclass.add() method
    public static void wrapUnwrapLabel(final Event event, final Label label, final String wrapStyleClass, final String unwrapStyleClass) {
        Optional.ofNullable(label)
                .map(Node::getStyleClass)
                .ifPresent(
                       styleClass -> {
                           if(styleClass.contains(wrapStyleClass)){
                               styleClass.remove(wrapStyleClass);
                               styleClass.add(unwrapStyleClass);
                           }
                           else if(styleClass.contains(unwrapStyleClass)){
                               styleClass.remove(unwrapStyleClass);
                               styleClass.add(wrapStyleClass);
                           }
                       }
                );
        Optional.ofNullable(event)
                .ifPresent(Event::consume);
    }

    public static void playAndPauseOnFlowClicked(final Event event, final Pane paneOnClicked, final MediaPlayer mediaPlayer) {
        Optional.ofNullable(event)
                .filter(e -> e.getSource().equals(paneOnClicked))
                .map(e -> mediaPlayer)
                .map(MediaPlayer::getStatus)
                .filter(status -> status == MediaPlayer.Status.PLAYING)
                .ifPresentOrElse(
                        s -> mediaPlayer.pause(),
                        () -> Optional.ofNullable(mediaPlayer).ifPresent(MediaPlayer::play)
                );
    }

    private static void addLikeToFlow(final Event event, final int flowId, final Label likeLabel, final ImageView likeImageView) {
        Optional.of(DatabaseService.getInstance())
                .flatMap(service -> service.createLike(Session.getInstance().orElseThrow().getUserId(), flowId))
                .filter(bool -> bool)
                .ifPresentOrElse(
                        bool -> {
                            likeLabel.setText(String.valueOf(Integer.parseInt(likeLabel.getText()) + 1));
                            final InputStream likedLike = SoeldiHubApplication.class.getResourceAsStream("icons/LikeIconLiked.png");
                            likeImageView.setImage(new Image(likedLike));
                            event.consume();
                        },
                        event::consume
                );
    }

    public static Optional<Node> addStyleClassAndToChildren(final Node node, final Pane parent, final String styleClass) {
        Optional.ofNullable(node)
                .map(Node::getStyleClass)
                .ifPresent(sc -> sc.add(styleClass));
        Optional.ofNullable(parent)
                .map(Pane::getChildren)
                .ifPresent(p -> p.add(node));
        return Optional.ofNullable(node);
    }
}