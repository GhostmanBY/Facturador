package com.facturador.service;

import java.util.List;

import com.facturador.model.Cliente;
import com.facturador.model.User;
import com.facturador.repository.ClienteRepository;

public class ClienteServices {
    private ClienteRepository clienteRepository;

    public ClienteServices() {
        this.clienteRepository = new ClienteRepository();
    }

    public void createCliente(Cliente cliente) {
        try {
            this.clienteRepository.createCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCliente(Cliente cliente) {
        try {
            this.clienteRepository.updateCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desactivarCliente(Cliente cliente) {
        try {
            this.clienteRepository.desactivarCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activarCliente(Cliente cliente) {
        try {
            this.clienteRepository.activarCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cliente getClienteById(int id) {
        try {
            return this.clienteRepository.getClienteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Cliente> getClientes() {
        try {
            return this.clienteRepository.getClientes();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
