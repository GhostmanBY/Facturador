package com.facturador.service;

import java.util.List;

import com.facturador.model.Proveedores;
import com.facturador.repository.ProveedoresRepository;

public class ProveedoreServices {
    private ProveedoresRepository proveedoreRepository;

    public ProveedoreServices() {
        this.proveedoreRepository = new ProveedoresRepository();
    }

    public void createProveedore(Proveedores proveedore) {
        try {
            this.proveedoreRepository.createProveedore(proveedore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProveedore(Proveedores proveedore) {
        try {
            this.proveedoreRepository.updateProveedore(proveedore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desactivarProveedore(Proveedores proveedore) {
        try {
            this.proveedoreRepository.desactivarProveedore(proveedore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activarProveedore(Proveedores proveedore) {
        try {
            this.proveedoreRepository.activarProveedore(proveedore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Proveedores getProveedoreById(int id) {
        try {
            return this.proveedoreRepository.getProveedoreById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Proveedores> getProveedores() {
        try {
            return this.proveedoreRepository.getProveedore();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
