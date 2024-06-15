package cl.ingenieriasoftware.demo_t2.services;

import cl.ingenieriasoftware.demo_t2.entities.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceImpl implements Services {

    // Singleton instance
    private static ServiceImpl instance;
    // List to store services
    private static List<Service> services = new ArrayList<>();

    // Private constructor to prevent instantiation
    private ServiceImpl() {}

    // Singleton getInstance method
    public static synchronized ServiceImpl getInstance() {
        if (instance == null) {
            instance = new ServiceImpl();
        }
        return instance;
    }

    // Method to seed initial services into the system
    public void SeedServices(){
        // Adding initial services with their prices
        AddService("Lavado Exterior", 20000);
        AddService("Aspirado interior", 5000);
        AddService("Encerado", 10000);
        AddService("Limpieza de llantas", 3000);
        AddService("Lavado de motor", 8000);
        AddService("Limpieza de vidrios", 4000);
        AddService("Limpieza de tapiz", 12000);
        AddService("Pulido de pintura", 15000);
        AddService("Descontaminacion de pintura", 20000);
        AddService("Tratamiento de cuero", 18000);
    }

    @Override
    public void AddService(String nombre, int precio) {
        // Creating a new Service object and adding it to the list
        Service newService = new Service(nombre, precio);
        services.add(newService);
    }

    @Override
    public List<Service> GetServices() {
        // Returning the list of services
        return services;
    }

    @Override
    public boolean DeleteService(int id) {
        // Iterating through the list to find and delete a service by its ID
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getId() == id) {
                services.remove(i);
                return true; // Service found and deleted
            }
        }
        return false; // Service with the given ID not found
    }

    @Override
    public boolean EditService(int id, int editPrecio, String editNombre) {
        // Iterating through the list to find and edit a service by its ID
        for (Service service : services) {
            if (service.getId() == id) {
                service.setPrecio(editPrecio); // Updating price
                service.setNombre(editNombre); // Updating name
                return true; // Service found and edited
            }
        }
        return false; // Service with the given ID not found
    }

    @Override
    public Service GetService(int id) {
        // Iterating through the list to find a service by its ID and return it
        for (Service service : services) {
            if (service.getId() == id) {
                return service; // Return service if found
            }
        }
        return null; // Service with the given ID not found
    }

    @Override
    public boolean ExistService(String nombre) {
        // Iterating through the list to check if a service with the given name exists
        for (Service service : services) {
            if (service.getNombre().equals(nombre)) {
                return true; // Service with the given name found
            }
        }
        return false; // Service with the given name not found
    }
}