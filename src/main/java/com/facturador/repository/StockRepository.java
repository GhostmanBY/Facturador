package com.facturador.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.facturador.database.database;
import com.facturador.model.Producto;

public class StockRepository {
    private final database db;

    public StockRepository() {
        this.db = new database();
    }

    private Producto mapProducto(ResultSet resultado) throws SQLException {
        Producto producto = new Producto.Builder()
        .id(resultado.getInt("id"))
        .proveedorId(resultado.getInt("proveedor_id"))
        .name(resultado.getString("name"))
        .description(resultado.getString("description"))
        .price(resultado.getDouble("price"))
        .stock(resultado.getInt("stock"))
        .isActive(resultado.getBoolean("is_active"))
        .build();
        return producto;
    }

    public void createStock(Producto producto) {
        String sql = "INSERT INTO productos (proveedor_id, name, description, price, stock) VALUES (?, ?, ?, ?, ?)";
        
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, producto.getProveedorId());
            stmt.setString(2, producto.getName());
            stmt.setString(3, producto.getDescription());
            stmt.setDouble(4, producto.getPrice());
            stmt.setInt(5, producto.getStock());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyStock(Producto producto) {
        String sql = "UPDATE productos SET proveedor_id = IFNULL(?, proveedor_id), name = IFNULL(?, name), description = IFNULL(?, description), price = IFNULL(?, price), stock = IFNULL(?, stock) WHERE id = ?";
        
        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, producto.getProveedorId());
            stmt.setString(2, producto.getName());
            stmt.setString(3, producto.getDescription());
            stmt.setDouble(4, producto.getPrice());
            stmt.setInt(5, producto.getStock());
            stmt.setInt(6, producto.getId());
            
            stmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Producto> getStock() {
        String sql = "SELECT * FROM productos";
        List<Producto> productos = new ArrayList<>();
        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                productos.add(mapProducto(resultado));
            }
            return productos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Producto getStockById(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";

        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                return mapProducto(resultado);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void activateStock(int id) {
        String sql = "UPDATE productos SET is_active = true WHERE id = ?";

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deactivateStock(int id) {
        String sql = "UPDATE productos SET is_active = false WHERE id = ?";

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
