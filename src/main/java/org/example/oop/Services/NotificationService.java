package org.example.oop.Services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.oop.AlarmInterface;
import org.example.oop.Controllers.NotificationController;
import org.example.oop.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class NotificationService {
    public enum NotificationResult {
        DISMISS,
        SNOOZE,
        IGNORE
    }
    private final Set<Long> shownAlarmsIds = new HashSet<>();
    private Stage mainStage;
    private final AlarmSoundService alarmSoundService = new AlarmSoundService();

    public NotificationService() {}

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void showAlarmNotification(AlarmInterface alarm, Consumer<NotificationResult> callback) {
        if (shownAlarmsIds.contains(alarm.getId())) {
           callback.accept(NotificationResult.IGNORE);
           return;
        }
        shownAlarmsIds.add(alarm.getId());
        try {
            alarmSoundService.playAlarmSound("src/main/resources/org/example/oop/sounds/alarm.mp3");

            Dialog<ButtonType> dialog = createNotificationDialog(alarm);
            NotificationResult result = showAndResultDialog(dialog);

            alarmSoundService.stopAlarmSound();

            callback.accept(result);
        }
        finally {
            shownAlarmsIds.remove(alarm.getId());
        }
    }

    private Dialog<ButtonType> createNotificationDialog(AlarmInterface alarm) {
        Dialog<ButtonType> dialog = new Dialog<>();

        if (mainStage != null) {
            dialog.initOwner(mainStage);
            dialog.initModality(Modality.WINDOW_MODAL);
        }


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/oop/alarm_notification.fxml"));

            DialogPane notification = loader.load();

            NotificationController controller = loader.getController();

            controller.initialize(alarm);

            dialog.setDialogPane(notification);

            //Скрываем кнопку CANCEL_CLOSE
            for (ButtonType buttonType : notification.getButtonTypes()) {
                if (buttonType.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                    Node cancelButton = notification.lookupButton(buttonType);
                    if (cancelButton != null) {
                        cancelButton.setVisible(false);
                        cancelButton.setManaged(false);
                    }
                    break;
                }
            }

        } catch (IOException e) {
            Utils.showError("Ошибка загрузки оповещения будильника");
        }
        return dialog;
    }

    private NotificationResult showAndResultDialog(Dialog<ButtonType> dialog) {
        Toolkit.getDefaultToolkit().beep(); //Звук

        try {
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent()) {
                ButtonType buttonType = result.get();
                if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    return NotificationResult.DISMISS;
                } else if (buttonType.getButtonData() == ButtonBar.ButtonData.OTHER) {
                    return NotificationResult.SNOOZE;
                } else if (buttonType.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                    return NotificationResult.IGNORE;
                }
            }
        } catch (Exception e) {
            Utils.showError("Ошибка загрузки диалога");
        }
        return NotificationResult.IGNORE;
    }
}
