package com.facturador.controller;

import com.facturador.service.AuthServices;
import com.facturador.model.User;

public class AuthController {
    private AuthServices authServices;

    public AuthController() {
        this.authServices = new AuthServices();
    }

    public boolean Login(String user, String password) {
        try {
            return this.authServices.login(user, password);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    public User getUserActual() {
        return this.authServices.getUserActual();
    }
}
