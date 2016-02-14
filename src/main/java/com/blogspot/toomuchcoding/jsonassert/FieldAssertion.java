package com.blogspot.toomuchcoding.jsonassert;

import java.util.LinkedList;

import com.jayway.jsonpath.DocumentContext;

class FieldAssertion extends JsonAsserter {
	protected FieldAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer,
			Object fieldName, JsonAsserterConfiguration jsonAsserterConfiguration) {
		super(parsedJson, jsonPathBuffer, fieldName, jsonAsserterConfiguration);
	}
}