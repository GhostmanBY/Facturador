package com.facturador.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

public class datosStarter {
    private database db = new database();

    public datosStarter(){
        insertDefaultUsers();
        insertDefaultClientes();
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

    private void insertDefaultProductos() {
        String sql = """
            INSERT INTO productos (name, description, code, price, stock) VALUES
            ('Coca Cola 500ml', 'Gaseosa Coca Cola', 'CC500', 1200.00, 50),
            ('Pepsi 500ml', 'Gaseosa Pepsi', 'PP500', 1100.00, 40),
            ('Agua Mineral 500ml', 'Agua sin gas', 'AG500', 800.00, 80),
            ('Sprite 500ml', 'Gaseosa Sprite', 'SP500', 1150.00, 35),
            ('Fanta Naranja 500ml', 'Gaseosa sabor naranja', 'FN500', 1150.00, 30),
            ('Papas Fritas Clásicas', 'Snack de papa', 'PF100', 1500.00, 25),
            ('Papas Fritas BBQ', 'Snack sabor barbacoa', 'PFBBQ', 1600.00, 20),
            ('Maní Salado', 'Maní tostado y salado', 'MS200', 1300.00, 40),
            ('Chocolate Milka', 'Chocolate con leche', 'CM100', 1800.00, 30),
            ('Alfajor Havanna', 'Alfajor de chocolate', 'AH70', 2200.00, 50),
            ('Galletitas Oreo', 'Galletitas rellenas', 'GO118', 1700.00, 45),
            ('Yerba Playadito 1kg', 'Yerba mate tradicional', 'YP1K', 4500.00, 20),
            ('Azúcar Ledesma 1kg', 'Azúcar refinada', 'AL1K', 1400.00, 35),
            ('Leche La Serenísima 1L', 'Leche entera larga vida', 'LS1L', 1900.00, 40),
            ('Café Instantáneo Nescafé', 'Café soluble', 'CN170', 5200.00, 15),
            ('Arroz Gallo 1kg', 'Arroz largo fino', 'AG1K', 2100.00, 25),
            ('Fideos Matarazzo', 'Spaghetti', 'FM500', 1300.00, 40),
            ('Atún La Campagnola', 'Atún al natural', 'AT170', 2900.00, 20),
            ('Aceite Cocinero 900ml', 'Aceite mezcla', 'AC900', 3200.00, 30),
            ('Sal Dos Anclas 500g', 'Sal fina', 'SA500', 900.00, 50),
            ('Lavandina Ayudín 1L', 'Lavandina concentrada', 'LA1L', 1500.00, 25),
            ('Detergente Magistral', 'Detergente limón', 'DM750', 1900.00, 30),
            ('Papel Higiénico Elite x4', 'Papel doble hoja', 'PH4', 2800.00, 40),
            ('Servilletas Sussex', 'Pack x100', 'SS100', 1100.00, 30),
            ('Gaseosa Paso de los Toros', 'Pomelo 500ml', 'PT500', 1250.00, 20),
            ('Jugo Cepita Naranja', 'Jugo 1L', 'JC1L', 1800.00, 25),
            ('Energizante Speed', 'Lata 250ml', 'SP250', 1900.00, 20),
            ('Agua Saborizada Levité', 'Manzana 500ml', 'LV500', 1100.00, 30),
            ('Caramelos Mogul', 'Bolsa surtida', 'CM250', 1500.00, 20),
            ('Chicles Beldent', 'Menta', 'CB14', 900.00, 35);     
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
