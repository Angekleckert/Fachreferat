module com.example.spaceadventurefachreferat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.ange.spaceadventurefachreferat to javafx.fxml;
    exports com.ange.spaceadventurefachreferat;
    exports com.ange.spaceadventurefachreferat.graphic;
    exports com.ange.spaceadventurefachreferat.entity;
    exports com.ange.spaceadventurefachreferat.entity.pos;
    exports com.ange.spaceadventurefachreferat.entity.collision;
    exports com.ange.spaceadventurefachreferat.entity.impl;

}