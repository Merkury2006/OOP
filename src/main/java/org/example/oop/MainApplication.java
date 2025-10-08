package org.example.oop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.oop.Controllers.MainController;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Будильник");
        stage.setScene(scene);
        stage.show();
        MainController controller = fxmlLoader.getController();
        controller.setMainStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }

}