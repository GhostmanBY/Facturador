package com.facturador.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.facturador.database.database;
import com.facturador.model.User;


public class UserRepository {
    private final database db;

    public UserRepository() {
        this.db = new database();
    }   

    private User mapUser(ResultSet resultado) throws SQLException {
        User user = new User.Builder()
        .id(resultado.getInt("id"))
        .name(resultado.getString("name"))
        .email(resultado.getString("email"))
        .hashPassword(resultado.getString("password"))
        .documento(resultado.getString("documento"))
        .domicilio(resultado.getString("domicilio"))
        .telefono(resultado.getString("telefono"))
        .isActive(resultado.getBoolean("is_active"))
        .role(User.UserRole.valueOf(resultado.getString("role")))
        .build();
        return user;
    }

    public void createUser(User user) {
        String sql = "insert into users (name, email, hash_pass, documento, domicilio, telefono) values (?, ?, ?, ?, ?, ?)";
        
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getHashPassword());
            stmt.setString(4, user.getDocumento());
            stmt.setString(5, user.getDomicilio());
            stmt.setString(6, user.getTelefono());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }   
    }
    
    public void modifyUser(User user) {
        String sql = "UPDATE users SET name = ?, domicilio = ?, telefono = ? WHERE id = ?";

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getDomicilio());
            stmt.setString(3, user.getTelefono());
            stmt.setInt(4, user.getId());
            
            stmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                return mapUser(resultado);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getUser() {
        String sql = "select * from users";
        ArrayList<User> usuairos = new ArrayList<>();

        try (Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            ResultSet resultado = stmt.executeQuery();
            
            while (resultado.next()) {
                usuairos.add(mapUser(resultado));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return usuairos;
    }

    public void desactivarUser(User user) {
        String sql = "UPDATE users SET is_active = false  WHERE id = ?";

        try (Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
           stmt.setInt(1, user.getId());    
           stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } 

    public void activarUser(User user) {
        String sql = "UPDATE users SET is_active = true  WHERE id = ?";

        try (Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
           stmt.setInt(1, user.getId());     
           stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } 
}
