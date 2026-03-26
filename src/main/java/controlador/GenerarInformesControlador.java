package controlador;

import dao.EvaluacionDAO;
import dao.PracticaDAO;
import dao.SeguimientoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.Evaluacion;
import modelo.Practica;
import modelo.Seguimiento;
import modelo.Usuario;
import util.GestorExcepciones;
import java.util.Date;
import java.util.List;

public class GenerarInformesControlador {

	@FXML
	private TableView<Seguimiento> tablaSeguimientos;
	@FXML
	private TableColumn<Seguimiento, Date> colFecha;
	@FXML
	private TableColumn<Seguimiento, Float> colHoras;
	@FXML
	private TableColumn<Seguimiento, String> colDescripcion;
	@FXML
	private TableColumn<Seguimiento, Boolean> colValidado;
	@FXML
	private TableView<Evaluacion> tablaEvaluaciones;
	@FXML
	private TableColumn<Evaluacion, Float> colNota;
	@FXML
	private TableColumn<Evaluacion, String> colComentarios;
	@FXML
	private Label labelInfo;
	@FXML
	private Label labelResumen;
	@FXML
	private Label mensajeInfo;

	private SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
	private EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
	private PracticaDAO practicaDAO = new PracticaDAO();
	private Practica practicaActual;
	private Usuario usuarioActual;

	public void setUsuario(Usuario usuario) {
		this.usuarioActual = usuario;
		try {
			practicaActual = practicaDAO.obtenerPorTutorCentro(usuario.getId());
			if (practicaActual != null) {
				labelInfo
						.setText("Práctica ID: " + practicaActual.getId() + " | Estado: " + practicaActual.getEstado());
				cargarTablas();
			} else {
				labelInfo.setText("No tienes ninguna práctica asignada.");
			}
		} catch (Exception e) {
			GestorExcepciones.errorBD(e);
		}
	}

	private void cargarTablas() {
		try {
			ObservableList<Seguimiento> seguimientos = FXCollections
					.observableArrayList(seguimientoDAO.listarPorPractica(practicaActual.getId()));
			tablaSeguimientos.setItems(seguimientos);

			ObservableList<Evaluacion> evaluaciones = FXCollections
					.observableArrayList(evaluacionDAO.listarPorPractica(practicaActual.getId()));
			tablaEvaluaciones.setItems(evaluaciones);

			float totalHoras = 0;
			int horasValidadas = 0;
			for (Seguimiento s : seguimientos) {
				totalHoras += s.getHoras();
				if (s.isValidado())
					horasValidadas++;
			}
			labelResumen.setText("Total horas registradas: " + totalHoras + " | Registros validados: " + horasValidadas
					+ " | Evaluaciones: " + evaluaciones.size());

		} catch (Exception e) {
			GestorExcepciones.errorBD(e);
		}
	}

	@FXML
	private void handleGenerar() {
		if (practicaActual == null) {
			GestorExcepciones.errorValidacion("No hay práctica asignada para generar el informe.");
			return;
		}
		try {
			List<Seguimiento> seguimientos = seguimientoDAO.listarPorPractica(practicaActual.getId());
			List<Evaluacion> evaluaciones = evaluacionDAO.listarPorPractica(practicaActual.getId());

			float totalHoras = 0;
			for (Seguimiento s : seguimientos)
				totalHoras += s.getHoras();

			System.out.println("========== INFORME DE PRÁCTICAS ==========");
			System.out.println("Práctica ID: " + practicaActual.getId());
			System.out.println("Estado: " + practicaActual.getEstado());
			System.out.println("------------------------------------------");
			System.out.println("Total registros de horas: " + seguimientos.size());
			System.out.println("Total horas registradas: " + totalHoras);
			System.out.println("Total evaluaciones: " + evaluaciones.size());
			if (!evaluaciones.isEmpty()) {
				System.out.println("Última nota: " + evaluaciones.get(evaluaciones.size() - 1).getNota());
			}
			System.out.println("==========================================");

			mensajeInfo.setStyle("-fx-text-fill: green;");
			mensajeInfo.setText("Informe generado en consola correctamente.");
			GestorExcepciones.info("Informe generado", "El informe se ha generado correctamente en la consola.");

		} catch (Exception e) {
			GestorExcepciones.errorBD(e);
		}
	}

	@FXML
	private void handleVolver() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/panelTutorCentro.fxml"));
			Parent root = loader.load();
			PanelTutorCentroControlador ctrl = loader.getController();
			ctrl.setUsuario(usuarioActual);
			Stage stage = (Stage) labelInfo.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			GestorExcepciones.errorCargaPantalla(e);
		}
	}
}