package com.facturador.view.Helpers;

import com.facturador.model.Producto;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class ActualizarProductos {
    public void actualizarProductos(Producto producto, int diferencia, TableView<Producto> tablaProductos) {
        ObservableList<Producto> items = tablaProductos.getItems();
        for (int i = 0; i < items.size(); i++) {
            Producto actual = items.get(i);
            if (actual.getId() == producto.getId()) {
                Producto actualizado = actual.toBuilder()
                    .stock(actual.getStock() - diferencia)
                    .build();
                items.set(i, actualizado);
                break;
            }
        }
    }
}
