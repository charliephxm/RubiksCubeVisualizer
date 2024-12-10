module com.rubikssolver.rubikscubevisualizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jgrapht.core;


    opens com.rubikssolver.rubikscubevisualizer to javafx.fxml;
    exports com.rubikssolver.rubikscubevisualizer;
}