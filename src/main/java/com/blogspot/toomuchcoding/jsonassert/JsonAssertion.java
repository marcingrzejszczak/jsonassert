package com.blogspot.toomuchcoding.jsonassert;

import java.util.LinkedList;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

/**
 * Entry point for assertions. Use the static factory method and you're ready to go!
 *
 * @see JsonVerifiable
 *
 * @author Marcin Grzejszczak
 */
public class JsonAssertion {
	private final DocumentContext parsedJson;
	private final LinkedList<String> jsonPathBuffer = new LinkedList<String>();

	private JsonAssertion(DocumentContext parsedJson) {
		this.parsedJson = parsedJson;
	}

	public static JsonVerifiable assertThat(String body) {
		DocumentContext parsedJson = JsonPath.parse(body);
		return new JsonAssertion(parsedJson).root();
	}

	public static JsonVerifiable assertThat(DocumentContext parsedJson) {
		return new JsonAssertion(parsedJson).root();
	}

	private JsonVerifiable root() {
		NamelessArrayHavingFieldAssertion asserter = new NamelessArrayHavingFieldAssertion(parsedJson, jsonPathBuffer, "");
		asserter.jsonPathBuffer.offer("$");
		return asserter;
	}

	public void matchesJsonPath(String jsonPath) {
		assert !parsedJson.read(jsonPath, JSONArray.class).isEmpty();
	}

}
