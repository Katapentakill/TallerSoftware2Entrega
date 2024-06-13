package cl.ingenieriasoftware.demo_t2.services;

import cl.ingenieriasoftware.demo_t2.entities.Users;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{
    private static List<Users> usuarios = new ArrayList<>();

    public void Admin(){
        String nombre = "Esteban";
        int edad = 50;
        String email = "Esteban@gmail.com";
        String contra = "1234";
        registerUser(nombre,edad,email,contra);
        Users user = GetUserByEmail("Esteban@gmail.com");
        user.setEsJefeDeLocal(true);
    }

    @Override
    public boolean isValidLogin(String email, String contra) {
        for (Users usuario : usuarios) {
            if (usuario.getCorreo().equals(email) && usuario.getContraseña().equals(contra)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean ExistEmail(String email) {
        for(Users usuario : usuarios) {
            if(usuario.getCorreo().equals(email)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerUser(String name, int age, String email, String password) {
        usuarios.add(new Users(name,age,email,password));
    }

    @Override
    public Users GetUserByEmail(String email) {
        for(Users usuario : usuarios){
            if(usuario.getCorreo().equals(email)){
                return usuario;
            }
        }
        return null;
    }
}
