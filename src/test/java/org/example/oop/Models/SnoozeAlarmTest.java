package org.example.oop.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class SnoozeAlarmTest {

    private SnoozeAlarm alarm;

    @BeforeEach
    void setUp() {
        alarm = new SnoozeAlarm(2L, LocalTime.of(8, 5), true, "Мелодия", "Snooze", 1L);
    }

    @Test
    void testSnoozeAlarmCreation() {
        assertNotNull(alarm);
        assertEquals(2L, alarm.getId());
        assertEquals(1L, alarm.getParentAlarmId());
        assertFalse(alarm.isEditable());
        assertEquals(Alarm.AlarmType.SNOOZE, alarm.getType());
    }

    @Test
    void testReschedule() {
        LocalTime newTime = LocalTime.of(8, 10);
        alarm.reschedule(newTime);
        assertEquals(newTime, alarm.getTime());
    }
}