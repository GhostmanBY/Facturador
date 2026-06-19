package com.facturador.view.Tabs;

import java.util.List;
import java.util.Optional;

import com.facturador.controller.ClienteController;
import com.facturador.controller.FacturaController;
import com.facturador.controller.ProveedoreController;
import com.facturador.controller.UserController;
import com.facturador.model.Cliente;
import com.facturador.model.Proveedores;
import com.facturador.model.User;
import com.facturador.model.User.UserRole;
import com.facturador.view.Dialog.DialogNuevoCliente;
import com.facturador.view.Dialog.DialogNuevoProveedor;
import com.facturador.view.Dialog.DialogNuevoUsuario;
import com.facturador.view.Helpers.ErrorAlert;
import com.facturador.service.AuthServices;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TabAdministracion {
    private Label totalFacturasLabel;
    private Label totalUsuariosLabel;
    private Label totalClientesLabel;
    private Label totalProveedoresLabel;
    private TableView<User> tabla_user;
    TableView<Cliente> tabla_cliente;
    private TableView<Proveedores> tabla_proveedor;
    private UserController userController;
    private ClienteController clienteController;
    private ProveedoreController proveedoreController;
    private FacturaController facturaController;
    private ErrorAlert alert = new ErrorAlert();
    private AuthServices authServices = AuthServices.getInstance();

    public TabAdministracion() {
        this.userController = new UserController();
        this.clienteController = new ClienteController();
        this.proveedoreController = new ProveedoreController();
        this.facturaController = new FacturaController();
    }

    public Parent buildAdministracionTab() {
        VBox root = new VBox(16);
        root.getStyleClass().add("admin-root");
        root.setPadding(new Insets(20));

        // ── Stat total facturado ──────────────────────────────────────
        // ── Stats ─────────────────────────────────────────────────────
        int totalFacturas = facturaController.getFactura().size();
        int totalUsuarios = userController.getUser().size();
        int totalClientes = clienteController.getClientes().size();
        int totalProveedores = proveedoreController.getProveedores().size();

        totalFacturasLabel = new Label(String.valueOf(totalFacturas));
        totalUsuariosLabel = new Label(String.valueOf(totalUsuarios));
        totalClientesLabel = new Label(String.valueOf(totalClientes));
        totalProveedoresLabel = new Label(String.valueOf(totalProveedores));

        HBox statsRow = new HBox(12);
        statsRow.getStyleClass().add("admin-stats-row");

        statsRow.getChildren().addAll(
            buildStatBox("TOTAL FACTURAS",    totalFacturasLabel,     "admin-stat-value"),
            buildStatBox("TOTAL USUARIOS",    totalUsuariosLabel,     "admin-stat-value-blue"),
            buildStatBox("TOTAL CLIENTES",    totalClientesLabel,     "admin-stat-value-amber"),
            buildStatBox("TOTAL PROVEEDORES", totalProveedoresLabel,  "admin-stat-value-purple")
        );

        // ── Tablas ────────────────────────────────────────────────────
        VBox tablesContainer = new VBox(12);
        VBox.setVgrow(tablesContainer, Priority.ALWAYS);

        HBox row1 = new HBox(12);
        VBox panelUsuarios = buildPanelUsuarios();
        VBox panelClientes = buildPanelClientes();
        HBox.setHgrow(panelUsuarios, Priority.ALWAYS);
        HBox.setHgrow(panelClientes, Priority.ALWAYS);
        row1.getChildren().addAll(panelUsuarios, panelClientes);

        VBox panelProveedores = buildPanelProveedores();

        tablesContainer.getChildren().addAll(row1, panelProveedores);
        root.getChildren().addAll(statsRow, tablesContainer);
        return root;
    }

    private VBox buildStatBox(String etiqueta, Label labelValor, String estiloValor) {
        VBox contenedor = new VBox(4);
        contenedor.getStyleClass().add("admin-stat");
        HBox.setHgrow(contenedor, Priority.ALWAYS);

        Label labelEtiqueta = new Label(etiqueta);
        labelEtiqueta.getStyleClass().add("admin-stat-label");
        labelValor.getStyleClass().add(estiloValor);

        contenedor.getChildren().addAll(labelEtiqueta, labelValor);
        return contenedor;
    }

    // ── Panel Usuarios ────────────────────────────────────────────────
    private VBox buildPanelUsuarios() {
        ObservableList<User> usuarios = FXCollections.observableArrayList(userController.getUser());

        VBox panel = new VBox(0);
        panel.getStyleClass().add("admin-panel");

        // Header
        HBox header = buildPanelHeader("Usuarios", () -> {
            DialogNuevoUsuario dialogoNuevoUsuario = new DialogNuevoUsuario();
            Optional<User> nuevoUsuario = dialogoNuevoUsuario.mostrarDialogo();
            nuevoUsuario.ifPresent(usuario -> {
                userController.createUser(usuario);
                usuarios.setAll(userController.getUser());
            });
        });

        // Tabla
        tabla_user = new TableView<>(usuarios);
        tabla_user.getStyleClass().add("admin-table");
        tabla_user.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(tabla_user, Priority.ALWAYS);

        TableColumn<User, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(new PropertyValueFactory<>("role"));
        colRol.setPrefWidth(90);

        TableColumn<User, Boolean> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("active"));
        colEstado.setPrefWidth(80);
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean active, boolean empty) {
                super.updateItem(active, empty);
                if (empty || active == null) { setGraphic(null); return; }
                Label badge = new Label(active ? "Activo" : "Inactivo");
                badge.getStyleClass().add(active ? "badge-active" : "badge-inactive");
                setGraphic(badge);
                setText(null);
            }
        });

        TableColumn<User, Void> colAcciones = new TableColumn<>("");
        colAcciones.setPrefWidth(110);
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnToggle = new Button();
            private final HBox box = new HBox(6, btnEditar, btnToggle);
            
            {
                btnEditar.getStyleClass().add("table-btn-edit");
                btnToggle.getStyleClass().add("table-btn-toggle");
                box.setAlignment(Pos.CENTER);

                btnEditar.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    DialogNuevoUsuario editar = new DialogNuevoUsuario(user); 
                    Optional<User> usuarioEditado = editar.mostrarDialogo();
                    usuarioEditado.ifPresent(usuario -> {
                        userController.modifyUser(usuario);

                        tabla_user.getItems().setAll(userController.getUser());
                    });
                });

                btnToggle.setOnAction(e -> {
                    User seleccionado = getTableView().getItems().get(getIndex());
                    User actual = authServices.getUserActual();

                    if (actual.getRole() == UserRole.GERENTE) {
                        if (seleccionado.getRole() == UserRole.ADMIN) {
                            alert.mostrarError("No puede deshabilitar administradores");
                            return;
                        }
                        if (seleccionado.getRole() == UserRole.GERENTE) {
                            alert.mostrarError("No puede deshabilitar usuarios con su mismo rol");
                            return;
                        }
                    }
                    if (seleccionado.isActive()) {
                        userController.desactivarUser(seleccionado);
                    } else {
                        userController.activarUser(seleccionado);
                    }
                    tabla_user.getItems().setAll(userController.getUser());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                User user = getTableView().getItems().get(getIndex());
                User actual = authServices.getUserActual();
                boolean puedeEditar = true;

                if (user.getRole() == UserRole.ADMIN &&
                    actual.getRole() == UserRole.ADMIN) {
                    puedeEditar = false;
                }
                if (user.getRole() == UserRole.ADMIN &&
                    actual.getRole() == UserRole.GERENTE) {
                    puedeEditar = false;
                }
                if (user.getRole() == UserRole.GERENTE &&
                    actual.getRole() == UserRole.GERENTE) {
                    puedeEditar = false;
                }
                btnEditar.setDisable(!puedeEditar);

                btnToggle.setText(user.isActive() ? "Desactivar" : "Activar");
                btnToggle.getStyleClass().removeAll("table-btn-deactivate", "table-btn-activate");
                btnToggle.getStyleClass().add(user.isActive() ? "table-btn-deactivate" : "table-btn-activate");
                setGraphic(box);
            }
        });

        tabla_user.getColumns().add(colNombre);
        tabla_user.getColumns().add(colRol);
        tabla_user.getColumns().add(colEstado);
        tabla_user.getColumns().add(colAcciones);
        panel.getChildren().add(header);
        panel.getChildren().add(tabla_user);
        return panel;
    }

    // ── Panel Clientes ────────────────────────────────────────────────
    private VBox buildPanelClientes() {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList(clienteController.getClientes());

        VBox panel = new VBox(0);
        panel.getStyleClass().add("admin-panel");

        HBox header = buildPanelHeader("Clientes", () -> {
            DialogNuevoCliente dialogoNuevoUsuario = new DialogNuevoCliente();
            Optional<Cliente> nuevoCliente = dialogoNuevoUsuario.mostrarDialogo();
            nuevoCliente.ifPresent(cliente -> {
                clienteController.createCliente(cliente);
                clientes.setAll(clienteController.getClientes());
            });
        });

        tabla_cliente = new TableView<>(clientes);
        tabla_cliente.getStyleClass().add("admin-table");
        tabla_cliente.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(tabla_cliente, Priority.ALWAYS);

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> colDocumento = new TableColumn<>("Documento");
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colDocumento.setPrefWidth(110);

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colTelefono.setPrefWidth(100);

        TableColumn<Cliente, Boolean> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("active"));
        colEstado.setPrefWidth(80);
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean active, boolean empty) {
                super.updateItem(active, empty);
                if (empty || active == null) { setGraphic(null); return; }
                Label badge = new Label(active ? "Activo" : "Inactivo");
                badge.getStyleClass().add(active ? "badge-active" : "badge-inactive");
                setGraphic(badge);
                setText(null);
            }
        });

        TableColumn<Cliente, Void> colAcciones = new TableColumn<>("");
        colAcciones.setPrefWidth(110);
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnToggle = new Button();
            private final HBox box = new HBox(6, btnEditar, btnToggle);

            {
                btnEditar.getStyleClass().add("table-btn-edit");
                btnToggle.getStyleClass().add("table-btn-toggle");
                box.setAlignment(Pos.CENTER);

                btnEditar.setOnAction(e -> {
                    Cliente clientes = getTableView().getItems().get(getIndex());
                    DialogNuevoCliente editar = new DialogNuevoCliente(clientes); 
                    Optional<Cliente> clienteEditado = editar.mostrarDialogo();
                    clienteEditado.ifPresent(cliente -> {
                        clienteController.updateCliente(cliente);

                        tabla_cliente.getItems().setAll(clienteController.getClientes());
                    });
                });

                btnToggle.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    if (cliente.isActive()) {
                        clienteController.desactivarCliente(cliente);
                    } else {
                        clienteController.activarCliente(cliente);
                    }
                    tabla_cliente.getItems().setAll(clienteController.getClientes());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Cliente c = getTableView().getItems().get(getIndex());
                btnToggle.setText(c.isActive() ? "Desactivar" : "Activar");
                btnToggle.getStyleClass().removeAll("table-btn-deactivate", "table-btn-activate");
                btnToggle.getStyleClass().add(c.isActive() ? "table-btn-deactivate" : "table-btn-activate");
                setGraphic(box);
            }
        });

        tabla_cliente.getColumns().add(colNombre);
        tabla_cliente.getColumns().add(colDocumento);
        tabla_cliente.getColumns().add(colTelefono);
        tabla_cliente.getColumns().add(colEstado);
        tabla_cliente.getColumns().add(colAcciones);
        panel.getChildren().add(header);
        panel.getChildren().add(tabla_cliente);
        return panel;
    }

    // ── Panel Proveedores ──────────────────────────────────────────────
    private VBox buildPanelProveedores() {
        ObservableList<Proveedores> proveedores = FXCollections.observableArrayList(proveedoreController.getProveedores());

        VBox panel = new VBox(0);
        panel.getStyleClass().add("admin-panel");

        HBox header = buildPanelHeader("Proveedores", () -> {
            DialogNuevoProveedor dialogo = new DialogNuevoProveedor();
            Optional<Proveedores> nuevo = dialogo.mostrarDialogo();
            nuevo.ifPresent(p -> {
                proveedores.setAll(proveedoreController.getProveedores());
            });
        });

        tabla_proveedor = new TableView<>(proveedores);
        tabla_proveedor.getStyleClass().add("admin-table");
        tabla_proveedor.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(tabla_proveedor, Priority.ALWAYS);

        TableColumn<Proveedores, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Proveedores, String> colCuit = new TableColumn<>("CUIT");
        colCuit.setCellValueFactory(new PropertyValueFactory<>("cuit"));
        colCuit.setPrefWidth(120);

        TableColumn<Proveedores, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colTelefono.setPrefWidth(100);

        TableColumn<Proveedores, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(150);

        TableColumn<Proveedores, Boolean> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("active"));
        colEstado.setPrefWidth(80);
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean active, boolean empty) {
                super.updateItem(active, empty);
                if (empty || active == null) { setGraphic(null); return; }
                Label badge = new Label(active ? "Activo" : "Inactivo");
                badge.getStyleClass().add(active ? "badge-active" : "badge-inactive");
                setGraphic(badge);
                setText(null);
            }
        });

        TableColumn<Proveedores, Void> colAcciones = new TableColumn<>("");
        colAcciones.setPrefWidth(110);
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnToggle = new Button();
            private final HBox box = new HBox(6, btnEditar, btnToggle);

            {
                btnEditar.getStyleClass().add("table-btn-edit");
                btnToggle.getStyleClass().add("table-btn-toggle");
                box.setAlignment(Pos.CENTER);

                btnEditar.setOnAction(e -> {
                    Proveedores p = getTableView().getItems().get(getIndex());
                    DialogNuevoProveedor editar = new DialogNuevoProveedor(p);
                    Optional<Proveedores> editado = editar.mostrarDialogo();
                    editado.ifPresent(pr -> {
                        tabla_proveedor.getItems().setAll(proveedoreController.getProveedores());
                    });
                });

                btnToggle.setOnAction(e -> {
                    Proveedores p = getTableView().getItems().get(getIndex());
                    if (p.isActive()) {
                        proveedoreController.desactivarProveedore(p);
                    } else {
                        proveedoreController.activarProveedore(p);
                    }
                    tabla_proveedor.getItems().setAll(proveedoreController.getProveedores());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Proveedores p = getTableView().getItems().get(getIndex());
                btnToggle.setText(p.isActive() ? "Desactivar" : "Activar");
                btnToggle.getStyleClass().removeAll("table-btn-deactivate", "table-btn-activate");
                btnToggle.getStyleClass().add(p.isActive() ? "table-btn-deactivate" : "table-btn-activate");
                setGraphic(box);
            }
        });

        tabla_proveedor.getColumns().add(colNombre);
        tabla_proveedor.getColumns().add(colCuit);
        tabla_proveedor.getColumns().add(colTelefono);
        tabla_proveedor.getColumns().add(colEmail);
        tabla_proveedor.getColumns().add(colEstado);
        tabla_proveedor.getColumns().add(colAcciones);
        panel.getChildren().add(header);
        panel.getChildren().add(tabla_proveedor);
        return panel;
    }

    // ── Helper header de panel ────────────────────────────────────────
    private HBox buildPanelHeader(String titulo, Runnable onAgregar) {
        HBox header = new HBox();
        header.getStyleClass().add("admin-panel-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("admin-panel-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAgregar = new Button("+ Agregar");
        btnAgregar.getStyleClass().add("btn-primary");
        btnAgregar.setOnAction(e -> onAgregar.run());

        header.getChildren().addAll(lblTitulo, spacer, btnAgregar);
        return header;
    }

    public void recargarData() {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList(this.clienteController.getClientes());
        tabla_cliente.setItems(clientes);

        ObservableList<User> users = FXCollections.observableArrayList(this.userController.getUser());
        tabla_user.setItems(users);

        ObservableList<Proveedores> proveedores = FXCollections.observableArrayList(this.proveedoreController.getProveedores());
        tabla_proveedor.setItems(proveedores);
        
        int totalFacturas = facturaController.getFactura().size();
        int totalUsuarios = userController.getUser().size();
        int totalClientes = clienteController.getClientes().size();
        int totalProveedores = proveedoreController.getProveedores().size();

        totalFacturasLabel.setText(String.valueOf(totalFacturas));
        totalUsuariosLabel.setText(String.valueOf(totalUsuarios));
        totalClientesLabel.setText(String.valueOf(totalClientes));
        totalProveedoresLabel.setText(String.valueOf(totalProveedores));
    }
}