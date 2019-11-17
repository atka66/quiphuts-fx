package hu.atka.quiphutsfx.view.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import hu.atka.quiphutsfx.controller.ContentFactory;
import hu.atka.quiphutsfx.model.Prompt;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class MainController implements Initializable {

	@FXML
	private HBox pane;

	@FXML
	private ListView<Prompt> promptListView;

	@FXML
	private TextField promptField;

	private Prompt selectedPrompt;

	public void initialize(URL location, ResourceBundle resources) {
		promptListView.getSelectionModel().selectedItemProperty().addListener(
			(observable, oldValue, newValue) -> {
				selectedPrompt = observable.getValue();
				promptField.setText(selectedPrompt.getText());
			}
		);
	}

	@FXML
	public void handleLoadPromptsButton(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
		if (file != null) {
			List<Prompt> loadedPrompts = null;
			try {
				loadedPrompts = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)
					.stream().map(Prompt::new).collect(Collectors.toList());
			} catch (IOException ex) {
				ex.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("An error occurred during reading file!");
				alert.showAndWait();
			}
			promptListView.setItems(FXCollections.observableArrayList(loadedPrompts));
		}
	}

	@FXML
	public void handleSavePromptsButton(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
		if (file != null) {
			try {
				savePromptsToFile(file, promptListView.getItems());
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Prompts saved successfully!");
				alert.showAndWait();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("File not found!");
				alert.showAndWait();
			}
		}
	}

	@FXML
	public void handleGenerateFolderButton(ActionEvent e) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File directory = directoryChooser.showDialog(pane.getScene().getWindow());
		ContentFactory contentFactory = new ContentFactory(promptListView.getItems(), directory.toPath().toString());
		try {
			contentFactory.buildFileStructure();
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText(null);
			alert.setContentText("File structure generated successfully!");
			alert.showAndWait();
		} catch (IOException ex) {
			ex.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Could not generate file structure!");
			alert.showAndWait();
		}
	}

	@FXML
	public void handleUpdatePromptButton(ActionEvent e) {
		promptListView.getItems()
			.stream()
			.filter(prompt -> prompt.getId().equals(selectedPrompt.getId()))
			.findFirst()
			.get().setText(promptField.getText());
		promptListView.refresh();
	}

	@FXML
	public void handleAddPromptButton(ActionEvent e) {
		promptListView.getItems().add(new Prompt(promptField.getText()));
	}

	@FXML
	public void handleAddBlankToPromptButton(ActionEvent e) {
		promptField.appendText("<BLANK>");
	}

	private void savePromptsToFile(File file, List<Prompt> prompts) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		for (Prompt prompt : prompts) {
			writer.println(prompt.getText());
		}
		writer.close();
	}
}
