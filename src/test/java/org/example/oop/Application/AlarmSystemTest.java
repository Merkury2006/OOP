package org.example.oop.Application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlarmSystemTest {

    private AlarmSystem alarmSystem;

    @BeforeEach
    void setUp() {
        alarmSystem = new AlarmSystem();
    }

    @Test
    void testAlarmSystemCreation() {
        assertNotNull(alarmSystem);
    }

    @Test
    void testImplementsInterface() {
        assertTrue(alarmSystem instanceof AlarmSystemInterface);
    }

    @Test
    void testShutDown() {
        assertDoesNotThrow(() -> alarmSystem.shutDown());
    }

    @Test
    void testReloadAlarmList() {
        assertDoesNotThrow(() -> alarmSystem.reloadAlarmList());
    }
}