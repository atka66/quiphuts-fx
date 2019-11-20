package hu.atka.quiphutsfx.view.service;

import java.util.List;
import hu.atka.quiphutsfx.controller.ContentFactory;
import hu.atka.quiphutsfx.model.Prompt;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GeneratorService extends Service<Void> {
	private List<Prompt> prompts;
	private String path;

	public List<Prompt> getPrompts() {
		return prompts;
	}

	public void setPrompts(List<Prompt> prompts) {
		this.prompts = prompts;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			private final List<Prompt> prompts = getPrompts();
			private final String path = getPath();

			@Override
			protected Void call() throws Exception {
				ContentFactory contentFactory = new ContentFactory(prompts, path);
				contentFactory.buildFileStructure();
				return null;
			}
		};
	}
}
