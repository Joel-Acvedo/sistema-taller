package com.gesa.dao;

import com.gesa.db.Conexion;
import com.gesa.models.Refaccion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RefaccionDAO {

    public List<Refaccion> listar() {
        List<Refaccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM refacciones ORDER BY nombre";

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
                r.setPrecioVenta(rs.getDouble("precio_venta_sugerido")); // Dato calculado en BD
                lista.add(r);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

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
            e.printStackTrace();
            return false;
        }
    }
}