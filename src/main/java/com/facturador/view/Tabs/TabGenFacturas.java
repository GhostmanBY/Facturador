package com.facturador.view.Tabs;

import java.util.List;
import java.util.Optional;

import com.facturador.controller.StockController;
import com.facturador.model.ProductFactura;
import com.facturador.model.Producto;
import com.facturador.model.ResumenFactura;
import com.facturador.model.User;
import com.facturador.utils.Utils;
import com.facturador.view.Dialog.DialogAgregarProductoFactura;
import com.facturador.view.Dialog.DialogEditarProductoFactura;
import com.facturador.view.Dialog.DialogGenerarFactura;
import com.facturador.view.Helpers.ActualizarProductos;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TabGenFacturas {
    private StockController stockController;
    private TableView<Producto> tablaProductos;
    private ObservableList<Producto> datos;
    private ObservableList<ProductFactura> itemsFactura;
    private static final int IVA = 21;
    private Label lblSubValor = new Label("0.00");
    private Label lblImpuestosValor = new Label("0.00");
    private Label lblTotalValor = new Label("0.00");
    private User user;
    private ActualizarProductos aProductos;
    

    public TabGenFacturas(User user) {
        this.stockController = new StockController();
        this.user = user;
        this.aProductos = new ActualizarProductos();
    }

    public Parent buildFacturasTab() {
        tablaProductos = new TableView<>();
        tablaProductos.getStyleClass().add("table-view");
        
        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());
        TableColumn<Producto, Double> colPrice = new TableColumn<>("Precio");
        colPrice.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()).asObject());

        tablaProductos.getColumns().add(colNombre);
        tablaProductos.getColumns().add(colStock);
        tablaProductos.getColumns().add(colPrice);

        recargarProductos();
        VBox.setVgrow(tablaProductos, Priority.ALWAYS);


        TableView<ProductFactura> tablaFactura = new TableView<>();
        tablaFactura.getStyleClass().add("table-view");

        TableColumn<ProductFactura, String> colNombreF = new TableColumn<>("Nombre");
        colNombreF.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        TableColumn<ProductFactura, Integer> colCantidad = new TableColumn<>("Cant.");
        colCantidad.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getCantidad()).asObject());

        TableColumn<ProductFactura, Double> colUnitario = new TableColumn<>("P. Unit.");
        colUnitario.setCellValueFactory(c ->new SimpleDoubleProperty(c.getValue().getPrecioUnitario()).asObject());

        TableColumn<ProductFactura, Double> colDesc = new TableColumn<>("Desc %");
        colDesc.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getDescuento()).asObject());

        TableColumn<ProductFactura, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getSubtotal()).asObject());

        tablaFactura.getColumns().add(colNombreF);
        tablaFactura.getColumns().add(colCantidad);
        tablaFactura.getColumns().add(colUnitario);
        tablaFactura.getColumns().add(colDesc);
        tablaFactura.getColumns().add(colSubtotal);

        tablaFactura.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                ProductFactura seleccionado = tablaFactura.getSelectionModel().getSelectedItem();
                if (seleccionado == null) {
                    return;
                }

                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar eliminación");
                confirmacion.setHeaderText("Eliminar " + seleccionado.getName());
                confirmacion.setContentText("¿Está seguro de que desea eliminar este producto de la factura?");
                
                Optional<ButtonType> resultado = confirmacion.showAndWait();
                
                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                    Producto producto = stockController.getStockById(seleccionado.getProductoid());
    
                    this.aProductos.actualizarProductos(
                        producto,
                        -seleccionado.getCantidad(), // devuelve stock
                        tablaProductos
                    );
                    itemsFactura.remove(seleccionado);
                    actualizarResumenFactura();
                }
            }
        });


        itemsFactura = FXCollections.observableArrayList();

        tablaFactura.setItems(itemsFactura);

        DialogAgregarProductoFactura dialogAgregar = new DialogAgregarProductoFactura(this::actualizarResumenFactura);
        
        dialogAgregar.configurarAgregarProductos(
            tablaProductos,
            itemsFactura
        );
        DialogEditarProductoFactura dialogEditar = new DialogEditarProductoFactura(tablaProductos,this::actualizarResumenFactura);
        
        dialogEditar.configurarEditarProductos(
            tablaFactura,
            itemsFactura
        );

        Label tituloProductos = new Label("Productos");
        tituloProductos.getStyleClass().add("label-title");

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar producto...");
        txtBuscar.getStyleClass().add("text-field");
        txtBuscar.setAlignment(Pos.CENTER_LEFT);
        
        HBox searchBox = new HBox(tituloProductos, txtBuscar);
        searchBox.getStyleClass().add("search-box");
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefHeight(70);
        
        HBox.setHgrow(txtBuscar, Priority.ALWAYS);
        txtBuscar.setMaxWidth(Double.MAX_VALUE);
        HBox.setMargin(tituloProductos, new Insets(0, 0, 0, 10));
        HBox.setMargin(txtBuscar, new Insets(10, 10, 10, 10));
        txtBuscar.textProperty().addListener((obs, oldText, newText) -> {
            String filtro = newText.toLowerCase();
            ObservableList<Producto> filtrados = FXCollections.observableArrayList();

            for (Producto p : datos) {
                if (p.getName().toLowerCase().contains(filtro)) {
                    filtrados.add(p);
                }
            }

            tablaProductos.setItems(filtrados);
        });

        VBox panelProductos = new VBox(
            searchBox,
            tablaProductos
        );
        panelProductos.getStyleClass().add("factura-panel");
        
        VBox.setVgrow(tablaProductos, Priority.ALWAYS);
        
        Label tituloFactura = new Label("Factura");
        tituloFactura.getStyleClass().add("label-title");
        tituloFactura.setAlignment(Pos.CENTER_LEFT);

        HBox headerFactura = new HBox(
            tituloFactura,
            new Region() // espacio vacío
        );
        HBox.setMargin(tituloFactura, new Insets(0, 0, 0, 10));
        headerFactura.setAlignment(Pos.CENTER_LEFT);
        headerFactura.setPrefHeight(70);

        VBox panelFactura = new VBox(
            headerFactura,
            tablaFactura
        );

        panelFactura.getStyleClass().add("factura-panel");
        VBox.setVgrow(tablaFactura, Priority.ALWAYS);

        SplitPane splitPane = new SplitPane(
            panelProductos,
            panelFactura
        );

        splitPane.setDividerPositions(0.5);

        Label lblSubtotal = new Label("Subtotal: ");
        lblSubValor.getStyleClass().add("factura-values");
        HBox subtotalBox = new HBox(lblSubtotal, lblSubValor);
        subtotalBox.getStyleClass().add("factura-resumen-item");

        Label lblImpuestos = new Label("Impuestos: ");
        lblImpuestosValor.getStyleClass().add("factura-values");
        HBox impuestosBox = new HBox(lblImpuestos, lblImpuestosValor);
        impuestosBox.getStyleClass().add("factura-resumen-item");

        Label lblTotal = new Label("TOTAL: ");
        lblTotalValor.getStyleClass().add("factura-total-value");
        HBox totalBox = new HBox(lblTotal, lblTotalValor);
        totalBox.getStyleClass().add("factura-total");

        lblSubtotal.getStyleClass().add("factura-resumen-item");
        lblImpuestos.getStyleClass().add("factura-resumen-item");

        lblTotal.getStyleClass().add("factura-total");

        HBox resumen = new HBox(
            subtotalBox,
            impuestosBox,
            totalBox
        );
        resumen.getStyleClass().add("factura-resumen-bar");

        Button btnGenerar = new Button("Generar Factura");
        btnGenerar.getStyleClass().add("button");

        DialogGenerarFactura dialgoGenFactura = new DialogGenerarFactura(this::actualizarResumenFactura, lblTotalValor, lblSubValor, lblImpuestosValor, this.user, IVA);

        dialgoGenFactura.configurarGenerarFactura(
            btnGenerar,
            itemsFactura
        );

        HBox.setHgrow(resumen, Priority.ALWAYS);

        HBox footer = new HBox(
            resumen,
            btnGenerar
        );

        footer.setAlignment(Pos.CENTER_LEFT);
        footer.getStyleClass().add("factura-footer");

        BorderPane root = new BorderPane();
        root.setCenter(splitPane);
        root.setBottom(footer);

        return root;
    }

    private void actualizarResumenFactura() {
        ResumenFactura resumen =
            Utils.calcularResumen(itemsFactura, IVA);

        lblSubValor.setText(
            String.format("$ %.2f", resumen.subtotal())
        );

        lblImpuestosValor.setText(
            String.format("$ %.2f", resumen.impuestos())
            + " (21%)"
        );

        lblTotalValor.setText(
            String.format("$ %.2f", resumen.total())
        );
    }

    public void recargarProductos() {
        List<Producto> listado = this.stockController.getStock()
        .stream()
        .filter(producto -> producto.getIsActive())
        .toList();

        datos = FXCollections.observableArrayList(listado);
        tablaProductos.setItems(datos);
    }
}
