package com.facturador.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.facturador.database.database;
import com.facturador.model.Proveedores;

public class ProveedoresRepository {
    private database db;

    public ProveedoresRepository() {
        this.db = new database();
    }

    private Proveedores mapProveedore(ResultSet resultado) throws SQLException {
        Proveedores cliente = new Proveedores.Builder()
        .id(resultado.getInt("id"))
        .nombre(resultado.getString("nombre"))
        .direccion(resultado.getString("domicilio"))
        .documento(resultado.getString("documento"))
        .telefono(resultado.getString("telefono"))
        .email(resultado.getString("email"))
        .isActive(resultado.getBoolean("is_active"))
        .build();
        return cliente;
    }

    public void createProveedore(Proveedores proveedor) {
        String sql = "INSERT INTO proveedores (nombre, documento, domicilio, telefono, email) VALUES (?, ?, ?, ?, ?)";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getDocumento());
            stmt.setString(3, proveedor.getDireccion());
            stmt.setString(4, proveedor.getTelefono());
            stmt.setString(5, proveedor.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProveedore(Proveedores proveedore) {
        String sql = "UPDATE proveedores SET nombre = IFNULL(?, nombre), documento = IFNULL(?, documento), domicilio = IFNULL(?, domicilio), telefono = IFNULL(?, telefono), email = IFNULL(?, email) WHERE id = ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proveedore.getNombre());
            stmt.setString(2, proveedore.getDocumento());
            stmt.setString(3, proveedore.getDireccion());
            stmt.setString(4, proveedore.getTelefono());
            stmt.setString(5, proveedore.getEmail());
            stmt.setInt(6, proveedore.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void desactivarProveedore(Proveedores proveedore) {
        String sql = "UPDATE proveedores SET is_active = 0 WHERE id = ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, proveedore.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void activarProveedore(Proveedores proveedore) {
        String sql = "UPDATE proveedores SET is_active = 1 WHERE id = ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, proveedore.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Proveedores> getProveedore() {
        String sql = "SELECT * FROM proveedores";
        List<Proveedores> proveedores = new ArrayList<>();
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                proveedores.add(mapProveedore(resultado));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return proveedores;
    }

    public Proveedores getProveedoreById(int id) {
        String sql = "SELECT * FROM proveedores WHERE id = ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                return mapProveedore(resultado);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
