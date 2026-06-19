package com.facturador.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class datosStarter {
    private database db = new database();

    public datosStarter(){
        insertDefaultUsers();
        insertDefaultClientes();
        insertDefaulProveedor();
        insertDefaultProductos();
    }

    private void insertDefaultUsers() {
        String sql = """
            INSERT INTO users (name, email, password, telefono, domicilio, documento, is_active, role) VALUES
            ('Nahuel', 'nahuel@facturador.com', 'PoE0800', '2235457919', 'Jose Ingenieros 921', '47563604', true, 'ADMIN'),
            ('Pardo', 'pardo@facturador.com', 'PoE0800', '2235123456', 'Av. Colón 1234', '30123456', true, 'GERENTE'),
            ('Marcos', 'marcos@facturador.com', 'Bruno2025', '2235234567', 'San Martín 2456', '28987654', true, 'GERENTE'),
            ('Santiago', 'santiago@facturador.com', 'Mono', '2235345678', 'Luro 3578', '32765432', true, 'GERENTE'),
            ('Nico', 'nico@facturador.com', 'admin', '2235456789', 'Independencia 456', '34567890', true, 'GERENTE'),
            ('Laura', 'laura@facturador.com', 'laura1234', '2235567890', 'Rivadavia 789', '31234567', true, 'CAJERO'),
            ('Tomás', 'tomas@facturador.com', 'tomas1234', '2235678901', 'Belgrano 1023', '29876543', true, 'CAJERO'),
            ('Valentina', 'valentina@facturador.com', 'vale1234', '2235789012', 'Mitre 456', '33456789', true, 'CAJERO'),
            ('Rodrigo', 'rodrigo@facturador.com', 'rodri1234', '2235890123', 'Córdoba 2345', '35678901', true, 'REPOSITOR'),
            ('Florencia', 'florencia@facturador.com', 'flor1234', '2235901234', 'Italia 678', '27654321', true, 'REPOSITOR');        
        """;

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void insertDefaultClientes() {
        String sql = """
            INSERT INTO cliente (nombre, documento, email, telefono, domicilio) VALUES
            ('Distribuidora Norte', '30-12345678-9', 'ventas@norte.com', '2234556677', 'Av. Independencia 1234'),
            ('Bebidas del Sur', '30-87654321-0', 'contacto@sur.com', '2234889900', 'Av. Colón 2500'),
            ('Autoservicio Don José', '30-11223344-5', 'compras@donjose.com', '2234771111', 'Jujuy 1500'),
            ('Mercado Central MDP', '30-99887766-5', 'pedidos@mercadocentral.com', '2234881122', 'Luro 4500'),
            ('Kiosco La Esquina', '30-55443322-1', 'info@laesquina.com', '2234667788', 'Moreno 3200'),
            ('Almacén San Cayetano', '30-22334455-6', 'ventas@sancayetano.com', '2234332211', 'Rivadavia 2900'),
            ('Despensa El Sol', '30-77889911-3', 'contacto@elsol.com', '2234559988', 'Mitre 1200'),
            ('Supermercado Atlántico', '30-66554433-2', 'compras@atlantico.com', '2234778899', 'Constitución 5400'),
            ('Distribuciones Costa Azul', '30-33445566-7', 'ventas@costaazul.com', '2234223344', 'Av. Juan B. Justo 1900'),
            ('Mini Mercado Familiar', '30-44332211-8', 'pedidos@familiar.com', '2234112233', 'Belgrano 1700'); 
        """;

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void insertDefaulProveedor() {
        String sql = """
            INSERT INTO proveedores (nombre, cuit, domicilio, telefono, email) VALUES
            ('Coca-Cola FEMSA', '30-50001234-5', 'Av. Corrientes 1234', '011-4000-1000', 'ventas@cocacola-femsa.com'),
            ('PepsiCo Argentina', '30-50005678-9', 'Av. Rivadavia 5678', '011-4000-2000', 'ventas@pepsico.com.ar'),
            ('Danone Argentina', '30-50009012-3', 'Calle Florida 901', '011-4000-3000', 'ventas@danone.com.ar'),
            ('Molinos Río de la Plata', '30-50003456-7', 'Av. Alem 456', '011-4000-4000', 'ventas@molinos.com.ar'),
            ('Arcor S.A.', '30-50007890-1', 'Ruta 9 Km 45', '011-4000-5000', 'ventas@arcor.com'),
            ('La Serenísima', '30-50001234-6', 'Av. San Martín 789', '011-4000-6000', 'ventas@lserenisima.com.ar'),
            ('Nestlé Argentina', '30-50005678-0', 'Av. del Libertador 123', '011-4000-7000', 'ventas@nestle.com.ar'),
            ('Unilever Argentina', '30-50009012-4', 'Av. Callao 456', '011-4000-8000', 'ventas@unilever.com.ar');    
        """;

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void insertDefaultProductos() {
        String sql = """
            INSERT INTO productos (proveedor_id, name, description, price, stock) VALUES
            (1, 'Coca Cola 500ml', 'Gaseosa Coca Cola', 1200.00, 50),
            (2, 'Pepsi 500ml', 'Gaseosa Pepsi', 1100.00, 40),
            (8, 'Agua Mineral 500ml', 'Agua sin gas', 800.00, 80),
            (1, 'Sprite 500ml', 'Gaseosa Sprite', 1150.00, 35),
            (1, 'Fanta Naranja 500ml', 'Gaseosa sabor naranja', 1150.00, 30),
            (2, 'Papas Fritas Clásicas', 'Snack de papa', 1500.00, 25),
            (2, 'Papas Fritas BBQ', 'Snack sabor barbacoa', 1600.00, 20),
            (5, 'Maní Salado', 'Maní tostado y salado', 1300.00, 40),
            (7, 'Chocolate Milka', 'Chocolate con leche', 1800.00, 30),
            (5, 'Alfajor Havanna', 'Alfajor de chocolate', 2200.00, 50),
            (7, 'Galletitas Oreo', 'Galletitas rellenas', 1700.00, 45),
            (5, 'Yerba Playadito 1kg', 'Yerba mate tradicional', 4500.00, 20),
            (5, 'Azúcar Ledesma 1kg', 'Azúcar refinada', 1400.00, 35),
            (6, 'Leche La Serenísima 1L', 'Leche entera larga vida', 1900.00, 40),
            (7, 'Café Instantáneo Nescafé', 'Café soluble', 5200.00, 15),
            (4, 'Arroz Gallo 1kg', 'Arroz largo fino', 2100.00, 25),
            (4, 'Fideos Matarazzo', 'Spaghetti', 1300.00, 40),
            (4, 'Atún La Campagnola', 'Atún al natural', 2900.00, 20),
            (4, 'Aceite Cocinero 900ml', 'Aceite mezcla', 3200.00, 30),
            (4, 'Sal Dos Anclas 500g', 'Sal fina', 900.00, 50),
            (8, 'Lavandina Ayudín 1L', 'Lavandina concentrada', 1500.00, 25),
            (8, 'Detergente Magistral', 'Detergente limón', 1900.00, 30),
            (8, 'Papel Higiénico Elite x4', 'Papel doble hoja', 2800.00, 40),
            (8, 'Servilletas Sussex', 'Pack x100', 1100.00, 30),
            (2, 'Gaseosa Paso de los Toros', 'Pomelo 500ml', 1250.00, 20),
            (3, 'Jugo Cepita Naranja', 'Jugo 1L', 1800.00, 25),
            (2, 'Energizante Speed', 'Lata 250ml', 1900.00, 20),
            (3, 'Agua Saborizada Levité', 'Manzana 500ml', 1100.00, 30),
            (5, 'Caramelos Mogul', 'Bolsa surtida', 1500.00, 20),
            (5, 'Chicles Beldent', 'Menta', 900.00, 35);  
        """;

        try (
            Connection conn = this.db.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
