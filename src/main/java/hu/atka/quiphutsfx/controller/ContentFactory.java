package hu.atka.quiphutsfx.controller;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ContentFactory {

	private List<String> questions;

	public ContentFactory(List<String> questions) {
		this.questions = questions;
	}

	public void buildFileStructure() throws IOException {
		Files.createDirectory(Paths.get("content"));
		this.buildManifest();
		this.buildQuestions();
	}

	private void buildManifest() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter manifestWriter = new PrintWriter("content/manifest.jet", StandardCharsets.UTF_8.name());
		manifestWriter.print(ManifestFactory.build());
		manifestWriter.close();
	}

	private void buildQuestions() throws IOException {
		int currentQuestionId = 1000;
		PrintWriter questionWriter = new PrintWriter("content/Question.jet", StandardCharsets.UTF_8.name());
		List<JSONObject> questionContentsJson = new ArrayList<>();

		for (String question : this.questions) {
			String escapedQuestion = StringEscapeUtils.escapeJson(question);
			currentQuestionId++;
			String questionPath = "content/Question/" + currentQuestionId;
			Files.createDirectories(Paths.get(questionPath));
			questionContentsJson.add(QuestionFactory.build(escapedQuestion, currentQuestionId));

			PrintWriter dataJetWriter = new PrintWriter(questionPath + "/data.jet", StandardCharsets.UTF_8.name());
			dataJetWriter.print(DataFactory.build(escapedQuestion));
			dataJetWriter.close();

			Files.copy(getClass().getResource("/audio/empty.mp3").openStream(), Paths.get(questionPath + "/" + "prompt.mp3"));
		}
		questionWriter.print(new JSONObject().put("content", questionContentsJson).put("episodeid", 1234).toString());
		questionWriter.close();
	}
}
