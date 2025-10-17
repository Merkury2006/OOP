package org.example.oop.Services;

import org.junit.jupiter.api.Test;
import javafx.collections.ObservableList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlarmSoundServiceTest {

    @Test
    void testGetListKeysOfMelodiesPaths() {
        ObservableList<String> melodies = AlarmSoundService.getListKeysOfMelodiesPaths();

        assertNotNull(melodies);
        assertFalse(melodies.isEmpty());
        assertTrue(melodies.contains("Утро, пианино, птички"));
    }

    @Test
    void testGetMelodyPath() {
        String path = AlarmSoundService.getMelodyPath("Утро, пианино, птички");

        assertNotNull(path);
        assertTrue(path.contains(".mp3"));
    }

    @Test
    void testGetMelodyPath_WhenNotFound() {
        String path = AlarmSoundService.getMelodyPath("Несуществующая мелодия");

        assertNull(path);
    }
}