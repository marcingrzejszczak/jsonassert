package com.blogspot.toomuchcoding.jsonpathassert;

import java.util.LinkedList;

import com.jayway.jsonpath.DocumentContext;

class ArrayValueAssertion extends FieldAssertion {
	protected ArrayValueAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer, Object arrayName) {
		super(parsedJson, jsonPathBuffer, arrayName);
	}

	protected ArrayValueAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer) {
		super(parsedJson, jsonPathBuffer, null);
	}

	@Override
	public JsonPathVerifiable contains(Object value) {
		return new ArrayValueAssertion(parsedJson, jsonPathBuffer, value).isEqualTo(value);
	}

	@Override
	public JsonPathVerifiable isEqualTo(String value) {
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName);
		readyToCheck.jsonPathBuffer.offer("[?(@ == " + wrapValueWithSingleQuotes(value) + ")]");
		return readyToCheck;
	}

	@Override
	public JsonPathVerifiable isEqualTo(Number value) {
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName);
		readyToCheck.jsonPathBuffer.offer("[?(@ == " + String.valueOf(value) + ")]");
		return readyToCheck;
	}

	@Override
	public JsonPathVerifiable matches(String value) {
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName);
		readyToCheck.jsonPathBuffer.offer("[?(@ =~ /" + value + "/)]");
		return readyToCheck;
	}

	@Override
	public JsonPathVerifiable isEqualTo(Boolean value) {
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName);
		readyToCheck.jsonPathBuffer.offer("[?(@ == " + String.valueOf(value) + ")]");
		return readyToCheck;
	}

	@Override
	public boolean isAssertingAValueInArray() {
		return true;
	}

}