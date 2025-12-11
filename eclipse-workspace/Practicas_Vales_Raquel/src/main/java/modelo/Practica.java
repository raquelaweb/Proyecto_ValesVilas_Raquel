package modelo;

import java.util.Date;

public class Practica {
	private int id;
	private Alumno alumno;
	private Empresa empresa;
	private Tutor tutorEmpresa;
	private Tutor tutorCentro;
	private Date fechaInicio;
	private Date fechaFin;
	private String estado; // Activa, Finalizada, Pendiente...

	public Practica() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Tutor getTutorEmpresa() {
		return tutorEmpresa;
	}

	public void setTutorEmpresa(Tutor tutorEmpresa) {
		this.tutorEmpresa = tutorEmpresa;
	}

	public Tutor getTutorCentro() {
		return tutorCentro;
	}

	public void setTutorCentro(Tutor tutorCentro) {
		this.tutorCentro = tutorCentro;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
