module ApplicationModule {
    requires javafx.controls;
    requires javafx.graphics;
    requires tornadofx;
    requires kotlin.stdlib;
    requires commons.csv;
    requires java.net.http;
    opens ui;
    opens app;
    opens controller;
}
