package cl.ingenieriasoftware.demo_t2;

import cl.ingenieriasoftware.demo_t2.entities.Users;
import cl.ingenieriasoftware.demo_t2.services.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DemoApplication extends Application {

    // Método principal de inicio de la aplicación JavaFX
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        // Obtener la instancia del servicio de usuarios
        UserService userService = UserServiceImpl.getInstance();

        // Método para crear un usuario administrador inicial (opcional)
        userService.Admin();

        // Cargar la vista "iniciar-sesion-view.fxml" usando FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader(DemoApplication.class.getResource("iniciar-sesion-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // Crear una nueva escena con la vista cargada

        // Configurar la ventana principal (Stage)
        stage.setTitle("Bienvenido(a)"); // Título de la ventana
        stage.setScene(scene); // Establecer la escena en la ventana
        stage.show(); // Mostrar la ventana principal
    }
}