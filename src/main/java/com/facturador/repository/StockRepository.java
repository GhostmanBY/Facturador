package com.facturador.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

import com.facturador.database.database;
import com.facturador.model.Producto;

public class StockRepository {
    private final database db;

    public StockRepository() {
        this.db = new database();
    }

    private Producto mapProducto(ResultSet resultado) throws SQLException {
        Producto producto = new Producto.Builder()
        .id(resultado.getInt("id_producto"))
        .name(resultado.getString("name"))
        .description(resultado.getString("description"))
        .price(resultado.getDouble("price"))
        .stock(resultado.getInt("stock"))
        .isActive(resultado.getBoolean("active"))
        .build();
        return producto;
    }

    public void createStock(Producto producto) {
        String sql = "insert into stock (id_producto, name, description, price, stock) values (?, ?, ?, ?, ?)";
        
        try (
            Connection conn = this.db.connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, producto.getId());
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
        String sql = "UPDATE stock SET name = ?, description = ?, price = ?, stock = ? WHERE id_producto = ?";

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, producto.getName());
            stmt.setString(2, producto.getDescription());
            stmt.setDouble(3, producto.getPrice());
            stmt.setInt(4, producto.getStock());
            stmt.setInt(5, producto.getId());
            
            stmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Producto getStock(int offset, int limit) {
        String sql = "select * from stock offset ? limit ?";

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, offset);
            stmt.setInt(2, limit);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                return mapProducto(resultado);
            }
            return null;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Producto getStockById(int id) {
        String sql = "SELECT * FROM stock WHERE id_producto = ?";

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
        String sql = "UPDATE stock SET active = true WHERE id_producto = ?";

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
        String sql = "UPDATE stock SET active = false WHERE id_producto = ?";

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
