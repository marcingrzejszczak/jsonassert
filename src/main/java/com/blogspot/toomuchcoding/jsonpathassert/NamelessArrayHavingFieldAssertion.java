package com.blogspot.toomuchcoding.jsonpathassert;

import java.util.LinkedList;

import com.jayway.jsonpath.DocumentContext;

class NamelessArrayHavingFieldAssertion extends FieldAssertion {
	protected NamelessArrayHavingFieldAssertion(DocumentContext parsedJson,
			LinkedList<String> jsonPathBuffer, Object fieldName) {
		super(parsedJson, jsonPathBuffer, fieldName);
	}

	@Override
	public boolean isIteratingOverNamelessArray() {
		return true;
	}

}