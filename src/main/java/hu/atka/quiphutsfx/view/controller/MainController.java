package hu.atka.quiphutsfx.view.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import hu.atka.quiphutsfx.controller.ContentFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MainController implements Initializable {

	@FXML
	public void handleGenerateButton(ActionEvent e) throws IOException {
		ContentFactory contentFactory = new ContentFactory(Files.readAllLines(Paths.get("questions.txt"), StandardCharsets.UTF_8));
		contentFactory.buildFileStructure();
		System.out.println("done");
	}

	public void initialize(URL location, ResourceBundle resources) {
	}
}
