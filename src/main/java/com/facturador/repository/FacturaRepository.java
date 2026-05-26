package com.facturador.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;

import com.facturador.database.database;
import com.facturador.model.Factura;
import com.facturador.model.ProductFactura;

public class FacturaRepository {
    private database db;

    public FacturaRepository(database db) {
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

    private ProductFactura mapDetailFactura(ResultSet resultado) throws SQLException {
        ProductFactura detalle = new ProductFactura.Builder()
        .id(resultado.getInt("id"))
        .productoid(resultado.getInt("producto_id"))
        .cantidad(resultado.getInt("cantidad"))
        .precioUnitario(resultado.getDouble("precioUni"))
        .descuento(resultado.getDouble("descuento"))
        .subtotal(resultado.getDouble("total"))
        .isActive(resultado.getBoolean("is_active"))
        .facturaId(resultado.getInt("factura_id"))
        .build();
        return detalle;
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

    public void createDetailFactura(ProductFactura detalle) {
        String sql = "INSERT INTO detalle_factura (producto_id, cantidad, precioUni, total, factura_id) VALUES (?, ?, ?, ?, ?)";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getProductoid());
            stmt.setInt(2, detalle.getCantidad());
            stmt.setDouble(3, detalle.getPrecioUnitario());
            stmt.setDouble(4, detalle.getSubtotal());
            stmt.setInt(5, detalle.getFacturaId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ProductFactura getDetailFactura(ProductFactura detalle, int offset, int limit) {
        String sql = "SELECT * FROM detalle_factura WHERE factura_id = ? OFFSET ? LIMIT ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getFacturaId());
            stmt.setInt(2, offset);
            stmt.setInt(3, limit);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                return mapDetailFactura(resultado);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } 
}
