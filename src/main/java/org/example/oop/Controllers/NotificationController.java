package org.example.oop.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.oop.AlarmInterface;

public class NotificationController {
    @FXML private Label labelAlarmToRing;


    public void initialize(AlarmInterface alarm) {
        String notificationString = "Будильник: " + alarm.getTime();
        labelAlarmToRing.setText(notificationString);
    }
}
