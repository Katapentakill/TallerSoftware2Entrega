package cl.ingenieriasoftware.demo_t2.entities;

import java.io.Serializable;

public class Service implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int idCounter = 1;

    private int id;
    private String nombre;
    private int precio;

    public Service(String nombre, int precio) {
        this.id = idCounter++;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y setters

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}