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
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

public class ServicesController {
    @FXML
    private TableView<Service> TableAdmin;   // Tabla para mostrar servicios
    @FXML
    private TableColumn<Service, Integer> TableId;   // Columna de ID en la tabla
    @FXML
    private TableColumn<Service, String> TableNombre;   // Columna de Nombre en la tabla
    @FXML
    private TableColumn<Service, Integer> TablePrecio;   // Columna de Precio en la tabla
    @FXML
    private TextField AddNombre;   // Campo de texto para agregar nombre de servicio
    @FXML
    private TextField AddPrecio;   // Campo de texto para agregar precio de servicio
    @FXML
    private TextField DeleteId;   // Campo de texto para eliminar por ID de servicio
    @FXML
    private TextField UpdateId;   // Campo de texto para editar por ID de servicio
    @FXML
    private TextField UpdateNombre;   // Campo de texto para editar nombre de servicio
    @FXML
    private TextField UpdatePrecio;   // Campo de texto para editar precio de servicio

    private Services service;   // Servicio para la lógica de negocio

    // Constructor que inicializa el servicio
    public ServicesController() {
        this.service = ServiceImpl.getInstance();
    }

    // Método inicializador del controlador
    public void initialize() {
        // Si no hay servicios registrados, se genera una lista inicial de servicios
        if (service.GetServices().isEmpty()) {
            service.SeedServices();
        }
        // Carga los servicios en la tabla
        loadServices();
    }

    // Método privado para cargar los servicios en la tabla
    private void loadServices() {
        List<Service> services = service.GetServices();
        if (services == null || services.isEmpty()) {
            // Si no hay servicios, muestra un mensaje de información y limpia la tabla
            AlertMessage.show(Alert.AlertType.INFORMATION, "Información", "No hay servicios registrados");
            TableAdmin.setItems(FXCollections.emptyObservableList());
        } else {
            // Si hay servicios, los carga en la tabla
            ObservableList<Service> observableServices = FXCollections.observableArrayList(services);
            TableAdmin.setItems(observableServices);
        }
        // Configura las propiedades de las columnas de la tabla
        TableId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        TableNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        TablePrecio.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrecio()));
    }

    // Método privado para mostrar un diálogo de confirmación
    private boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType buttonTypeYes = new ButtonType("Sí");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        return alert.showAndWait().orElse(buttonTypeNo) == buttonTypeYes;
    }

    // Manejador de eventos para agregar servicio
    @FXML
    private void handleAddService() {
        String nombre = AddNombre.getText();
        String precioText = AddPrecio.getText();

        // Validaciones para agregar un servicio
        if (nombre.isEmpty()) {
            AlertMessage.show(AlertType.ERROR, "Error", "El nombre del servicio no puede estar vacío");
            return;
        }

        if (service.ExistService(nombre)) {
            AlertMessage.show(AlertType.ERROR, "Error", "Este nombre de servicio ya existe");
            return;
        }

        if (precioText.isEmpty()) {
            AlertMessage.show(AlertType.ERROR, "Error", "El precio del servicio no puede estar vacío");
            return;
        }

        int precio;
        try {
            precio = Integer.parseInt(precioText);
        } catch (NumberFormatException e) {
            AlertMessage.show(AlertType.ERROR, "Error", "El precio debe ser un número");
            return;
        }

        if (precio < 300) {
            AlertMessage.show(AlertType.ERROR, "Error", "El precio del servicio no puede ser inferior a 300 pesos");
            return;
        }

        // Si se confirma la adición del servicio, se agrega y se recarga la lista
        if (showConfirmationDialog("¿Está seguro que desea agregar este servicio?")) {
            if (showConfirmationDialog("¿Realmente está seguro?")) {
                service.AddService(nombre, precio);
                loadServices();
                AddNombre.clear();
                AddPrecio.clear();
            }
        }
    }

    // Manejador de eventos para eliminar servicio
    @FXML
    private void handleDeleteService() {
        String idText = DeleteId.getText();

        // Validaciones para eliminar un servicio
        if (idText.isEmpty()) {
            AlertMessage.show(AlertType.ERROR, "Error", "El ID del servicio no puede estar vacío");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            AlertMessage.show(AlertType.ERROR, "Error", "El ID debe ser un número");
            return;
        }

        // Si se confirma la eliminación del servicio, se elimina y se recarga la lista
        if (showConfirmationDialog("¿Está seguro que desea eliminar este servicio?")) {
            if (showConfirmationDialog("¿Realmente está seguro?")) {
                if (service.DeleteService(id)) {
                    loadServices();
                    AlertMessage.show(AlertType.INFORMATION, "SUCCESS", "Servicio eliminado con éxito");
                } else {
                    AlertMessage.show(AlertType.ERROR, "ERROR", "Servicio no pudo eliminarse");
                }
                DeleteId.clear();
            }
        }
    }

    // Manejador de eventos para editar servicio
    @FXML
    private void handleEditService() {
        String idText = UpdateId.getText();
        String nombre = UpdateNombre.getText();
        String precioText = UpdatePrecio.getText();

        // Validaciones para editar un servicio
        if (idText.isEmpty()) {
            AlertMessage.show(AlertType.ERROR, "Error", "El ID del servicio no puede estar vacío");
            return;
        }

        if (nombre.isEmpty()) {
            AlertMessage.show(AlertType.ERROR, "Error", "El nombre del servicio no puede estar vacío");
            return;
        }

        if (precioText.isEmpty()) {
            AlertMessage.show(AlertType.ERROR, "Error", "El precio del servicio no puede estar vacío");
            return;
        }

        int id, precio;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            AlertMessage.show(AlertType.ERROR, "Error", "El ID debe ser un número");
            return;
        }

        try {
            precio = Integer.parseInt(precioText);
        } catch (NumberFormatException e) {
            AlertMessage.show(AlertType.ERROR, "Error", "El precio debe ser un número");
            return;
        }

        if (precio < 300) {
            AlertMessage.show(AlertType.ERROR, "Error", "El precio del servicio no puede ser inferior a 300 pesos");
            return;
        }

        // Si se confirma la edición del servicio, se edita y se recarga la lista
        if (showConfirmationDialog("¿Está seguro que desea editar este servicio?")) {
            if (showConfirmationDialog("¿Realmente está seguro?")) {
                service.EditService(id, precio, nombre);
                loadServices();
            }
        }
    }
}