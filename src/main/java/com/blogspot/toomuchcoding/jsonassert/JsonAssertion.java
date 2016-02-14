package com.blogspot.toomuchcoding.jsonassert;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

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
	private final JsonAsserterConfiguration jsonAsserterConfiguration = new JsonAsserterConfiguration();
	private static final Map<String, DocumentContext> CACHE = new ConcurrentHashMap<String, DocumentContext>();

	private JsonAssertion(DocumentContext parsedJson) {
		this.parsedJson = parsedJson;
	}

	private JsonAssertion(String body) {
		DocumentContext documentContext = CACHE.get(body);
		if (documentContext == null) {
			documentContext = JsonPath.parse(body);
			CACHE.put(body, documentContext);
		}
		this.parsedJson = documentContext;
	}

	public static JsonVerifiable assertThat(String body) {
		return new JsonAssertion(body).root();
	}

	public static JsonVerifiable assertThat(DocumentContext parsedJson) {
		return new JsonAssertion(parsedJson).root();
	}

	private JsonVerifiable root() {
		NamelessArrayHavingFieldAssertion asserter = new NamelessArrayHavingFieldAssertion(parsedJson, jsonPathBuffer, "", jsonAsserterConfiguration);
		asserter.jsonPathBuffer.offer("$");
		return asserter;
	}

}
