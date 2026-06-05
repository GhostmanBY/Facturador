package com.facturador.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

import com.facturador.model.Factura;
import com.facturador.model.ProductFactura;
import com.facturador.database.database;

public class ProductFRepository {
    private database db;

    public ProductFRepository() {
        this.db = new database();
    }

    private ProductFactura mapDetailFactura(ResultSet resultado) throws SQLException {
        ProductFactura detalle = new ProductFactura.Builder()
        .id(resultado.getInt("id"))
        .productoid(resultado.getInt("producto_id"))
        .facturaId(resultado.getInt("factura_id"))
        .cantidad(resultado.getInt("cantidad"))
        .precioUnitario(resultado.getDouble("precio_unitario"))
        .descuento(resultado.getDouble("descuento"))
        .subtotal(resultado.getDouble("subtotal"))
        .isActive(resultado.getBoolean("is_active"))
        .build();
        return detalle;
    }

    public void createDetailFactura(ProductFactura detalle) {
        String sql = "INSERT INTO product_factura (producto_id, cantidad, precio_unitario, factura_id, subtotal, descuento) VALUES (?, ?, ?, ?, ?, ?)";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getProductoid());
            stmt.setInt(2, detalle.getCantidad());
            stmt.setDouble(3, detalle.getPrecioUnitario());
            stmt.setInt(4, detalle.getFacturaId());
            stmt.setDouble(5, detalle.getSubtotal());
            stmt.setDouble(6, detalle.getDescuento());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductFactura> getDetailFactura(Factura factura) {
        String sql = "SELECT * FROM product_factura WHERE factura_id = ?";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, factura.getId());
            ResultSet resultado = stmt.executeQuery();
            List<ProductFactura> detalles = new ArrayList<>();
            while (resultado.next()) {
                detalles.add(mapDetailFactura(resultado));
            }
            return detalles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
