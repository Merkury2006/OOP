package org.example.oop.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.oop.Config.AppConfig;
import org.example.oop.Controllers.MainController;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/org/example/oop/main.fxml"));


        AlarmSystemInterface alarmSystem = new AlarmSystem();

        MainController mainController = new MainController(alarmSystem, alarmSystem::showAndAddAlarmDialog);

        fxmlLoader.setController(mainController);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(AppConfig.APP_TITLE);
        stage.setScene(scene);

        alarmSystem.initializeSystem(stage);
        stage.setOnCloseRequest(event -> alarmSystem.shutDown());

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}