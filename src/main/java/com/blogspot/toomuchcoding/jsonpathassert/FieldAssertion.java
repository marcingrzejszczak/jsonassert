package com.blogspot.toomuchcoding.jsonpathassert;

import java.util.LinkedList;

import com.jayway.jsonpath.DocumentContext;

class FieldAssertion extends JsonAsserter {
	protected FieldAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer, Object fieldName) {
		super(parsedJson, jsonPathBuffer, fieldName);
	}
}