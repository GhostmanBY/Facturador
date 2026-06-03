package com.facturador.controller;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;

import com.facturador.model.Factura;
import com.facturador.service.FacturaServices;

public class FacturaController {
    private FacturaServices facturaServices;
    
    public FacturaController() {
        this.facturaServices = new FacturaServices();
    }

    public Factura createFactura(Factura factura) {
        try {
            
            Factura facturaCreada = factura.toBuilder().fecha(LocalDate.now()).build();

            return this.facturaServices.createFactura(facturaCreada);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Factura> getFactura(int offset, int limit) {
        try {
            return this.facturaServices.getFactura(offset, limit) != null ? this.facturaServices.getFactura(offset, limit) : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
