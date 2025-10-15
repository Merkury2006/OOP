package org.example.oop.Application;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.oop.Managers.AlarmManager;
import org.example.oop.Managers.AlarmManagerInterface;
import org.example.oop.Models.AlarmInterface;
import org.example.oop.Services.*;
import org.example.oop.Utils.Utils;

import java.time.DayOfWeek;
import java.util.Optional;
import java.util.Set;

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
            AlarmAddDialogService.ResultAddAlarm data = result.get();
            Set<DayOfWeek> repeatDays = data.getDays();
            AlarmInterface newAlarm;
            if (repeatDays.isEmpty()) {
                newAlarm = alarmManager.addAlarm(result.get().getTime(), true, result.get().getMelody(), result.get().getTitle());
            }
            else {
                newAlarm = alarmManager.addAlarm(result.get().getTime(), true, result.get().getMelody(), result.get().getTitle(), result.get().getDays());
            }
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
