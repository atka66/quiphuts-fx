package hu.atka.quiphutsfx.model;

public class Prompt {
	private String text;
	private boolean valid;

	public Prompt(String text) {
		this.text = text;
	}

	public Prompt(String text, boolean valid) {
		this.text = text;
		this.valid = valid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return this.text;
	}
}
