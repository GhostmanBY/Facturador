package com.facturador.service;

import com.facturador.repository.UserRepository;

import java.util.List;

import com.facturador.model.User;

public class UserServices {
    private final UserRepository userRepository;
    
    public UserServices() {
        this.userRepository = new UserRepository();
    }

    public void createUser(User user) {
        if (user.getName().isBlank() || user.getEmail().isBlank() || user.getHashPassword().isBlank()) {
            throw new IllegalArgumentException("Name, email and password no pueden estar vacíos");
        }

        this.userRepository.createUser(user);
    }

    public void modifyUser(User user) {
        if (user.getId() == 0 || user.getId() < 0) {
            throw new IllegalArgumentException("identificador no válido");
        }

        this.userRepository.modifyUser(user);
    }

    public User getUserById(int id) {
        return this.userRepository.getUserById(id);
    }

    public List<User> getUser() {
        return this.userRepository.getUser();
    }

    public void desactivarUser(User user) {
        this.userRepository.desactivarUser(user);
    }

    public void activarUser(User user) {
        this.userRepository.activarUser(user);
    }
}
