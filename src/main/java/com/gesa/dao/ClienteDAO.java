package com.gesa.dao;

import com.gesa.db.Conexion;
import com.gesa.models.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // MÉTODO PARA TRAER TODOS (SELECT)
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, telefono, email, rfc FROM clientes ORDER BY id DESC";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setRfc(rs.getString("rfc"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println(" Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    // MÉTODO PARA GUARDAR NUEVO (INSERT)
    public boolean guardar(Cliente c) {
        String sql = "INSERT INTO clientes (nombre, telefono, email, rfc, creado_por_id) VALUES (?, ?, ?, ?, 1)"; 
        // Nota: Puse '1' fijo en creado_por_id porque aun no tenemos login activo.

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getTelefono());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getRfc());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println(" Error al guardar cliente: " + e.getMessage());
            return false;
        }
    }
    // MÉTODO PARA ACTUALIZAR (UPDATE)
    public boolean editar(Cliente c) {
        // Actualizamos datos y registramos que el usuario 1 lo modificó
        String sql = "UPDATE clientes SET nombre=?, telefono=?, email=?, rfc=?, modificado_por_id=1, modificado_en=NOW() WHERE id=?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getTelefono());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getRfc());
            stmt.setInt(5, c.getId()); // IMPORTANTE: El ID nos dice a quién actualizar

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println(" Error al editar cliente: " + e.getMessage());
            return false;
        }
    }
}