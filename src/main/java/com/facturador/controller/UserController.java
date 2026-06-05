package com.facturador.controller;

import java.util.List;

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

    public User getUserById(int id) {
        try {
            return this.userServices.getUserById(id);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<User> getUser() {
        try {
            return this.userServices.getUser();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void desactivarUser(User user) {
        try {
            this.userServices.desactivarUser(user);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void activarUser(User user) {
        try {
            this.userServices.activarUser(user);
        }  catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
