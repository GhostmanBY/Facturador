package com.facturador.service;

import com.facturador.model.Factura;
import com.facturador.model.ProductFactura;
import com.facturador.repository.ProRepository;

public class ProServices {
    private ProRepository productrepository;

    public ProServices() {
        this.productrepository = new ProRepository();
    }

    public void createDetailFactura(ProductFactura producto) {
        try {
            this.productrepository.createDetailFactura(producto);
        } catch (Exception e){
            System.err.print(e.getMessage());
        }
    }

    public ProductFactura getDetailFactura(Factura factura,int offset, int limit) {
        try {
            return this.productrepository.getDetailFactura(factura, offset, limit);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return null;
        }
    }
}
