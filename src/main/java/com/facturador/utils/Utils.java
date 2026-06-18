package com.facturador.utils;

import java.util.List;

import com.facturador.model.ProductFactura;
import com.facturador.model.ResumenFactura;

public class Utils {
    public boolean esEmailValido(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public boolean esNumero(String texto) {
        return texto.matches("\\d+");
    }

    public boolean esTelefonoValido(String telefono) {
        return telefono.matches("[0-9\\-+() ]{6,20}");
    }

    public double calcularSubtotal(int cantidad, double precio,double descuento) {
        double subtotal = cantidad * precio;
        return subtotal - (subtotal * descuento / 100);
    }

    public static ResumenFactura calcularResumen(List<ProductFactura> items, double iva) {
        double subtotal = 0;
        double descuento = 0;

        for (ProductFactura item : items) {
            double bruto = item.getCantidad() * item.getPrecioUnitario();

            subtotal += bruto;
            descuento += bruto * item.getDescuento() / 100;
        }

        double neto = subtotal - descuento;

        double impuestos = neto * 21.0 / 100; // por ahora
        double total = subtotal - descuento + impuestos;
        
        return new ResumenFactura(
            subtotal,
            descuento,
            impuestos,
            total
        );
    }
}
