package hu.atka.quiphutsfx.view.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import hu.atka.quiphutsfx.controller.exception.InvalidPromptTextException;
import hu.atka.quiphutsfx.model.Prompt;
import hu.atka.quiphutsfx.view.service.GeneratorService;
import hu.atka.quiphutsfx.view.service.PromptLoaderService;
import hu.atka.quiphutsfx.view.service.PromptSaverService;
import hu.atka.quiphutsfx.view.util.AlertFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
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

	@FXML
	private ProgressBar progressBar;

	public void initialize(URL location, ResourceBundle resources) {
		promptListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		promptListView.getSelectionModel().selectedItemProperty().addListener(
			(observable, oldValue, newValue) -> {
				Prompt selectedPrompt = observable.getValue();
				if (selectedPrompt != null) {
					promptField.setText(selectedPrompt.getText());
				}
			}
		);
	}

	@FXML
	public void handleLoadPromptsMenu(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
		if (file != null) {
			toggleProgressFreeze(true);
			PromptLoaderService service = new PromptLoaderService();
			service.setPath(file.toPath());
			service.setOnSucceeded(event -> {
				toggleProgressFreeze(false);
				promptListView.setItems(FXCollections.observableArrayList(service.getValue()));
				updatePromptListInfoText();
			});
			service.setOnFailed(event -> {
				toggleProgressFreeze(false);
				AlertFactory.showErrorWithStackTrace("Could not load prompts!", event.getSource().getException());
			});
			service.start();
		}
	}

	@FXML
	public void handleSavePromptsMenu(ActionEvent e) {
		if (!promptListView.getItems().isEmpty()) {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
			if (file != null) {
				toggleProgressFreeze(true);
				PromptSaverService service = new PromptSaverService();
				service.setFile(file);
				service.setPrompts(promptListView.getItems());
				service.setOnSucceeded(event -> {
					toggleProgressFreeze(false);
					AlertFactory.showInfo("Prompts saved successfully!");
				});
				service.setOnFailed(event -> {
					toggleProgressFreeze(false);
					AlertFactory.showErrorWithStackTrace("Could not save prompts to file!", event.getSource().getException());
				});
				service.start();
			}
		} else {
			AlertFactory.showWarning("No prompts to save.");
		}
	}

	@FXML
	public void handleGenerateFolderMenu(ActionEvent e) {
		if (!promptListView.getItems().isEmpty()) {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			File directory = directoryChooser.showDialog(pane.getScene().getWindow());
			if (directory != null) {
				toggleProgressFreeze(true);
				GeneratorService service = new GeneratorService();
				service.setPrompts(promptListView.getItems());
				service.setPath(directory.toPath().toString());
				service.setOnSucceeded(event -> {
					toggleProgressFreeze(false);
					AlertFactory.showInfo("File structure generated successfully!");
				});
				service.setOnFailed(event -> {
					toggleProgressFreeze(false);
					AlertFactory.showErrorWithStackTrace("Could not generate file structure!", event.getSource().getException());
				});
				service.start();
			}
		} else {
			AlertFactory.showWarning("No prompts to save.");
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
				AlertFactory.showErrorWithStackTrace("Invalid prompt text!", ex);
			}
		} else {
			AlertFactory.showWarning("Nothing to update.");
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
			AlertFactory.showErrorWithStackTrace("Invalid prompt text!", ex);
		}
	}

	@FXML
	public void handleRemovePromptButton(ActionEvent e) {
		if (isPromptSelected()) {
			promptListView.getItems().removeAll(promptListView.getSelectionModel().getSelectedItems());
			updatePromptListInfoText();
		} else {
			AlertFactory.showWarning("Nothing to remove.");
		}
	}

	@FXML
	public void handleAddBlankToPromptButton(ActionEvent e) {
		promptField.appendText("<BLANK>");
	}

	private void checkPromptText(String promptText) throws InvalidPromptTextException {
		if (promptText == null || promptText.trim().isEmpty()) {
			throw new InvalidPromptTextException("Prompt text cannot be empty!");
		}
		if (promptText.matches(".*[őűŐŰ].*")) {
			throw new InvalidPromptTextException("Prompt text cannot contain 'ő', 'ű' letters!");
		}
	}

	private boolean isPromptSelected() {
		return !(promptListView.getSelectionModel().getSelectedIndex() < 0);
	}

	private void updatePromptListInfoText() {
		promptListInfoText.setText(String.format("%s prompt(s)", promptListView.getItems().size()));
	}

	private void toggleProgressFreeze(boolean freeze) {
		promptListView.setDisable(freeze);
		promptField.setDisable(freeze);

		progressBar.setProgress(freeze ? -1.0 : 0.0);
	}
}
