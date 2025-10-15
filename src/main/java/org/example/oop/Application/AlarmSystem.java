package org.example.oop.Application;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.oop.Managers.AlarmManager;
import org.example.oop.Managers.AlarmManagerInterface;
import org.example.oop.Models.AlarmInterface;
import org.example.oop.Services.*;
import org.example.oop.Utils.Utils;

import java.util.Optional;

public class AlarmSystem implements AlarmSystemInterface {
    private final AlarmManagerInterface alarmManager;
    private final AlarmAddDialogService alarmAddDialogService;
    private final AlarmItemsService alarmItemsService;
    private final NotificationService notificationService;
    private final AlarmCheckingService alarmCheckingService;

    public AlarmSystem() {
        this.alarmManager = new AlarmManager();
        this.alarmAddDialogService = new AlarmAddDialogService();
        this.alarmItemsService = new AlarmItemsService(alarmManager);
        this.notificationService = new NotificationService();
        this.alarmCheckingService = new AlarmCheckingService(alarmManager, notificationService, alarmItemsService);
    }

    @Override
    public void initializeSystem(Stage stage) {
        try {
            notificationService.setMainStage(stage);
            alarmAddDialogService.setMainStage(stage);
            alarmCheckingService.startAlarmChecker();
        } catch (Exception e) {
            Utils.showError("Ошибка инициализации: " + e.getMessage());
        }
    }

    @Override
    public void reloadAlarmList() {
        alarmItemsService.reloadAlarmList();
    }

    @Override
    public void showAndAddAlarmDialog() {
        Optional<AlarmAddDialogService.ResultAddAlarm> result = alarmAddDialogService.showAlarmAddDialog();
        if (result.isPresent()) {
            AlarmInterface newAlarm = alarmManager.addAlarm(result.get().getTime(), true, result.get().getMelody());
            alarmItemsService.createAndAddAlarmItem(newAlarm);
        }
    }

    @Override
    public void shutDown(){
        alarmCheckingService.stop();
    }

    @Override
    public void setupUIContainers(VBox alarmsContainer, Label noAlarmsLabel) {
        alarmItemsService.setAlarmsItemsContainers(alarmsContainer, noAlarmsLabel);
    }
}
