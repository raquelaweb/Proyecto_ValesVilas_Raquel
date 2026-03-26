package dao;

import modelo.Seguimiento;
import modelo.Practica;
import util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SeguimientoDAO {

    // LISTAR SEGUIMIENTOS DE UNA PRACTICA
    public List<Seguimiento> listarPorPractica(int practicaId) {
        List<Seguimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM seguimiento WHERE practica_id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, practicaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Seguimiento s = new Seguimiento();
                s.setId(rs.getInt("id"));
                s.setFecha(rs.getDate("fecha"));
                s.setHoras(rs.getFloat("horas"));
                s.setDescripcion(rs.getString("descripcion"));
                s.setValidado(rs.getBoolean("validado"));
                Practica p = new Practica();
                p.setId(practicaId);
                s.setPractica(p);
                lista.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // REGISTRAR HORAS
    public boolean registrar(Seguimiento s) {
        String sql = "INSERT INTO seguimiento (practica_id, fecha, horas, descripcion, validado) VALUES (?, ?, ?, ?, false)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getPractica().getId());
            ps.setDate(2, new java.sql.Date(s.getFecha().getTime()));
            ps.setFloat(3, s.getHoras());
            ps.setString(4, s.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // VALIDAR HORAS
    public boolean validar(int id) {
        String sql = "UPDATE seguimiento SET validado = true WHERE id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}