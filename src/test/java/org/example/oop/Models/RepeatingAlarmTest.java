package org.example.oop.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class RepeatingAlarmTest {

    private RepeatingAlarm alarm;

    @BeforeEach
    void setUp() {
        alarm = new RepeatingAlarm(1L, LocalTime.of(9, 0), true, "Мелодия", "Рабочий",
                Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY));
    }

    @Test
    void testRepeatingAlarmCreation() {
        assertNotNull(alarm);
        assertEquals(1L, alarm.getId());
        assertEquals(LocalTime.of(9, 0), alarm.getTime());
        assertTrue(alarm.isActive());
        assertEquals(Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), alarm.getRepeatDays());
        assertEquals(Alarm.AlarmType.REPEATING, alarm.getType());
    }

    @Test
    void testGetDisplayName() {
        String displayName = alarm.getDisplayName();
        assertTrue(displayName.contains("Повторяющийся"));
        assertTrue(displayName.contains("Пон") || displayName.contains("Пт"));
    }

    @Test
    void testSetRepeatDays() {
        var newDays = Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        alarm.setRepeatDays(newDays);
        assertEquals(newDays, alarm.getRepeatDays());
    }
}