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
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

public class AlarmAddDialogService {

     public class ResultAddAlarm {
        private final LocalTime time;
        private final String melody;
        private final String title;
        private final Set<DayOfWeek> days;

        public ResultAddAlarm(LocalTime time, String melody, String title, Set<DayOfWeek> days) {
            this.time = time;
            this.melody = melody;
            this.title = title;
            this.days = days;
        }

        public LocalTime getTime() {
            return time;
        }

        public String getMelody() {
            return melody;
        }

        public String getTitle(){
            return title;
        }

         public Set<DayOfWeek> getDays() {
             return days;
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
                    return new ResultAddAlarm(controller.getSelectedTime(), controller.getSelectedMelody(), controller.getSelectedTitle(), controller.getSelectedDays());
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
