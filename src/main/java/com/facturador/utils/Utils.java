package com.facturador.utils;

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

    public static String generarEAN13(int idProducto) {
    String base = String.format("779%09d", idProducto); 
    
    int suma = 0;
    for (int i = 0; i < 12; i++) {
        int digito = Character.getNumericValue(base.charAt(i));
        suma += (i % 2 == 0) ? digito : digito * 3;
    }
    int verificador = (10 - (suma % 10)) % 10;
    
    return base + verificador;
}
}
