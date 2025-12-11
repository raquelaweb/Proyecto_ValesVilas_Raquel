package modelo;

public class Tutor extends Usuario {
	private String tipo; // "empresa" o "centro"
	private Empresa empresa; // Puede ser null si es tutor del centro

	public Tutor() {
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
}
