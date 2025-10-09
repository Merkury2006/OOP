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
    private AlarmManagerInterface alarmManager;
    private NotificationService notificationService;
    private AlarmAddDialogService alarmAddDialogService;
    private AlarmItemsService alarmItemsService;

    @FXML
    public void initialize(){
        this.alarmManager = new AlarmManager();
        this.notificationService = new NotificationService();
        this.alarmAddDialogService = new AlarmAddDialogService();
        this.alarmItemsService = new AlarmItemsService(alarmManager);
        Platform.runLater(() -> alarmItemsService.reloadAlarmList());
        startAlarmChecker();
    }

    public void setMainStage(Stage stage) {
        try {
        notificationService.setMainStage(stage);
        alarmAddDialogService.setMainStage(stage);
        alarmItemsService.setAlarmsItemsContainers(alarmsContainer, labelNoAlarms);

        } catch (Exception e) {
            Utils.showError("Ошибка инициализации: " + e.getMessage());
        }
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
            Platform.runLater(() -> {
                alarmManager.markAlarmAsTriggered(alarm);
                notificationService.showAlarmNotification(alarm,
                        result -> {
                            switch (result) {
                                case SNOOZE:
                                    LocalTime snoozeTime = alarm.getTime().plusMinutes(3);

                                    if (alarm instanceof SnoozeAlarm) {
                                        ((SnoozeAlarm) alarm).reschedule(snoozeTime);
                                        alarmManager.saveAlarms();
                                    } else if (alarm instanceof RegularAlarm) {
                                       alarmManager.addAlarm(snoozeTime, true, alarm.getMelody(),
                                               alarm.getName() + ", отложенный от: " + alarm.getTime(), alarm.getId());
                                    }
                                    break;

                                case DISMISS:
                                    if (alarm instanceof SnoozeAlarm) {
                                        alarmManager.updateAlarmStatus( ((SnoozeAlarm) alarm).getParentAlarmId(), false);
                                        alarmManager.deleteAlarm(alarm.getId());
                                    } else {
                                        alarmManager.updateAlarmStatus(alarm.getId(), false);
                                    }
                                    break;

                                case IGNORE:
                                    break;
                            }

                            alarmItemsService.reloadAlarmList();
                        });
            });
        });
    }

    @FXML
    public void addAlarm() {
        Optional<AlarmAddDialogService.ResultAddAlarm> result = alarmAddDialogService.showAlarmAddDialog();
        if (result.isPresent()){
            AlarmInterface newAlarm = alarmManager.addAlarm(result.get().getTime(), true, result.get().getMelody());
            alarmItemsService.createAndAddAlarmItem(newAlarm);
        }

    }
}