package com.facturador.model;

public class Producto {
    private int id;
    private int proveedorId;
    private String name;
    private String description;
    private double price;
    private int stock;
    private boolean is_active;

    public static class Builder {
        private int id;
        private int proveedorId;
        private String name;
        private String description;
        private double price;
        private int stock;
        private boolean is_active;
        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder proveedorId(int proveedorId) {
            this.proveedorId = proveedorId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder stock(int stock) {
            this.stock = stock;
            return this;
        }

        public Builder isActive(boolean is_active) {
            this.is_active = is_active;
            return this;
        }

        public Producto build() {
            return new Producto(this);
        }
    }

    private Producto(Builder builder) {
        this.id = builder.id;
        this.proveedorId = builder.proveedorId;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.stock = builder.stock;
        this.is_active = builder.is_active;
    }

    public Builder toBuilder() {
        return new Builder()
        .id(this.id)
        .proveedorId(this.proveedorId)
        .name(this.name)
        .description(this.description)
        .price(this.price)
        .stock(this.stock)
        .isActive(this.is_active);
    }

    public static Builder builder() { return new Builder(); }

    public int getId() {return this.id; }
    public int getProveedorId() {return this.proveedorId; }
    public String getName() {return this.name; }
    public String getDescription() {return this.description; }
    public double getPrice() {return this.price; }
    public int getStock() {return this.stock; }
    public boolean getIsActive() {return this.is_active; }

}
