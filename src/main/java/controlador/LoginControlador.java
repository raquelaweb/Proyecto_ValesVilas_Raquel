package controlador;

import dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Usuario;
import util.GestorExcepciones;

public class LoginControlador {

    @FXML private TextField campoEmail;
    @FXML private PasswordField campoPassword;
    @FXML private Label mensajeError;

    @FXML
    private void handleLogin() {
        String email = campoEmail.getText().trim();
        String password = campoPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            mensajeError.setText("Por favor, rellena todos los campos.");
            return;
        }

        try {
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.login(email, password);

            if (usuario == null) {
                mensajeError.setText("Correo o contraseña incorrectos.");
            } else {
                redirigirSegunRol(usuario);
            }
        } catch (Exception e) {
            GestorExcepciones.errorGenerico("Login", e);
        }
    }

    private void redirigirSegunRol(Usuario usuario) {
        try {
            String fxml;
            switch (usuario.getRol()) {
                case "ADMIN":        fxml = "/panelAdmin.fxml"; break;
                case "ALUMNO":       fxml = "/panelAlumno.fxml"; break;
                case "TUTOR_EMPRESA": fxml = "/panelTutorEmpresa.fxml"; break;
                case "TUTOR_CENTRO": fxml = "/panelTutorCentro.fxml"; break;
                default:
                    mensajeError.setText("Rol no reconocido.");
                    return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            switch (usuario.getRol()) {
                case "ADMIN":
                    PanelAdminControlador adminCtrl = loader.getController();
                    adminCtrl.setUsuario(usuario);
                    break;
                case "ALUMNO":
                    PanelAlumnoControlador alumnoCtrl = loader.getController();
                    alumnoCtrl.setUsuario(usuario);
                    break;
                case "TUTOR_EMPRESA":
                    PanelTutorEmpresaControlador empresaCtrl = loader.getController();
                    empresaCtrl.setUsuario(usuario);
                    break;
                case "TUTOR_CENTRO":
                    PanelTutorCentroControlador centroCtrl = loader.getController();
                    centroCtrl.setUsuario(usuario);
                    break;
            }

            Scene scene = new Scene(root);
            Stage stage = (Stage) campoEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            GestorExcepciones.errorCargaPantalla(e);
        }
    }
}