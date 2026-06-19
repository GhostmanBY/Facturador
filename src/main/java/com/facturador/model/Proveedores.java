package com.facturador.model;

public class Proveedores {
    private int id;
    private String nombre;
    private String cuit;
    private String direccion;
    private String telefono;
    private String email;
    private boolean isActive;

    public static class Builder {
        private int id;
        private String nombre;
        private String cuit;
        private String direccion;
        private String telefono;
        private String email;
        private boolean isActive;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder cuit(String cuit) {
            this.cuit = cuit;
            return this;
        }

        public Builder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Proveedores build() {
            return new Proveedores(this);
        }
    }

    private Proveedores(Builder builder) {
        this.id = builder.id;
        this.nombre = builder.nombre;
        this.cuit = builder.cuit;
        this.direccion = builder.direccion;
        this.telefono = builder.telefono;
        this.email = builder.email;
        this.isActive = builder.isActive;
    }

    public Builder toBuilder() {
        return new Builder()
        .id(this.id)
        .nombre(this.nombre)
        .cuit(this.cuit)
        .direccion(this.direccion)
        .telefono(this.telefono)
        .email(this.email)
        .isActive(this.isActive);
    }

    public static Builder builder() { return new Builder(); }

    public int getId() { return this.id; }
    public String getNombre() { return this.nombre; }
    public String getCuit() { return this.cuit; }
    public String getDireccion() { return this.direccion; }
    public String getTelefono() { return this.telefono; }
    public String getEmail() { return this.email; }
    public boolean isActive() { return this.isActive; }
}
