package controlador;

import dao.EvaluacionDAO;
import dao.PracticaDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.Evaluacion;
import modelo.Practica;
import modelo.Usuario;
import util.GestorExcepciones;

public class VerEvaluacionesControlador {

	@FXML
	private TableView<Evaluacion> tablaEvaluaciones;
	@FXML
	private TableColumn<Evaluacion, Float> colNota;
	@FXML
	private TableColumn<Evaluacion, String> colComentarios;
	@FXML
	private Label labelInfo;

	private EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
	private PracticaDAO practicaDAO = new PracticaDAO();
	private Usuario usuarioActual;

	public void setUsuario(Usuario usuario) {
		this.usuarioActual = usuario;
		try {
			Practica practica = practicaDAO.obtenerPorAlumno(usuario.getId());
			if (practica != null) {
				labelInfo.setText("Práctica ID: " + practica.getId() + " | Estado: " + practica.getEstado());
				tablaEvaluaciones
						.setItems(FXCollections.observableArrayList(evaluacionDAO.listarPorPractica(practica.getId())));
			} else {
				labelInfo.setText("No tienes ninguna práctica asignada.");
			}
		} catch (Exception e) {
			GestorExcepciones.errorBD(e);
		}
	}

	@FXML
	private void handleVolver() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/panelAlumno.fxml"));
			Parent root = loader.load();
			PanelAlumnoControlador ctrl = loader.getController();
			ctrl.setUsuario(usuarioActual);
			Stage stage = (Stage) labelInfo.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			GestorExcepciones.errorCargaPantalla(e);
		}
	}
}
