package com.gesa.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PruebaConsulta {

    public static void main(String[] args) {
        System.out.println("🔎 Iniciando consulta de clientes...");

        // Definimos la consulta SQL (Queremos ver clientes y sus autos)
        String sql = "SELECT c.nombre, c.telefono, a.marca, a.modelo, a.placas " +
                     "FROM clientes c " +
                     "JOIN autos a ON c.id = a.cliente_id";

        // Usamos try-with-resources (cierra la conexión solo cuando termina)
        try (
            //Como funciona?
            // Primero llamamos la "api" que creamos , esa api nos da 2 valores , uno que es la llave si si conecta y otro que es null si no conecta
            //si recibimos la llave , se la ponemos a nuestra conection de nombre conn
            Connection conn = Conexion.getConexion();
            //El preparedStatement es como un controlador que nos ayuda a ejecutar la consulta SQL, es mandar una consulta o comando a la base de datos
            PreparedStatement stmt = conn.prepareStatement(sql);
            //despues guardamos el resultado de la consulta en un ResultSet de nombre rs
            // se guardan como lista 
            ResultSet rs = stmt.executeQuery()) {

            // Si la conexión falló y devolvió null, detenemos
            if (conn == null) return;
            System.out.println("\n--- RESULTADOS DE LA BD ---");
            
            // Recorremos las filas (mientras haya datos, sigue leyendo)
            while (rs.next()) {
                String cliente = rs.getString("nombre");
                String telefono = rs.getString("telefono");
                String auto = rs.getString("marca") + " " + rs.getString("modelo");
                String placas = rs.getString("placas");

                // Imprimimos bonito en la consola
                System.out.printf("👤 Cliente: %s (%s) | 🚗 Auto: %s [%s]\n", 
                                  cliente, telefono, auto, placas);
            }
            
            System.out.println("---------------------------\n");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar: " + e.getMessage());
        }
    }
}