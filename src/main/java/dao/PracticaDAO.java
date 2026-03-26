package dao;

import modelo.Practica;
import modelo.Alumno;
import util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PracticaDAO {

    public Practica obtenerPorAlumno(int alumnoId) {
        String sql = "SELECT * FROM practica WHERE alumno_id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, alumnoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Practica p = new Practica();
                p.setId(rs.getInt("id"));
                p.setEstado(rs.getString("estado"));
                Alumno a = new Alumno();
                a.setId(alumnoId);
                p.setAlumno(a);
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Practica obtenerPorTutorEmpresa(int tutorId) {
        String sql = "SELECT * FROM practica WHERE tutor_empresa_id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tutorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Practica p = new Practica();
                p.setId(rs.getInt("id"));
                p.setEstado(rs.getString("estado"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Practica obtenerPorTutorCentro(int tutorId) {
        String sql = "SELECT * FROM practica WHERE tutor_centro_id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tutorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Practica p = new Practica();
                p.setId(rs.getInt("id"));
                p.setEstado(rs.getString("estado"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}