package com.facturador.model;


public class User {
    public enum UserRole {
        ADMIN,
        CAJERO,
        REPOSITOR
    }

    private Integer id;
    private String name;
    private String email;
    private String hashPassword;
    private String documento;
    private String domicilio;
    private String telefono;
    private boolean isActive;
    private UserRole role;

    public static class Builder{
        private Integer id;
        private String name;
        private String email;
        private String hashPassword;
        private String documento;
        private String domicilio;
        private String telefono;
        private boolean isActive;    
        private UserRole role;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder hashPassword(String hashPassword) {
            this.hashPassword = hashPassword;
            return this;
        }

        public Builder documento(String documento) {
            this.documento = documento;
            return this;
        }

        public Builder domicilio(String domicilio) {
            this.domicilio = domicilio;
            return this;
        }

        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder role(UserRole role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.hashPassword = builder.hashPassword;
        this.documento = builder.documento;
        this.domicilio = builder.domicilio;
        this.telefono = builder.telefono;
        this.isActive = builder.isActive;
        this.role = builder.role;
    }

    public Builder toBuilder() {
        return new Builder()
        .id(this.id)
        .name(this.name)
        .email(this.email)
        .hashPassword(this.hashPassword)
        .documento(this.documento)
        .domicilio(this.domicilio)
        .telefono(this.telefono)
        .isActive(this.isActive)
        .role(this.role);
    }

    public static Builder builder() { return new Builder(); }

    public Integer getId() { return this.id; }
    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public String getHashPassword() { return this.hashPassword; }
    public String getDocumento() { return this.documento; }
    public String getDomicilio() { return this.domicilio; }
    public String getTelefono() { return this.telefono; }
    public boolean isActive() { return this.isActive; }
    public UserRole getRole() { return this.role; }
}
