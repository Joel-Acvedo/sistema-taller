package com.gesa.dao;

import com.gesa.db.Conexion;
import com.gesa.models.Cotizacion;
import com.gesa.models.DetalleCotizacion;
import java.sql.*;
import java.util.List;

public class CotizacionDAO {

    // GUARDAR COMPLETO (Encabezado + Detalles)
    public boolean guardarCotizacion(Cotizacion cotizacion, List<DetalleCotizacion> detalles) {
        String sqlEncabezado = "INSERT INTO cotizaciones (cliente_id, auto_id, subtotal, iva_total, gran_total) VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalles_cotizacion (cotizacion_id, tipo, descripcion, precio_unitario_sin_iva, cantidad, total_renglon) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = Conexion.getConexion();
            conn.setAutoCommit(false); // ⚠️ INICIO DE TRANSACCIÓN (Si falla algo, no se guarda nada)

            // 1. Guardar Encabezado
            PreparedStatement stmtC = conn.prepareStatement(sqlEncabezado, Statement.RETURN_GENERATED_KEYS);
            stmtC.setInt(1, cotizacion.getClienteId());
            stmtC.setInt(2, cotizacion.getVehiculoId());
            stmtC.setDouble(3, cotizacion.getTotal()); // Asumiendo que guardamos el total
            stmtC.setDouble(4, cotizacion.getTotal() * 0.16);
            stmtC.setDouble(5, cotizacion.getTotal() * 1.16);

            stmtC.executeUpdate();

            // Obtener el ID generado (ej. Folio 50)
            ResultSet rs = stmtC.getGeneratedKeys();
            int idCotizacion = 0;
            if (rs.next()) {
                idCotizacion = rs.getInt(1);
            }

            // 2. Guardar Detalles
            PreparedStatement stmtD = conn.prepareStatement(sqlDetalle);
            for (DetalleCotizacion det : detalles) {
                stmtD.setInt(1, idCotizacion);
                stmtD.setString(2, "Refaccion"); // O Servicio
                stmtD.setString(3, det.getDescripcion());
                stmtD.setDouble(4, det.getPrecioUnitario());
                stmtD.setInt(5, det.getCantidad());
                stmtD.setDouble(6, det.getImporte());
                stmtD.addBatch(); // Agregamos al lote
            }
            stmtD.executeBatch(); // Ejecutamos todos los insert de detalles

            conn.commit(); // ✅ CONFIRMAR CAMBIOS
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // ❌ SI FALLA, DESHACER TODO
            } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // Restaurar estado
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}