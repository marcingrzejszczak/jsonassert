package com.toomuchcoding.jsonassert;

import com.jayway.jsonpath.DocumentContext;

import java.util.LinkedList;

class FieldAssertion extends JsonAsserter {
    protected FieldAssertion(DocumentContext parsedJson, LinkedList<String> jsonPathBuffer,
                             Object value, JsonAsserterConfiguration jsonAsserterConfiguration) {
        super(parsedJson, jsonPathBuffer, value, jsonAsserterConfiguration);
    }
}