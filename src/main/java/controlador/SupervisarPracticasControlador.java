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

public class SupervisarPracticasControlador {

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
            practicaActual = practicaDAO.obtenerPorTutorCentro(usuario.getId());
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