package org.example.oop.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.oop.Application.AlarmSystemInterface;


public class MainController {
    @FXML private Label labelNoAlarms;
    @FXML private VBox alarmsContainer;


    private Runnable showAndAddAlarmDialog;

    private final AlarmSystemInterface alarmSystem;

    public MainController(AlarmSystemInterface alarmSystem, Runnable showAndAddAlarmDialog) {
        this.alarmSystem = alarmSystem;
        this.showAndAddAlarmDialog = showAndAddAlarmDialog;
    }

    @FXML
    public void initialize(){
        alarmSystem.setupUIContainers(alarmsContainer, labelNoAlarms);
        alarmSystem.reloadAlarmList();
    }

    @FXML
    public void addAlarm() {
        if (showAndAddAlarmDialog != null) {
            showAndAddAlarmDialog.run();
        }
    }
}