package org.example.oop;

import javafx.scene.control.Alert;

public class Utils {

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
