package cl.ingenieriasoftware.demo_t2.services;

import cl.ingenieriasoftware.demo_t2.entities.Users;

public interface UserService {

    /**
     * Método para agregar un usuario administrador con datos predefinidos.
     * Este método es específico del demo y no es parte del contrato de la interfaz UserService.
     */
    void Admin();

    /**
     * Método para validar el inicio de sesión de un usuario.
     *
     * @param email    Correo electrónico del usuario
     * @param contra   Contraseña del usuario
     * @return true si el inicio de sesión es válido, false de lo contrario
     */
    boolean isValidLogin(String email, String contra);

    /**
     * Método para verificar si ya existe un usuario registrado con el email proporcionado.
     *
     * @param email   Correo electrónico a verificar
     * @return true si existe un usuario con ese email, false de lo contrario
     */
    boolean ExistEmail(String email);

    /**
     * Método para registrar un nuevo usuario.
     *
     * @param name     Nombre del usuario
     * @param age      Edad del usuario
     * @param email    Correo electrónico del usuario
     * @param password Contraseña del usuario
     */
    void registerUser(String name, int age, String email, String password);

    /**
     * Método para obtener un usuario por su correo electrónico.
     *
     * @param email   Correo electrónico del usuario a buscar
     * @return Objeto Users correspondiente al usuario encontrado, o null si no se encuentra
     */
    Users GetUserByEmail(String email);

    /**
     * Método para limpiar la lista de usuarios
     */
    void clearUsuarios();

}