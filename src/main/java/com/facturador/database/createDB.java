package com.facturador.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class createDB {
    public static final Dotenv dotenv = Dotenv.load();

    private database db = new database();

    public void createTables() throws SQLException {
        createClienteTable();
        createProveedoresTable();
        createUsersTable();
        createProductosTable();
        createFacturasTable();
        createProductFacturaTable();
    }
    
    public boolean databaseExists(){
        String sql = "SHOW DATABASES LIKE '" + dotenv.get("DB_NAME") + "'";

        try (
            Connection conn = db.connectSinBase();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public void createDatabase() throws SQLException {
        String sql = "CREATE SCHEMA " + dotenv.get("DB_NAME");

        try (
            Connection conn = db.connectSinBase();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void createClienteTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS cliente (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(150) NOT NULL,
                documento VARCHAR(50),
                email VARCHAR(150),
                telefono VARCHAR(50),
                domicilio VARCHAR(255),
                is_active BOOLEAN NOT NULL DEFAULT TRUE
            )
        """;

        try (
            Connection conn = db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.execute();
        }
    }

    private void createProveedoresTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS proveedores (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(150) NOT NULL,
                cuit VARCHAR(50),
                email VARCHAR(150),
                telefono VARCHAR(50),
                domicilio VARCHAR(255),
                is_active BOOLEAN NOT NULL DEFAULT TRUE
            )
        """;

        try (
            Connection conn = db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.execute();
        }
    }

    private void createUsersTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(150) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                telefono VARCHAR(50) NOT NULL,
                domicilio VARCHAR(255) NOT NULL,
                documento VARCHAR(50) NOT NULL UNIQUE,
                is_active BOOLEAN NOT NULL DEFAULT TRUE,
                role ENUM('ADMIN', 'GERENTE', 'CAJERO', 'REPOSITOR') NOT NULL
            )
        """;

        try (
            Connection conn = db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.execute();
        }
    }

    private void createProductosTable() throws SQLException {
    String sql = """
            CREATE TABLE IF NOT EXISTS productos (
                id INT AUTO_INCREMENT PRIMARY KEY,
                proveedor_id INT NOT NULL,
                name VARCHAR(150) NOT NULL,
                description TEXT,
                price DECIMAL(10,2) NOT NULL,
                stock INT NOT NULL DEFAULT 0,
                is_active BOOLEAN NOT NULL DEFAULT TRUE,
                FOREIGN KEY (proveedor_id) REFERENCES proveedores(id)
            )
        """;

        try (
            Connection conn = db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.execute();
        }
    }

    private void createFacturasTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS facturas (
                id INT AUTO_INCREMENT PRIMARY KEY,
                cliente_id INT NOT NULL,
                vendedor_id INT NOT NULL,
                fecha DATE NOT NULL,
                subtotal DECIMAL(10,2) NOT NULL,
                impuesto DECIMAL(10,2) NOT NULL,
                descuento DECIMAL(10,2) NOT NULL,
                total DECIMAL(10,2) NOT NULL DEFAULT 0,
                FOREIGN KEY (cliente_id) REFERENCES cliente(id),
                FOREIGN KEY (vendedor_id) REFERENCES users(id)
            )
        """;

        try (
            Connection conn = db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.execute();
        }
    }

    private void createProductFacturaTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS product_factura (
                id INT AUTO_INCREMENT PRIMARY KEY,
                factura_id INT NOT NULL,
                producto_id INT NOT NULL,
                cantidad INT NOT NULL,
                precio_unitario DECIMAL(10,2) NOT NULL,
                descuento DECIMAL(10,2) NOT NULL DEFAULT 0,
                subtotal DECIMAL(10,2) NOT NULL,
                is_active BOOLEAN NOT NULL DEFAULT TRUE,
                FOREIGN KEY (factura_id) REFERENCES facturas(id),
                FOREIGN KEY (producto_id) REFERENCES productos(id)
            )
        """;

        try (
            Connection conn = db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.execute();
        }
    }
}
