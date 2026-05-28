package com.facturador.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;

import com.facturador.database.database;
import com.facturador.model.Factura;

public class FacturaRepository {
    private database db;

    public FacturaRepository() {
        this.db = new database();
    }

    private Factura mapFactura(ResultSet resultado) throws SQLException {
        Factura factura = new Factura.Builder()
        .id(resultado.getInt("id"))
        .proveedorId(resultado.getInt("proveedorId"))
        .fecha(resultado.getDate("fecha").toLocalDate())
        .total(resultado.getDouble("total"))
        .build();
        return factura;
    }

    public void createFactura(Factura factura) {
        String sql = "INSERT INTO factura (proveedorId, fecha, total) VALUES (?, ?, ?)";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, factura.getProveedoreId());
            stmt.setDate(2, Date.valueOf(factura.getFecha()));
            stmt.setDouble(3, factura.getTotal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Factura getFactura(int offset, int limit) {
        String sql = "SELECT * FROM factura OFFSET ? LIMIT ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, limit);
            stmt.executeQuery();
            if (stmt.executeQuery().next()) {
                return mapFactura(stmt.executeQuery());
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } 
}
