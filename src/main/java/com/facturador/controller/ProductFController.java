package com.facturador.controller;

import com.facturador.service.ProductFServices;

import java.util.Collections;
import java.util.List;

import com.facturador.model.Factura;
import com.facturador.model.ProductFactura;

public class ProductFController {
    private ProductFServices proServicio;

    public ProductFController() {
        this.proServicio = new ProductFServices();
    }

    public void createDetailFactura(ProductFactura ProductFactura) {
        try {
            this.proServicio.createDetailFactura(ProductFactura);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ProductFactura> getDetailFactura(Factura factura ) {
        try {
            return this.proServicio.getDetailFactura(factura) != null ? this.proServicio.getDetailFactura(factura) : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
