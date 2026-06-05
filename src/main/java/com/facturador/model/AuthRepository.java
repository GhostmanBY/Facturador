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

    public boolean Login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? and password = ?";

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                this.userActual = User.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .documento(rs.getString("documento"))
                .domicilio(rs.getString("domicilio"))
                .telefono(rs.getString("telefono"))
                .isActive(rs.getBoolean("is_active"))
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
