package soeldi.hub.soeldihub.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class WindowUtils {

    private WindowUtils(){

    }

    public static void openNewWindow(final URL urlToFxml, final String title, final URL urlToCss) {
        try{
            final Parent root = FXMLLoader.load(urlToFxml);
            final Stage stage = new Stage();
            final Scene scene = new Scene(root, 1000, 800);
            scene.getStylesheets().add(Objects.requireNonNull(urlToCss).toExternalForm());
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException exception) {

        }
    }

    public static void closeWindow(final Node node) {
        node.getScene().getWindow().hide();
    }

}
