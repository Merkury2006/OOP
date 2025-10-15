package org.example.oop.Services;

import javafx.application.Platform;
import org.example.oop.Config.AppConfig;
import org.example.oop.Managers.AlarmManagerInterface;
import org.example.oop.Models.AlarmInterface;
import org.example.oop.Models.RegularAlarm;
import org.example.oop.Models.RepeatingAlarm;
import org.example.oop.Models.SnoozeAlarm;
import org.example.oop.Utils.Utils;

import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlarmCheckingService {
    private final ScheduledExecutorService scheduler;

    private final AlarmManagerInterface alarmManager;
    private final NotificationService notificationService;
    private final AlarmItemsService alarmItemsService;

    public AlarmCheckingService(AlarmManagerInterface alarmManager, NotificationService notificationService, AlarmItemsService alarmItemsService) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, AppConfig.CHECK_ALARMS_FLOW_TITLE);
            thread.setDaemon(true);
            return thread;
        });

        this.alarmManager = alarmManager;
        this.notificationService = notificationService;
        this.alarmItemsService = alarmItemsService;
    }

    public void startAlarmChecker() {
        scheduler.scheduleAtFixedRate(this::checkAlarms, 0,  AppConfig.ALARM_CHECK_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public void stop(){
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(AppConfig.SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void checkAlarms() {
        try {
            alarmManager.getAlarmsToRing().forEach(alarm -> {
                alarmManager.markAlarmAsTriggered(alarm);
                Platform.runLater(() -> {
                    notificationService.showAlarmNotification(alarm, result -> {
                        handleAlarmAction(alarm, result);
                        alarmItemsService.reloadAlarmList();
                    });
                });
            });
        }catch (Exception e) {
            Utils.showError("Ошибка при проверке будульников: " + e.getMessage());
        }
    }

    private void handleAlarmAction(AlarmInterface alarm, NotificationService.NotificationResult result) {
        switch (result) {
            case SNOOZE:
                handleSnooze(alarm);
                break;
            case DISMISS:
               handleDismiss(alarm);
                break;
            case IGNORE:
                break;
        }
    }

    private void handleSnooze(AlarmInterface alarm) {
        LocalTime snoozeTime = alarm.getTime().plusMinutes(AppConfig.SNOOZE_MINUTES);

        if (alarm instanceof SnoozeAlarm snoozeAlarm) {
            snoozeAlarm.reschedule(snoozeTime);
            alarmManager.saveAlarms();
        } else {
            alarmManager.addAlarm(snoozeTime, true, alarm.getMelody(),
                    alarm.getName() + AppConfig.ALARM_SNOOZE_PREFIX + alarm.getTime(), alarm.getId());
        }
    }

    private void handleDismiss(AlarmInterface alarm) {
        switch (alarm) {
            case SnoozeAlarm snoozeAlarm -> handleSnoozeAlarmDismiss(snoozeAlarm);
            case RegularAlarm regularAlarm -> alarmManager.updateAlarmStatus(regularAlarm.getId(), false);
            case RepeatingAlarm repeatingAlarm -> {}
            default -> throw new IllegalStateException("Unexpected value: " + alarm);
        }
    }

    private void handleSnoozeAlarmDismiss(AlarmInterface alarm) {
        alarmManager.deleteAlarm(alarm.getId());
    }
}
