package org.example.oop.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.oop.Models.AlarmInterface;

public class NotificationController {
    @FXML private Label labelAlarmToRing;


    public void initialize(AlarmInterface alarm) {
        String notificationString = alarm.getName() + " - " + alarm.getTime();
        labelAlarmToRing.setText(notificationString);
    }
}
