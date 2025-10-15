package org.example.oop.Services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.oop.Models.AlarmInterface;
import org.example.oop.Controllers.NotificationController;
import org.example.oop.Utils.Utils;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class NotificationService {
    public enum NotificationResult {
        DISMISS,
        SNOOZE,
        IGNORE
    }
    private Stage mainStage;
    private final AlarmSoundService alarmSoundService = new AlarmSoundService();

    public NotificationService() {}

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void showAlarmNotification(AlarmInterface alarm, Consumer<NotificationResult> callback) {
        try {
            alarmSoundService.playAlarmSound(AlarmSoundService.getMelodyPath(alarm.getMelody()));

            Dialog<ButtonType> dialog = createNotificationDialog(alarm);
            NotificationResult result = showAndResultDialog(dialog);

            alarmSoundService.stopAlarmSound();

            callback.accept(result);
        } catch (Exception e) {
            Utils.showError("Ошибка показа уведомления: " + e.getMessage());
            callback.accept(NotificationResult.IGNORE);
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
            Utils.showError("Ошибка загрузки оповещения будильника" + e.getMessage());
        }
        return dialog;
    }

    private NotificationResult showAndResultDialog(Dialog<ButtonType> dialog) {
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
            Utils.showError("Ошибка загрузки диалога" + e.getMessage());
        }
        return NotificationResult.IGNORE;
    }
}
