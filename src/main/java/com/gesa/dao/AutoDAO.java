package com.gesa.dao;

import com.gesa.db.Conexion;
import com.gesa.models.Auto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutoDAO {

    // 1. LISTAR POR CLIENTE
    public List<Auto> listarPorCliente(int clienteId) {
        List<Auto> lista = new ArrayList<>();
        // Agregamos "color" y "kilometraje" a la consulta
        String sql = "SELECT * FROM autos WHERE cliente_id = ? AND eliminado = FALSE ORDER BY marca, modelo";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Auto v = new Auto();
                v.setId(rs.getInt("id"));
                v.setClienteId(rs.getInt("cliente_id"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));
                v.setAnio(rs.getInt("ano"));
                v.setColor(rs.getString("color"));
                v.setPlacas(rs.getString("placas"));
                v.setKilometraje(rs.getInt("kilometraje")); // <--- ¡AQUÍ ESTÁ!
                v.setMotorCategoria(rs.getString("motor_categoria"));
                v.setCombustible(rs.getString("combustible"));
                v.setVin(rs.getString("vin"));

                // Fechas (pueden ser nulas)
                if(rs.getDate("fecha_ultimo_servicio") != null)
                    v.setFechaUltimoServicio(rs.getDate("fecha_ultimo_servicio").toLocalDate());

                if(rs.getDate("fecha_proximo_servicio") != null)
                    v.setFechaProximoServicio(rs.getDate("fecha_proximo_servicio").toLocalDate());

                lista.add(v);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar autos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // 2. GUARDAR (INSERT)
    public boolean guardar(Auto v) {
        // Agregamos "kilometraje" al INSERT
        String sql = "INSERT INTO autos (cliente_id, marca, modelo, ano, color, placas, kilometraje, motor_categoria, combustible, vin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, v.getClienteId());
            stmt.setString(2, v.getMarca());
            stmt.setString(3, v.getModelo());
            stmt.setInt(4, v.getAnio());
            stmt.setString(5, v.getColor());
            stmt.setString(6, v.getPlacas());
            stmt.setInt(7, v.getKilometraje()); // <--- ¡AQUÍ TAMBIÉN!
            stmt.setString(8, v.getMotorCategoria());
            stmt.setString(9, v.getCombustible());
            stmt.setString(10, v.getVin());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar auto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 3. EDITAR (UPDATE)
    public boolean editar(Auto v) {
        // Agregamos "kilometraje" al UPDATE
        String sql = "UPDATE autos SET marca=?, modelo=?, ano=?, color=?, placas=?, kilometraje=?, motor_categoria=?, combustible=?, vin=? WHERE id=?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, v.getMarca());
            stmt.setString(2, v.getModelo());
            stmt.setInt(3, v.getAnio());
            stmt.setString(4, v.getColor());
            stmt.setString(5, v.getPlacas());
            stmt.setInt(6, v.getKilometraje()); // <--- ¡AQUÍ TAMBIÉN!
            stmt.setString(7, v.getMotorCategoria());
            stmt.setString(8, v.getCombustible());
            stmt.setString(9, v.getVin());
            stmt.setInt(10, v.getId()); // El ID va al final

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al editar auto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 4. ELIMINAR (SOFT DELETE)
    public boolean eliminar(int id) {
        String sql = "UPDATE autos SET eliminado = TRUE WHERE id = ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar auto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}