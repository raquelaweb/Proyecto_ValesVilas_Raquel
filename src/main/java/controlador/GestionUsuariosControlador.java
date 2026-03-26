package controlador;

import dao.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.Usuario;
import util.GestorExcepciones;

public class GestionUsuariosControlador {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TextField campoNombre;
    @FXML private TextField campoEmail;
    @FXML private TextField campoPassword;
    @FXML private ComboBox<String> comboRol;
    @FXML private Label mensajeInfo;

    private UsuarioDAO dao = new UsuarioDAO();
    private Usuario usuarioActual;

    @FXML
    public void initialize() {
        comboRol.setItems(FXCollections.observableArrayList(
            "ADMIN", "TUTOR_CENTRO", "TUTOR_EMPRESA", "ALUMNO"
        ));
        cargarTabla();

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    usuarioActual = newVal;
                    campoNombre.setText(newVal.getNombre());
                    campoEmail.setText(newVal.getEmail());
                    campoPassword.setText("");
                    comboRol.setValue(newVal.getRol());
                }
            }
        );
    }

    private void cargarTabla() {
        try {
            ObservableList<Usuario> lista = FXCollections.observableArrayList(dao.listarTodos());
            tablaUsuarios.setItems(lista);
        } catch (Exception e) {
            GestorExcepciones.errorBD(e);
        }
    }

    @FXML
    private void handleAnadir() {
        if (campoNombre.getText().isEmpty() || campoEmail.getText().isEmpty()
                || campoPassword.getText().isEmpty() || comboRol.getValue() == null) {
            GestorExcepciones.errorValidacion("Rellena todos los campos antes de añadir un usuario.");
            return;
        }
        try {
            Usuario u = new Usuario();
            u.setNombre(campoNombre.getText().trim());
            u.setEmail(campoEmail.getText().trim());
            u.setPasswordHash(campoPassword.getText().trim());
            u.setRol(comboRol.getValue());

            if (dao.crear(u)) {
                mensajeInfo.setStyle("-fx-text-fill: green;");
                mensajeInfo.setText("Usuario creado correctamente.");
                cargarTabla();
                limpiarFormulario();
            } else {
                mensajeInfo.setStyle("-fx-text-fill: red;");
                mensajeInfo.setText("Error al crear el usuario.");
            }
        } catch (Exception e) {
            GestorExcepciones.errorBD(e);
        }
    }

    @FXML
    private void handleActualizar() {
        if (usuarioActual == null) {
            GestorExcepciones.errorValidacion("Selecciona un usuario de la tabla para actualizar.");
            return;
        }
        try {
            usuarioActual.setNombre(campoNombre.getText().trim());
            usuarioActual.setEmail(campoEmail.getText().trim());
            usuarioActual.setRol(comboRol.getValue());

            if (dao.actualizar(usuarioActual)) {
                mensajeInfo.setStyle("-fx-text-fill: green;");
                mensajeInfo.setText("Usuario actualizado correctamente.");
                cargarTabla();
                limpiarFormulario();
            } else {
                mensajeInfo.setStyle("-fx-text-fill: red;");
                mensajeInfo.setText("Error al actualizar el usuario.");
            }
        } catch (Exception e) {
            GestorExcepciones.errorBD(e);
        }
    }

    @FXML
    private void handleEliminar() {
        if (usuarioActual == null) {
            GestorExcepciones.errorValidacion("Selecciona un usuario de la tabla para eliminar.");
            return;
        }
        boolean confirmar = GestorExcepciones.confirmar(
            "Eliminar usuario",
            "¿Estás segura de que quieres eliminar a " + usuarioActual.getNombre() + "?"
        );
        if (!confirmar) return;

        try {
            if (dao.eliminar(usuarioActual.getId())) {
                mensajeInfo.setStyle("-fx-text-fill: green;");
                mensajeInfo.setText("Usuario eliminado correctamente.");
                cargarTabla();
                limpiarFormulario();
            } else {
                mensajeInfo.setStyle("-fx-text-fill: red;");
                mensajeInfo.setText("Error al eliminar el usuario.");
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
        campoEmail.setText("");
        campoPassword.setText("");
        comboRol.setValue(null);
        usuarioActual = null;
    }
}