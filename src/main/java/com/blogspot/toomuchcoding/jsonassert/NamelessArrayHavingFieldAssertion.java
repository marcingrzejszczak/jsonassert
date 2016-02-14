package com.blogspot.toomuchcoding.jsonassert;

import java.util.LinkedList;

import com.jayway.jsonpath.DocumentContext;

class NamelessArrayHavingFieldAssertion extends FieldAssertion {
	protected NamelessArrayHavingFieldAssertion(DocumentContext parsedJson,
			LinkedList<String> jsonPathBuffer, Object fieldName, JsonAsserterConfiguration jsonAsserterConfiguration) {
		super(parsedJson, jsonPathBuffer, fieldName, jsonAsserterConfiguration);
	}

	@Override
	public boolean isIteratingOverNamelessArray() {
		return true;
	}

}