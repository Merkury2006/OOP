package org.example.oop.Models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestableAlarm extends Alarm {
    public TestableAlarm(Long id, LocalTime time, boolean active, String melody, String name) {
        super(id, time, active, melody, name);
    }

    @Override
    public boolean shouldRingToday() {
        return isActive() && isTimeToRingNow();
    }

    @Override
    public AlarmType getType() {
        return AlarmType.REGULAR;
    }
}

class AlarmTest {

    private TestableAlarm alarm;
    private final LocalTime testTime = LocalTime.of(10, 30);
    private final Long testId = 1L;
    private final String testName = "Test Alarm";
    private final String testMelody = "melody.mp3";

    @BeforeEach
    void setUp() {
        alarm = new TestableAlarm(testId, testTime, true, testMelody, testName);
    }

    @Test
    @DisplayName("Should create alarm with correct initial values")
    void shouldCreateAlarmWithCorrectInitialValues() {
        assertEquals(testId, alarm.getId());
        assertEquals(testTime, alarm.getTime());
        assertTrue(alarm.isActive());
        assertEquals(testName, alarm.getName());
        assertEquals(testMelody, alarm.getMelody());
    }

    @Test
    @DisplayName("Should update alarm time correctly")
    void shouldUpdateAlarmTime() {
        LocalTime newTime = LocalTime.of(14, 45);

        alarm.setTime(newTime);

        assertEquals(newTime, alarm.getTime());
    }

    @Test
    @DisplayName("Should update alarm active status")
    void shouldUpdateAlarmActiveStatus() {
        alarm.setActive(false);

        assertFalse(alarm.isActive());
    }

    @Test
    @DisplayName("Should update alarm name")
    void shouldUpdateAlarmName() {
        String newName = "Updated Alarm";

        alarm.setName(newName);

        assertEquals(newName, alarm.getName());
    }

    @Test
    @DisplayName("Should update alarm melody")
    void shouldUpdateAlarmMelody() {
        String newMelody = "new_melody.mp3";

        alarm.setMelody(newMelody);

        assertEquals(newMelody, alarm.getMelody());
    }

    @Test
    @DisplayName("Should return correct alarm type")
    void shouldReturnCorrectAlarmType() {
        assertEquals(AlarmType.REGULAR, alarm.getType());
    }

    @Test
    @DisplayName("Should not ring when alarm is inactive")
    void shouldNotRingWhenAlarmIsInactive() {
        alarm.setActive(false);

        assertFalse(alarm.shouldRingToday());
    }

    @Test
    @DisplayName("Should handle null values in constructor")
    void shouldHandleNullValuesInConstructor() {
        TestableAlarm alarmWithNulls = new TestableAlarm(null, null, true, null, null);

        assertNull(alarmWithNulls.getId());
        assertNull(alarmWithNulls.getTime());
        assertNull(alarmWithNulls.getName());
        assertNull(alarmWithNulls.getMelody());
        assertTrue(alarmWithNulls.isActive());
    }

    @Test
    @DisplayName("Should handle empty strings")
    void shouldHandleEmptyStrings() {
        TestableAlarm alarmWithEmptyStrings = new TestableAlarm(2L, testTime, false, "", "");

        assertEquals("", alarmWithEmptyStrings.getName());
        assertEquals("", alarmWithEmptyStrings.getMelody());
    }
}