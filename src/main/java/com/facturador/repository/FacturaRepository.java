package com.facturador.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
        .clienteId(resultado.getInt("cliente_id"))
        .vendedorId(resultado.getInt("vendedor_id"))
        .subtotal(resultado.getDouble("subtotal"))
        .descuento(resultado.getDouble("descuento"))
        .impuestos(resultado.getDouble("impuesto"))
        .fecha(resultado.getDate("fecha").toLocalDate())
        .total(resultado.getDouble("total"))
        .build();
        return factura;
    }

    public Factura createFactura(Factura factura) {
        String sql = "INSERT INTO facturas (cliente_id, vendedor_id, subtotal, descuento, impuesto, fecha, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, factura.getClienteId());
            stmt.setInt(2, factura.getVendedorId());
            stmt.setDouble(3, factura.getSubtotal());
            stmt.setDouble(4, factura.getDescuento());
            stmt.setDouble(5, factura.getImpuestos());
            stmt.setDate(6, Date.valueOf(factura.getFecha()));
            stmt.setDouble(7, factura.getTotal());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Factura facturaCreada = factura.toBuilder().id(rs.getInt(1)).build();
                return facturaCreada;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Factura> getFactura() {
        String sql = "SELECT * FROM facturas";
        List<Factura> facturas = new ArrayList<>();
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                facturas.add(mapFactura(resultado));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return facturas;
    }
}
