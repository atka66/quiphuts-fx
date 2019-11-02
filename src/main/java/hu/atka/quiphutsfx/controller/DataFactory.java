package hu.atka.quiphutsfx.controller;

import org.json.JSONObject;

public class DataFactory {
	private DataFactory() {
	}

	public static String build(String question) {
		return new JSONObject().put(
			"fields",
			new JSONObject[]{
				new JSONObject().put("t", "B").put("v", "false").put("n", "HasJokeAudio"),
				new JSONObject().put("t", "S").put("v", "").put("n", "Keywords"),
				new JSONObject().put("t", "S").put("v", "").put("n", "Author"),
				new JSONObject().put("t", "S").put("v", "").put("n", "KeywordResponseText"),
				new JSONObject().put("t", "S").put("v", question).put("n", "PromptText"),
				new JSONObject().put("t", "S").put("v", "").put("n", "Location"),
				new JSONObject().put("t", "A").put("v", "kwresponse").put("n", "KeywordResponseAudio"),
				new JSONObject().put("t", "A").put("v", "prompt").put("n", "PromptAudio"),
				new JSONObject().put("t", "B").put("v", "Custom").put("n", "true")
			}
		).toString();
	}
}
