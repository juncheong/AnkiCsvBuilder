module ApplicationModule {
    requires javafx.controls;
    requires javafx.graphics;
    requires tornadofx;
    requires kotlin.stdlib;
    requires commons.csv;
    requires java.net.http;
    requires com.google.gson;
    requires org.jsoup;
    opens ui;
    opens app;
    opens controller;
}
