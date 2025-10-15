package org.example.oop.Application;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.oop.Models.AlarmInterface;

public interface AlarmSystemInterface {
    void initializeSystem(Stage stage);
    void reloadAlarmList();
    void showAndAddAlarmDialog();

    void showEditAlarmDialog(AlarmInterface alarm);

    void shutDown();
    void setupUIContainers(VBox alarmsContainer, Label noAlarmsLabel);

}
