package com.toomuchcoding.jsonassert;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Entry point for assertions. Use the static factory method and you're ready to go!
 *
 * @author Marcin Grzejszczak
 * @since 0.2.0
 *
 * @see JsonVerifiable
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
        if (documentContext == null && !empty(body)) {
            documentContext = JsonPath.parse(body);
            CACHE.put(body, documentContext);
        }
        this.parsedJson = documentContext;
    }

    private boolean empty(String text) {
        return text == null || text.length() == 0 || text.matches("^\\s*$");
    }

    /**
     * Starts assertions for the JSON provided as {@code String}
     */
    public static JsonVerifiable assertThat(String body) {
        return new JsonAssertion(body).root();
    }

    /**
     * Starts assertions for the JSON provided as {@code DocumentContext}
     */
    public static JsonVerifiable assertThat(DocumentContext parsedJson) {
        return new JsonAssertion(parsedJson).root();
    }

    /**
     * Helper method so that there are no clashes with other static methods of that name
     *
     * @see JsonAssertion#assertThat(String)
     */
    public static JsonVerifiable assertThatJson(String body) {
        return assertThat(body);
    }

    /**
     * Helper method so that there are no clashes with other static methods of that name
     *
     * @see JsonAssertion#assertThat(DocumentContext)
     */
    public static JsonVerifiable assertThatJson(DocumentContext parsedJson) {
        return assertThat(parsedJson);
    }

    private JsonVerifiable root() {
        NamelessArrayHavingFieldAssertion asserter = new NamelessArrayHavingFieldAssertion(parsedJson, jsonPathBuffer, "", jsonAsserterConfiguration);
        asserter.jsonPathBuffer.offer("$");
        return asserter;
    }

}
