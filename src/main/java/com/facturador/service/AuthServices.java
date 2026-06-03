package com.facturador.service;

import com.facturador.model.AuthRepository;
import com.facturador.model.User;

import org.mindrot.jbcrypt.BCrypt;

public class AuthServices {
    private AuthRepository authrepository;

    public AuthServices() {
        this.authrepository = new AuthRepository();
    }
    
    public void createHashPassword(User user, String password) {
        try {
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            user.toBuilder()
            .hashPassword(hash);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
    }

    public boolean login(String email, String password) {
        try {
            return this.authrepository.Login(email, password);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    public User getUserActual() {
        return this.authrepository.getUserActual();
    }
}
