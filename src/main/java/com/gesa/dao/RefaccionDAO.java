package com.gesa.dao;

import com.gesa.db.Conexion;
import com.gesa.models.Refaccion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RefaccionDAO {

    // 1. LISTAR (Solo las NO eliminadas)
    public List<Refaccion> listar() {
        List<Refaccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM refacciones WHERE eliminado = FALSE ORDER BY nombre";

        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Refaccion r = new Refaccion();
                r.setId(rs.getInt("id"));
                r.setCodigoInterno(rs.getString("codigo_interno"));
                r.setNombre(rs.getString("nombre"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setCostoProveedor(rs.getDouble("costo_proveedor"));
                r.setStockActual(rs.getInt("stock_actual"));

                // El precio venta es una columna calculada en la BD, solo la leemos
                r.setPrecioVenta(rs.getDouble("precio_venta_sugerido"));

                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar refacciones: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // 2. GUARDAR (INSERT)
    public boolean guardar(Refaccion r) {
        String sql = "INSERT INTO refacciones (codigo_interno, nombre, descripcion, costo_proveedor, stock_actual) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, r.getCodigoInterno());
            stmt.setString(2, r.getNombre());
            stmt.setString(3, r.getDescripcion());
            stmt.setDouble(4, r.getCostoProveedor());
            stmt.setInt(5, r.getStockActual());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar refacción: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 3. EDITAR (UPDATE)
    public boolean editar(Refaccion r) {
        String sql = "UPDATE refacciones SET codigo_interno=?, nombre=?, descripcion=?, costo_proveedor=?, stock_actual=? WHERE id=?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, r.getCodigoInterno());
            stmt.setString(2, r.getNombre());
            stmt.setString(3, r.getDescripcion());
            stmt.setDouble(4, r.getCostoProveedor());
            stmt.setInt(5, r.getStockActual());
            stmt.setInt(6, r.getId()); // Importante: el ID va al final en el WHERE

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al editar refacción: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 4. ELIMINAR (SOFT DELETE)
    public boolean eliminar(int id) {
        // No borramos el registro, solo lo marcamos como eliminado
        String sql = "UPDATE refacciones SET eliminado = TRUE WHERE id = ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar refacción: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}