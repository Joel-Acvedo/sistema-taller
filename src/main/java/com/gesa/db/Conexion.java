package com.gesa.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // 1. Datos de tu Base de Datos (Docker)
    private static final String URL = "jdbc:postgresql://localhost:5432/puntoventa_dv";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Noviembre0511";

    // 2. Método para obtener la conexión (La Llave)
    public static Connection getConexion() {
        try {
            // Cargar el driver (opcional en versiones nuevas, pero buena práctica)
            Class.forName("org.postgresql.Driver");
            
            // Intentar conectar
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
            
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ ERROR CRÍTICO: No se pudo conectar a la BD");
            e.printStackTrace();
            return null;
        }
    }

    // 3. PRUEBA RÁPIDA (Solo para ver si jala ahorita)
    public static void main(String[] args) {
        System.out.println("🔄 Intentando conectar a PostgreSQL...");
        
        Connection test = getConexion();
        
        if (test != null) {
            System.out.println("✅ ¡ÉXITO! Conexión establecida correctamente.");
            System.out.println("🚀 Estamos listos para despegar, capitán.");
        } else {
            System.out.println("💥 FALLÓ. Revisa que tu Docker esté prendido.");
        }
    }
}