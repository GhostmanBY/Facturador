package com.facturador.controller;

import java.util.Collections;
import java.util.List;

import com.facturador.model.Proveedores;
import com.facturador.service.ProveedoreServices;

public class ProveedoreController {
    private ProveedoreServices proveedoreServices;

    public ProveedoreController() {
        this.proveedoreServices = new ProveedoreServices();
    }

    public void createProveedore(Proveedores proveedore) {
        try {
            this.proveedoreServices.createProveedore(proveedore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProveedore(Proveedores proveedore) {
        try {
            this.proveedoreServices.updateProveedore(proveedore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desactivarProveedore(Proveedores proveedore) {
        try {
            this.proveedoreServices.desactivarProveedore(proveedore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activarProveedore(Proveedores proveedore) {
        try {
            this.proveedoreServices.activarProveedore(proveedore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Proveedores getProveedoreById(int id) {
        try {
            return this.proveedoreServices.getProveedoreById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Proveedores> getProveedores() {
        try {
            return this.proveedoreServices.getProveedores() != null ? this.proveedoreServices.getProveedores() : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
