package cl.ingenieriasoftware.demo_t2.entities;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private static int counter = 0;  // Contador estático para asignar IDs únicos a cada usuario
    private int id;  // ID del usuario
    private String nombre;  // Nombre del usuario
    private int edad;  // Edad del usuario
    private String correo;  // Correo electrónico del usuario
    private String contraseña;  // Contraseña del usuario
    private boolean esJefeDeLocal;  // Indica si el usuario es jefe de local o no
    private List<Giftcard> giftcards;  // Lista de giftcards asociadas al usuario
    private String creditCard;  // Información de la tarjeta de crédito del usuario
    private int puntos;  // Puntos acumulados por el usuario
    private String Token;  // Token de autenticación del usuario

    // Constructor de la clase Users
    public Users(String nombre, int edad, String correo, String contraseña) {
        this.id = ++counter;  // Asigna un nuevo ID único cada vez que se crea un usuario
        this.nombre = nombre;  // Asigna el nombre del usuario
        this.edad = edad;  // Asigna la edad del usuario
        this.correo = correo;  // Asigna el correo electrónico del usuario
        this.contraseña = contraseña;  // Asigna la contraseña del usuario
        this.esJefeDeLocal = false;  // Inicializa esJefeDeLocal como falso por defecto
        this.giftcards = new ArrayList<>();  // Inicializa la lista de giftcards como una lista vacía
        this.puntos = 0;  // Inicializa los puntos acumulados como 0
        this.Token = "";  // Inicializa el token como cadena vacía
    }

    // Getters y setters para los atributos de la clase

    public String getToken() {
        return Token;  // Retorna el token de autenticación del usuario
    }

    public void setToken(String Tok) {
        this.Token = Tok;  // Establece el token de autenticación del usuario
    }

    public int getId() {
        return id;  // Retorna el ID del usuario
    }

    public String getNombre() {
        return nombre;  // Retorna el nombre del usuario
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;  // Establece el nombre del usuario
    }

    public int getEdad() {
        return edad;  // Retorna la edad del usuario
    }

    public void setEdad(int edad) {
        this.edad = edad;  // Establece la edad del usuario
    }

    public String getCorreo() {
        return correo;  // Retorna el correo electrónico del usuario
    }

    public void setCorreo(String correo) {
        this.correo = correo;  // Establece el correo electrónico del usuario
    }

    public String getContraseña() {
        return contraseña;  // Retorna la contraseña del usuario
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;  // Establece la contraseña del usuario
    }

    public boolean isEsJefeDeLocal() {
        return esJefeDeLocal;  // Retorna true si el usuario es jefe de local, false en caso contrario
    }

    public void setEsJefeDeLocal(boolean esJefeDeLocal) {
        this.esJefeDeLocal = esJefeDeLocal;  // Establece si el usuario es jefe de local
    }

    public List<Giftcard> getGiftcards() {
        return giftcards;  // Retorna la lista de giftcards asociadas al usuario
    }

    public void setGiftcards(List<Giftcard> giftcards) {
        this.giftcards = giftcards;  // Establece la lista de giftcards asociadas al usuario
    }

    public String getCreditCard() {
        return creditCard;  // Retorna la información de la tarjeta de crédito del usuario
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;  // Establece la información de la tarjeta de crédito del usuario
    }

    public int getPuntos() {
        return puntos;  // Retorna los puntos acumulados por el usuario
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;  // Establece los puntos acumulados por el usuario
    }
}