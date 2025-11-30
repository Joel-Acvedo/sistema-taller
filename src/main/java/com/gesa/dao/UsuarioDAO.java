package com.gesa.dao;

import com.gesa.db.Conexion;
import com.gesa.models.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

////Clase no acabada , se tiene que trabajar mas despues
/// Esta clase se encarga de hacer las operaciones CRUD (Create, Read, Update, Delete) en la tabla de usuarios

public class UsuarioDAO {

    /**
     * Intenta iniciar sesión con usuario y contraseña.
     * @return Objeto Usuario si es correcto, o null si falló.
     */
    public Usuario validarLogin(String user, String pass) {
        Usuario usuarioEncontrado = null;
        
        // SQL: Buscamos un usuario que coincida y que esté ACTIVO
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password_hash = ? AND activo = TRUE";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Llenamos los huecos (?) con los datos que nos mandaron
            stmt.setString(1, user);
            stmt.setString(2, pass); // Nota: En producción, aquí deberíamos encriptar la contraseña antes de comparar

            ResultSet rs = stmt.executeQuery();

            // Si hay un resultado (rs.next() es verdadero), es que sí existe
            if (rs.next()) {
                usuarioEncontrado = new Usuario();
                usuarioEncontrado.setId(rs.getInt("id"));
                usuarioEncontrado.setNombreCompleto(rs.getString("nombre_completo"));
                usuarioEncontrado.setUsername(rs.getString("username"));
                usuarioEncontrado.setRol(rs.getString("rol"));
                // ¡ÉXITO! Encontramos al usuario
            }

        } catch (SQLException e) {
            System.err.println("Error al validar login: " + e.getMessage());
        }

        return usuarioEncontrado;
    }
}