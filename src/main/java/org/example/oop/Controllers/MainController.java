package org.example.oop.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.oop.*;
import org.example.oop.Services.AlarmAddDialogService;
import org.example.oop.Services.AlarmItemsService;
import org.example.oop.Services.NotificationService;

import java.time.LocalTime;
import java.util.Optional;


public class MainController {
    @FXML private Label labelNoAlarms;
    @FXML private VBox alarmsContainer;
    private final AlarmManagerInterface alarmManager = new AlarmManager();
    private final NotificationService notificationService = new NotificationService();
    private final AlarmAddDialogService alarmAddDialogService = new AlarmAddDialogService();
    private final AlarmItemsService alarmItemsService = new AlarmItemsService(alarmManager);

    @FXML
    public void initialize(){
        alarmManager.loadAlarms();
        startAlarmChecker();
    }

    public void setMainStage(Stage stage) {
        notificationService.setMainStage(stage);
        alarmAddDialogService.setMainStage(stage);
        alarmItemsService.setAlarmsItemsContainers(alarmsContainer, labelNoAlarms);
    }

    private void startAlarmChecker() {
        Thread alarmThread = new Thread(() -> {
            while (true) {
                try {
                    checkAlarms();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        alarmThread.setDaemon(true);
        alarmThread.start();
    }

    private void checkAlarms() {
        alarmManager.getAlarmsToRing().forEach(alarm -> {
            Platform.runLater( () -> {
                notificationService.showAlarmNotification(alarm,
                        result ->  {
                            if (result == NotificationService.NotificationResult.SNOOZE) {
                                LocalTime snoozeTime = alarm.getTime().plusMinutes(5);
                                alarmManager.addAlarm(snoozeTime, true, alarm.getName() + ' ' + alarm.getTime() + " (Отложенный)");
                            }
                            alarmManager.updateAlarmStatus(alarm.getId(), false);
                            alarmManager.saveAlarms();
                            alarmItemsService.reloadAlarmList();
                        });
            });
        });
    }



    @FXML
    public void addAlarm() {
        Optional<LocalTime> result = alarmAddDialogService.showAlarmAddDialog();
        if (result.isPresent()){
            AlarmInterface newAlarm = alarmManager.addAlarm(result.get(), true);
            alarmItemsService.createAndAddAlarmItem(newAlarm);
        }

    }
}