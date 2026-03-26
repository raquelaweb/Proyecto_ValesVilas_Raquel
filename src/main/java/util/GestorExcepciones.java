package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GestorExcepciones {

    // Error de base de datos
    public static void errorBD(Exception e) {
        System.err.println("[ERROR BD] " + e.getMessage());
        mostrarAlerta(
            AlertType.ERROR,
            "Error de base de datos",
            "Se produjo un error al acceder a la base de datos.",
            e.getMessage()
        );
    }

    // Error de conexión
    public static void errorConexion(Exception e) {
        System.err.println("[ERROR CONEXIÓN] " + e.getMessage());
        mostrarAlerta(
            AlertType.ERROR,
            "Error de conexión",
            "No se pudo conectar a la base de datos.",
            "Comprueba que MySQL está en ejecución y que los datos de conexión en application.properties son correctos."
        );
    }

    // Error al cargar pantalla FXML
    public static void errorCargaPantalla(Exception e) {
        System.err.println("[ERROR FXML] " + e.getMessage());
        mostrarAlerta(
            AlertType.ERROR,
            "Error al cargar pantalla",
            "No se pudo cargar la pantalla solicitada.",
            e.getMessage()
        );
    }

    // Error de validación de formulario
    public static void errorValidacion(String mensaje) {
        mostrarAlerta(
            AlertType.WARNING,
            "Datos incorrectos",
            "Por favor, revisa los datos introducidos.",
            mensaje
        );
    }

    // Error genérico
    public static void errorGenerico(String contexto, Exception e) {
        System.err.println("[ERROR] " + contexto + ": " + e.getMessage());
        mostrarAlerta(
            AlertType.ERROR,
            "Error inesperado",
            "Se produjo un error en: " + contexto,
            e.getMessage()
        );
    }

    // Información
    public static void info(String titulo, String mensaje) {
        mostrarAlerta(AlertType.INFORMATION, titulo, mensaje, null);
    }

    // Confirmación (devuelve true si el usuario acepta)
    public static boolean confirmar(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.setContentText("Esta acción no se puede deshacer.");
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK;
    }

    // Método privado para mostrar alertas
    private static void mostrarAlerta(AlertType tipo, String titulo, String cabecera, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        if (contenido != null && !contenido.isEmpty()) {
            alert.setContentText(contenido);
        }
        alert.showAndWait();
    }
}
