package org.example.oop.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.oop.Managers.AlarmManager;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class AlarmCheckingServiceTest {

    private AlarmManager alarmManager;
    private TestNotificationService notificationService;
    private AlarmItemsService alarmItemsService;
    private AlarmCheckingService alarmCheckingService;

    @BeforeEach
    void setUp() throws Exception {
        alarmManager = new AlarmManager();
        resetAlarmManagerState();

        notificationService = new TestNotificationService();
        alarmItemsService = new AlarmItemsService(alarmManager, alarm -> {});

        alarmCheckingService = new AlarmCheckingService(
                alarmManager, notificationService, alarmItemsService
        );
    }

    private void resetAlarmManagerState() throws Exception {
        var alarmListField = AlarmManager.class.getDeclaredField("alarmList");
        alarmListField.setAccessible(true);
        alarmListField.set(alarmManager, new java.util.ArrayList<>());

        var nextIdField = AlarmManager.class.getDeclaredField("nextId");
        nextIdField.setAccessible(true);
        nextIdField.set(alarmManager, 1L);
    }

    private static class TestNotificationService extends NotificationService {
        @Override
        public void showAlarmNotification(org.example.oop.Models.AlarmInterface alarm,
                                          java.util.function.Consumer<NotificationResult> callback) {
            callback.accept(NotificationResult.DISMISS);
        }
    }

    @Test
    void testAlarmCheckingServiceCreation() {
        assertNotNull(alarmCheckingService);
    }

    @Test
    void testStartAndStop() {
        assertDoesNotThrow(() -> {
            alarmCheckingService.startAlarmChecker();
            Thread.sleep(100);
            alarmCheckingService.stop();
        });
    }

    @Test
    void testCheckAlarms_WhenNoAlarms() throws Exception {
        Method checkAlarmsMethod = AlarmCheckingService.class.getDeclaredMethod("checkAlarms");
        checkAlarmsMethod.setAccessible(true);

        assertDoesNotThrow(() -> checkAlarmsMethod.invoke(alarmCheckingService));
    }

    @Test
    void testCheckAlarms_WithAlarms() throws Exception {
        alarmManager.addAlarm(java.time.LocalTime.now().plusHours(1), true, "Мелодия", "Тест");

        Method checkAlarmsMethod = AlarmCheckingService.class.getDeclaredMethod("checkAlarms");
        checkAlarmsMethod.setAccessible(true);

        assertDoesNotThrow(() -> checkAlarmsMethod.invoke(alarmCheckingService));
    }

    @Test
    void testCheckAlarms_ExceptionHandling() throws Exception {
        Method checkAlarmsMethod = AlarmCheckingService.class.getDeclaredMethod("checkAlarms");
        checkAlarmsMethod.setAccessible(true);

        assertDoesNotThrow(() -> checkAlarmsMethod.invoke(alarmCheckingService));
    }
}