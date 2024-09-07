package soeldi.hub.soeldihub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import soeldi.hub.soeldihub.login.LoginController;

import java.io.IOException;
import java.util.Objects;

public class SoeldiHubApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("Login.fxml"));
        final Scene scene = new Scene(fxmlLoader.load(), 800,  500);
        scene.getStylesheets().add(Objects.requireNonNull(LoginController.class.getResource("LoginStyle.css")).toExternalForm());
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}