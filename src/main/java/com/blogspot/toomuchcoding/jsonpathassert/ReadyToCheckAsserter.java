package com.blogspot.toomuchcoding.jsonpathassert;

import java.util.LinkedList;

import com.jayway.jsonpath.DocumentContext;

class ReadyToCheckAsserter extends JsonPathAsserter {

	public ReadyToCheckAsserter(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer, Object fieldName) {
		super(parsedJson, jsonPathBuffer, fieldName);
	}

	@Override
	public boolean isReadyToCheck() {
		return true;
	}

}