package org.example.oop.Application;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public interface AlarmSystemInterface {
    void initializeSystem(Stage stage);
    void reloadAlarmList();
    void showAndAddAlarmDialog();
    void shutDown();
    void setupUIContainers(VBox alarmsContainer, Label noAlarmsLabel);

}
