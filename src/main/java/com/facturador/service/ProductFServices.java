package com.facturador.service;

import java.util.List;

import com.facturador.model.Factura;
import com.facturador.model.ProductFactura;
import com.facturador.repository.ProductFRepository;

public class ProductFServices {
    private ProductFRepository productrepository;

    public ProductFServices() {
        this.productrepository = new ProductFRepository();
    }

    public void createDetailFactura(ProductFactura producto) {
        if (producto.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        if (producto.getPrecioUnitario() <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero.");
        }
        if (producto.getName().isBlank() || producto.getFacturaId() <= 0 ) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }

        this.productrepository.createDetailFactura(producto);
        
    }

    public List<ProductFactura> getDetailFactura(Factura factura) {
        if (factura.getId() <= 0 ) {
            throw new IllegalArgumentException("El ID de la factura debe ser mayor a cero.");
        }
        if (factura.getClienteId() <= 0) {
            throw new IllegalArgumentException("El ID del cliente debe ser mayor a cero.");
        }


        return this.productrepository.getDetailFactura(factura);
    }
}
