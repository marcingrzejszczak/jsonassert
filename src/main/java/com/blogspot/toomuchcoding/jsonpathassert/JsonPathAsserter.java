package com.blogspot.toomuchcoding.jsonpathassert;

import java.util.LinkedList;

import com.jayway.jsonpath.DocumentContext;

import net.minidev.json.JSONArray;

class JsonPathAsserter implements JsonPathVerifiable {

	protected final DocumentContext parsedJson;
	protected final LinkedList<String> jsonPathBuffer;
	protected final Object fieldName;

	protected JsonPathAsserter(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer, Object fieldName) {
		this.parsedJson = parsedJson;
		this.jsonPathBuffer = new LinkedList<String>(jsonPathBuffer);
		this.fieldName = fieldName;
	}

	@Override
	public JsonPathVerifiable contains(final Object value) {
		FieldAssertion asserter = new FieldAssertion(parsedJson, jsonPathBuffer, value);
		// this is fake part of jsonpath since in the next section we will remove this entry
		asserter.jsonPathBuffer.offer("[*]");
		return asserter;
	}

	@Override
	public FieldAssertion field(final Object value) {
		FieldAssertion asserter = new FieldAssertion(parsedJson, jsonPathBuffer, value);
		asserter.jsonPathBuffer.offer("." + String.valueOf(value));
		return asserter;
	}

	@Override
	public ArrayAssertion array(final Object value) {
		if (value == null) {
			return array();
		}
		ArrayAssertion asserter = new ArrayAssertion(parsedJson, jsonPathBuffer, value);
		asserter.jsonPathBuffer.offer("." + String.valueOf(value) + "[*]");
		return asserter;
	}

	@Override
	public ArrayValueAssertion arrayField(final Object value) {
		ArrayValueAssertion asserter = new ArrayValueAssertion(parsedJson, jsonPathBuffer, value);
		asserter.jsonPathBuffer.offer("." + String.valueOf(value));
		return asserter;
	}

	@Override
	public ArrayValueAssertion arrayField() {
		return new ArrayValueAssertion(parsedJson, jsonPathBuffer);
	}

	@Override
	public ArrayAssertion array() {
		ArrayAssertion asserter = new ArrayAssertion(parsedJson, jsonPathBuffer);
		asserter.jsonPathBuffer.offer("[*]");
		return asserter;
	}

	@Override
	public ArrayAssertion iterationPassingArray() {
		return new ArrayAssertion(parsedJson, jsonPathBuffer);
	}

	@Override
	public JsonPathVerifiable isEqualTo(String value) {
		if (value == null) {
			return isNull();
		}
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName);
		readyToCheck.jsonPathBuffer.removeLast();
		readyToCheck.jsonPathBuffer.offer("[?(@." + String.valueOf(fieldName)+ " == " + wrapValueWithSingleQuotes(value) + ")]");
		return readyToCheck;
	}

	@Override
	public JsonPathVerifiable isEqualTo(Object value) {
		if (value == null) {
			return isNull();
		}
		if (value instanceof Number) {
			return isEqualTo((Number) value);
		} else if (value instanceof Boolean) {
			return isEqualTo((Boolean) value);
		}
		return isEqualTo(value.toString());
	}

	@Override
	public JsonPathVerifiable isEqualTo(Number value) {
		if (value == null) {
			return isNull();
		}
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName);
		readyToCheck.jsonPathBuffer.removeLast();
		readyToCheck.jsonPathBuffer.offer("[?(@." + String.valueOf(fieldName)+ " == " + value + ")]");
		return readyToCheck;
	}

	@Override
	public JsonPathVerifiable isNull() {
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName);
		readyToCheck.jsonPathBuffer.removeLast();
		readyToCheck.jsonPathBuffer.offer("[?(@." + String.valueOf(fieldName) + " == null)]");
		return readyToCheck;
	}

	@Override
	public JsonPathVerifiable matches(String value) {
		if (value == null) {
			return isNull();
		}
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName);
		readyToCheck.jsonPathBuffer.removeLast();
		readyToCheck.jsonPathBuffer.offer("[?(@." + String.valueOf(fieldName)
				+ " =~ /" + stringWithEscapedSingleQuotes(value) + "/)]");
		return readyToCheck;
	}

	@Override
	public JsonPathVerifiable isEqualTo(Boolean value) {
		if (value == null) {
			return isNull();
		}
		ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName);
		readyToCheck.jsonPathBuffer.removeLast();
		readyToCheck.jsonPathBuffer.offer("[?(@." + String.valueOf(fieldName)+ " == " + String.valueOf(value) + ")]");
		return readyToCheck;
	}

	@Override
	public JsonPathVerifiable value() {
		return new ReadyToCheckAsserter(parsedJson,
				jsonPathBuffer, fieldName);
	}

	@Override
	public void check() {
		assert !parsedJson.read(createJsonPathString(), JSONArray.class).isEmpty();
	}

	private String createJsonPathString() {
		LinkedList<String> queue = new LinkedList<String>(jsonPathBuffer);
		StringBuilder stringBuffer = new StringBuilder();
		while (!queue.isEmpty()) {
			stringBuffer.append(queue.remove());
		}
		return stringBuffer.toString();
	}

	@Override
	public String jsonPath() {
		return createJsonPathString();
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!getClass().equals(o.getClass()))
			return false;
		JsonPathAsserter jsonPathAsserter = (JsonPathAsserter) o;
		if (!fieldName.equals(jsonPathAsserter.fieldName))
			return false;
		return jsonPathBuffer.equals(jsonPathAsserter.jsonPathBuffer);

	}

	public int hashCode() {
		int result;
		result = (parsedJson != null ? parsedJson.hashCode() : 0);
		result = 31 * result + (jsonPathBuffer != null ? jsonPathBuffer.hashCode() : 0);
		result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "\\nAsserter{\n    " + "jsonPathBuffer=" + String.valueOf(jsonPathBuffer)
				+ "\n}";
	}

	@Override
	public boolean isReadyToCheck() {
		return false;
	}

	@Override
	public boolean isIteratingOverNamelessArray() {
		return false;
	}

	@Override
	public boolean isIteratingOverArray() {
		return false;
	}

	@Override
	public boolean isAssertingAValueInArray() {
		return false;
	}

	protected static String stringWithEscapedSingleQuotes(Object object) {
		String stringValue = object.toString();
		return stringValue.replaceAll("'", "\\\\'");
	}

	protected String wrapValueWithSingleQuotes(Object value) {
		return value instanceof String ?
				"'" + stringWithEscapedSingleQuotes(value) + "'" :
				value.toString();
	}
}