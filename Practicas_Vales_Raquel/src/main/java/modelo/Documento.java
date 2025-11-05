package modelo;

import java.util.Date;

public class Documento {
	private int id;
	private Practica practica;
	private String tipo;
	private String ruta;
	private Date fechaSubida;
	private Usuario subidoPor;

	public Documento() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Practica getPractica() {
		return practica;
	}

	public void setPractica(Practica practica) {
		this.practica = practica;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public Date getFechaSubida() {
		return fechaSubida;
	}

	public void setFechaSubida(Date fechaSubida) {
		this.fechaSubida = fechaSubida;
	}

	public Usuario getSubidoPor() {
		return subidoPor;
	}

	public void setSubidoPor(Usuario subidoPor) {
		this.subidoPor = subidoPor;
	}
}