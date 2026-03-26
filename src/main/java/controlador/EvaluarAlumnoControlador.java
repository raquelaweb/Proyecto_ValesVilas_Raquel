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
import modelo.Tutor;
import modelo.Usuario;
import util.GestorExcepciones;

public class EvaluarAlumnoControlador {

	@FXML
	private TableView<Evaluacion> tablaEvaluaciones;
	@FXML
	private TableColumn<Evaluacion, Float> colNota;
	@FXML
	private TableColumn<Evaluacion, String> colComentarios;
	@FXML
	private TextField campoNota;
	@FXML
	private TextField campoComentarios;
	@FXML
	private Label labelInfo;
	@FXML
	private Label mensajeInfo;

	private EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
	private PracticaDAO practicaDAO = new PracticaDAO();
	private Practica practicaActual;
	private Usuario usuarioActual;

	public void setUsuario(Usuario usuario) {
		this.usuarioActual = usuario;
		try {
			practicaActual = practicaDAO.obtenerPorTutorEmpresa(usuario.getId());
			if (practicaActual != null) {
				labelInfo
						.setText("Práctica ID: " + practicaActual.getId() + " | Estado: " + practicaActual.getEstado());
				cargarTabla();
			} else {
				labelInfo.setText("No tienes ninguna práctica asignada.");
			}
		} catch (Exception e) {
			GestorExcepciones.errorBD(e);
		}
	}

	private void cargarTabla() {
		try {
			tablaEvaluaciones.setItems(
					FXCollections.observableArrayList(evaluacionDAO.listarPorPractica(practicaActual.getId())));
		} catch (Exception e) {
			GestorExcepciones.errorBD(e);
		}
	}

	@FXML
	private void handleEvaluar() {
		if (campoNota.getText().isEmpty() || campoComentarios.getText().isEmpty()) {
			GestorExcepciones.errorValidacion("Rellena todos los campos antes de guardar.");
			return;
		}
		try {
			float nota = Float.parseFloat(campoNota.getText().trim());
			if (nota < 0 || nota > 10) {
				GestorExcepciones.errorValidacion("La nota debe estar entre 0 y 10.");
				return;
			}
			Evaluacion ev = new Evaluacion();
			ev.setPractica(practicaActual);
			Tutor t = new Tutor();
			t.setId(usuarioActual.getId());
			ev.setTutor(t);
			ev.setNota(nota);
			ev.setComentarios(campoComentarios.getText().trim());

			if (evaluacionDAO.crear(ev)) {
				mensajeInfo.setStyle("-fx-text-fill: green;");
				mensajeInfo.setText("Evaluación guardada correctamente.");
				cargarTabla();
				campoNota.setText("");
				campoComentarios.setText("");
			} else {
				mensajeInfo.setStyle("-fx-text-fill: red;");
				mensajeInfo.setText("Error al guardar la evaluación.");
			}
		} catch (NumberFormatException e) {
			GestorExcepciones.errorValidacion("La nota debe ser un número válido.");
		} catch (Exception e) {
			GestorExcepciones.errorBD(e);
		}
	}

	@FXML
	private void handleVolver() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/panelTutorEmpresa.fxml"));
			Parent root = loader.load();
			PanelTutorEmpresaControlador ctrl = loader.getController();
			ctrl.setUsuario(usuarioActual);
			Stage stage = (Stage) campoNota.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			GestorExcepciones.errorCargaPantalla(e);
		}
	}
}