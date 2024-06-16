module cl.ingenieriasoftware.demo_t {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires java.desktop;

    opens cl.ingenieriasoftware.demo_t2 to javafx.fxml;
    opens cl.ingenieriasoftware.demo_t2.controllers to javafx.fxml;
    exports cl.ingenieriasoftware.demo_t2;
    exports cl.ingenieriasoftware.demo_t2.controllers;
    exports cl.ingenieriasoftware.demo_t2.services;
    exports cl.ingenieriasoftware.demo_t2.entities;
}
