package controlador;

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
import modelo.Practica;
import modelo.Seguimiento;
import modelo.Usuario;
import util.GestorExcepciones;
import java.util.Date;

public class RegistrarHorasControlador {

    @FXML private TableView<Seguimiento> tablaSeguimientos;
    @FXML private TableColumn<Seguimiento, Date> colFecha;
    @FXML private TableColumn<Seguimiento, Float> colHoras;
    @FXML private TableColumn<Seguimiento, String> colDescripcion;
    @FXML private TableColumn<Seguimiento, Boolean> colValidado;
    @FXML private DatePicker campoFecha;
    @FXML private TextField campoHoras;
    @FXML private TextField campoDescripcion;
    @FXML private Label labelPractica;
    @FXML private Label mensajeInfo;

    private SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
    private PracticaDAO practicaDAO = new PracticaDAO();
    private Practica practicaActual;
    private Usuario usuarioActual;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        try {
            practicaActual = practicaDAO.obtenerPorAlumno(usuario.getId());
            if (practicaActual != null) {
                labelPractica.setText("Práctica ID: " + practicaActual.getId()
                    + " | Estado: " + practicaActual.getEstado());
                cargarTabla();
            } else {
                labelPractica.setText("No tienes ninguna práctica asignada.");
            }
        } catch (Exception e) {
            GestorExcepciones.errorBD(e);
        }
    }

    private void cargarTabla() {
        try {
            ObservableList<Seguimiento> lista = FXCollections.observableArrayList(
                seguimientoDAO.listarPorPractica(practicaActual.getId())
            );
            tablaSeguimientos.setItems(lista);
        } catch (Exception e) {
            GestorExcepciones.errorBD(e);
        }
    }

    @FXML
    private void handleRegistrar() {
        if (campoFecha.getValue() == null || campoHoras.getText().isEmpty()
                || campoDescripcion.getText().isEmpty()) {
            GestorExcepciones.errorValidacion("Rellena todos los campos antes de registrar.");
            return;
        }
        if (practicaActual == null) {
            GestorExcepciones.errorValidacion("No tienes ninguna práctica asignada.");
            return;
        }
        try {
            float horas = Float.parseFloat(campoHoras.getText().trim());
            if (horas <= 0) {
                GestorExcepciones.errorValidacion("Las horas deben ser un número mayor que cero.");
                return;
            }
            Seguimiento s = new Seguimiento();
            s.setPractica(practicaActual);
            s.setFecha(java.sql.Date.valueOf(campoFecha.getValue()));
            s.setHoras(horas);
            s.setDescripcion(campoDescripcion.getText().trim());

            if (seguimientoDAO.registrar(s)) {
                mensajeInfo.setStyle("-fx-text-fill: green;");
                mensajeInfo.setText("Horas registradas correctamente.");
                cargarTabla();
                campoFecha.setValue(null);
                campoHoras.setText("");
                campoDescripcion.setText("");
            } else {
                mensajeInfo.setStyle("-fx-text-fill: red;");
                mensajeInfo.setText("Error al registrar las horas.");
            }
        } catch (NumberFormatException e) {
            GestorExcepciones.errorValidacion("El valor de horas debe ser un número válido.");
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
            Stage stage = (Stage) campoDescripcion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            GestorExcepciones.errorCargaPantalla(e);
        }
    }
}