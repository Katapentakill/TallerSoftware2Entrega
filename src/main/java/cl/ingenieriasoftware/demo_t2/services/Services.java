package cl.ingenieriasoftware.demo_t2.services;

import cl.ingenieriasoftware.demo_t2.entities.Service;
import java.util.List;
public interface Services {
    void SeedServices();
    void AddService(String nombre, int precio);
    List<Service> GetServices();
    boolean DeleteService(int id);
    boolean EditService(int id,  int editPrecio, String editNombre);
    Service GetService(int id);
    boolean ExistService(String nombre);
}
