package com.toomuchcoding.jsonassert;

import com.jayway.jsonpath.DocumentContext;

import java.util.LinkedList;

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