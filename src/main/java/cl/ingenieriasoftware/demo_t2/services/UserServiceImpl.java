package cl.ingenieriasoftware.demo_t2.services;

import cl.ingenieriasoftware.demo_t2.entities.Users;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static UserServiceImpl instance;  // Instancia única de UserServiceImpl
    private static List<Users> usuarios = new ArrayList<>();  // Lista de usuarios registrados

    // Constructor privado para evitar la creación directa de instancias
    private UserServiceImpl() {}

    /**
     * Método estático para obtener la instancia única de UserServiceImpl utilizando sincronización para evitar
     * problemas de concurrencia en entornos multihilo.
     *
     * @return Instancia única de UserServiceImpl
     */
    public static synchronized UserServiceImpl getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    /**
     * Método para agregar un usuario administrador con datos predefinidos.
     * Este método es específico del demo y no es parte de la interfaz UserService.
     */
    public void Admin() {
        // Ruta del archivo de texto que contiene los jefes de local
        String filePath = "jefes_de_local.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Separar los datos por comas
                String[] datos = line.split(",");

                // Extraer los datos individuales
                String nombre = datos[0].trim();
                int edad = Integer.parseInt(datos[1].trim());
                String email = datos[2].trim();
                String contra = datos[3].trim();

                // Registrar el usuario como jefe de local
                registerUser(nombre, edad, email, contra);

                // Marcar al usuario como jefe de local
                Users user = GetUserByEmail(email);
                if (user != null) {
                    user.setEsJefeDeLocal(true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de errores de lectura del archivo
        }
    }

    @Override
    public boolean isValidLogin(String email, String contra) {
        // Validar si existe un usuario con el email y contraseña proporcionados
        for (Users usuario : usuarios) {
            if (usuario.getCorreo().equals(email) && usuario.getContraseña().equals(contra)) {
                return true;  // Usuario válido
            }
        }
        return false;  // Usuario no encontrado o contraseña incorrecta
    }

    @Override
    public boolean ExistEmail(String email) {
        // Verificar si ya existe un usuario registrado con el email proporcionado
        for (Users usuario : usuarios) {
            if (usuario.getCorreo().equals(email)) {
                return true;  // Email encontrado en la lista de usuarios
            }
        }
        return false;  // Email no encontrado en la lista de usuarios
    }

    @Override
    public void registerUser(String name, int age, String email, String password) {
        // Registrar un nuevo usuario con los datos proporcionados
        usuarios.add(new Users(name, age, email, password));
    }

    @Override
    public Users GetUserByEmail(String email) {
        // Obtener un usuario específico por su email
        for (Users usuario : usuarios) {
            if (usuario.getCorreo().equals(email)) {
                return usuario;  // Retornar el usuario encontrado
            }
        }
        return null;  // Retornar null si no se encuentra ningún usuario con el email proporcionado
    }
}