package controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modelo.Usuario;
import util.GestorExcepciones;

public class PanelAlumnoControlador {

	@FXML
	private Label labelBienvenida;
	private Usuario usuarioActual;

	public void setUsuario(Usuario usuario) {
		this.usuarioActual = usuario;
		labelBienvenida.setText("Bienvenido/a, " + usuario.getNombre());
	}

	@FXML
	private void handleRegistrarHoras() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/registrarHoras.fxml"));
			Parent root = loader.load();
			RegistrarHorasControlador ctrl = loader.getController();
			ctrl.setUsuario(usuarioActual);
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
	private void handleVerEvaluaciones() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/verEvaluaciones.fxml"));
			Parent root = loader.load();
			VerEvaluacionesControlador ctrl = loader.getController();
			ctrl.setUsuario(usuarioActual);
			Stage stage = (Stage) labelBienvenida.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			GestorExcepciones.errorCargaPantalla(e);
		}
	}
}