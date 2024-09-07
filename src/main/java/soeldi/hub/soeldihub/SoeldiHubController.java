package soeldi.hub.soeldihub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class SoeldiHubController {

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
    void initialize() {
        titleLabel.setText(LOGIN_TEXT);
        isSignUpMode = false;
        backtoLoginButton = new Button("back");
        backtoLoginButton.setOnAction(e -> exitSignUpMode());

        loadSocialsIcons();
    }

    void loadSocialsIcons() {
        try(final InputStream d = getClass().getResourceAsStream("icons/github-icon.png")){
            assert d != null;
            final Image githubIconImage = new Image(d);
            socialsImageView1.setImage(githubIconImage);
            socialsImageView1.setFitHeight(50);
            socialsImageView1.setFitWidth(50);
        } catch (IOException ignored) {
            //do nothing
        }
    }


    @FXML
    private void onLoginButtonClicked(final ActionEvent actionEvent) {
        if(validateLoginInput()) {
            //TODO implement Login logic
        }
    }

    @FXML
    private void onSignUpButtonClicked(final ActionEvent actionEvent) {
        if(!isSignUpMode) {
            enterSignUpMode();
        }
        else {
            executeSignUp();
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

    private void executeSignUp() {
        //TODO implement sign up logic
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
}