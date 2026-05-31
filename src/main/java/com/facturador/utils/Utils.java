package com.facturador.utils;

public class Utils {
    public static String generarEAN13(int idProducto) {
    // Prefijo 779 = Argentina, luego el id del producto con padding
    String base = String.format("779%09d", idProducto); // 12 dígitos
    
    // Calcular dígito verificador
    int suma = 0;
    for (int i = 0; i < 12; i++) {
        int digito = Character.getNumericValue(base.charAt(i));
        suma += (i % 2 == 0) ? digito : digito * 3;
    }
    int verificador = (10 - (suma % 10)) % 10;
    
    return base + verificador;
}
}
