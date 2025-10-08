package org.example.oop.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class TimePickerController implements Initializable {

    @FXML
    private Spinner<Integer> hourSpinner;

    @FXML
    private Spinner<Integer> minuteSpinner;

    @FXML
    private Label selectedTimeLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        hourSpinner.setEditable(true);
        minuteSpinner.setEditable(true);

        hourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateTimeDisplay());
        minuteSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateTimeDisplay());
        updateTimeDisplay();
    }

    private void updateTimeDisplay() {
        String time = String.format("%02d:%02d", hourSpinner.getValue(), minuteSpinner.getValue());
        selectedTimeLabel.setText(time);
    }

    @FXML
    private void setCurrentTime() {
        LocalTime now = LocalTime.now();
        hourSpinner.getValueFactory().setValue(now.getHour());
        minuteSpinner.getValueFactory().setValue(now.getMinute());
    }

    @FXML
    private void setMorningTime() {
        hourSpinner.getValueFactory().setValue(7);
        minuteSpinner.getValueFactory().setValue(0);
    }

    @FXML
    private void setEveningTime() {
        hourSpinner.getValueFactory().setValue(22);
        minuteSpinner.getValueFactory().setValue(0);
    }

    public LocalTime getSelectedTime() {
        return LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue());
    }

    public String getSelectedTimeFormatted() {
        return String.format("%02d:%02d", hourSpinner.getValue(), minuteSpinner.getValue());
    }
}