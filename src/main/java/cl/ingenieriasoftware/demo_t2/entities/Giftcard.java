package cl.ingenieriasoftware.demo_t2.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Giftcard {
    private static int counter = 0;
    private String codigo;
    private LocalDateTime fechaCompra;
    private List<Service> servicios;
    private LocalDate fechaVencimiento;

    public Giftcard(List<Service> servicios) {
        this.codigo = generateCodigo();
        this.fechaCompra = LocalDateTime.now();
        this.servicios = servicios;
        this.fechaVencimiento = fechaCompra.toLocalDate().plusMonths(6);
    }

    private String generateCodigo() {
        counter++;
        return String.format("%08d", counter);
    }

    // Getters y setters

    public String getCodigo() {
        return codigo;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public List<Service> getServicios() {
        return servicios;
    }

    public void setServicios(List<Service> servicios) {
        this.servicios = servicios;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}