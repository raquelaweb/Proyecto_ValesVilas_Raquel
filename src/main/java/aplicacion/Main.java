package aplicacion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.InitDB;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		InitDB.runScript();

		Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Gestión de Prácticas");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}