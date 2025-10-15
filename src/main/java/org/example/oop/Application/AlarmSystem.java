package org.example.oop.Application;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.oop.Managers.AlarmManager;
import org.example.oop.Managers.AlarmManagerInterface;
import org.example.oop.Models.AlarmInterface;
import org.example.oop.Models.RegularAlarm;
import org.example.oop.Models.RepeatingAlarm;
import org.example.oop.Services.*;
import org.example.oop.Utils.Utils;

import java.time.DayOfWeek;
import java.util.Set;

public class AlarmSystem implements AlarmSystemInterface {
    private final AlarmManagerInterface alarmManager;
    private final AlarmAddOrEditDialogService alarmAddDialogService;
    private final AlarmItemsService alarmItemsService;
    private final NotificationService notificationService;
    private final AlarmCheckingService alarmCheckingService;

    public AlarmSystem() {
        this.alarmManager = new AlarmManager();
        this.alarmAddDialogService = new AlarmAddOrEditDialogService();
        this.alarmItemsService = new AlarmItemsService(alarmManager, this::showEditAlarmDialog);
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
        alarmAddDialogService.showAlarmAddDialog().ifPresent(result -> {
            Set<DayOfWeek> repeatDays = result.getDays();
            AlarmInterface newAlarm;
            if (repeatDays.isEmpty()) {
                newAlarm = alarmManager.addAlarm(result.getTime(), true, result.getMelody(), result.getTitle());
            }
            else {
                newAlarm = alarmManager.addAlarm(result.getTime(), true, result.getMelody(), result.getTitle(), result.getDays());
            }
            alarmItemsService.createAndAddAlarmItem(newAlarm);
        });
    }

    @Override
    public void showEditAlarmDialog(AlarmInterface alarm) {
        alarmAddDialogService.showAlarmEditDialog(alarm).ifPresent(result -> {
            switch (alarm) {
                case RegularAlarm _ -> alarmManager.updateAlarm(alarm.getId(), result.getTime(), result.getMelody(), result.getTitle());
                case RepeatingAlarm _ ->  alarmManager.updateAlarm(alarm.getId(), result.getTime(), result.getMelody(), result.getTitle(), result.getDays());
                default -> throw new IllegalStateException("Unexpected value: " + alarm);
            }
            reloadAlarmList();
        });
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
