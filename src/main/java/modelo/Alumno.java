package modelo;

public class Alumno extends Usuario {
	private String expediente;
	private String curso;

	// Constructor vacío
	public Alumno() {
	}

	// Getters y setters
	public String getExpediente() {
		return expediente;
	}

	public void setExpediente(String expediente) {
		this.expediente = expediente;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}
}
