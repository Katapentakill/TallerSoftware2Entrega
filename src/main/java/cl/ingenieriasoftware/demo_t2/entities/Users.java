package cl.ingenieriasoftware.demo_t2.entities;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private static int counter = 0;
    private int id;
    private String nombre;
    private int edad;
    private String correo;
    private String contraseña;
    private boolean esJefeDeLocal;
    private List<Giftcard> giftcards;
    private String creditCard;
    private int puntos;
    private String Token;

    public Users(String nombre, int edad, String correo, String contraseña) {
        this.id = ++counter;
        this.nombre = nombre;
        this.edad = edad;
        this.correo = correo;
        this.contraseña = contraseña;
        this.esJefeDeLocal = false;
        this.giftcards = new ArrayList<>();
        this.puntos = 0;
        this.Token = "";
    }

    // Getters y setters
    public String getToken() {
        return Token;
    }

    public void setToken(String Tok) {
        this.Token = Tok;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public boolean isEsJefeDeLocal() {
        return esJefeDeLocal;
    }

    public void setEsJefeDeLocal(boolean esJefeDeLocal) {
        this.esJefeDeLocal = esJefeDeLocal;
    }

    public List<Giftcard> getGiftcards() {
        return giftcards;
    }

    public void setGiftcards(List<Giftcard> giftcards) {
        this.giftcards = giftcards;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}