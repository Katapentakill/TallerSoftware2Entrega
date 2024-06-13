package cl.ingenieriasoftware.demo_t2.controllers;

import cl.ingenieriasoftware.demo_t2.Util.AlertMessage;
import cl.ingenieriasoftware.demo_t2.entities.Service;
import cl.ingenieriasoftware.demo_t2.services.Services;
import cl.ingenieriasoftware.demo_t2.services.ServiceImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class ServicesController {
    @FXML
    private TableView<Service> TableAdmin;
    @FXML
    private TableColumn<Service, Integer> TableId;
    @FXML
    private TableColumn<Service, String> TableNombre;
    @FXML
    private TableColumn<Service, Integer> TablePrecio;
    @FXML
    private TextField AddNombre;
    @FXML
    private TextField AddPrecio;
    @FXML
    private TextField DeleteId;
    @FXML
    private TextField UpdateId;
    @FXML
    private TextField UpdateNombre;
    @FXML
    private TextField UpdatePrecio;

    private Services service;

    public ServicesController(){
        this.service = new ServiceImpl();
    }

    public void initialize() {
        if (service.GetServices().isEmpty()) {
            service.SeedServices();
        }
        loadServices();
    }

    private void loadServices() {
        List<Service> services = service.GetServices();
        if (services == null || services.isEmpty()) {
            AlertMessage.show(Alert.AlertType.INFORMATION, "Información", "No hay servicios registrados");
            TableAdmin.setItems(FXCollections.emptyObservableList());
        } else {
            ObservableList<Service> observableServices = FXCollections.observableArrayList(services);
            TableAdmin.setItems(observableServices);
        }
        TableId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        TableNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        TablePrecio.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrecio()));
    }

    @FXML
    private void handleAddService() {
        String nombre = AddNombre.getText();
        String precioText = AddPrecio.getText();

        if (nombre.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El nombre del servicio no puede estar vacío");
            return;
        }

        if (service.ExistService(nombre)) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "Este nombre de servicio ya existe");
            return;
        }

        if (precioText.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El precio del servicio no puede estar vacío");
            return;
        }

        int precio;
        try {
            precio = Integer.parseInt(precioText);
        } catch (NumberFormatException e) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El precio debe ser un número");
            return;
        }

        if (precio < 300) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El precio del servicio no puede ser inferior a 300 pesos");
            return;
        }

        service.AddService(nombre, precio);
        loadServices();
        AddNombre.clear();
        AddPrecio.clear();
    }

    @FXML
    private void handleDeleteService() {
        String idText = DeleteId.getText();

        if (idText.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El ID del servicio no puede estar vacío");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El ID debe ser un número");
            return;
        }

        if (service.DeleteService(id)) {
            loadServices();
            AlertMessage.show(Alert.AlertType.INFORMATION, "SUCESS", "Servicio eliminado con éxito");
        } else {
            AlertMessage.show(Alert.AlertType.ERROR, "ERROR", "Servicio no pudo eliminarse");
        }
        DeleteId.clear();
    }

    @FXML
    private void handleEditService(){
        String idText = UpdateId.getText();
        String nombre = UpdateNombre.getText();
        String precioText = UpdatePrecio.getText();

        if (idText.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El ID del servicio no puede estar vacío");
            return;
        }

        if (nombre.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El nombre del servicio no puede estar vacío");
            return;
        }

        if (precioText.isEmpty()) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El precio del servicio no puede estar vacío");
            return;
        }

        int id, precio;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El ID debe ser un número");
            return;
        }

        try {
            precio = Integer.parseInt(precioText);
        } catch (NumberFormatException e) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El precio debe ser un número");
            return;
        }

        if (precio < 300) {
            AlertMessage.show(Alert.AlertType.ERROR, "Error", "El precio del servicio no puede ser inferior a 300 pesos");
            return;
        }

        service.EditService(id, precio, nombre);
        loadServices();
    }
}