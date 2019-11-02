package hu.atka.quiphutsfx.controller;

import org.json.JSONObject;

public class ManifestFactory {
	private ManifestFactory() {
	}

	public static String build() {
		return new JSONObject().put("id", "Main").put("name", "Main Content Pack").put("types", new String[]{"Question"}).toString();
	}
}
