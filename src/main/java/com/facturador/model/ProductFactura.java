package com.facturador.model;

public class ProductFactura {
    private int id;
    private int productoid;
    private String name;
    private int cantidad;
    private double precioUnitario;
    private double descuento;
    private double subtotal;
    private boolean is_active;
    private int facturaId;

    public static class Builder {
        private int id;
        private int productoid;
        private String name;
        private int cantidad;
        private double precioUnitario;
        private double descuento;
        private double subtotal;
        private boolean is_active;
        private int facturaId;
        
        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder productoid(int productoid) {
            this.productoid = productoid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder cantidad(int cantidad) {
            this.cantidad = cantidad;
            return this;
        }

        public Builder precioUnitario(double precioUnitario) {
            this.precioUnitario = precioUnitario;
            return this;
        }

        public Builder descuento(double descuento) {
            this.descuento = descuento;
            return this;
        }

        public Builder subtotal(double subtotal) {
            this.subtotal = subtotal;
            return this;
        }

        public Builder isActive(boolean is_active) {
            this.is_active = is_active;
            return this;
        }

        public Builder facturaId(int facturaId) {
            this.facturaId = facturaId;
            return this;
        }

        public ProductFactura build() {
            return new ProductFactura(this);
        }
    }

    private ProductFactura(Builder builder) {
        this.id = builder.id;
        this.productoid = builder.productoid;
        this.name = builder.name;
        this.cantidad = builder.cantidad;
        this.precioUnitario = builder.precioUnitario;
        this.descuento = builder.descuento;
        this.subtotal = builder.subtotal;
        this.is_active = builder.is_active;
        this.facturaId = builder.facturaId;
    }

    public Builder toBuilder() {
        return new Builder()
        .id(this.id)
        .productoid(this.productoid)
        .name(this.name)
        .cantidad(this.cantidad)
        .precioUnitario(this.precioUnitario)
        .descuento(this.descuento)
        .subtotal(this.subtotal)
        .isActive(this.is_active)
        .facturaId(this.facturaId);
    }

    public static Builder builder() { return new Builder(); }

    public int getId() { return this.id; }
    public int getProductoid() { return this.productoid; }
    public String getName() { return this.name; }
    public int getCantidad() { return this.cantidad; }
    public double getPrecioUnitario() { return this.precioUnitario; }
    public double getDescuento() { return this.descuento; }
    public double getSubtotal() { return this.subtotal; }
    public int getFacturaId() { return this.facturaId; }
    public boolean getIsActive() { return this.is_active; }
}
