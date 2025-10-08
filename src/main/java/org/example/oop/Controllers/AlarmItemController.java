package org.example.oop.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import org.example.oop.AlarmInterface;

import java.util.function.Consumer;

public class AlarmItemController {
    private boolean isActive;

    @FXML private Label nameAlarmLabel;
    @FXML private Label statusLabel;
    @FXML private Label timeLabel;
    @FXML private ToggleButton activeToggle;
    private Runnable onDeleteCallback;
    private Consumer<Boolean> onStatusChangeCallback;


    public void initialize(AlarmInterface alarm, Runnable onDeleteCallback, Consumer<Boolean> onStatusChangeCallback) {
        this.isActive = alarm.isActive();
        this.timeLabel.setText(alarm.getTime().toString());
        this.nameAlarmLabel.setText(alarm.getName());
        this.activeToggle.setSelected(isActive);
        updateStatusText();

        this.onDeleteCallback = onDeleteCallback;
        this.onStatusChangeCallback = onStatusChangeCallback;

        this.activeToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            this.isActive = newValue;
            updateStatusText();
            if(onStatusChangeCallback != null) {
                onStatusChangeCallback.accept(newValue);
            }
        });
    }


    @FXML
    public void handleDelete(ActionEvent actionEvent) {
        if (onDeleteCallback != null) {
            onDeleteCallback.run();
        }
    }

    private void updateStatusText() {
        statusLabel.setText(isActive ? "Активный" : "Неактивный");
        statusLabel.setStyle(isActive ? "-fx-pref-width: 100; -fx-pref-height: 20; -fx-background-radius: 5; -fx-background-color: #2a9c35;" :
                "-fx-pref-width: 100; -fx-pref-height: 20; -fx-background-radius: 5; -fx-background-color: transparent;");
        activeToggle.setText(isActive ? "✗ Выкл" : "✓ Вкл");
    }
}
