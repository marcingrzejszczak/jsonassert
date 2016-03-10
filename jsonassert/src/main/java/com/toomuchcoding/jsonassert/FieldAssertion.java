package com.toomuchcoding.jsonassert;

import com.jayway.jsonpath.DocumentContext;

import java.util.LinkedList;

class FieldAssertion extends JsonAsserter {
    protected FieldAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer,
                             Object fieldName, JsonAsserterConfiguration jsonAsserterConfiguration) {
        super(parsedJson, jsonPathBuffer, fieldName, jsonAsserterConfiguration);
    }
}