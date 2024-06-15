package cl.ingenieriasoftware.demo_t2.controllers;

import cl.ingenieriasoftware.demo_t2.Util.AlertMessage;   // Utilidad para mostrar alertas
import cl.ingenieriasoftware.demo_t2.services.UserService;   // Servicio de usuario
import cl.ingenieriasoftware.demo_t2.services.UserServiceImpl;   // Implementación del servicio de usuario
import javafx.event.ActionEvent;   // Evento de acción de JavaFX
import javafx.fxml.FXML;   // Anotación para vincular con elementos del archivo FXML
import javafx.scene.control.Alert;   // Clase de alerta de JavaFX
import javafx.scene.control.PasswordField;   // Campo de contraseña de JavaFX
import javafx.scene.control.TextField;   // Campo de texto de JavaFX

public class RegisterController {

    private UserService userService;   // Servicio para la lógica de registro de usuarios

    // Elementos de la interfaz vinculados desde el archivo FXML
    @FXML
    private TextField txtNombreR;   // Campo de texto para el nombre
    @FXML
    private TextField txtEdadR;   // Campo de texto para la edad
    @FXML
    private TextField txtEmailR;   // Campo de texto para el correo electrónico
    @FXML
    private PasswordField txtContraR;   // Campo de contraseña para la contraseña
    @FXML
    private PasswordField txtContraRC;   // Campo de contraseña para repetir la contraseña

    // Constructor del controlador que inicializa el servicio de usuario
    public RegisterController(){
        this.userService = UserServiceImpl.getInstance();
    }

    // Método manejador de evento para el registro de usuario
    @FXML
    public void handleRegister(ActionEvent event){
        // Obtener los valores ingresados por el usuario
        String nombre = txtNombreR.getText();
        String edadString = txtEdadR.getText();
        String email = txtEmailR.getText();
        String contra = txtContraR.getText();
        String contraRC = txtContraRC.getText();

        // Validar campos vacíos
        if (nombre.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Debe completar el campo nombre");
            return;
        }
        if (edadString.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Debe completar el campo edad");
            return;
        }
        if (email.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Debe completar el campo correo electrónico");
            return;
        }
        if (contra.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Debe completar el campo contraseña");
            return;
        }
        if (contraRC.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Debe completar el campo repetir contraseña");
            return;
        }

        // Validar que la edad sea numérica
        int edad;
        try {
            edad = Integer.parseInt(edadString);
        } catch (NumberFormatException e) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "La edad debe ser numérica, Ej: 22");
            return;
        }

        // Validar rango de edad
        if (edad < 18 || edad > 65) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "La edad no puede ser inferior a 18 ni mayor a 65");
            return;
        }

        // Validar que las contraseñas coincidan
        if (!contra.equals(contraRC)) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Las contraseñas ingresadas no coinciden");
            return;
        }

        // Validar que el correo electrónico sea único
        if (userService.ExistEmail(email)) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El correo electrónico ingresado ya existe en el sistema");
            return;
        }

        // Registrar usuario si pasa todas las validaciones
        userService.registerUser(nombre, edad, email, contra);
        AlertMessage.show(Alert.AlertType.INFORMATION, "Success", "Usuario registrado con éxito");
    }
}