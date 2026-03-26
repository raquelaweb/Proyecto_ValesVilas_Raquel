package dao;

import modelo.Evaluacion;
import modelo.Practica;
import modelo.Tutor;
import util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EvaluacionDAO {

	public boolean crear(Evaluacion e) {
		String sql = "INSERT INTO evaluacion (practica_id, tutor_id, nota, comentarios) VALUES (?, ?, ?, ?)";
		try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, e.getPractica().getId());
			ps.setInt(2, e.getTutor().getId());
			ps.setFloat(3, e.getNota());
			ps.setString(4, e.getComentarios());
			return ps.executeUpdate() > 0;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public List<Evaluacion> listarPorPractica(int practicaId) {
		List<Evaluacion> lista = new ArrayList<>();
		String sql = "SELECT e.*, u.nombre AS tutor_nombre FROM evaluacion e " + "JOIN tutor t ON e.tutor_id = t.id "
				+ "JOIN usuario u ON t.id = u.id " + "WHERE e.practica_id = ?";
		try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, practicaId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Evaluacion ev = new Evaluacion();
				ev.setId(rs.getInt("id"));
				ev.setNota(rs.getFloat("nota"));
				ev.setComentarios(rs.getString("comentarios"));
				Practica p = new Practica();
				p.setId(practicaId);
				ev.setPractica(p);
				Tutor t = new Tutor();
				t.setId(rs.getInt("tutor_id"));
				t.setNombre(rs.getString("tutor_nombre"));
				ev.setTutor(t);
				lista.add(ev);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
}