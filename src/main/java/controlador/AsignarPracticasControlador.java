package controlador;

import dao.AsignacionDAO;
import dao.EmpresaDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import util.GestorExcepciones;
import java.util.List;

public class AsignarPracticasControlador {

	@FXML
	private TableView<String[]> tablaAsignaciones;
	@FXML
	private TableColumn<String[], String> colId;
	@FXML
	private TableColumn<String[], String> colAlumno;
	@FXML
	private TableColumn<String[], String> colEmpresa;
	@FXML
	private TableColumn<String[], String> colTutorEmpresa;
	@FXML
	private TableColumn<String[], String> colTutorCentro;
	@FXML
	private TableColumn<String[], String> colFechaInicio;
	@FXML
	private TableColumn<String[], String> colFechaFin;
	@FXML
	private TableColumn<String[], String> colEstado;

	@FXML
	private ComboBox<String> comboAlumno;
	@FXML
	private ComboBox<String> comboEmpresa;
	@FXML
	private ComboBox<String> comboTutorEmpresa;
	@FXML
	private ComboBox<String> comboTutorCentro;
	@FXML
	private DatePicker campoFechaInicio;
	@FXML
	private DatePicker campoFechaFin;
	@FXML
	private Label mensajeInfo;

	private AsignacionDAO asignacionDAO = new AsignacionDAO();
	private EmpresaDAO empresaDAO = new EmpresaDAO();

	private List<String[]> alumnos;
	private List<String[]> empresas;
	private List<String[]> tutoresEmpresa;
	private List<String[]> tutoresCentro;

	@FXML
	public void initialize() {
		configurarColumnas();
		cargarCombos();
		cargarTabla();
	}

	private void configurarColumnas() {
		colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
		colAlumno.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
		colEmpresa.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));
		colTutorEmpresa.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[3]));
		colTutorCentro.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[4]));
		colFechaInicio.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[5]));
		colFechaFin.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[6]));
		colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[7]));
	}

	private void cargarCombos() {
		try {
			alumnos = asignacionDAO.listarAlumnos();
			comboAlumno.setItems(FXCollections.observableArrayList(alumnos.stream().map(a -> a[1]).toList()));

			empresas = empresaDAO.listarTodas().stream()
					.map(e -> new String[] { String.valueOf(e.getId()), e.getNombre() }).toList();
			comboEmpresa.setItems(FXCollections.observableArrayList(empresas.stream().map(e -> e[1]).toList()));

			tutoresEmpresa = asignacionDAO.listarTutoresEmpresa();
			comboTutorEmpresa
					.setItems(FXCollections.observableArrayList(tutoresEmpresa.stream().map(t -> t[1]).toList()));

			tutoresCentro = asignacionDAO.listarTutoresCentro();
			comboTutorCentro
					.setItems(FXCollections.observableArrayList(tutoresCentro.stream().map(t -> t[1]).toList()));
		} catch (Exception e) {
			GestorExcepciones.errorBD(e);
		}
	}

	private void cargarTabla() {
		try {
			tablaAsignaciones.setItems(FXCollections.observableArrayList(asignacionDAO.listarAsignaciones()));
		} catch (Exception e) {
			GestorExcepciones.errorBD(e);
		}
	}

	@FXML
	private void handleAsignar() {
		if (comboAlumno.getValue() == null || comboEmpresa.getValue() == null || comboTutorEmpresa.getValue() == null
				|| comboTutorCentro.getValue() == null || campoFechaInicio.getValue() == null
				|| campoFechaFin.getValue() == null) {
			GestorExcepciones.errorValidacion("Rellena todos los campos antes de asignar.");
			return;
		}
		if (campoFechaFin.getValue().isBefore(campoFechaInicio.getValue())) {
			GestorExcepciones.errorValidacion("La fecha de fin no puede ser anterior a la fecha de inicio.");
			return;
		}
		try {
			int alumnoId = Integer.parseInt(alumnos.get(comboAlumno.getSelectionModel().getSelectedIndex())[0]);
			int empresaId = Integer.parseInt(empresas.get(comboEmpresa.getSelectionModel().getSelectedIndex())[0]);
			int tutorEmpresaId = Integer
					.parseInt(tutoresEmpresa.get(comboTutorEmpresa.getSelectionModel().getSelectedIndex())[0]);
			int tutorCentroId = Integer
					.parseInt(tutoresCentro.get(comboTutorCentro.getSelectionModel().getSelectedIndex())[0]);

			if (asignacionDAO.asignarPractica(alumnoId, empresaId, tutorEmpresaId, tutorCentroId,
					campoFechaInicio.getValue().toString(), campoFechaFin.getValue().toString())) {
				mensajeInfo.setStyle("-fx-text-fill: green;");
				mensajeInfo.setText("Práctica asignada correctamente.");
				cargarTabla();
				limpiarFormulario();
			} else {
				mensajeInfo.setStyle("-fx-text-fill: red;");
				mensajeInfo.setText("Error al asignar la práctica.");
			}
		} catch (Exception e) {
			GestorExcepciones.errorBD(e);
		}
	}

	@FXML
	private void handleVolver() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/panelAdmin.fxml"));
			Stage stage = (Stage) comboAlumno.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			GestorExcepciones.errorCargaPantalla(e);
		}
	}

	private void limpiarFormulario() {
		comboAlumno.setValue(null);
		comboEmpresa.setValue(null);
		comboTutorEmpresa.setValue(null);
		comboTutorCentro.setValue(null);
		campoFechaInicio.setValue(null);
		campoFechaFin.setValue(null);
	}
}