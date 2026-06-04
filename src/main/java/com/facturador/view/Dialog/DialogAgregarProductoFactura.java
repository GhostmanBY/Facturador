package com.facturador.view.Dialog;

import java.util.Optional;

import com.facturador.model.DProFactura;
import com.facturador.model.ProductFactura;
import com.facturador.model.Producto;
import com.facturador.utils.Utils;
import com.facturador.view.Helpers.ErrorAlert;
import com.facturador.view.Helpers.ActualizarProductos;

import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class DialogAgregarProductoFactura {
    private DialogHelperFactura dialoghelp;
    private ErrorAlert alert;
    private Utils utils;
    private ActualizarProductos aProductos;

    private Runnable onFacturaActualizada;
     
    public DialogAgregarProductoFactura(Runnable onFacturaActualizada) {
        this.onFacturaActualizada = onFacturaActualizada;
        this.dialoghelp = new DialogHelperFactura();
        this.alert = new ErrorAlert();
        this.utils = new Utils();
        this.aProductos = new ActualizarProductos();
    }

    public void configurarAgregarProductos(TableView<Producto> tablaProductos, ObservableList<ProductFactura> itemsFactura) {
        tablaProductos.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Producto producto = row.getItem();
                    Optional<DProFactura> dialog = dialoghelp.mostrarDialogoProducto(
                        "Agregar a Factura: " + producto.getName(),
                        1,
                        0
                    );

                    if (dialog.isEmpty()) {
                        return;
                    }

                    try {
                        int cantidad = dialog.get().cantidad();
                        double descuento = dialog.get().descuento();
                        if (cantidad <= 0) {
                            this.alert.mostrarError("La cantidad debe ser mayor a cero");
                            return;
                        }
                        if (cantidad > producto.getStock()) {
                            this.alert.mostrarError("Stock insuficiente");
                            return;
                        }
                        if (descuento < 0 || descuento > 100) {
                            this.alert.mostrarError("El descuento debe estar entre 0 y 100");
                            return;
                        }

                        ProductFactura existente = itemsFactura.stream()
                            .filter(p -> p.getProductoid() == producto.getId())
                            .findFirst()
                            .orElse(null);

                        if (existente != null) {
                            int nuevaCantidad = existente.getCantidad() + cantidad;

                            if (nuevaCantidad > producto.getStock()) {
                                this.alert.mostrarError("Stock insuficiente");
                                return;
                            }

                            int index = itemsFactura.indexOf(existente);

                            ProductFactura actualizado = existente.toBuilder()
                            .cantidad(nuevaCantidad)
                            .subtotal(
                                utils.calcularSubtotal(
                                    nuevaCantidad,
                                    producto.getPrice(),
                                    existente.getDescuento()
                                )
                            )
                            .build();
                        
                            itemsFactura.set(index, actualizado);
                        } else {
                            ProductFactura item = ProductFactura.builder()
                                .productoid(producto.getId())
                                .cantidad(cantidad)
                                .name(producto.getName())
                                .precioUnitario(producto.getPrice())
                                .descuento(descuento)
                                .subtotal(utils.calcularSubtotal(cantidad, producto.getPrice(), descuento))
                                .isActive(true)
                                .build();                            
                            itemsFactura.add(item);
                        }

                        if (onFacturaActualizada != null) {
                            this.onFacturaActualizada.run();
                        }
                        tablaProductos.refresh();
                        this.aProductos.actualizarProductos(producto, cantidad, tablaProductos);
                    } catch (NumberFormatException ex) {
                        this.alert.mostrarError("Ingrese números válidos");
                    }
                }
            });
            return row;
        });
    }
}
