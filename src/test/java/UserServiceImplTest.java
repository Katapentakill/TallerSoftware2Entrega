import cl.ingenieriasoftware.demo_t2.entities.Users;
import cl.ingenieriasoftware.demo_t2.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {

    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        userService = UserServiceImpl.getInstance();
        userService.clearUsuarios();  // Limpiar la lista de usuarios antes de cada prueba
    }

    @AfterEach
    public void tearDown() {
        userService.clearUsuarios();  // Limpiar la lista de usuarios después de cada prueba
    }

    @Test
    public void testSingletonInstance() {
        UserServiceImpl instance1 = UserServiceImpl.getInstance();
        UserServiceImpl instance2 = UserServiceImpl.getInstance();
        assertSame(instance1, instance2, "Debe ser la misma instancia");
    }

    @Test
    public void testIsValidLogin() {
        userService.registerUser("John Doe", 30, "john@example.com", "password123");
        assertTrue(userService.isValidLogin("john@example.com", "password123"), "El inicio de sesión debería ser válido");
        assertFalse(userService.isValidLogin("john@example.com", "wrongpassword"), "El inicio de sesión debería ser inválido con una contraseña incorrecta");
        assertFalse(userService.isValidLogin("nonexistent@example.com", "password123"), "El inicio de sesión debería ser inválido con un correo electrónico inexistente");
    }

    @Test
    public void testExistEmail() {
        userService.registerUser("Jane Doe", 25, "jane@example.com", "password123");
        assertTrue(userService.ExistEmail("jane@example.com"), "El correo electrónico debería existir");
        assertFalse(userService.ExistEmail("nonexistent@example.com"), "El correo electrónico no debería existir");
    }

    @Test
    public void testRegisterUser() {
        userService.registerUser("Alice", 28, "alice@example.com", "password123");
        Users user = userService.GetUserByEmail("alice@example.com");
        assertNotNull(user, "El usuario debería ser registrado y encontrado");
        assertEquals("Alice", user.getNombre(), "El nombre del usuario debería coincidir");
        assertEquals(28, user.getEdad(), "La edad del usuario debería coincidir");
        assertEquals("alice@example.com", user.getCorreo(), "El correo del usuario debería coincidir");
        assertEquals("password123", user.getContraseña(), "La contraseña del usuario debería coincidir");
    }

    @Test
    public void testGetUserByEmail() {
        userService.registerUser("Bob", 35, "bob@example.com", "password123");
        Users user = userService.GetUserByEmail("bob@example.com");
        assertNotNull(user, "El usuario debería ser encontrado");
        assertEquals("Bob", user.getNombre(), "El nombre del usuario debería coincidir");
        assertNull(userService.GetUserByEmail("nonexistent@example.com"), "No debería encontrar un usuario con un correo inexistente");
    }
}
