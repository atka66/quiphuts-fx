package hu.atka.quiphutsfx.view.service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import hu.atka.quiphutsfx.model.Prompt;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PromptLoaderService extends Service<List<Prompt>> {
	private Path path;

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	@Override
	protected Task<List<Prompt>> createTask() {
		return new Task<List<Prompt>>() {
			private final Path path = getPath();

			@Override
			protected List<Prompt> call() throws Exception {
				return Files.readAllLines(path, StandardCharsets.UTF_8)
					.stream().map(Prompt::new).collect(Collectors.toList());
			}
		};
	}
}
