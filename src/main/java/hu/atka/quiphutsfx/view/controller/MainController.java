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
import hu.atka.quiphutsfx.controller.exception.InvalidPromptTextException;
import hu.atka.quiphutsfx.model.Prompt;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class MainController implements Initializable {

	@FXML
	private VBox pane;

	@FXML
	private ListView<Prompt> promptListView;

	@FXML
	private TextField promptField;

	@FXML
	private Text promptListInfoText;

	public void initialize(URL location, ResourceBundle resources) {
		promptListView.getSelectionModel().selectedItemProperty().addListener(
			(observable, oldValue, newValue) -> promptField.setText(observable.getValue().getText())
		);
	}

	@FXML
	public void handleLoadPromptsMenu(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
		if (file != null) {
			try {
				List<Prompt> loadedPrompts = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)
					.stream().map(Prompt::new).collect(Collectors.toList());
				promptListView.setItems(FXCollections.observableArrayList(loadedPrompts));
				updatePromptListInfoText();
			} catch (IOException ex) {
				ex.printStackTrace();
				this.showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
			}
		}
	}

	@FXML
	public void handleSavePromptsMenu(ActionEvent e) {
		if (!promptListView.getItems().isEmpty()) {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
			if (file != null) {
				try {
					savePromptsToFile(file, promptListView.getItems());
					this.showAlert(Alert.AlertType.INFORMATION, "Success", "Prompts saved successfully!");
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
					this.showAlert(Alert.AlertType.ERROR, "Error", "Could not save prompts to file!");
				}
			}
		} else {
			this.showAlert(Alert.AlertType.WARNING, "Warning", "No prompts to save.");
		}
	}

	@FXML
	public void handleGenerateFolderMenu(ActionEvent e) {
		if (!promptListView.getItems().isEmpty()) {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			File directory = directoryChooser.showDialog(pane.getScene().getWindow());
			if (directory != null) {
				ContentFactory contentFactory = new ContentFactory(promptListView.getItems(), directory.toPath().toString());
				try {
					contentFactory.buildFileStructure();
					this.showAlert(Alert.AlertType.INFORMATION, "Success", "File structure generated successfully!");
				} catch (IOException ex) {
					this.showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
				}
			}
		} else {
			this.showAlert(Alert.AlertType.WARNING, "Warning", "No prompts to save.");
		}
	}

	@FXML
	public void handleUpdatePromptButton(ActionEvent e) {
		String promptFieldText = promptField.getText();
		if (isPromptSelected()) {
			try {
				this.checkPromptText(promptFieldText);
				promptListView.getSelectionModel().getSelectedItem().setText(promptFieldText);
				promptListView.refresh();
			} catch (InvalidPromptTextException ex) {
				this.showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
			}
		} else {
			this.showAlert(Alert.AlertType.WARNING, "Warning", "Nothing to update.");
		}
	}

	@FXML
	public void handleAddPromptButton(ActionEvent e) {
		String promptFieldText = promptField.getText();
		try {
			this.checkPromptText(promptFieldText);
			promptListView.getItems().add(new Prompt(promptFieldText));
			updatePromptListInfoText();
		} catch (InvalidPromptTextException ex) {
			this.showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
		}
	}

	@FXML
	public void handleRemovePromptButton(ActionEvent e) {
		if (isPromptSelected()) {
			promptListView.getItems().remove(promptListView.getSelectionModel().getSelectedItem());
			updatePromptListInfoText();
		} else {
			this.showAlert(Alert.AlertType.WARNING, "Warning", "Nothing to remove.");
		}
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

	private void checkPromptText(String promptText) throws InvalidPromptTextException {
		if (promptText == null || promptText.trim().isEmpty()) {
			throw new InvalidPromptTextException("Prompt text cannot be empty!");
		}
		if (promptText.matches(".*[őűŐŰ].*")) {
			throw new InvalidPromptTextException("Prompt text cannot contain 'ő', 'ű' letters!");
		}
	}

	private void showAlert(Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private boolean isPromptSelected() {
		return !(promptListView.getSelectionModel().getSelectedIndex() < 0);
	}

	private void updatePromptListInfoText() {
		promptListInfoText.setText(String.format("%s prompts", promptListView.getItems().size()));
	}
}
