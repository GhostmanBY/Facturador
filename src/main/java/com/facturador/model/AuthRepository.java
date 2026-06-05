package com.facturador.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.facturador.database.database;

public class AuthRepository {
    private database db;
    private User userActual;

    public AuthRepository() {
        this.db = new database();
    }

    public boolean Login(String user, String password) {
        String sql = "SELECT * FROM users WHERE name = ? and password = ?";

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, user);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                this.userActual = User.builder()
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .documento(rs.getString("documento"))
                .domicilio(rs.getString("domicilio"))
                .telefono(rs.getString("telefono"))
                .role(User.UserRole.valueOf(rs.getString("role")))
                .build();
                
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    public User getUserActual() {
        return userActual;
    }
}
