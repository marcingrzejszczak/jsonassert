package com.blogspot.toomuchcoding.jsonpathassert;

import java.util.LinkedList;

import com.jayway.jsonpath.DocumentContext;

class ArrayAssertion extends JsonPathAsserter {
	protected ArrayAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer, Object arrayName) {
		super(parsedJson, jsonPathBuffer, arrayName);
	}

	protected ArrayAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer) {
		super(parsedJson, jsonPathBuffer, null);
	}

	@Override
	public boolean isIteratingOverArray() {
		return true;
	}
}