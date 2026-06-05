package com.facturador.service;

import java.util.List;

import com.facturador.model.Producto;
import com.facturador.repository.StockRepository;
import com.facturador.utils.Utils;

public class StockServices {
    private final StockRepository StockRepository;

    public StockServices() {
        this.StockRepository = new StockRepository();
    }

    public void createStock(Producto producto) {
        if (producto.getName().isBlank() || producto.getPrice() <= 0 || producto.getStock() < 0) {
            throw new IllegalArgumentException("Name no puede estar vacío, price debe ser mayor a 0 y stock no puede ser negativo");
        }

        String code = Utils.generarEAN13(producto.getId());

        producto = producto.toBuilder()
        .code(code)
        .build();

        this.StockRepository.createStock(producto);
    }

    public void modifyStock(Producto producto) {
        if (producto.getId() == 0 || producto.getId() < 0) {
            throw new IllegalArgumentException("identificador no válido");
        }

        this.StockRepository.modifyStock(producto);
    }

    public List<Producto> getStock() {
        return this.StockRepository.getStock();
    }

    public Producto getStockById(int id) {
        if (id == 0 || id < 0) {
            throw new IllegalArgumentException("identificador no válido");
        }

        return this.StockRepository.getStockById(id);
    }

    public void activateStock(int id) {
        if (id == 0 || id < 0) {
            throw new IllegalArgumentException("identificador no válido");
        }

        this.StockRepository.activateStock(id);
    }

    public void deactivateStock(int id) {
        if (id == 0 || id < 0) {
            throw new IllegalArgumentException("identificador no válido");
        }

        this.StockRepository.deactivateStock(id);
    }
}
