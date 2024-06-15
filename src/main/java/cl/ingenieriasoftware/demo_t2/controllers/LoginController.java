package cl.ingenieriasoftware.demo_t2.controllers;

import cl.ingenieriasoftware.demo_t2.DemoApplication;   // Clase principal de la aplicación
import cl.ingenieriasoftware.demo_t2.Util.AlertMessage;   // Utilidad para mostrar alertas
import cl.ingenieriasoftware.demo_t2.entities.Users;   // Entidad de usuario
import cl.ingenieriasoftware.demo_t2.services.ApiService;   // Servicio de API
import cl.ingenieriasoftware.demo_t2.services.UserService;   // Servicio de usuario
import cl.ingenieriasoftware.demo_t2.services.UserServiceImpl;   // Implementación del servicio de usuario
import javafx.event.ActionEvent;   // Evento de acción de JavaFX
import javafx.fxml.FXML;   // Anotación para vincular con elementos del archivo FXML
import javafx.scene.control.Alert;   // Clase de alerta de JavaFX
import javafx.scene.control.Label;   // Etiqueta de JavaFX
import javafx.scene.control.PasswordField;   // Campo de contraseña de JavaFX
import javafx.scene.control.TextField;   // Campo de texto de JavaFX
import javafx.fxml.FXMLLoader;   // Cargador de FXML de JavaFX
import javafx.scene.Parent;   // Elemento raíz de JavaFX
import javafx.scene.Scene;   // Escena de JavaFX
import javafx.scene.input.MouseEvent;   // Evento de clic de ratón de JavaFX
import javafx.stage.Stage;   // Ventana de JavaFX
import java.io.IOException;   // Excepción de entrada/salida de Java

public class LoginController {

    private UserService userService;   // Servicio para la lógica de usuario

    // Elementos de la interfaz vinculados desde el archivo FXML
    @FXML
    private TextField txtUsuario;   // Campo de texto para el nombre de usuario (correo electrónico)
    @FXML
    private PasswordField txtContrasenia;   // Campo de contraseña
    @FXML
    private Label btnRegistrar;   // Etiqueta para el enlace de registro

    // Constructor del controlador que inicializa el servicio de usuario
    public LoginController() {
        this.userService = UserServiceImpl.getInstance();
    }

    // Método manejador de evento para el inicio de sesión
    @FXML
    private void handleLogin(ActionEvent event) throws IOException, InterruptedException {
        String username = txtUsuario.getText();   // Obtener el nombre de usuario (correo electrónico)
        String password = txtContrasenia.getText();   // Obtener la contraseña

        // Validar que el campo de nombre de usuario no esté vacío
        if (username.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Debe ingresar su correo electrónico para iniciar sesión");
            return;
        }

        // Validar que el campo de contraseña no esté vacío
        if (password.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Debe ingresar su contraseña para iniciar sesión");
            return;
        }

        // Validar la existencia del correo electrónico en el sistema
        if (!userService.ExistEmail(username)) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Usuario no registrado o contraseña incorrecta");
            return;
        }

        // Validar la autenticidad del inicio de sesión
        if (!userService.isValidLogin(username, password)) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Usuario no registrado o contraseña incorrecta");
            return;
        }

        // Obtener el token del usuario desde el servicio de API
        String Token = ApiService.login("fhamen26", "123789");   // Ejemplo: Esto debe ajustarse según la implementación real

        // Obtener el objeto de usuario desde el servicio de usuario
        Users usu = userService.GetUserByEmail(username);

        // Establecer el token obtenido al usuario
        usu.setToken(Token);

        // Imprimir el token del usuario en la consola (ejemplo)
        System.out.println("Token del usuario: " + usu.getToken());

        // Determinar la vista a mostrar según el tipo de usuario
        if (usu.isEsJefeDeLocal()) {
            // Cargar la vista de administración de servicios para el jefe de local
            FXMLLoader loader = new FXMLLoader(DemoApplication.class.getResource("administrar-servicios-view.fxml"));
            Parent root = loader.load();   // Cargar la interfaz
            Stage stage = new Stage();   // Crear una nueva ventana
            stage.setScene(new Scene(root));   // Establecer la escena con la interfaz cargada
            stage.setTitle("Servicios");   // Establecer el título de la ventana
            stage.show();   // Mostrar la ventana
        } else {
            // Si no es jefe de local, cargar la vista para comprar giftcard
            FXMLLoader loader = new FXMLLoader(DemoApplication.class.getResource("comprar-giftcard-view.fxml"));
            Parent root = loader.load();   // Cargar la interfaz
            Stage stage = new Stage();   // Crear una nueva ventana
            stage.setScene(new Scene(root));   // Establecer la escena con la interfaz cargada
            stage.setTitle("Comprar giftcard");   // Establecer el título de la ventana
            stage.show();   // Mostrar la ventana
        }
    }

    // Método manejador de evento para el clic en el botón de registro
    @FXML
    private void handleRegisterClick(MouseEvent event) {
        try {
            // Cargar la vista de registro de usuario al hacer clic en la etiqueta
            FXMLLoader loader = new FXMLLoader(DemoApplication.class.getResource("registrar-usuario-view.fxml"));
            Parent root = loader.load();   // Cargar la interfaz
            Stage stage = new Stage();   // Crear una nueva ventana
            stage.setScene(new Scene(root));   // Establecer la escena con la interfaz cargada
            stage.setTitle("Registrar usuario");   // Establecer el título de la ventana
            stage.show();   // Mostrar la ventana
        } catch (IOException e) {
            e.printStackTrace();   // Manejar cualquier excepción de carga de la vista
        }
    }
}