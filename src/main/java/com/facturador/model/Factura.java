package com.facturador.model;

import java.time.LocalDate;
import java.util.List;

public class Factura {
    private int id;
    private int clienteId;
    private int vendedorId;
    private double subtotal;
    private double descuento;
    private double impuestos;
    private LocalDate fecha;
    private double total;
    private List<ProductFactura> detalles;

    public static class Builder {
        private int id;
        private int clienteId;
        private int vendedorId;
        private double subtotal;
        private double descuento;
        private double impuestos;
        private LocalDate fecha;
        private double total;
        private List<ProductFactura> detalles;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder clienteId(int clienteId) {
            this.clienteId = clienteId;
            return this;
        }

        public Builder vendedorId(int vendedorId) {
            this.vendedorId = vendedorId;
            return this;
        }

        public Builder subtotal(double subtotal) {
            this.subtotal = subtotal;
            return this;
        }

        public Builder descuento(double descuento) {
            this.descuento = descuento;
            return this;
        }

        public Builder impuestos(double impuestos) {
            this.impuestos = impuestos;
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
        this.clienteId = builder.clienteId;
        this.vendedorId = builder.vendedorId;
        this.subtotal = builder.subtotal;
        this.descuento = builder.descuento;
        this.impuestos = builder.impuestos;
        this.fecha = builder.fecha;
        this.total = builder.total;
        this.detalles = builder.detalles;
    }

    public Builder toBuilder() {
        return new Builder()
        .id(this.id)
        .clienteId(this.clienteId)
        .vendedorId(this.vendedorId)
        .subtotal(this.subtotal)
        .descuento(this.descuento)
        .impuestos(this.impuestos)
        .fecha(this.fecha)
        .total(this.total)
        .detalles(this.detalles)
        .fecha(this.fecha)
        .total(this.total)
        .detalles(this.detalles);
    }

    public static Builder builder() { return new Builder(); }

    public int getId() { return this.id; }
    public int getClienteId() { return this.clienteId; }
    public int getVendedorId() { return this.vendedorId; }
    public double getSubtotal() { return this.subtotal; }
    public double getDescuento() { return this.descuento; }
    public double getImpuestos() { return this.impuestos; }
    public LocalDate getFecha() { return this.fecha; }
    public double getTotal() { return this.total; }
    public List<ProductFactura> getDetalles() { return this.detalles; }
}