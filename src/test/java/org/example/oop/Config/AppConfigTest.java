package org.example.oop.Config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    @Test
    void testAppConfigConstants() {
        assertEquals(5, AppConfig.SNOOZE_MINUTES);
        assertEquals(1, AppConfig.ALARM_CHECK_INTERVAL_SECONDS);
        assertEquals(2, AppConfig.SHUTDOWN_TIMEOUT_SECONDS);
        assertEquals("data/alarms.json", AppConfig.ALARMS_FILE);
        assertEquals("AlarmChecker", AppConfig.CHECK_ALARMS_FLOW_TITLE);
        assertEquals("Будульник", AppConfig.APP_TITLE);
    }

    @Test
    void testPrivateConstructor() {
        assertThrows(IllegalAccessException.class, () -> {
            AppConfig.class.newInstance();
        });
    }
}