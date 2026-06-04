package com.facturador.view.Dialog;

import java.util.Optional;

import com.facturador.model.DProFactura;
import com.facturador.view.Helpers.ErrorAlert;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DialogHelperFactura {
    private ErrorAlert alert;

    public Optional<DProFactura> mostrarDialogoProducto(String titulo, int cantidadInicial, double descuentoInicial) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        DialogPane pane = dialog.getDialogPane();
        pane.getStylesheets().add(
            getClass().getResource("/assets/dialog.css").toExternalForm()
        );
        pane.getStyleClass().add("dialog-pane");

        TextField txtCantidad = new TextField(String.valueOf(cantidadInicial));
        TextField txtDescuento = new TextField(String.valueOf(descuentoInicial));
         
        Label lblcan = new Label("Cantidad");
        lblcan.getStyleClass().add("dialog-subtitle-label");
        Label lbldes =new Label("Descuento (%)");
        lbldes.getStyleClass().add("dialog-subtitle-label");

        VBox content = new VBox(
            10,
            lblcan,
            txtCantidad,
            lbldes,
            txtDescuento
        );
        content.getStyleClass().add("dialog-header-custom");

        pane.setContent(content);

        ButtonType btnAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);

        pane.getButtonTypes().addAll(btnAceptar, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isEmpty() || result.get() != btnAceptar) {
            return Optional.empty();
        }

        try {
            return Optional.of(
                new DProFactura(
                    Integer.parseInt(txtCantidad.getText()),
                    Double.parseDouble(txtDescuento.getText())
                )
            );
        } catch (NumberFormatException e) {
            this.alert.mostrarError("Ingrese números válidos");
            return Optional.empty();
        }
    }
}
