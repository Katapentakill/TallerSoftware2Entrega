package cl.ingenieriasoftware.demo_t2;

import cl.ingenieriasoftware.demo_t2.entities.Users;
import cl.ingenieriasoftware.demo_t2.services.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DemoApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        UserService userService = new UserServiceImpl();
        userService.Admin();
        FXMLLoader fxmlLoader = new FXMLLoader(DemoApplication.class.getResource("iniciar-sesion-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bienvenido(a)");
        stage.setScene(scene);
        stage.show();
    }
}