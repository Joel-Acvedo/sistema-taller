package com.gesa.dao;

import com.gesa.db.Conexion;
import com.gesa.models.Vehiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    // Listar autos de un cliente específico
    public List<Vehiculo> listarPorCliente(int clienteId) {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM autos WHERE cliente_id = ? AND eliminado = FALSE";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehiculo v = new Vehiculo();
                v.setId(rs.getInt("id"));
                v.setClienteId(rs.getInt("cliente_id"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));
                v.setAnio(rs.getInt("ano")); // Ojo: en BD es "ano"
                v.setPlacas(rs.getString("placas"));
                v.setMotorCategoria(rs.getString("motor_categoria"));
                v.setCombustible(rs.getString("combustible"));
                v.setVin(rs.getString("vin"));

                // Fechas (pueden ser nulas)
                if(rs.getDate("fecha_ultimo_servicio") != null)
                    v.setFechaUltimoServicio(rs.getDate("fecha_ultimo_servicio").toLocalDate());

                lista.add(v);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean guardar(Vehiculo v) {
        String sql = "INSERT INTO autos (cliente_id, marca, modelo, ano, placas, motor_categoria, combustible, vin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, v.getClienteId());
            stmt.setString(2, v.getMarca());
            stmt.setString(3, v.getModelo());
            stmt.setInt(4, v.getAnio());
            stmt.setString(5, v.getPlacas());
            stmt.setString(6, v.getMotorCategoria());
            stmt.setString(7, v.getCombustible());
            stmt.setString(8, v.getVin());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Aquí podrías agregar editar() y eliminar() siguiendo el patrón del ClienteDAO
}