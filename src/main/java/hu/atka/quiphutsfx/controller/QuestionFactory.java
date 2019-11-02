package hu.atka.quiphutsfx.controller;

import org.json.JSONObject;

public class QuestionFactory {

	private QuestionFactory() {
	}

	public static JSONObject build(String question, int questionId) {
		return new JSONObject().put("x", false).put("id", questionId).put("prompt", question);
	}
}
