package com.facturador.view.Tabs;

import com.facturador.controller.ClienteController;
import com.facturador.controller.FacturaController;
import com.facturador.controller.ProductFController;
import com.facturador.controller.ProveedoreController;
import com.facturador.controller.StockController;
import com.facturador.model.Cliente;
import com.facturador.model.Factura;
import com.facturador.model.ProductFactura;
import com.facturador.model.Producto;
import com.facturador.model.Proveedores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabViewFacturas {
    private FacturaController facturaController;
    private ProductFController productFController;
    private StockController stockController;
    private ClienteController clienteController;
    private ProveedoreController proveedoreController;
    private Map<Integer, String> productoProveedorMap;

    private VBox panelDetalle;
    private Label lblDetalleTitulo;
    private Label lblDetalleCliente;
    private Label lblDetalleFecha;
    private TableView<ProductFactura> tablaItems;
    private Label lblSubtotalVal;
    private Label lblDescuentoVal;
    private Label lblImpuestosVal;
    private Label lblTotalVal;
    private VBox cardActiva;

    private FlowPane flowPane;
    private FilteredList<Factura> facturasFiltradas;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TabViewFacturas() {
        this.facturaController = new FacturaController();
        this.clienteController = new ClienteController();
        this.productFController = new ProductFController();
        this.stockController = new StockController();
        this.proveedoreController = new ProveedoreController();
    }

    public Parent buildViewFacturasTab() {
        // ── Root ──────────────────────────────────────────────────────
        HBox root = new HBox();
        root.getStyleClass().add("facturas-root");

        // ── Panel izquierdo ───────────────────────────────────────────
        VBox panelIzquierdo = new VBox();
        panelIzquierdo.getStyleClass().add("facturas-left");
        HBox.setHgrow(panelIzquierdo, Priority.ALWAYS);

        // Toolbar
        HBox toolbar = buildToolbar();

        // Grid de cards
        flowPane = new FlowPane();
        flowPane.getStyleClass().add("facturas-flow");
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setPadding(new Insets(14));

        ScrollPane scroll = new ScrollPane(flowPane);
        scroll.getStyleClass().add("facturas-scroll");
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        // Datos de ejemplo — reemplazá con tu controlador
        ObservableList<Factura> facturas_sinDetalles = cargarFacturas();
        ObservableList<Factura> facturas = cargarProductFacturas(facturas_sinDetalles);
        facturasFiltradas = new FilteredList<>(facturas, f -> true);

        // Conectar filtros
        TextField txtBuscar = (TextField) toolbar.lookup("#txtBuscar");
        DatePicker dpDesde = (DatePicker) toolbar.lookup("#dpDesde");
        DatePicker dpHasta = (DatePicker) toolbar.lookup("#dpHasta");

        Runnable aplicarFiltros = () -> {
            String texto = txtBuscar.getText().toLowerCase().trim();
            LocalDate desde = dpDesde.getValue();
            LocalDate hasta = dpHasta.getValue();

            facturasFiltradas.setPredicate(factura -> {
                Cliente cliente = this.clienteController.getClienteById(factura.getClienteId());
                boolean matchTexto = texto.isEmpty()
                || cliente.getNombre().toLowerCase().contains(texto)
                || String.valueOf(factura.getId()).contains(texto);

                boolean matchDesde = desde == null || !factura.getFecha().isBefore(desde);
                boolean matchHasta = hasta == null || !factura.getFecha().isAfter(hasta);

                return matchTexto && matchDesde && matchHasta;
            });

            recargarFacturas();
        };

        txtBuscar.textProperty().addListener((o, ov, nv) -> aplicarFiltros.run());
        dpDesde.valueProperty().addListener((o, ov, nv) -> aplicarFiltros.run());
        dpHasta.valueProperty().addListener((o, ov, nv) -> aplicarFiltros.run());

        // Cargar inicial
        facturas.forEach(factura  -> flowPane.getChildren().add(buildCard(factura )));

        panelIzquierdo.getChildren().addAll(toolbar, scroll);

        // ── Panel derecho (detalle) ───────────────────────────────────
        panelDetalle = buildPanelDetalle();
        panelDetalle.setVisible(false);
        panelDetalle.setManaged(false);

        root.getChildren().addAll(panelIzquierdo, panelDetalle);
        return root;
    }

    // ── Toolbar ───────────────────────────────────────────────────────
    private HBox buildToolbar() {
        HBox toolbar = new HBox(8);
        toolbar.getStyleClass().add("facturas-toolbar");
        toolbar.setAlignment(Pos.CENTER_LEFT);

        TextField txtBuscar = new TextField();
        txtBuscar.setId("txtBuscar");
        txtBuscar.setPromptText("Buscar cliente o N° factura...");
        txtBuscar.getStyleClass().add("facturas-search");
        HBox.setHgrow(txtBuscar, Priority.ALWAYS);

        Label lblDesde = new Label("Desde:");
        lblDesde.getStyleClass().add("toolbar-label");
        DatePicker dpDesde = new DatePicker();
        dpDesde.setId("dpDesde");
        dpDesde.setPromptText("dd/mm/aaaa");
        dpDesde.getStyleClass().add("facturas-datepicker");

        Label lblHasta = new Label("Hasta:");
        lblHasta.getStyleClass().add("toolbar-label");
        DatePicker dpHasta = new DatePicker();
        dpHasta.setId("dpHasta");
        dpHasta.setPromptText("dd/mm/aaaa");
        dpHasta.getStyleClass().add("facturas-datepicker");

        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.getStyleClass().add("btn-ghost");
        btnLimpiar.setOnAction(e -> {
            txtBuscar.clear();
            dpDesde.setValue(null);
            dpHasta.setValue(null);
        });

        toolbar.getChildren().addAll(txtBuscar, lblDesde, dpDesde, lblHasta, dpHasta, btnLimpiar);
        return toolbar;
    }

    // ── Card de factura ───────────────────────────────────────────────
    private VBox buildCard(Factura factura) {
        Cliente cliente = this.clienteController.getClienteById(factura.getClienteId());
        VBox card = new VBox(0);
        card.getStyleClass().add("factura-card");
        card.setPrefWidth(200);

        // Header de la card
        HBox cardHeader = new HBox();
        cardHeader.getStyleClass().add("card-header");
        cardHeader.setAlignment(Pos.CENTER_LEFT);

        Label lblNum = new Label("#" + String.format("%05d", factura.getId()));
        lblNum.getStyleClass().add("card-num");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label lblFecha = new Label(factura.getFecha().format(FORMATTER));
        lblFecha.getStyleClass().add("card-fecha");

        cardHeader.getChildren().addAll(lblNum, spacer, lblFecha);

        // Body
        VBox cardBody = new VBox(4);
        cardBody.getStyleClass().add("card-body");

        Label lblCliente = new Label(cliente.getNombre());
        lblCliente.getStyleClass().add("card-cliente");
        lblCliente.setMaxWidth(Double.MAX_VALUE);

        Label lblTotalLabel = new Label("Total");
        lblTotalLabel.getStyleClass().add("card-total-label");
        Label lblTotal = new Label(formatMoney(factura.getTotal()));
        lblTotal.getStyleClass().add("card-total-value");

        cardBody.getChildren().addAll(lblCliente, lblTotalLabel, lblTotal);

        // Footer con botón
        HBox cardFooter = new HBox();
        cardFooter.getStyleClass().add("card-footer");

        Button btnDetalle = new Button("Ver detalle");
        btnDetalle.getStyleClass().add("card-btn-detalle");
        btnDetalle.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnDetalle, Priority.ALWAYS);

        btnDetalle.setOnAction(e -> abrirDetalle(factura, card));

        cardFooter.getChildren().add(btnDetalle);
        card.getChildren().addAll(cardHeader, cardBody, cardFooter);
        return card;
    }

    // ── Panel detalle ─────────────────────────────────────────────────
    private VBox buildPanelDetalle() {
        VBox panel = new VBox(0);
        panel.getStyleClass().add("detalle-panel");
        panel.setPrefWidth(400);
        panel.setMinWidth(380);
        panel.setMaxWidth(450);

        // Header
        HBox header = new HBox();
        header.getStyleClass().add("detalle-header");
        header.setAlignment(Pos.CENTER_LEFT);

        lblDetalleTitulo = new Label("Factura #00000");
        lblDetalleTitulo.getStyleClass().add("detalle-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnCerrar = new Button("✕");
        btnCerrar.getStyleClass().add("detalle-close");
        btnCerrar.setOnAction(e -> cerrarDetalle());

        header.getChildren().addAll(lblDetalleTitulo, spacer, btnCerrar);

        // Body con scroll
        VBox body = new VBox(16);
        body.getStyleClass().add("detalle-body");

        // Meta info
        VBox meta = new VBox(6);
        meta.getStyleClass().add("detalle-meta");

        lblDetalleCliente = new Label();
        lblDetalleCliente.getStyleClass().add("detalle-meta-value");
        lblDetalleFecha = new Label();
        lblDetalleFecha.getStyleClass().add("detalle-meta-secondary");

        meta.getChildren().addAll(lblDetalleCliente, lblDetalleFecha);

        // Tabla de items
        Label lblItemsTitle = new Label("PRODUCTOS");
        lblItemsTitle.getStyleClass().add("detalle-section-label");

        tablaItems = new TableView<>();
        tablaItems.getStyleClass().add("detalle-tabla");
        tablaItems.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tablaItems.setFixedCellSize(48);
        tablaItems.setPrefHeight(440);

        TableColumn<ProductFactura, String> colNombre = new TableColumn<>("Producto");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNombre.getStyleClass().add("col-producto");

        TableColumn<ProductFactura, String> colProveedor = new TableColumn<>("Proveedor");
        colProveedor.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(
                productoProveedorMap != null
                    ? productoProveedorMap.getOrDefault(c.getValue().getProductoid(), "N/A")
                    : "N/A"
            )
        );

        TableColumn<ProductFactura, Integer> colCantidad = new TableColumn<>("Cant.");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCantidad.setPrefWidth(50);

        TableColumn<ProductFactura, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tablaItems.getColumns().add(colNombre);
        tablaItems.getColumns().add(colCantidad);
        tablaItems.getColumns().add(colSubtotal);
        tablaItems.getColumns().add(colProveedor);

        // Totales
        Label lblTotalesTitle = new Label("RESUMEN");
        lblTotalesTitle.getStyleClass().add("detalle-section-label");

        VBox totalesBox = new VBox(6);
        totalesBox.getStyleClass().add("detalle-totales");

        lblSubtotalVal    = buildTotalRow(totalesBox, "Subtotal", false);
        lblDescuentoVal   = buildTotalRow(totalesBox, "Descuento", false);
        lblImpuestosVal   = buildTotalRow(totalesBox, "Impuestos", false);

        Region sep = new Region();
        sep.getStyleClass().add("totales-sep");
        totalesBox.getChildren().add(sep);

        lblTotalVal = buildTotalRow(totalesBox, "TOTAL", true);

        body.getChildren().addAll(meta, lblItemsTitle, tablaItems, lblTotalesTitle, totalesBox);

        ScrollPane scrollBody = new ScrollPane(body);
        scrollBody.getStyleClass().add("detalle-scroll");
        scrollBody.setFitToWidth(true);
        VBox.setVgrow(scrollBody, Priority.ALWAYS);

        panel.getChildren().addAll(header, scrollBody);
        return panel;
    }

    private Label buildTotalRow(VBox parent, String labelText, boolean isMain) {
        HBox row = new HBox();
        row.getStyleClass().add(isMain ? "total-row-main" : "total-row");
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add(isMain ? "total-label-main" : "total-label");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label val = new Label("$0.00");
        val.getStyleClass().add(isMain ? "total-value-main" : "total-value");
        row.getChildren().addAll(lbl, spacer, val);
        parent.getChildren().add(row);
        return val;
    }

    // ── Abrir / cerrar detalle ────────────────────────────────────────
    private void abrirDetalle(Factura factura , VBox card) {
        Cliente cliente = this.clienteController.getClienteById(factura.getClienteId());

        if (cardActiva != null) cardActiva.getStyleClass().remove("factura-card-active");
        cardActiva = card;
        card.getStyleClass().add("factura-card-active");

        lblDetalleTitulo.setText("Factura #" + String.format("%05d", factura.getId()));
        lblDetalleCliente.setText(cliente.getNombre());
        lblDetalleFecha.setText(factura.getFecha().format(FORMATTER));

        productoProveedorMap = new HashMap<>();
        for (ProductFactura item : factura.getDetalles()) {
            Producto prod = stockController.getStockById(item.getProductoid());
            if (prod != null) {
                Proveedores prov = proveedoreController.getProveedoreById(prod.getProveedorId());
                productoProveedorMap.put(prod.getId(), prov != null ? prov.getNombre() : "N/A");
            }
        }

        tablaItems.setItems(FXCollections.observableArrayList(factura.getDetalles()));
        tablaItems.setPrefHeight(Math.min(factura.getDetalles().size() * 36 + 36, 300));

        double subtotal = factura.getSubtotal();
        double descuento = factura.getDescuento();
        double descPct = subtotal > 0 ? (descuento / subtotal * 100) : 0;

        lblSubtotalVal.setText(formatMoney(subtotal));
        lblDescuentoVal.setText("-" + formatMoney(descuento) + " (" + String.format("%.0f", descPct) + "%)");
        lblImpuestosVal.setText(formatMoney(factura.getImpuestos()) + " (21%)");
        lblTotalVal.setText(formatMoney(factura.getTotal()));

        panelDetalle.setVisible(true);
        panelDetalle.setManaged(true);
    }

    private void cerrarDetalle() {
        panelDetalle.setVisible(false);
        panelDetalle.setManaged(false);
        if (cardActiva != null) {
            cardActiva.getStyleClass().remove("factura-card-active");
            cardActiva = null;
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────
    private String formatMoney(double value) {
        return String.format("$%,.2f", value);
    }

    private ObservableList<Factura> cargarFacturas() {
        return FXCollections.observableArrayList(
            this.facturaController.getFactura()
        );
    }

    private ObservableList<Factura> cargarProductFacturas(ObservableList<Factura> facturas) {
        ObservableList<Factura> nuevasFacturas = FXCollections.observableArrayList();
        for (Factura factura : facturas) {
            // .strem(): Genera una instancia, de una coleccion, para mapear los datos y permite modificar toda el contenido
            List<ProductFactura> newDetails = this.productFController.getDetailFactura(factura)
            .stream() // isntancia
            .map(details -> {
                Producto item = this.stockController.getStockById(details.getProductoid());
                return details.toBuilder()
                .name(item.getName())
                .build();
            }) // operacion
            .toList(); // operacion terminal

            Factura newFactura = factura.toBuilder()
            .detalles(newDetails)
            .build();

            nuevasFacturas.add(newFactura);
        }
        return nuevasFacturas;
    }

    public void recargarFacturas() {
        ObservableList<Factura> facturas = cargarProductFacturas(cargarFacturas());

        flowPane.getChildren().clear();
        facturas.forEach(f -> flowPane.getChildren().add(buildCard(f)));
    }
}