package com.gesa.dao;

import com.gesa.db.Conexion;
import com.gesa.models.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario login(String username, String password) {
        // Buscamos usuario activo que coincida en nombre y pass
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password_hash = ? AND activo = TRUE";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // NOTA: En prod deberíamos encriptar esto

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre_completo"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("rol")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en Login: " + e.getMessage());
        }
        return null; // Si retorna null, el login falló
    }
}