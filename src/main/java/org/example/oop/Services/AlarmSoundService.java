package org.example.oop.Services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.example.oop.Utils;

import java.util.*;

public class AlarmSoundService {
    private MediaPlayer mediaPlayer;
    private static final LinkedHashMap<String, String> mapOfMelodiesPaths = new LinkedHashMap<>();

    static {
        mapOfMelodiesPaths.put("Классический будильник", "/org/example/oop/sounds/alarm.mp3");
        mapOfMelodiesPaths.put("Нежный звонок", "/org/example/oop/sounds/gentle.mp3");
        mapOfMelodiesPaths.put("Электронный", "/org/example/oop/sounds/electronic.mp3");
        mapOfMelodiesPaths.put("Птички", "/org/example/oop/sounds/birds.mp3");
    }

    public static ObservableList<String> getListKeysOfMelodiesPaths() {
        return FXCollections.observableArrayList(mapOfMelodiesPaths.keySet());
    }

    public static String getMelodyPath(String nameMelody) {
        return mapOfMelodiesPaths.get(nameMelody);
    }

    public void playAlarmSound(String soundFilePath) {
        stopAlarmSound();

        try {
            String soundPath = Objects.requireNonNull(getClass().getResource(soundFilePath)).toExternalForm();
            Media media = new Media(soundPath);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } catch (Exception e) {
            Utils.showError("Ошибка при воспроизведении звука" + e.getMessage());
        }
    }

    public void stopAlarmSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }
}
