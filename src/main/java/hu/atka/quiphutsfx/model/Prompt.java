package hu.atka.quiphutsfx.model;

public class Prompt {
	private static Long currentId = 0L;
	private Long id;
	private String text;

	public Prompt(String text) {
		this.id = currentId++;
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}
}
