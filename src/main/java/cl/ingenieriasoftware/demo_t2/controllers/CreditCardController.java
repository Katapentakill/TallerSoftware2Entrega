package cl.ingenieriasoftware.demo_t2.controllers;

import cl.ingenieriasoftware.demo_t2.DemoApplication;
import cl.ingenieriasoftware.demo_t2.Util.AlertMessage;
import cl.ingenieriasoftware.demo_t2.entities.CreditCard;
import cl.ingenieriasoftware.demo_t2.services.ApiService;
import cl.ingenieriasoftware.demo_t2.services.CreditCardService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreditCardController {

    @FXML
    private TextField txtNumero;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtFecha;
    @FXML
    private TextField txtCodigoSeguridad;
    @FXML
    private ListView<CreditCard> ListViewCreditCard;

    private ObservableList<CreditCard> creditCardList;

    private ApiService service;

    @FXML
    private void handleVerificar(ActionEvent event) {
        String numero = txtNumero.getText();
        String nombre = txtNombre.getText();
        String fecha = txtFecha.getText();
        String codigoSeguridad = txtCodigoSeguridad.getText();

        if (numero.isEmpty() || nombre.isEmpty() || fecha.isEmpty() || codigoSeguridad.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Por favor, complete todos los campos");
            return;
        }

        if (!fecha.matches("\\d{2}/\\d{4}")) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Por favor, ingrese una fecha en el formato Mes/Año Ej: 12/2024");
            return;
        }
        String[] partesFecha = fecha.split("/");
        int mes = Integer.parseInt(partesFecha[0]);
        int anio = Integer.parseInt(partesFecha[1]);
        int codigoSeguridadInt = Integer.parseInt(codigoSeguridad);

        try {
            boolean respuesta = service.validarTarjeta(numero, mes, anio, codigoSeguridadInt);
            if (respuesta) {
                AlertMessage.show(Alert.AlertType.INFORMATION, "Éxito", "Tarjeta Válida");
                CreditCardService.getInstance().addCreditCard(numero, nombre, partesFecha[0], partesFecha[1], codigoSeguridad);
            } else {
                AlertMessage.show(Alert.AlertType.ERROR, "Error", "Tarjeta Inválida");
            }
        } catch (ApiService.TokenExpiredException e) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "La sesión ha expirado. Por favor, inicie sesión nuevamente.");
            redirectToLogin(event);
        } catch (IOException | InterruptedException e) {
            String mensaje = e.getMessage();
            if (mensaje == null) {
                mensaje = "Por el momento no es posible verificar la tarjeta";
            }
            AlertMessage.show(Alert.AlertType.ERROR, "Error", mensaje);
        }
    }

    @FXML
    private void handleVerTarjetas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(DemoApplication.class.getResource("ver-tarjetas-view.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL); // Establecer la modalidad
        stage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Establecer la ventana propietaria
        stage.setScene(new Scene(root));
        stage.setTitle("Ver Tarjetas");

        CreditCardController controller = loader.getController();

        creditCardList = FXCollections.observableArrayList(CreditCardService.getInstance().getTarjetas());

        controller.ListViewCreditCard.setCellFactory(param -> new ListCell<CreditCard>() {
            @Override
            protected void updateItem(CreditCard creditCard, boolean empty) {
                super.updateItem(creditCard, empty);

                if (empty || creditCard == null) {
                    setText(null);
                } else {
                    setText(creditCard.toString());
                }
            }
        });

        controller.ListViewCreditCard.setItems(creditCardList);
        stage.showAndWait(); // Mostrar la ventana modal y esperar hasta que se cierre
    }

    private void redirectToLogin(ActionEvent event) {
        try {
            // Cerrar la ventana actual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(DemoApplication.class.getResource("login-view.fxml"));
            Parent root = loader.load();

            // Mostrar la vista de login
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Iniciar Sesión");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}