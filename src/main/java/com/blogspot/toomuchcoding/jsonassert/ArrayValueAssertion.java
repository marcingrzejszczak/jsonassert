package com.blogspot.toomuchcoding.jsonassert;

import java.util.LinkedList;

import com.jayway.jsonpath.DocumentContext;

class ArrayValueAssertion extends FieldAssertion {
	protected ArrayValueAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer,
			Object arrayName, JsonAsserterConfiguration jsonAsserterConfiguration) {
		super(parsedJson, jsonPathBuffer, arrayName, jsonAsserterConfiguration);
	}

	protected ArrayValueAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer,
			JsonAsserterConfiguration jsonAsserterConfiguration) {
		super(parsedJson, jsonPathBuffer, null, jsonAsserterConfiguration);
	}

	@Override
	public JsonVerifiable contains(Object value) {
		return new ArrayValueAssertion(parsedJson, jsonPathBuffer, value, jsonAsserterConfiguration).isEqualTo(value);
	}

	@Override
	public JsonVerifiable isEqualTo(String value) {
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName, jsonAsserterConfiguration);
		readyToCheck.jsonPathBuffer.offer("[?(@ == " + wrapValueWithSingleQuotes(value) + ")]");
		return readyToCheck;
	}

	@Override
	public JsonVerifiable isEqualTo(Number value) {
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName, jsonAsserterConfiguration);
		readyToCheck.jsonPathBuffer.offer("[?(@ == " + String.valueOf(value) + ")]");
		return readyToCheck;
	}

	@Override
	public JsonVerifiable matches(String value) {
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName, jsonAsserterConfiguration);
		readyToCheck.jsonPathBuffer.offer("[?(@ =~ /" + value + "/)]");
		return readyToCheck;
	}

	@Override
	public JsonVerifiable isEqualTo(Boolean value) {
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName, jsonAsserterConfiguration);
		readyToCheck.jsonPathBuffer.offer("[?(@ == " + String.valueOf(value) + ")]");
		return readyToCheck;
	}

	@Override
	public boolean isAssertingAValueInArray() {
		return true;
	}

}