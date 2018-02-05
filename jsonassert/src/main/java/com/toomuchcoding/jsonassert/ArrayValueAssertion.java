package com.toomuchcoding.jsonassert;

import com.jayway.jsonpath.DocumentContext;

import java.util.LinkedList;

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
        readyToCheck.checkBufferedJsonPathString();
        return readyToCheck;
    }

    @Override
    public JsonVerifiable isEqualTo(Number value) {
        ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
                jsonPathBuffer, fieldName, jsonAsserterConfiguration);
        readyToCheck.jsonPathBuffer.offer("[?(@ == " + numberValue(value) + ")]");
        readyToCheck.checkBufferedJsonPathString();
        return readyToCheck;
    }

    @Override
    public JsonVerifiable matches(String value) {
        ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
                jsonPathBuffer, fieldName, jsonAsserterConfiguration);
        readyToCheck.jsonPathBuffer.offer("[?(@ =~ /" + stringWithEscapedSingleQuotesForRegex(value) + "/)]");
        readyToCheck.checkBufferedJsonPathString();
        return readyToCheck;
    }

    @Override
    public JsonVerifiable isEqualTo(Boolean value) {
        ReadyToCheckAsserter readyToCheck = new ReadyToCheckAsserter(parsedJson,
                jsonPathBuffer, fieldName, jsonAsserterConfiguration);
        readyToCheck.jsonPathBuffer.offer("[?(@ == " + String.valueOf(value) + ")]");
        readyToCheck.checkBufferedJsonPathString();
        return readyToCheck;
    }

    @Override
    public boolean isAssertingAValueInArray() {
        return true;
    }

}