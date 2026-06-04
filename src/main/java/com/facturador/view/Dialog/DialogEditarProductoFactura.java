package com.facturador.view.Dialog;

import java.util.Optional;

import com.facturador.controller.StockController;
import com.facturador.model.DProFactura;
import com.facturador.model.ProductFactura;
import com.facturador.model.Producto;
import com.facturador.utils.Utils;
import com.facturador.view.Helpers.ActualizarProductos;

import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class DialogEditarProductoFactura {
    private DialogHelperFactura dialoghelp;
    private StockController stockController;
    private ActualizarProductos aProductos;
    private Utils utils;
    private TableView<Producto> tablaProductos;

    private Runnable onFacturaActualizada;
    
    public DialogEditarProductoFactura(TableView<Producto> tablaProductos,Runnable onFacturaActualizada) {
        this.tablaProductos = tablaProductos;
        this.onFacturaActualizada = onFacturaActualizada;
        this.stockController = new StockController();
        this.dialoghelp = new DialogHelperFactura();
        this.aProductos = new ActualizarProductos();
        this.utils = new Utils();
    }


    public void configurarEditarProductos(TableView<ProductFactura> tablaFactura, ObservableList<ProductFactura> itemsFactura) {
        tablaFactura.setRowFactory(tv -> {
            TableRow<ProductFactura> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    ProductFactura item = row.getItem();
                    Optional<DProFactura> dialog = this.dialoghelp.mostrarDialogoProducto(
                        "Editar: " + item.getName(),
                        item.getCantidad(),
                        item.getDescuento()
                    );

                    if (dialog.isEmpty()) {
                        return;
                    }

                    int cantidad = dialog.get().cantidad();
                    double descuento = dialog.get().descuento();

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
                }
            });
            return row;
        });
    }
}
