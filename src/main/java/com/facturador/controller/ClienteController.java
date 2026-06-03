package com.facturador.controller;

import java.util.Collections;
import java.util.List;

import com.facturador.model.Cliente;
import com.facturador.service.ClienteServices;

public class ClienteController {
    private ClienteServices clienteServices;

    public ClienteController() {
        this.clienteServices = new ClienteServices();
    }

    public void createCliente(Cliente cliente) {
        try {
            this.clienteServices.createCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCliente(Cliente cliente) {
        try {
            this.clienteServices.updateCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desactivarCliente(Cliente cliente) {
        try {
            this.clienteServices.desactivarCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activarCliente(Cliente cliente) {
        try {
            this.clienteServices.activarCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cliente getClienteById(int id) {
        try {
            return this.clienteServices.getClienteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Cliente> getClientes() {
        try {
            return this.clienteServices.getClientes() != null ? this.clienteServices.getClientes() : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
