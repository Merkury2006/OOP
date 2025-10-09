package org.example.oop.Services;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.oop.AlarmInterface;
import org.example.oop.AlarmManagerInterface;
import org.example.oop.Controllers.AlarmItemController;
import org.example.oop.Utils;

import java.io.IOException;

public class AlarmItemsService {
    private final AlarmManagerInterface alarmManager;
    private VBox alarmsContainer;
    private Label labelNoAlarms;

    public AlarmItemsService(AlarmManagerInterface alarmManager) {
        this.alarmManager = alarmManager;
    }

    public void setAlarmsItemsContainers(VBox alarmsContainer, Label labelNoAlarms) {
        this.alarmsContainer = alarmsContainer;
        this.labelNoAlarms = labelNoAlarms;
    }

    public void createAndAddAlarmItem(AlarmInterface alarm) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/oop/alarm_item.fxml"));
        try {
            HBox alarmItem = loader.load();
            AlarmItemController alarmItemController = loader.getController();

            alarmItemController.initialize(alarm,
                    () -> handleDeleteAlarm(alarm),
                    (newStatus) -> handleStatusChange(alarm, newStatus));

            updateNoAlarmsLabel();

            alarmsContainer.getChildren().add(alarmItem);

        } catch (IOException e) {
            Utils.showError("Ошибка создания будульника" + e.getMessage());
        }
    }

    private void handleDeleteAlarm(AlarmInterface alarm){
        alarmManager.deleteAlarm(alarm.getId());
        updateNoAlarmsLabel();
        reloadAlarmList();
    }

    private void handleStatusChange(AlarmInterface alarm, boolean newStatus) {
        alarmManager.updateAlarmStatus(alarm.getId(), newStatus);
        reloadAlarmList();
    }

    private void updateNoAlarmsLabel() {
        if (alarmManager.hasAlarm()) {
            labelNoAlarms.setVisible(false);
            return;
        }
        labelNoAlarms.setVisible(true);
    }

    public void reloadAlarmList() {
        if (alarmsContainer != null) {
            alarmsContainer.getChildren().clear();
            alarmManager.getAllAlarms().forEach(alarm -> createAndAddAlarmItem(alarm));
            updateNoAlarmsLabel();
        }
    }
}
