module ca.bcit.comp2522.termproject.anacondaadventure {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    //requires org.controlsfx.controls;
    //requires org.kordamp.bootstrapfx.core;

    opens ca.bcit.comp2522.termproject.anacondaadventure to javafx.fxml;
    exports ca.bcit.comp2522.termproject.anacondaadventure;
}