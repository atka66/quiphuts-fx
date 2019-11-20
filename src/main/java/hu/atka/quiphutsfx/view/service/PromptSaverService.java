package hu.atka.quiphutsfx.view.service;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import hu.atka.quiphutsfx.model.Prompt;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PromptSaverService extends Service<Void> {
	private File file;
	private List<Prompt> prompts;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<Prompt> getPrompts() {
		return prompts;
	}

	public void setPrompts(List<Prompt> prompts) {
		this.prompts = prompts;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			private final File file = getFile();
			private final List<Prompt> prompts = getPrompts();

			@Override
			protected Void call() throws Exception {
				PrintWriter writer = new PrintWriter(file);
				for (Prompt prompt : prompts) {
					writer.println(prompt.getText());
				}
				writer.close();
				return null;
			}
		};
	}
}
