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

public class ValidarHorasControlador {

    @FXML private TableView<Seguimiento> tablaSeguimientos;
    @FXML private TableColumn<Seguimiento, Date> colFecha;
    @FXML private TableColumn<Seguimiento, Float> colHoras;
    @FXML private TableColumn<Seguimiento, String> colDescripcion;
    @FXML private TableColumn<Seguimiento, Boolean> colValidado;
    @FXML private Label labelInfo;
    @FXML private Label mensajeInfo;

    private SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
    private PracticaDAO practicaDAO = new PracticaDAO();
    private Practica practicaActual;
    private Usuario usuarioActual;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        try {
            practicaActual = practicaDAO.obtenerPorTutorEmpresa(usuario.getId());
            if (practicaActual != null) {
                labelInfo.setText("Práctica ID: " + practicaActual.getId()
                    + " | Estado: " + practicaActual.getEstado());
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
            ObservableList<Seguimiento> lista = FXCollections.observableArrayList(
                seguimientoDAO.listarPorPractica(practicaActual.getId())
            );
            tablaSeguimientos.setItems(lista);
        } catch (Exception e) {
            GestorExcepciones.errorBD(e);
        }
    }

    @FXML
    private void handleValidar() {
        Seguimiento seleccionado = tablaSeguimientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            GestorExcepciones.errorValidacion("Selecciona un registro de la tabla para validar.");
            return;
        }
        if (seleccionado.isValidado()) {
            GestorExcepciones.info("Ya validado", "Ese registro ya está validado.");
            return;
        }
        try {
            if (seguimientoDAO.validar(seleccionado.getId())) {
                mensajeInfo.setStyle("-fx-text-fill: green;");
                mensajeInfo.setText("Horas validadas correctamente.");
                cargarTabla();
            } else {
                mensajeInfo.setStyle("-fx-text-fill: red;");
                mensajeInfo.setText("Error al validar las horas.");
            }
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
            Stage stage = (Stage) labelInfo.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            GestorExcepciones.errorCargaPantalla(e);
        }
    }
}