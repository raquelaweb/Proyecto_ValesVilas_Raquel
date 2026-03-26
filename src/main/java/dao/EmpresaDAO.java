package dao;

import modelo.Empresa;
import util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

    public List<Empresa> listarTodas() {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT * FROM empresa";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearEmpresa(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean crear(Empresa e) {
        String sql = "INSERT INTO empresa (nombre, cif, direccion, contacto, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCif());
            ps.setString(3, e.getDireccion());
            ps.setString(4, e.getContacto());
            ps.setString(5, e.getEmail());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean actualizar(Empresa e) {
        String sql = "UPDATE empresa SET nombre=?, cif=?, direccion=?, contacto=?, email=? WHERE id=?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCif());
            ps.setString(3, e.getDireccion());
            ps.setString(4, e.getContacto());
            ps.setString(5, e.getEmail());
            ps.setInt(6, e.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM empresa WHERE id=?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Empresa mapearEmpresa(ResultSet rs) throws Exception {
        Empresa e = new Empresa();
        e.setId(rs.getInt("id"));
        e.setNombre(rs.getString("nombre"));
        e.setCif(rs.getString("cif"));
        e.setDireccion(rs.getString("direccion"));
        e.setContacto(rs.getString("contacto"));
        e.setEmail(rs.getString("email"));
        return e;
    }
}