package org.example.oop.Services;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.example.oop.Utils;

import java.io.File;

public class AlarmSoundService {
    private MediaPlayer mediaPlayer;

    public void playAlarmSound(String soundFilePath) {
        stopAlarmSound();
        try {

            File soundFile = new File(soundFilePath);
            if (!soundFile.exists()) {
                Utils.showError("Файл с мелодией не найден");
                return;
            }
            String mediaPath = soundFile.toURI().toString();
            Media media = new Media(mediaPath);

            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } catch (Exception e) {
            Utils.showError("Ошибка при воспроизведении звука");
        }
    }

    public void stopAlarmSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }
}
