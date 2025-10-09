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
        mapOfMelodiesPaths.put("Утро, пианино, птички", "/org/example/oop/sounds/Утро, пианино, птички (будильник) - zakachai1_ru.mp3");
        mapOfMelodiesPaths.put("Нежный звонок", "/org/example/oop/sounds/Нежная мелодия - zakachai1_ru.mp3");
        mapOfMelodiesPaths.put("Энигма", "/org/example/oop/sounds/Энигма — Principles - zakachai1_ru.mp3");
        mapOfMelodiesPaths.put("Imagine me", "/org/example/oop/sounds/IMAGINE ME (Sagi) - zakachai1_ru.mp3");
        mapOfMelodiesPaths.put("Мягкое пробуждение", "/org/example/oop/sounds/Будильник «Мягкое пробуждение» - zakachai1_ru.mp3");
        mapOfMelodiesPaths.put("Гроза и гитара", "/org/example/oop/sounds/Гроза и гитара (Y3MR Remix — Babel) - zakachai1_ru.mp3");
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
