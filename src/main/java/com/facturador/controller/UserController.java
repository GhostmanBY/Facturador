package com.facturador.controller;

import com.facturador.model.User;
import com.facturador.service.UserServices;

public class UserController {
    private final UserServices userServices;

    public UserController() {
        this.userServices = new UserServices();
    }

    public String createUser(User user) {
        try {
            this.userServices.createUser(user);
            return "Se creo el usuario correctamente";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String modifyUser(User user) {
        try {
            this.userServices.modifyUser(user);
            return "Se modifico el usuario correctamente";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public User getUser(int offset, int limit) {
        try {
            return this.userServices.getUser(offset, limit);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
