package org.example.oop.Controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.oop.Models.AlarmInterface;
import org.example.oop.Models.RepeatingAlarm;
import org.example.oop.Services.AlarmSoundService;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;


public class AlarmAddOrEditDialogController implements Initializable {
    @FXML private DialogPane dialogPane;
    @FXML private Spinner<Integer> hourSpinner;
    @FXML private Spinner<Integer> minuteSpinner;
    @FXML private Label selectedTimeLabel;
    @FXML private TextField titleAlarm;
    @FXML private ComboBox<String> melodyComboBox;
    @FXML private Label repeatLabel;
    @FXML private VBox daysContainer;
    @FXML private CheckBox sundayCheckBox;
    @FXML private CheckBox saturdayCheckBox;
    @FXML private CheckBox fridayCheckBox;
    @FXML private CheckBox thursdayCheckBox;
    @FXML private CheckBox wednesdayCheckBox;
    @FXML private CheckBox tuesdayCheckBox;
    @FXML private CheckBox mondayCheckBox;
    private boolean daysVisible = false;


    private static final Set<DayOfWeek> workDays = Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
    private static final Set<DayOfWeek> weekendDays = Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    private static final Set<DayOfWeek> allDays = EnumSet.allOf(DayOfWeek.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTimeSpinners();
        setupMelodyComboBox();
        setupCheckBoxListeners();
        updateTimeDisplay();
    }

    public void setEditMode(AlarmInterface alarm) {
        hourSpinner.getValueFactory().setValue(alarm.getTime().getHour());
        minuteSpinner.getValueFactory().setValue(alarm.getTime().getMinute());
        titleAlarm.setText(alarm.getName());
        melodyComboBox.setValue(alarm.getMelody());

        if (alarm instanceof RepeatingAlarm repeatingAlarm) {
            setSelectedDays(repeatingAlarm.getRepeatDays());
        }

        updateTimeDisplay();

        dialogPane.getButtonTypes().clear();
        dialogPane.getButtonTypes().addAll(
                new ButtonType("✅ Применить изменения", ButtonBar.ButtonData.OK_DONE),
                new ButtonType("❌ Отмена", ButtonBar.ButtonData.CANCEL_CLOSE)
        );

    }

    private void setupTimeSpinners() {
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        hourSpinner.setEditable(true);
        minuteSpinner.setEditable(true);

        hourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateTimeDisplay());
        minuteSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateTimeDisplay());
    }

    private void setupMelodyComboBox() {
        melodyComboBox.setItems(AlarmSoundService.getListKeysOfMelodiesPaths());
        melodyComboBox.getSelectionModel().selectFirst();
    }

    private void setupCheckBoxListeners() {
        CheckBox[] checkBoxes = {mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox};
        for (CheckBox checkBox : checkBoxes) {
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> updateRepeatLabel());
        }
    }

    private void updateTimeDisplay() {
        String time = String.format("%02d:%02d", hourSpinner.getValue(), minuteSpinner.getValue());
        selectedTimeLabel.setText(time);
    }

    private void updateRepeatLabel() {
        int selectedCount = getSelectedDays().size();
        if (selectedCount == 0) {
            repeatLabel.setText("Повторять в ▼");
        } else if (selectedCount == 7) {
            repeatLabel.setText("Каждый день ▲");
        } else {
            repeatLabel.setText("Повторять (" + selectedCount + " дней) ▲");
        }
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

    @FXML
    private void boxDaysVisibility(MouseEvent mouseEvent) {
        daysVisible = !daysVisible;
        FadeTransition fade = new FadeTransition(Duration.millis(300), daysContainer);

        if(daysVisible) {
            showDaysContainer(fade);
        } else {
            hideDaysContainer(fade);
        }
    }

    private void showDaysContainer(FadeTransition fade) {
        daysContainer.setVisible(true);
        daysContainer.setManaged(true);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        updateRepeatLabel();
    }

    private void hideDaysContainer(FadeTransition fade) {
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> {
            daysContainer.setVisible(false);
            daysContainer.setManaged(false);
        });
        fade.play();
        repeatLabel.setText("Повторять в ▼");
    }

    @FXML
    private void selectWorkDays() {
        setSelectedDays(workDays);
    }

    @FXML
    private void selectWeekend() {
       setSelectedDays(weekendDays);
    }

    @FXML
    private void selectAllDays() {
        setSelectedDays(allDays);
    }


    @FXML
    private void clearSelection() {
        setSelectedDays(Collections.emptySet());
    }

    private void setSelectedDays(Set<DayOfWeek> days) {
        clearAllCheckBoxes();

        for (DayOfWeek day: days) {
            switch (day) {
                case MONDAY -> mondayCheckBox.setSelected(true);
                case TUESDAY -> tuesdayCheckBox.setSelected(true);
                case WEDNESDAY -> wednesdayCheckBox.setSelected(true);
                case THURSDAY -> thursdayCheckBox.setSelected(true);
                case FRIDAY -> fridayCheckBox.setSelected(true);
                case SATURDAY -> saturdayCheckBox.setSelected(true);
                case SUNDAY -> sundayCheckBox.setSelected(true);
            }
        }
        updateRepeatLabel();
    }

    private void clearAllCheckBoxes() {
        mondayCheckBox.setSelected(false);
        tuesdayCheckBox.setSelected(false);
        wednesdayCheckBox.setSelected(false);
        thursdayCheckBox.setSelected(false);
        fridayCheckBox.setSelected(false);
        saturdayCheckBox.setSelected(false);
        sundayCheckBox.setSelected(false);
    }

    public Set<DayOfWeek> getSelectedDays() {
        Set<DayOfWeek> days = new HashSet<>();
        if(mondayCheckBox.isSelected()) days.add(DayOfWeek.MONDAY);
        if(tuesdayCheckBox.isSelected()) days.add(DayOfWeek.TUESDAY);
        if(wednesdayCheckBox.isSelected()) days.add(DayOfWeek.WEDNESDAY);
        if(thursdayCheckBox.isSelected()) days.add(DayOfWeek.THURSDAY);
        if(fridayCheckBox.isSelected()) days.add(DayOfWeek.FRIDAY);
        if(saturdayCheckBox.isSelected()) days.add(DayOfWeek.SATURDAY);
        if(sundayCheckBox.isSelected()) days.add(DayOfWeek.SUNDAY);
        return days;
    }

    public LocalTime getSelectedTime() {
        return LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue());
    }

    public String getSelectedMelody() {
        return melodyComboBox.getValue();
    }

    public String getSelectedTitle() {return titleAlarm.getText();}

    public String getSelectedTimeFormatted() {
        return String.format("%02d:%02d", hourSpinner.getValue(), minuteSpinner.getValue());
    }
}