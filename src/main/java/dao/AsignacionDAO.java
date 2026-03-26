package dao;

import util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO {

	public boolean asignarPractica(int alumnoId, int empresaId, int tutorEmpresaId, int tutorCentroId,
			String fechaInicio, String fechaFin) {
		String sql = "INSERT INTO practica (alumno_id, empresa_id, tutor_empresa_id, tutor_centro_id, "
				+ "fecha_inicio, fecha_fin, estado) VALUES (?, ?, ?, ?, ?, ?, 'ACTIVA')";
		try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, alumnoId);
			ps.setInt(2, empresaId);
			ps.setInt(3, tutorEmpresaId);
			ps.setInt(4, tutorCentroId);
			ps.setString(5, fechaInicio);
			ps.setString(6, fechaFin);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<String[]> listarAsignaciones() {
		List<String[]> lista = new ArrayList<>();
		String sql = "SELECT p.id, u.nombre AS alumno, e.nombre AS empresa, "
				+ "te.nombre AS tutor_empresa, tc.nombre AS tutor_centro, " + "p.fecha_inicio, p.fecha_fin, p.estado "
				+ "FROM practica p " + "JOIN alumno a ON p.alumno_id = a.id " + "JOIN usuario u ON a.id = u.id "
				+ "JOIN empresa e ON p.empresa_id = e.id " + "JOIN tutor tep ON p.tutor_empresa_id = tep.id "
				+ "JOIN usuario te ON tep.id = te.id " + "JOIN tutor tcp ON p.tutor_centro_id = tcp.id "
				+ "JOIN usuario tc ON tcp.id = tc.id";
		try (Connection conn = Conexion.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(new String[] { String.valueOf(rs.getInt("id")), rs.getString("alumno"),
						rs.getString("empresa"), rs.getString("tutor_empresa"), rs.getString("tutor_centro"),
						rs.getString("fecha_inicio"), rs.getString("fecha_fin"), rs.getString("estado") });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public List<String[]> listarAlumnos() {
		List<String[]> lista = new ArrayList<>();
		String sql = "SELECT a.id, u.nombre FROM alumno a JOIN usuario u ON a.id = u.id";
		try (Connection conn = Conexion.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(new String[] { String.valueOf(rs.getInt("id")), rs.getString("nombre") });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public List<String[]> listarTutoresEmpresa() {
		List<String[]> lista = new ArrayList<>();
		String sql = "SELECT t.id, u.nombre FROM tutor t JOIN usuario u ON t.id = u.id WHERE t.tipo = 'EMPRESA'";
		try (Connection conn = Conexion.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(new String[] { String.valueOf(rs.getInt("id")), rs.getString("nombre") });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public List<String[]> listarTutoresCentro() {
		List<String[]> lista = new ArrayList<>();
		String sql = "SELECT t.id, u.nombre FROM tutor t JOIN usuario u ON t.id = u.id WHERE t.tipo = 'CENTRO'";
		try (Connection conn = Conexion.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(new String[] { String.valueOf(rs.getInt("id")), rs.getString("nombre") });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
}