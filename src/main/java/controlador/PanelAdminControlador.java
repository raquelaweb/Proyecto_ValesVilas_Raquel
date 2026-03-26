package controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modelo.Usuario;
import util.GestorExcepciones;

public class PanelAdminControlador {

	@FXML
	private Label labelBienvenida;
	private Usuario usuarioActual;

	public void setUsuario(Usuario usuario) {
		this.usuarioActual = usuario;
		labelBienvenida.setText("Bienvenido/a, " + usuario.getNombre());
	}

	@FXML
	private void handleGestionarUsuarios() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gestionUsuarios.fxml"));
			Stage stage = (Stage) labelBienvenida.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			GestorExcepciones.errorCargaPantalla(e);
		}
	}

	@FXML
	private void handleCerrarSesion() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
			Stage stage = (Stage) labelBienvenida.getScene().getWindow();
			stage.setTitle("Gestión de Prácticas");
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			GestorExcepciones.errorCargaPantalla(e);
		}
	}

	@FXML
	private void handleGestionarEmpresas() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gestionEmpresas.fxml"));
			Stage stage = (Stage) labelBienvenida.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			GestorExcepciones.errorCargaPantalla(e);
		}
	}

	@FXML
	private void handleAsignarPracticas() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/asignarPracticas.fxml"));
			Stage stage = (Stage) labelBienvenida.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			GestorExcepciones.errorCargaPantalla(e);
		}
	}
}