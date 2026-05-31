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
        .name(resultado.getString("name"))
        .description(resultado.getString("description"))
        .code(resultado.getString("code"))
        .price(resultado.getDouble("price"))
        .stock(resultado.getInt("stock"))
        .isActive(resultado.getBoolean("is_active"))
        .build();
        return producto;
    }

    public void createStock(Producto producto) {
        String sql = "insert into productos (name ,description, code, price, stock) values (?, ?, ?, ?, ?)";
        
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, producto.getName());
            stmt.setString(2, producto.getDescription());
            stmt.setString(3, producto.getCode());
            stmt.setDouble(4, producto.getPrice());
            stmt.setInt(5, producto.getStock());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyStock(Producto producto) {
        String sql = "UPDATE productos SET name = ?, description = ?, code = ? ,price = ?, stock = ? WHERE id_producto = ?";

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, producto.getName());
            stmt.setString(2, producto.getDescription());
            stmt.setString(3, producto.getCode());
            stmt.setDouble(4, producto.getPrice());
            stmt.setInt(5, producto.getStock());
            stmt.setInt(6, producto.getId());
            
            stmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Producto> getStock(int offset, int limit) {
        String sql = "SELECT * FROM productos LIMIT ? OFFSET ?";
        List<Producto> productos = new ArrayList<>();
        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
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
        String sql = "SELECT * FROM productos WHERE id_producto = ?";

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
        String sql = "UPDATE productos SET active = true WHERE id_producto = ?";

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
        String sql = "UPDATE productos SET active = false WHERE id_producto = ?";

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
