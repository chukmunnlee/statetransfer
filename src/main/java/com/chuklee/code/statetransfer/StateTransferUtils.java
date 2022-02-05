package com.chuklee.code.statetransfer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.json.Json;
import jakarta.json.JsonValue;
import jakarta.json.JsonObjectBuilder;

public class StateTransferUtils {

	private static final Map<String, String> escp = new HashMap<>();
	private static final String TO_ESCAPE;

	// https://github.com/angular/angular/blob/master/packages/platform-browser/src/browser/transfer_state.ts#L13:19
	static {
		escp.put("&", "&a;");
		escp.put("\"", "&q;");
		escp.put("\'", "&s;");
		escp.put("<", "&l;");
		escp.put(">", "&g;");
		TO_ESCAPE = "[%s]".formatted(escp.keySet().stream().reduce("", (a, v) -> a + v));
	}

	public static String escapeHTML(String s) {

		final Pattern pattern = Pattern.compile(TO_ESCAPE);
		final Matcher matcher = pattern.matcher(s);

		final String escaped = matcher.replaceAll(match -> escp.get(match.group()));

		return escaped;
	}

	public static String serverTransition(String appId) {
		return "%s-state".formatted(appId);
	}

	public static String serverTransferState(final Map<String, JsonValue> toTransfer) {
		final JsonObjectBuilder states = Json.createObjectBuilder(); 
		for (String stateName: toTransfer.keySet())
			states.add(stateName, toTransfer.get(stateName));
		return escapeHTML(states.build().toString());
	}

	/*
	 <script id="serverApp-state" 
	 	type="application/json">
		{&q;server-message&q;:&q;This message was generated on the server on Sat Feb 05 2022 12:08:20 GMT+0800 (Singapore Standard Time)&q;,&q;user-profile&q;:{&q;name&q;:&q;fred&q;,&q;email&q;:&q;fred@gmail.com&q;}}
		</script>
	 */
	public static String scriptTag(final String appId, final Map<String, JsonValue> toTransfer) {

		final String transferAppId = serverTransition(appId);
		final String transferState = serverTransferState(toTransfer);

		return """
				<script id="%s" type="application/json">
					%s
				</script>
			""".formatted(transferAppId, transferState);
	}

	public static final void main (String[] args) {

		String message = "\"this is a message from the server\"";

		System.out.println(">> " + message);
		System.out.println("escaped>> " + escapeHTML(message));

		String json = "{\"name\": \"fred\", \"email\": \"fred@gmail.com\"}";
		System.out.println(">> " + json);
		System.out.println("escaped>> " + escapeHTML(json));

	}


}
