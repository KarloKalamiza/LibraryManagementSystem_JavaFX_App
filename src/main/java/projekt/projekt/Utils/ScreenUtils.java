package projekt.projekt.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projekt.projekt.HelloApplication;

import java.io.IOException;
import java.util.Objects;

public class ScreenUtils {

    public static void setNewSceneToStage(String file, String appName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(file));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getMainStage().setTitle(appName);
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
}