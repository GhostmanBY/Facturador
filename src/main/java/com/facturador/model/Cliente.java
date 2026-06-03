package com.facturador.model;

public class Cliente {
    private int id;
    private String nombre;
    private String documento;
    private String direccion;
    private String telefono;
    private String email;
    private boolean isActive;

    public static class Builder {
        private int id;
        private String nombre;
        private String documento;
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

        public Builder documento(String documento) {
            this.documento = documento;
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

        public Cliente build() {
            return new Cliente(this);
        }
    }

    private Cliente(Builder builder) {
        this.id = builder.id;
        this.nombre = builder.nombre;
        this.documento = builder.documento;
        this.direccion = builder.direccion;
        this.telefono = builder.telefono;
        this.email = builder.email;
        this.isActive = builder.isActive;
    }

    public Builder toBuilder() {
        return new Builder()
        .id(this.id)
        .nombre(this.nombre)
        .documento(this.documento)
        .direccion(this.direccion)
        .telefono(this.telefono)
        .email(this.email)
        .isActive(this.isActive);
    }

    public static Builder builder() { return new Builder(); }

    public int getId() { return this.id; }
    public String getNombre() { return this.nombre; }
    public String getDocumento() { return this.documento; }
    public String getDireccion() { return this.direccion; }
    public String getTelefono() { return this.telefono; }
    public String getEmail() { return this.email; }
    public boolean isActive() { return this.isActive; }
}
