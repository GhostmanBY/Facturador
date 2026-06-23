package com.facturador.view.Dialog;

import java.util.Optional;

import com.facturador.controller.StockController;
import com.facturador.model.DProFactura;
import com.facturador.model.ProductFactura;
import com.facturador.model.Producto;
import com.facturador.utils.Utils;

import com.facturador.view.Helpers.ActualizarProductos;
import com.facturador.view.Helpers.ErrorAlert;

import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class DialogEditarProductoFactura {
    private DialogHelperFactura dialoghelp;
    private StockController stockController;
    private ActualizarProductos aProductos;
    private Utils utils;
    private TableView<Producto> tablaProductos;
    private ErrorAlert alert;

    private Runnable onFacturaActualizada;
    
    public DialogEditarProductoFactura(TableView<Producto> tablaProductos,Runnable onFacturaActualizada) {
        this.tablaProductos = tablaProductos;
        this.onFacturaActualizada = onFacturaActualizada;
        this.stockController = new StockController();
        this.dialoghelp = new DialogHelperFactura();
        this.aProductos = new ActualizarProductos();
        this.utils = new Utils();
        this.alert = new ErrorAlert();
    }


    public void configurarEditarProductos(TableView<ProductFactura> tablaFactura, ObservableList<ProductFactura> itemsFactura) {
        tablaFactura.setRowFactory(tv -> {
            TableRow<ProductFactura> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    ProductFactura item = row.getItem();
                    Producto producto_now = this.tablaProductos.getItems()
                    .stream()
                    .filter(producto -> producto.getId() == item.getProductoid())
                    .findFirst()
                    .orElse(null);
                    Optional<DProFactura> dialog = this.dialoghelp.mostrarDialogoProducto(
                        "Editar: " + item.getName(),
                        item.getCantidad(),
                        item.getDescuento()
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
                        if (cantidad > producto_now.getStock()) {
                            this.alert.mostrarError("Stock insuficiente");
                            return;
                        }
                        if (descuento < 0 || descuento > 100) {
                            this.alert.mostrarError("El descuento debe estar entre 0 y 100");
                            return;
                        }

                        ProductFactura actualizado = item.toBuilder()
                        .cantidad(cantidad)
                        .descuento(descuento)
                        .subtotal(
                            this.utils.calcularSubtotal(
                                cantidad,
                                item.getPrecioUnitario(),
                                descuento
                            )
                        )
                        .build();

                        int index = itemsFactura.indexOf(item);
                        itemsFactura.set(index, actualizado);

                        if (onFacturaActualizada != null) {
                            onFacturaActualizada.run();
                        }
                        tablaFactura.refresh();
                        this.aProductos.actualizarProductos(
                            this.stockController.getStockById(item.getProductoid()),
                            cantidad - item.getCantidad(),
                            this.tablaProductos
                        );
                    } catch (NumberFormatException ex) {
                        this.alert.mostrarError("Ingrese números válidos");
                    }
                }
            });
            return row;
        });
    }
}
