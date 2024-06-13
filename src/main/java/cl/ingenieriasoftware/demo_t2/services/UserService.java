package cl.ingenieriasoftware.demo_t2.services;

import cl.ingenieriasoftware.demo_t2.entities.Users;

public interface UserService {
    void Admin();
    boolean isValidLogin(String email, String contra);
    boolean ExistEmail(String email);
    void registerUser(String name, int age, String email, String password);
    Users GetUserByEmail(String email);
}
