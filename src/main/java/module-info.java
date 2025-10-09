module org.example.oop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;
    requires gson.javatime.serialisers;

    opens org.example.oop to javafx.fxml, com.google.gson;
    opens org.example.oop.Controllers to javafx.fxml;

    exports org.example.oop;
    exports org.example.oop.Controllers;
    exports org.example.oop.Services;
}