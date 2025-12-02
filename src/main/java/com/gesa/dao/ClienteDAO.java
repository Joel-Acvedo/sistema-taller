package com.gesa.dao;

import com.gesa.db.Conexion;
import com.gesa.models.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // LISTAR (Solo los vivos)
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE eliminado = FALSE ORDER BY nombre";

        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setApellidos(rs.getString("apellidos"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setRfc(rs.getString("rfc"));
                // Datos Fiscales
                c.setRazonSocialFiscal(rs.getString("razon_social_fiscal"));
                c.setRegimenFiscal(rs.getString("regimen_fiscal"));
                c.setCpFiscal(rs.getString("cp_fiscal"));
                c.setUsoCfdi(rs.getString("uso_cfdi"));
                c.setEmailFacturacion(rs.getString("email_facturacion"));

                lista.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // GUARDAR (INSERT)
    public boolean guardar(Cliente c) {
        String sql = "INSERT INTO clientes (nombre, apellidos, telefono, email, rfc, razon_social_fiscal, regimen_fiscal, cp_fiscal, uso_cfdi, email_facturacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getApellidos());
            stmt.setString(3, c.getTelefono());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getRfc());
            stmt.setString(6, c.getRazonSocialFiscal());
            stmt.setString(7, c.getRegimenFiscal());
            stmt.setString(8, c.getCpFiscal());
            stmt.setString(9, c.getUsoCfdi());
            stmt.setString(10, c.getEmailFacturacion());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // EDITAR (UPDATE)
    public boolean editar(Cliente c) {
        String sql = "UPDATE clientes SET nombre=?, apellidos=?, telefono=?, email=?, rfc=?, razon_social_fiscal=?, regimen_fiscal=?, cp_fiscal=?, uso_cfdi=?, email_facturacion=? WHERE id=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getApellidos());
            stmt.setString(3, c.getTelefono());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getRfc());
            stmt.setString(6, c.getRazonSocialFiscal());
            stmt.setString(7, c.getRegimenFiscal());
            stmt.setString(8, c.getCpFiscal());
            stmt.setString(9, c.getUsoCfdi());
            stmt.setString(10, c.getEmailFacturacion());
            stmt.setInt(11, c.getId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ELIMINAR (SOFT DELETE)
    public boolean eliminar(int id) {
        String sql = "UPDATE clientes SET eliminado = TRUE WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}