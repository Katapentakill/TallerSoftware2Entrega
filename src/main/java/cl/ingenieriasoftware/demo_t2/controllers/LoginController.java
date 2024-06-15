package cl.ingenieriasoftware.demo_t2.controllers;

import cl.ingenieriasoftware.demo_t2.DemoApplication;
import cl.ingenieriasoftware.demo_t2.Util.AlertMessage;
import cl.ingenieriasoftware.demo_t2.entities.Users;
import cl.ingenieriasoftware.demo_t2.services.ApiService;
import cl.ingenieriasoftware.demo_t2.services.UserService;
import cl.ingenieriasoftware.demo_t2.services.UserServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    private UserService userService;

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasenia;

    @FXML
    private Label btnRegistrar;

    public LoginController() {
        this.userService = UserServiceImpl.getInstance();
    }
    @FXML
    private void handleLogin(ActionEvent event) throws IOException, InterruptedException {
        String username = txtUsuario.getText();
        String password = txtContrasenia.getText();

        if (username.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Debe ingresar su correo electrónico para iniciar sesión");
            return;
        }

        if (password.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Debe ingresar su contraseña para iniciar sesión");
            return;
        }

        if (!userService.ExistEmail(username)) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Usuario no registrado o contraseña incorrecta");
            return;
        }

        if (!userService.isValidLogin(username, password)) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Usuario no registrado o contraseña incorrecta");
            return;
        }
        String Token = ApiService.login("fhamen26","123789");
        Users usu = userService.GetUserByEmail(username);
        usu.setToken(Token);
        System.out.println("Token del usuario: " + usu.getToken());
        if (usu.isEsJefeDeLocal()) {
            FXMLLoader loader = new FXMLLoader(DemoApplication.class.getResource("administrar-servicios-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Servicios");
            stage.show();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(DemoApplication.class.getResource("comprar-giftcard-view.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Comprar giftcard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRegisterClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(DemoApplication.class.getResource("registrar-usuario-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrar usuario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}