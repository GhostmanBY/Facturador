package com.facturador.controller;

import java.util.Collections;
import java.util.List;

import com.facturador.model.Producto;
import com.facturador.service.StockServices;

public class StockController {
    private StockServices stockservices;

    public StockController() {
        this.stockservices = new StockServices();
    }

    public void createStock(Producto producto) {
        try {
            this.stockservices.createStock(producto);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void modifyStock(Producto producto) {
        try {
            this.stockservices.modifyStock(producto);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Producto> getStock(int offset, int limit) {
        try {
            return this.stockservices.getStock(offset, limit);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public Producto getStockById(int id) {
        try {
            return this.stockservices.getStockById(id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public void activateStock(int id) {
        try {
            this.stockservices.activateStock(id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void deactivateStock(int id) {
        try {
            this.stockservices.deactivateStock(id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
