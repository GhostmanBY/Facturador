package com.facturador.controller;

import com.facturador.service.AuthServices;
import com.facturador.model.User;

public class AuthController {
    private AuthServices authServices = AuthServices.getInstance();

    public AuthController() {}

    public boolean Login(String user, String password) {
        try {
            return authServices.login(user, password);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    public User getUserActual() {
        return authServices.getUserActual();
    }
}
