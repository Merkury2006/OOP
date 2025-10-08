package org.example.oop.Services;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.oop.Controllers.TimePickerController;
import org.example.oop.Utils;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Optional;

public class AlarmAddDialogService {
    private Stage mainStage;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public Optional<LocalTime> showAlarmAddDialog() {
        Dialog<LocalTime> dialog = new Dialog<>();
        dialog.setTitle("Новый будильник");

        if (mainStage != null) {
            dialog.initOwner(mainStage);
            dialog.initModality(Modality.WINDOW_MODAL);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/oop/timePicker.fxml"));
        try {
            DialogPane dialogPane = loader.load();
            dialog.setDialogPane(dialogPane);

            dialog.setResultConverter(buttonType -> {
                if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    TimePickerController controller = loader.getController();
                    return controller.getSelectedTime();
                }
                return null;
            });

            return dialog.showAndWait();

        } catch (IOException e) {
            Utils.showError("Ошибка загрузки");
            return Optional.empty();
        }
    }
}
