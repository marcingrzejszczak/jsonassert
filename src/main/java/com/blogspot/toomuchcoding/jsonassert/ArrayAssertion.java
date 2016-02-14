package com.blogspot.toomuchcoding.jsonassert;

import java.util.LinkedList;

import com.jayway.jsonpath.DocumentContext;

class ArrayAssertion extends JsonAsserter {
	protected ArrayAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer,
			Object arrayName, JsonAsserterConfiguration jsonAsserterConfiguration) {
		super(parsedJson, jsonPathBuffer, arrayName, jsonAsserterConfiguration);
	}

	protected ArrayAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer,
			JsonAsserterConfiguration jsonAsserterConfiguration) {
		super(parsedJson, jsonPathBuffer, null, jsonAsserterConfiguration);
	}

	@Override
	public boolean isIteratingOverArray() {
		return true;
	}
}