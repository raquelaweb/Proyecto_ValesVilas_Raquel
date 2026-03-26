package controlador;

import dao.EmpresaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.Empresa;
import util.GestorExcepciones;

public class GestionEmpresasControlador {

    @FXML private TableView<Empresa> tablaEmpresas;
    @FXML private TableColumn<Empresa, Integer> colId;
    @FXML private TableColumn<Empresa, String> colNombre;
    @FXML private TableColumn<Empresa, String> colCif;
    @FXML private TableColumn<Empresa, String> colDireccion;
    @FXML private TableColumn<Empresa, String> colContacto;
    @FXML private TableColumn<Empresa, String> colEmail;
    @FXML private TextField campoNombre;
    @FXML private TextField campoCif;
    @FXML private TextField campoDireccion;
    @FXML private TextField campoContacto;
    @FXML private TextField campoEmail;
    @FXML private Label mensajeInfo;

    private EmpresaDAO dao = new EmpresaDAO();
    private Empresa empresaActual;

    @FXML
    public void initialize() {
        cargarTabla();
        tablaEmpresas.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    empresaActual = newVal;
                    campoNombre.setText(newVal.getNombre());
                    campoCif.setText(newVal.getCif());
                    campoDireccion.setText(newVal.getDireccion());
                    campoContacto.setText(newVal.getContacto());
                    campoEmail.setText(newVal.getEmail());
                }
            }
        );
    }

    private void cargarTabla() {
        try {
            ObservableList<Empresa> lista = FXCollections.observableArrayList(dao.listarTodas());
            tablaEmpresas.setItems(lista);
        } catch (Exception e) {
            GestorExcepciones.errorBD(e);
        }
    }

    @FXML
    private void handleAnadir() {
        if (campoNombre.getText().isEmpty() || campoCif.getText().isEmpty()
                || campoEmail.getText().isEmpty()) {
            GestorExcepciones.errorValidacion("Nombre, CIF y email son obligatorios.");
            return;
        }
        try {
            Empresa e = new Empresa();
            e.setNombre(campoNombre.getText().trim());
            e.setCif(campoCif.getText().trim());
            e.setDireccion(campoDireccion.getText().trim());
            e.setContacto(campoContacto.getText().trim());
            e.setEmail(campoEmail.getText().trim());

            if (dao.crear(e)) {
                mensajeInfo.setStyle("-fx-text-fill: green;");
                mensajeInfo.setText("Empresa creada correctamente.");
                cargarTabla();
                limpiarFormulario();
            } else {
                mensajeInfo.setStyle("-fx-text-fill: red;");
                mensajeInfo.setText("Error al crear la empresa.");
            }
        } catch (Exception e) {
            GestorExcepciones.errorBD(e);
        }
    }

    @FXML
    private void handleActualizar() {
        if (empresaActual == null) {
            GestorExcepciones.errorValidacion("Selecciona una empresa de la tabla para actualizar.");
            return;
        }
        try {
            empresaActual.setNombre(campoNombre.getText().trim());
            empresaActual.setCif(campoCif.getText().trim());
            empresaActual.setDireccion(campoDireccion.getText().trim());
            empresaActual.setContacto(campoContacto.getText().trim());
            empresaActual.setEmail(campoEmail.getText().trim());

            if (dao.actualizar(empresaActual)) {
                mensajeInfo.setStyle("-fx-text-fill: green;");
                mensajeInfo.setText("Empresa actualizada correctamente.");
                cargarTabla();
                limpiarFormulario();
            } else {
                mensajeInfo.setStyle("-fx-text-fill: red;");
                mensajeInfo.setText("Error al actualizar la empresa.");
            }
        } catch (Exception e) {
            GestorExcepciones.errorBD(e);
        }
    }

    @FXML
    private void handleEliminar() {
        if (empresaActual == null) {
            GestorExcepciones.errorValidacion("Selecciona una empresa de la tabla para eliminar.");
            return;
        }
        boolean confirmar = GestorExcepciones.confirmar(
            "Eliminar empresa",
            "¿Estás segura de que quieres eliminar " + empresaActual.getNombre() + "?"
        );
        if (!confirmar) return;

        try {
            if (dao.eliminar(empresaActual.getId())) {
                mensajeInfo.setStyle("-fx-text-fill: green;");
                mensajeInfo.setText("Empresa eliminada correctamente.");
                cargarTabla();
                limpiarFormulario();
            } else {
                mensajeInfo.setStyle("-fx-text-fill: red;");
                mensajeInfo.setText("Error al eliminar la empresa.");
            }
        } catch (Exception e) {
            GestorExcepciones.errorBD(e);
        }
    }

    @FXML
    private void handleVolver() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/panelAdmin.fxml"));
            Stage stage = (Stage) campoNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            GestorExcepciones.errorCargaPantalla(e);
        }
    }

    private void limpiarFormulario() {
        campoNombre.setText("");
        campoCif.setText("");
        campoDireccion.setText("");
        campoContacto.setText("");
        campoEmail.setText("");
        empresaActual = null;
    }
}