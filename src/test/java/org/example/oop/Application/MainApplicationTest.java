package org.example.oop.Application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MainApplicationTest {

    @Test
    void testMainApplicationCreation() {
        MainApplication app = new MainApplication();
        assertNotNull(app);
    }

    @Test
    void testApplicationInheritance() {
        MainApplication app = new MainApplication();
        assertTrue(app instanceof javafx.application.Application);
    }
}