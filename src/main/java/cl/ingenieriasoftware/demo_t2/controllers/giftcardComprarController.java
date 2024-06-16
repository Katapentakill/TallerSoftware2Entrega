package cl.ingenieriasoftware.demo_t2.controllers;

import cl.ingenieriasoftware.demo_t2.entities.Giftcard;
import cl.ingenieriasoftware.demo_t2.entities.Service;
import cl.ingenieriasoftware.demo_t2.entities.Users;
import cl.ingenieriasoftware.demo_t2.services.ApiService;
import cl.ingenieriasoftware.demo_t2.services.ServiceImpl;
import cl.ingenieriasoftware.demo_t2.services.UserService;
import cl.ingenieriasoftware.demo_t2.services.UserServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class giftcardComprarController {
    @FXML
    private ListView<CheckBox> servicesListView;
    @FXML
    private Label totalAmountLabel;
    @FXML
    private RadioButton creditCardRadioButton;
    @FXML
    private RadioButton pointsRadioButton;
    @FXML
    private VBox creditCardDetailsVBox;
    @FXML
    private TextField cardNumberTextField;
    @FXML
    private TextField cardHolderNameTextField;
    @FXML
    private TextField cardExpiryMonthTextField;
    @FXML
    private TextField cardExpiryYearTextField;
    @FXML
    private TextField cardSecurityCodeTextField;
    @FXML
    private ToggleGroup metodoDePago;

    private ServiceImpl serviceImpl;
    private Users loggedInUser;

    /**
     * Configura el usuario logueado.
     * @param user Usuario logueado.
     */
    public void setLoggedInUser(Users user) {
        loggedInUser = user;
    }

    /**
     * Inicializa el controlador, carga los servicios disponibles y configura listeners.
     */
    public void initialize(){
        serviceImpl = new ServiceImpl();
        serviceImpl.SeedServices();

        List<Service> services = serviceImpl.GetServices();

        if(servicesListView != null){
            if(services.isEmpty()){
                showError("Por el momento no es posible la compra de tarjetas de regalos. Intente más tarde");
                return;
            }
            servicesListView.setItems(FXCollections.observableArrayList(
                    services.stream().map(service -> {
                        CheckBox checkBox = new CheckBox(service.getId() + " - " + service.getNombre() + " - $" + service.getPrecio());
                        checkBox.setUserData(service.getId());
                        checkBox.setOnAction(event -> updateTotalAmount());
                        return checkBox;
                    }).collect(Collectors.toList())
            ));
        }

        creditCardRadioButton.setOnAction(event -> updatePaymentMethod());
        pointsRadioButton.setOnAction(event -> updatePaymentMethod());
    }

    /**
     * Actualiza el monto total basado en los servicios seleccionados.
     */
    private void updateTotalAmount() {
        double totalAmount = servicesListView.getItems().stream()
                .filter(CheckBox::isSelected)
                .mapToDouble(this::extractPrice)
                .sum();
        totalAmountLabel.setText("$" + totalAmount);
        totalAmountLabel.setVisible(totalAmount > 0);
    }

    /**
     * Extrae el precio de un servicio a partir del texto del CheckBox.
     * @param checkBox CheckBox que contiene la información del servicio.
     * @return Precio del servicio.
     */
    private double extractPrice(CheckBox checkBox){
        String text = checkBox.getText();
        int firstSpaceIndex = text.indexOf(' ');

        if (firstSpaceIndex == -1) {
            showError("Formato incorrecto para obtener el precio del servicio.");
            return 0.0;
        }
        String priceString = text.substring(firstSpaceIndex).trim();
        priceString = priceString.replaceAll("[^\\d.]", "");

        return Double.parseDouble(priceString);
    }

    /**
     * Actualiza la visibilidad del VBox de detalles de tarjeta de crédito según el método de pago seleccionado.
     */
    private void updatePaymentMethod(){
        boolean isCreditCard = creditCardRadioButton.isSelected();
        creditCardDetailsVBox.setVisible(isCreditCard);
    }

    /**
     * Maneja el evento de compra de la tarjeta de regalo.
     * @param actionEvent Evento de acción que desencadena la compra.
     */
    public void handleBuyGiftcard(ActionEvent actionEvent) {
        if (loggedInUser == null){
            showError("Debe iniciar sesion para realizar una compra.");
            return;
        }

        List<CheckBox> selectedServices = servicesListView.getItems().stream().filter(CheckBox::isSelected).collect(Collectors.toList());

        Toggle optionPayment = metodoDePago.getSelectedToggle();

        if(selectedServices.isEmpty()){
            showError("Debe seleccionar al menos un servicio.");
            return;
        }

        if(optionPayment == null){
            showError("Debe seleccionar un metodo de pago");
            return;
        }

        String paymentMethod = ((RadioButton) metodoDePago.getSelectedToggle()).getText();
        double totalAmount = selectedServices.stream().mapToDouble(this::extractPrice).sum();

        if(paymentMethod.equals("Tarjeta de Crédito")){
            String cardNumber = cardNumberTextField.getText();
            String cardHolderName = cardHolderNameTextField.getText();
            String cardExpMonth = cardExpiryMonthTextField.getText();
            String cardExpYear = cardExpiryYearTextField.getText();
            String cardSecCode = cardSecurityCodeTextField.getText();

            if(cardNumber.isEmpty() || cardHolderName.isEmpty() || cardExpMonth.isEmpty() || cardExpYear.isEmpty() || cardSecCode.isEmpty()){
                showError("Debe ingresar todos los detalles de la tarjeta.");
                return;
            }

            if(!cardNumber.matches("\\d{16}")){
                showError("El numero de tarjeta debe tener 16 digitos");
                return;
            }

            if(!cardSecCode.matches("\\d{3}")){
                showError("El codigo de seguridad debe tener 3 digitos");
                return;
            }

            int cardExpiryMonth;
            int cardExpiryYear;
            try {
                cardExpiryMonth = Integer.parseInt(cardExpMonth.replaceFirst("^0+(?!$)", ""));
                cardExpiryYear = Integer.parseInt(cardExpYear);
            } catch (NumberFormatException e) {
                showError("El mes y el año de vencimiento deben ser números válidos.");
                return;
            }
            int cardSecurityCode = Integer.parseInt(cardSecCode.replaceFirst("^0+(?!$)", ""));
            String montoTotal = String.valueOf(totalAmount);
            System.out.println("Token:" + loggedInUser.getToken());

            try {
                boolean tarjetaValida = ApiService.validarTarjeta(cardNumber, cardExpiryMonth, cardExpiryYear, cardSecurityCode, loggedInUser.getToken());
                if (!tarjetaValida) {
                    showError("La tarjeta no es válida. Por favor, verifique los datos ingresados.");
                    return;
                }
                ApiService.realizarCargo(cardNumber,montoTotal,"Compra Autolavado Mendez",cardExpiryMonth,cardExpiryYear,cardSecurityCode, loggedInUser.getToken());

            } catch (IOException | InterruptedException e) {
                showError("Error al verificar la tarjeta: " + e.getMessage());
                return;
            }

        } else if (paymentMethod.equals("Puntos")){
            if (loggedInUser.getPuntos() < totalAmount){
                showError("No tiene suficientes puntos para realizar esta compra.");
                return;
            }
            loggedInUser.setPuntos(loggedInUser.getPuntos() - (int) totalAmount);
        }

        List<Service> selectedServicesList = selectedServices.stream()
                .map(checkBox -> {
                    int serviceId = (int) checkBox.getUserData();
                    return serviceImpl.GetService(serviceId);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Giftcard giftcard = new Giftcard(selectedServicesList);
        loggedInUser.getGiftcards().add(giftcard);

        int pointsEarned = (int) Math.round(totalAmount * 0.20);
        loggedInUser.setPuntos(loggedInUser.getPuntos() + pointsEarned);

        showInfo(giftcard);
    }

    /**
     * Muestra un mensaje de error en una ventana de alerta.
     * @param message Mensaje de error a mostrar.
     */
    private void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra información detallada sobre la tarjeta de regalo comprada.
     * @param giftcard Tarjeta de regalo comprada.
     */
    private void showInfo(Giftcard giftcard){
        StringBuilder infoMessage = new StringBuilder();
        infoMessage.append("Compra realizada con exito.\n");
        infoMessage.append("Codigo de giftcard: ").append(giftcard.getCodigo()).append("\n");
        infoMessage.append("Fecha y hora de compra: ").append(giftcard.getFechaCompra()).append("\n");
        infoMessage.append("Servicios:\n");
        for (Service servicio : giftcard.getServicios()){
            infoMessage.append("- ").append(servicio.getNombre()).append("\n");
        }
        infoMessage.append("Fecha de vencimiento: ").append(giftcard.getFechaVencimiento());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(infoMessage.toString());
        alert.showAndWait();
    }
}

