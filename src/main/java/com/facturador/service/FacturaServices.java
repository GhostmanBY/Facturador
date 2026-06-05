package com.facturador.service;

import java.util.List;

import com.facturador.model.Factura;
import com.facturador.repository.FacturaRepository;

public class FacturaServices {
    private FacturaRepository facturaRepository;

    public FacturaServices() {
        this.facturaRepository = new FacturaRepository();
    }

    public Factura createFactura(Factura factura) {
        try {
            return this.facturaRepository.createFactura(factura);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return null;
        }
    }

    public List<Factura> getFactura() {
        try {
            return this.facturaRepository.getFactura();
        } catch (Exception e){
            System.err.print(e.getMessage());
            return null;
        }
    }
}
