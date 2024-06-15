package cl.ingenieriasoftware.demo_t2.services;

import cl.ingenieriasoftware.demo_t2.entities.Service;
import java.util.List;

public interface Services {

    /**
     * Método para inicializar los servicios iniciales en el sistema.
     * Este método es específico del demo y no es parte del contrato de la interfaz Services.
     */
    void SeedServices();

    /**
     * Método para agregar un nuevo servicio al sistema.
     *
     * @param nombre Nombre del servicio a agregar
     * @param precio Precio del servicio a agregar
     */
    void AddService(String nombre, int precio);

    /**
     * Método para obtener la lista de todos los servicios disponibles en el sistema.
     *
     * @return Lista de objetos Service que representan todos los servicios
     */
    List<Service> GetServices();

    /**
     * Método para eliminar un servicio del sistema.
     *
     * @param id Identificador del servicio a eliminar
     * @return true si se eliminó el servicio correctamente, false de lo contrario
     */
    boolean DeleteService(int id);

    /**
     * Método para editar un servicio existente en el sistema.
     *
     * @param id Identificador del servicio a editar
     * @param editPrecio Nuevo precio del servicio
     * @param editNombre Nuevo nombre del servicio
     * @return true si se editó el servicio correctamente, false de lo contrario
     */
    boolean EditService(int id, int editPrecio, String editNombre);

    /**
     * Método para obtener un servicio por su identificador.
     *
     * @param id Identificador del servicio a obtener
     * @return Objeto Service correspondiente al servicio encontrado, o null si no se encuentra
     */
    Service GetService(int id);

    /**
     * Método para verificar si ya existe un servicio con el nombre proporcionado.
     *
     * @param nombre Nombre del servicio a verificar
     * @return true si ya existe un servicio con ese nombre, false de lo contrario
     */
    boolean ExistService(String nombre);
}