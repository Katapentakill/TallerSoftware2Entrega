package cl.ingenieriasoftware.demo_t2.services;

import cl.ingenieriasoftware.demo_t2.entities.Service;
import cl.ingenieriasoftware.demo_t2.entities.Users;

import java.util.ArrayList;
import java.util.List;

public class ServiceImpl implements Services {

    private static List<Service> services = new ArrayList<>();

    public void SeedServices(){
        String nombre = "Lavado Exterior";
        int Precio = 20000;
        AddService(nombre,Precio);
        String nombre1 = "Aspirado interior";
        int Precio1 = 5000;
        AddService(nombre1,Precio1);
        String nombre2 = "Encerado";
        int Precio2 = 10000;
        AddService(nombre2,Precio2);
        String nombre3 = "Limpieza de llantas";
        int Precio3 = 3000;
        AddService(nombre3,Precio3);
        String nombre4 = "Lavado de motor";
        int Precio4 = 8000;
        AddService(nombre4,Precio4);
        String nombre5 = "Limpieza de vidrios";
        int Precio5 = 4000;
        AddService(nombre5,Precio5);
        String nombre6 = "Limpieza de tapiz";
        int Precio6 = 12000;
        AddService(nombre6,Precio6);
        String nombre7 = "Pulido de pintura";
        int Precio7 = 15000;
        AddService(nombre7,Precio7);
        String nombre8 = "Descontaminacion de pintura";
        int Precio8 = 20000;
        AddService(nombre8,Precio8);
        String nombre9 = "Tratamiento de cuero";
        int Precio9 = 18000;
        AddService(nombre9,Precio9);
    }

    @Override
    public void AddService(String nombre, int precio) {
        Service newService = new Service(nombre, precio);
        services.add(newService);
    }

    @Override
    public List<Service> GetServices() {
        return services;
    }

    @Override
    public boolean DeleteService(int id) {
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getId() == id) {
                services.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean EditService(int id, int editPrecio, String editNombre) {
        for (Service service : services) {
            if (service.getId() == id) {
                service.setPrecio(editPrecio);
                service.setNombre(editNombre);
                return true;
            }
        }
        return false;
    }

    @Override
    public Service GetService(int id) {
        for (Service service : services) {
            if (service.getId() == id) {
                return service;
            }
        }
        return null;
    }

    @Override
    public boolean ExistService(String nombre) {
        for (Service service : services) {
            if (service.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }
}