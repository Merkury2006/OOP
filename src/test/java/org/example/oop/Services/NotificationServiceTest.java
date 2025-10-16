package org.example.oop.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();
    }

    @Test
    void testNotificationServiceCreation() {
        assertNotNull(notificationService);
    }

    @Test
    void testSetMainStage() {
        assertDoesNotThrow(() -> notificationService.setMainStage(null));
    }
}