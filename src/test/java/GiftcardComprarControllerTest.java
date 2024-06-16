import cl.ingenieriasoftware.demo_t2.controllers.giftcardComprarController;
import cl.ingenieriasoftware.demo_t2.entities.Giftcard;
import cl.ingenieriasoftware.demo_t2.entities.Service;
import cl.ingenieriasoftware.demo_t2.entities.Users;
import cl.ingenieriasoftware.demo_t2.services.ServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GiftcardComprarControllerTest {

    private giftcardComprarController controller;
    private Users loggedInUser;
    private ServiceImpl serviceImpl;
    private ObservableList<CheckBox> checkBoxes;

    @BeforeEach
    void setUp() {
        controller = new giftcardComprarController();
        loggedInUser = new Users("NombreUsuario", 1, "usuario@ejemplo.com", "contraseña");
        loggedInUser.setToken("test_token");
        controller.setLoggedInUser(loggedInUser);

        serviceImpl = new ServiceImpl();
        serviceImpl.SeedServices();
        checkBoxes = FXCollections.observableArrayList();
        for (Service service : serviceImpl.GetServices()) {
            CheckBox checkBox = new CheckBox(service.getId() + " - " + service.getNombre() + " - $" + service.getPrecio());
            checkBox.setUserData(service.getId());
            checkBoxes.add(checkBox);
        }

        controller.servicesListView = new javafx.scene.control.ListView<>(checkBoxes);
        controller.initialize();
    }

    private double extractPrice(CheckBox checkBox) {
        String text = checkBox.getText();
        int firstSpaceIndex = text.indexOf(' ');

        if (firstSpaceIndex == -1) {
            return 0.0;
        }
        String priceString = text.substring(firstSpaceIndex).trim();
        priceString = priceString.replaceAll("[^\\d.]", "");

        return Double.parseDouble(priceString);
    }
    @Test
    void testUpdateTotalAmount() {
        // Seleccionar algunos servicios
        checkBoxes.get(0).setSelected(true);
        checkBoxes.get(2).setSelected(true);

        controller.updateTotalAmount();

        double expectedTotal = extractPrice(checkBoxes.get(1)) + extractPrice(checkBoxes.get(3));
        assertEquals(expectedTotal, Double.parseDouble(controller.totalAmountLabel.getText().substring(1)));
    }

    @Test
    void testExtractPrice() {
        CheckBox checkBox = new CheckBox("123 - Test Service - $9.99");
        double expectedPrice = 9.99;
        assertEquals(expectedPrice, controller.extractPrice(checkBox));
    }

    @Test
    void testHandleBuyGiftcard_CreditCard() {
        // Configurar los detalles de la tarjeta de crédito
        controller.creditCardRadioButton.setSelected(true);
        controller.cardNumberTextField.setText("1234567890123456");
        controller.cardHolderNameTextField.setText("John Doe");
        controller.cardExpiryMonthTextField.setText("05");
        controller.cardExpiryYearTextField.setText("2025");
        controller.cardSecurityCodeTextField.setText("123");

        // Seleccionar algunos servicios
        checkBoxes.get(0).setSelected(true);
        checkBoxes.get(2).setSelected(true);

        controller.handleBuyGiftcard(null);

        assertFalse(loggedInUser.getGiftcards().isEmpty());
        Giftcard giftcard = loggedInUser.getGiftcards().get(0);
        assertEquals(2, giftcard.getServicios().size());
        assertTrue(giftcard.getServicios().contains(serviceImpl.GetService((int) checkBoxes.get(0).getUserData())));
        assertTrue(giftcard.getServicios().contains(serviceImpl.GetService((int) checkBoxes.get(2).getUserData())));
    }

    @Test
    void testHandleBuyGiftcard_Points() {
        // Configurar los puntos del usuario
        loggedInUser.setPuntos(1000);

        // Seleccionar algunos servicios
        checkBoxes.get(1).setSelected(true);
        checkBoxes.get(3).setSelected(true);

        controller.pointsRadioButton.setSelected(true);
        controller.handleBuyGiftcard(null);

        assertFalse(loggedInUser.getGiftcards().isEmpty());
        Giftcard giftcard = loggedInUser.getGiftcards().get(0);
        assertEquals(2, giftcard.getServicios().size());
        assertTrue(giftcard.getServicios().contains(serviceImpl.GetService((int) checkBoxes.get(1).getUserData())));
        assertTrue(giftcard.getServicios().contains(serviceImpl.GetService((int) checkBoxes.get(3).getUserData())));

        double totalAmount = extractPrice(checkBoxes.get(1)) + extractPrice(checkBoxes.get(3));
        int pointsEarned = (int) Math.round(totalAmount * 0.20);
        int expectedPoints = 1000 - (int) totalAmount + pointsEarned;
        assertEquals(expectedPoints, loggedInUser.getPuntos());
    }
}
