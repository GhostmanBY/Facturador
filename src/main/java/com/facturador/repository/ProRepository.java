package com.facturador.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import com.facturador.model.Factura;
import com.facturador.model.ProductFactura;
import com.facturador.database.database;

public class ProRepository {
    private database db;

    public ProRepository() {
        this.db = new database();
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

    public ProductFactura getDetailFactura(Factura factura, int offset, int limit) {
        String sql = "SELECT * FROM detalle_factura WHERE factura_id = ? OFFSET ? LIMIT ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, factura.getId());
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
