package com.facturador.model;

import java.time.LocalDate;
import java.util.List;

public class Factura {
    private int id;
    private int proveedorId;
    private LocalDate fecha;
    private double total;
    private List<ProductFactura> detalles;

    public static class Builder {
        private int id;
        private int proveedorId;
        private LocalDate fecha;
        private double total;
        private List<ProductFactura> detalles;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder proveedorId(int proveedorId) {
            this.proveedorId = proveedorId;
            return this;
        }

        public Builder fecha(LocalDate fecha) {
            this.fecha = fecha;
            return this;
        }

        public Builder total(double total) {
            this.total = total;
            return this;
        }

        public Builder detalles(List<ProductFactura> detalles) {
            this.detalles = detalles;
            return this;
        }

        public Factura build() {
            return new Factura(this);
        }
    }

    private Factura(Builder builder) {
        this.id = builder.id;
        this.proveedorId = builder.proveedorId;
        this.fecha = builder.fecha;
        this.total = builder.total;
        this.detalles = builder.detalles;
    }

    public Builder toBuilder() {
        return new Builder()
        .id(this.id)
        .proveedorId(this.proveedorId)
        .fecha(this.fecha)
        .total(this.total)
        .detalles(this.detalles);
    }

    public static Builder builder() { return new Builder(); }

    public int getId() { return this.id; }
    public int getProveedoreId() { return this.proveedorId; }
    public LocalDate getFecha() { return this.fecha; }
    public double getTotal() { return this.total; }
    public List<ProductFactura> getDetalles() { return this.detalles; }
}