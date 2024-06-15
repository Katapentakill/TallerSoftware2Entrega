package cl.ingenieriasoftware.demo_t2.controllers;

import cl.ingenieriasoftware.demo_t2.Util.AlertMessage;
import cl.ingenieriasoftware.demo_t2.services.UserService;
import cl.ingenieriasoftware.demo_t2.services.UserServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    private UserService userService;
    @FXML
    private TextField txtNombreR;
    @FXML
    private TextField txtEdadR;
    @FXML
    private TextField txtEmailR;
    @FXML
    private PasswordField txtContraR;
    @FXML
    private PasswordField txtContraRC;

    public RegisterController(){
        this.userService = UserServiceImpl.getInstance();
    }

    @FXML
    public void handleRegister(ActionEvent event){
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

        // Registrar usuario
        userService.registerUser(nombre, edad, email, contra);
        AlertMessage.show(Alert.AlertType.INFORMATION, "Success", "Usuario registrado con éxito");
    }
}