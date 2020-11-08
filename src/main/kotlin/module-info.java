module ApplicationModule {
    requires javafx.controls;
    requires javafx.graphics;
    requires tornadofx;
    requires kotlin.stdlib;
    requires commons.csv;
    opens ui;
    opens app;
}
