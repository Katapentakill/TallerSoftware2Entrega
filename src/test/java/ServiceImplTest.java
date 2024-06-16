import cl.ingenieriasoftware.demo_t2.entities.Service;
import cl.ingenieriasoftware.demo_t2.services.ServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceImplTest {

    private ServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = ServiceImpl.getInstance();
        service.clearServices(); // Limpiar la lista de servicios antes de cada prueba
    }

    @Test
    public void testSingletonInstance() {
        ServiceImpl instance1 = ServiceImpl.getInstance();
        ServiceImpl instance2 = ServiceImpl.getInstance();
        assertSame(instance1, instance2, "Debe ser la misma instancia");
    }

    @Test
    public void testSeedServices() {
        service.SeedServices();
        List<Service> services = service.GetServices();
        assertEquals(10, services.size(), "Debe haber 10 servicios después de la siembra inicial");
    }

    @Test
    public void testAddService() {
        service.AddService("Test Service", 15000);
        List<Service> services = service.GetServices();
        assertEquals(1, services.size(), "Debe haber un servicio después de agregar uno nuevo");
        assertEquals("Test Service", services.get(0).getNombre(), "El nombre del servicio debe coincidir");
        assertEquals(15000, services.get(0).getPrecio(), "El precio del servicio debe coincidir");
    }

    @Test
    public void testDeleteService() {
        service.AddService("Test Service", 15000);
        List<Service> servicesBeforeDelete = service.GetServices();
        assertTrue(service.DeleteService(servicesBeforeDelete.get(0).getId()), "El servicio debe ser eliminado correctamente");
        assertFalse(service.DeleteService(999), "No debe ser posible eliminar un servicio con ID inexistente");
    }

    @Test
    public void testEditService() {
        service.AddService("Test Service", 15000);
        List<Service> services = service.GetServices();
        int serviceId = services.get(0).getId();
        assertTrue(service.EditService(serviceId, 18000, "Edited Service"), "El servicio debe ser editado correctamente");
        assertEquals("Edited Service", service.GetService(serviceId).getNombre(), "El nombre del servicio editado debe coincidir");
        assertEquals(18000, service.GetService(serviceId).getPrecio(), "El precio del servicio editado debe coincidir");
        assertFalse(service.EditService(999, 18000, "Edited Service"), "No debe ser posible editar un servicio con ID inexistente");
    }

    @Test
    public void testGetService() {
        service.AddService("Test Service", 15000);
        List<Service> services = service.GetServices();
        int serviceId = services.get(0).getId();
        assertEquals("Test Service", service.GetService(serviceId).getNombre(), "El nombre del servicio recuperado debe coincidir");
        assertNull(service.GetService(999), "No debe encontrar un servicio con un ID inexistente");
    }

    @Test
    public void testExistService() {
        service.AddService("Test Service", 15000);
        assertTrue(service.ExistService("Test Service"), "Debe encontrar un servicio existente por nombre");
        assertFalse(service.ExistService("Nonexistent Service"), "No debe encontrar un servicio inexistente por nombre");
    }
}
