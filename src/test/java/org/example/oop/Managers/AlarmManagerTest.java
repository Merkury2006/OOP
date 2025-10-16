package org.example.oop.Managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.oop.Models.AlarmInterface;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AlarmManagerTest {

    private AlarmManager alarmManager;

    @BeforeEach
    void setUp() throws Exception {
        alarmManager = new AlarmManager();
        resetAlarmManagerState();
    }

    private void resetAlarmManagerState() throws Exception {
        Field alarmListField = AlarmManager.class.getDeclaredField("alarmList");
        alarmListField.setAccessible(true);
        alarmListField.set(alarmManager, new ArrayList<>());

        Field nextIdField = AlarmManager.class.getDeclaredField("nextId");
        nextIdField.setAccessible(true);
        nextIdField.set(alarmManager, 1L);
    }

    @Test
    void testAddRegularAlarm() {
        assertEquals(0, alarmManager.getAlarmCount());

        AlarmInterface alarm = alarmManager.addAlarm(LocalTime.of(8, 0), true, "Мелодия", "Тест");

        assertEquals(1, alarmManager.getAlarmCount());
        assertNotNull(alarm);
        assertEquals("Тест", alarm.getName());
    }

    @Test
    void testDeleteAlarm() {
        AlarmInterface alarm = alarmManager.addAlarm(LocalTime.of(8, 0), true, "Мелодия", "Тест");
        long alarmId = alarm.getId();
        assertEquals(1, alarmManager.getAlarmCount());

        alarmManager.deleteAlarm(alarmId);

        assertEquals(0, alarmManager.getAlarmCount());
        assertFalse(alarmManager.getAlarmById(alarmId).isPresent());
    }

    @Test
    void testGetActiveAlarms() {
        alarmManager.addAlarm(LocalTime.of(8, 0), true, "Мелодия", "Активный");
        alarmManager.addAlarm(LocalTime.of(9, 0), false, "Мелодия", "Неактивный");

        List<AlarmInterface> activeAlarms = alarmManager.getActiveAlarms();

        assertEquals(1, activeAlarms.size());
        assertEquals("Активный", activeAlarms.get(0).getName());
    }

    @Test
    void testUpdateAlarmStatus() {
        AlarmInterface alarm = alarmManager.addAlarm(LocalTime.of(8, 0), true, "Мелодия", "Тест");

        alarmManager.updateAlarmStatus(alarm.getId(), false);

        assertFalse(alarm.isActive());
    }

    @Test
    void testGetAllAlarms_WhenEmpty() {
        assertEquals(0, alarmManager.getAllAlarms().size());
        assertEquals(0, alarmManager.getAlarmCount());
        assertFalse(alarmManager.hasAlarm());
    }

    @Test
    void testHasAlarm_WhenHasAlarms() {
        alarmManager.addAlarm(LocalTime.of(8, 0), true, "Мелодия", "Тест");
        assertTrue(alarmManager.hasAlarm());
    }
}