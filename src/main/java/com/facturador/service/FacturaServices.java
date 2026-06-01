package com.facturador.service;

import com.facturador.model.Factura;
import com.facturador.repository.FacturaRepository;

public class FacturaServices {
    private FacturaRepository facturaRepository;

    public FacturaServices() {
        this.facturaRepository = new FacturaRepository();
    }

    public void createFactura(Factura factura) {
        try {
            this.facturaRepository.createFactura(factura);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
    }

    public Factura getFactura(int offset, int limit) {
        try {
            return this.facturaRepository.getFactura(offset, limit);
        } catch (Exception e){
            System.err.print(e.getMessage());
            return null;
        }
    }
}
