package hu.atka.quiphutsfx.view.main;

import hu.atka.quiphutsfx.view.resources.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainFXML.fxml"));

		Scene scene = new Scene(root, Settings.WINDOW_WIDTH - 10, Settings.WINDOW_HEIGHT - 10);
		stage.setTitle("Quiphuts");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}