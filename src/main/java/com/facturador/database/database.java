package com.facturador.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database {
    public static final Dotenv dotenv = Dotenv.load();

    public Connection connect() throws SQLException {
        String host = dotenv.get("DB_HOST");
        String port = dotenv.get("DB_PORT");
        String db = dotenv.get("DB_NAME");
        String user = dotenv.get("DB_USER");
        String pass = dotenv.get("DB_PASSWORD");
        
        String url = "jdbc:mariadb://" + host + ":" + port + "/" + db;

        return DriverManager.getConnection(url, user, pass);
    }

    public Connection connectSinBase() throws SQLException {
        String host = dotenv.get("DB_HOST");
        String port = dotenv.get("DB_PORT");
        String usuario = dotenv.get("DB_USER");
        String contrasena = dotenv.get("DB_PASSWORD");
        String url = "jdbc:mariadb://" + host + ":" + port + "/";
        return DriverManager.getConnection(url, usuario, contrasena);
    }
}
