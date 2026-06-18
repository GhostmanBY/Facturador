package com.facturador.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import com.facturador.view.Tabs.TabProductos;
import com.facturador.view.Tabs.TabGenFacturas;
import com.facturador.view.Tabs.TabViewFacturas;
import com.facturador.view.Tabs.TabAdministracion;

import com.facturador.model.User;
import com.facturador.model.User.UserRole;

public class MainView {
    private TabProductos tabProductos;
    private TabGenFacturas tabgenfacturas;
    private TabViewFacturas tabviewfacturas;
    private TabAdministracion tabadministracion;
    private VBox root;

    public MainView(Stage stage, User user) {
        this.tabProductos = new TabProductos();
        this.tabgenfacturas = new TabGenFacturas(user);
        this.tabviewfacturas = new TabViewFacturas();
        this.tabadministracion = new TabAdministracion();

        Label userName = new Label(user.getName());
        userName.getStyleClass().add("label-title");

        Label userRole = new Label(String.valueOf(user.getRole()));
        userRole.getStyleClass().add("label-subtitle");

        Button btn_logout = new Button("Cerrar Sesion");
        btn_logout.getStyleClass().add("button-danger");
        btn_logout.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            stage.getScene().setRoot(loginView.getView());
        });

        VBox userInfo = new VBox(userName, userRole);
        userInfo.setAlignment(Pos.CENTER_LEFT);

        VBox logout = new VBox(btn_logout);
        logout.setAlignment(Pos.CENTER_RIGHT);

        HBox topbar = new HBox(userInfo, logout);
        topbar.getStyleClass().add("topbar");
        topbar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(userInfo, Priority.ALWAYS);

        Tab tabProductos = new Tab("Productos");
        tabProductos.setClosable(false);
        tabProductos.setContent(this.tabProductos.buildProductosTab());
        tabProductos.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                this.tabProductos.recargarProductos();
            }
        });

        Tab tabgenfacturas = new Tab("Crear Facturas");
        tabgenfacturas.setClosable(false);
        tabgenfacturas.setContent(this.tabgenfacturas.buildFacturasTab());
        tabgenfacturas.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                this.tabgenfacturas.recargarProductos();
            }
        });


        Tab tabviewfacturas = new Tab("Ver Facturas");
        tabviewfacturas.setClosable(false);
        tabviewfacturas.setContent(this.tabviewfacturas.buildViewFacturasTab());
        tabviewfacturas.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                this.tabviewfacturas.recargarFacturas();
            }
        });

        Tab tabadministrador = new Tab("Panel Administrador");
        tabadministrador.setClosable(false);
        tabadministrador.setContent(this.tabadministracion.buildAdministracionTab());
        tabadministrador.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                this.tabadministracion.recargarData();
            }
        });

        TabPane tabPane = new TabPane();

        if (user.getRole() == UserRole.REPOSITOR || user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.GERENTE) {
            tabPane.getTabs().add(tabProductos);
        }
        
        if (user.getRole() == UserRole.CAJERO || user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.GERENTE) {
            tabPane.getTabs().add(tabgenfacturas);
            tabPane.getTabs().add(tabviewfacturas);
        }
        
        if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.GERENTE) {
            tabPane.getTabs().add(tabadministrador);
        }
        
        tabPane.getStyleClass().add("tab-pane");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        root = new VBox(topbar, tabPane);
        root.getStyleClass().add("root");
    }

    public Parent getView() {
        return root;
    }
}