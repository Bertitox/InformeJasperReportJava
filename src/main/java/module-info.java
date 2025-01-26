module org.example.practicainforme {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires net.sf.jasperreports.core;


    opens org.example.practicainforme to javafx.fxml;
    exports org.example.practicainforme;
}