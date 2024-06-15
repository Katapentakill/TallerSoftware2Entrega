package cl.ingenieriasoftware.demo_t2.entities;

import java.io.Serializable;

public class Service implements Serializable {
    private static final long serialVersionUID = 1L;  // Identificador de versión para serialización

    private static int idCounter = 1;  // Contador estático para generar IDs únicos

    private int id;  // ID del servicio
    private String nombre;  // Nombre del servicio
    private int precio;  // Precio del servicio

    // Constructor de la clase Service
    public Service(String nombre, int precio) {
        this.id = idCounter++;  // Asigna un nuevo ID único cada vez que se crea un servicio
        this.nombre = nombre;  // Asigna el nombre del servicio
        this.precio = precio;  // Asigna el precio del servicio
    }

    // Getters y setters para los atributos de la clase

    public int getId() {
        return id;  // Retorna el ID del servicio
    }

    public String getNombre() {
        return nombre;  // Retorna el nombre del servicio
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;  // Establece el nombre del servicio
    }

    public int getPrecio() {
        return precio;  // Retorna el precio del servicio
    }

    public void setPrecio(int precio) {
        this.precio = precio;  // Establece el precio del servicio
    }
}