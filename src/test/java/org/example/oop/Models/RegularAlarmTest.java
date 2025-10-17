package org.example.oop.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegularAlarmTest {

    private RegularAlarm alarm;

    @BeforeEach
    void setUp() {
        alarm = new RegularAlarm(1L, LocalTime.of(8, 0), true, "Мелодия", "Тестовый будильник");
    }

    @Test
    void testRegularAlarmCreation() {
        assertNotNull(alarm);
        assertEquals(1L, alarm.getId());
        assertEquals(LocalTime.of(8, 0), alarm.getTime());
        assertTrue(alarm.isActive());
        assertEquals("Мелодия", alarm.getMelody());
        assertEquals("Тестовый будильник", alarm.getName());
        assertEquals(AlarmType.REGULAR, alarm.getType());
    }

    @Test
    void testShouldRingToday_WhenActiveAndTimeMatches() {
        assertTrue(alarm.isActive());
    }

    @Test
    void testSetAlarmDate() {
        var newDate = java.time.LocalDate.now().plusDays(1);
        alarm.setAlarmDate(newDate);
        assertEquals(newDate, alarm.getAlarmDate());
    }
}
