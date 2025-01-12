module com.astarvisualization.astarvisualization {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.astarvisualization.astarvisualization to javafx.fxml;
    exports com.astarvisualization.astarvisualization;
}