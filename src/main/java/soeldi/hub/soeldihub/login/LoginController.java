package soeldi.hub.soeldihub.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import soeldi.hub.soeldihub.SoeldiHubApplication;
import soeldi.hub.soeldihub.model.DatabaseService;
import soeldi.hub.soeldihub.model.entities.Session;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static soeldi.hub.soeldihub.model.entities.User.newUser;
import static soeldi.hub.soeldihub.utils.WindowUtils.closeWindow;
import static soeldi.hub.soeldihub.utils.WindowUtils.openNewWindow;

public class LoginController {

    private static final String LOGIN_TEXT = "Login";
    private static final String SIGNUP_TEXT = "Sign-Up";
    private static final String GITHUB_LINK = "https://github.com/moritzsoelderer";
    @FXML
    private Label titleLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private HBox loginButtonsHBox;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Hyperlink socialsHyperLink1;
    @FXML
    private ImageView socialsImageView1;
    private Button backtoLoginButton;

    private boolean isSignUpMode;
    @FXML
    private Label errorLabel;

    @FXML
    void initialize() {
        titleLabel.setText(LOGIN_TEXT);
        isSignUpMode = false;
        backtoLoginButton = new Button("back");
        backtoLoginButton.setOnAction(e -> exitSignUpMode());
        errorLabel.setVisible(false);

        loadSocialsIcons();
    }


    @FXML
    private void onLoginButtonClicked(final ActionEvent actionEvent) {
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        Optional.of(DatabaseService.getInstance())
                .filter(service -> this.validateLoginInput())
                .flatMap(service -> service.findUser(username, password))
                .ifPresentOrElse(
                        user -> proceedWithLogin(user.username()),
                        () -> {
                            errorLabel.setText("Could not log in");
                            errorLabel.setVisible(true);
                        }
                );
    }

    private void proceedWithLogin(final String username) {
        titleLabel.setText("Willkommen " + username);
        Session.initialize(username);
        openNewWindow(SoeldiHubApplication.class.getResource("SoeldiHubApplication.fxml"),
                "Soeldi Hub",
                SoeldiHubApplication.class.getResource("SoeldiHubApplicationStyle.css")
        );
        closeWindow(errorLabel);
    }

    @FXML
    private void onSignUpButtonClicked(final ActionEvent actionEvent) {
        if(!isSignUpMode) {
            enterSignUpMode();
        }
        else {
            final String username = usernameField.getText();
            final String password = passwordField.getText();
            Optional.of(DatabaseService.getInstance())
                    .filter(service -> this.validateLoginInput())
                    .flatMap(service -> service.createUser(newUser(username, password)))
                    .filter(bool -> bool)
                    .ifPresentOrElse(
                            bool -> proceedWithLogin(username),
                            () -> {
                                errorLabel.setText("Could not create user");
                                errorLabel.setVisible(true);
                            }
                    );
        }
    }

    private void enterSignUpMode() {
        titleLabel.setText(SIGNUP_TEXT);
        loginButtonsHBox.getChildren().remove(loginButton);
        loginButtonsHBox.getChildren().add(backtoLoginButton);
        signUpButton.getStyleClass().add("suggested-button");
        this.isSignUpMode = true;
    }

    private void exitSignUpMode() {
        titleLabel.setText(LOGIN_TEXT);
        loginButtonsHBox.getChildren().clear();
        loginButtonsHBox.getChildren().add(loginButton);
        loginButtonsHBox.getChildren().add(signUpButton);
        signUpButton.getStyleClass().remove("suggested-button");
        this.isSignUpMode = false;
    }

    private boolean validateLoginInput() {
        if(usernameField.getText() == null || usernameField.getText().isEmpty()) {
            return false;
        }
        return passwordField.getText() != null && !passwordField.getText().isEmpty();
    }

    @FXML
    private void visitGithub(final ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI(GITHUB_LINK));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    void loadSocialsIcons() {
        try(final InputStream d = SoeldiHubApplication.class.getResourceAsStream("icons/github-icon.png")){
            assert d != null;
            final Image githubIconImage = new Image(d);
            socialsImageView1.setImage(githubIconImage);
            socialsImageView1.setFitHeight(50);
            socialsImageView1.setFitWidth(50);
        } catch (IOException ignored) {
            //do nothing
        }
    }
}