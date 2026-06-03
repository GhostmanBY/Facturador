package com.facturador.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.facturador.database.database;
import com.facturador.model.Cliente;

public class ClienteRepository {
    private database db;

    public ClienteRepository() {
        this.db = new database();
    }

    public void createCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (nombre, domicilio, telefono, email) VALUES (?, ?, ?, ?)";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getDireccion());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET nombre = ?, domicilio = ?, telefono = ?, email = ? WHERE id = ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getDireccion());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getEmail());
            stmt.setInt(5, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void desactivarCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET is_active = 0 WHERE id = ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void activarCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET is_active = 1 WHERE id = ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Cliente> getClientes() {
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = new Cliente.Builder()
                    .id(resultado.getInt("id"))
                    .nombre(resultado.getString("nombre"))
                    .direccion(resultado.getString("domicilio"))
                    .telefono(resultado.getString("telefono"))
                    .email(resultado.getString("email"))
                    .isActive(resultado.getBoolean("is_active"))
                    .build();
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientes;
    }

    public Cliente getClienteById(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Cliente cliente = new Cliente.Builder()
                    .id(resultado.getInt("id"))
                    .nombre(resultado.getString("nombre"))
                    .direccion(resultado.getString("domicilio"))
                    .telefono(resultado.getString("telefono"))
                    .email(resultado.getString("email"))
                    .isActive(resultado.getBoolean("is_active"))
                    .build();
                return cliente;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
