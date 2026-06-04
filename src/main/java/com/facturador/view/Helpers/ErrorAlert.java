package com.facturador.view.Helpers;

import javafx.scene.control.Alert;

public class ErrorAlert {
    public void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }   
}
