package org.example.oop.Services;

import org.junit.jupiter.api.Test;
import javafx.collections.ObservableList;
import static org.junit.jupiter.api.Assertions.*;

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