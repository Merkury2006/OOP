package org.example.oop.Services;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.oop.Config.AppConfig;
import org.example.oop.Controllers.AlarmAddDialogController;
import org.example.oop.Utils.Utils;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Optional;

public class AlarmAddDialogService {

     public class ResultAddAlarm {
        private final LocalTime time;
        private final String melody;

        public ResultAddAlarm(LocalTime time, String melody) {
            this.time = time;
            this.melody = melody;
        }

        public LocalTime getTime() {
            return time;
        }

        public String getMelody() {
            return melody;
        }
    }

    private Stage mainStage;

    public AlarmAddDialogService() {
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public Optional<ResultAddAlarm> showAlarmAddDialog() {
        Dialog<ResultAddAlarm> dialog = new Dialog<>();
        dialog.setTitle(AppConfig.ALARM_ADD_DIALOG_SERVICE_TITLE);

        if (mainStage != null) {
            dialog.initOwner(mainStage);
            dialog.initModality(Modality.WINDOW_MODAL);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/oop/alarm_add_dialog.fxml"));
        try {
            DialogPane dialogPane = loader.load();
            dialog.setDialogPane(dialogPane);

            dialog.setResultConverter(buttonType -> {
                if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    AlarmAddDialogController controller = loader.getController();
                    return new ResultAddAlarm(controller.getSelectedTime(), controller.getSelectedMelody());
                }
                return null;
            });

            return dialog.showAndWait();

        } catch (IOException e) {
            Utils.showError("Ошибка загрузки FXML: " + e.getMessage());
            return Optional.empty();
        }
    }
}
