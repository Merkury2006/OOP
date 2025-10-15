package org.example.oop.Services;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.oop.Config.AppConfig;
import org.example.oop.Controllers.AlarmAddOrEditDialogController;
import org.example.oop.Models.AlarmInterface;
import org.example.oop.Utils.Utils;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

public class AlarmAddOrEditDialogService {

     public class ResultAddOrEditAlarm {
        private final LocalTime time;
        private final String melody;
        private final String title;
        private final Set<DayOfWeek> days;

        public ResultAddOrEditAlarm(LocalTime time, String melody, String title, Set<DayOfWeek> days) {
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

    public AlarmAddOrEditDialogService() {
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public Optional<ResultAddOrEditAlarm> showAlarmAddDialog() {
        return showAlarmAddOrEditDialog(null);
    }

    public Optional<ResultAddOrEditAlarm> showAlarmEditDialog(AlarmInterface alarm) {
        return showAlarmAddOrEditDialog(alarm);
    }

    private Optional<ResultAddOrEditAlarm> showAlarmAddOrEditDialog(AlarmInterface editingAlarm) {
        Dialog<ResultAddOrEditAlarm> dialog = new Dialog<>();
        dialog.setTitle(editingAlarm == null ? AppConfig.ALARM_ADD_DIALOG_SERVICE_TITLE : AppConfig.ALARM_EDIT_DIALOG_SERVICE_TITLE);

        if (mainStage != null) {
            dialog.initOwner(mainStage);
            dialog.initModality(Modality.WINDOW_MODAL);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/oop/alarm_add_or_edit_dialog.fxml"));
        try {
            DialogPane dialogPane = loader.load();

            AlarmAddOrEditDialogController controller = loader.getController();

            if(editingAlarm != null) {
                controller.setEditMode(editingAlarm);
            }

            dialog.setDialogPane(dialogPane);

            dialog.setResultConverter(buttonType -> {
                if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    return new ResultAddOrEditAlarm(controller.getSelectedTime(), controller.getSelectedMelody(), controller.getSelectedTitle(), controller.getSelectedDays());
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
