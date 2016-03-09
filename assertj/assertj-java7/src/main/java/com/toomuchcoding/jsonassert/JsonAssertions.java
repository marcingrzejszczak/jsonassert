package com.toomuchcoding.jsonassert;

import org.assertj.core.api.Assertions;

import com.jayway.jsonpath.DocumentContext;

/**
 * Entry point for DocumentContext Assertions
 *
 * @author Marcin Grzejszczak
 */
public class JsonAssertions extends Assertions {

    public static JsonPathAssert assertThat(DocumentContext actual) {
        return new JsonPathAssert(actual);
    }

    public static JsonPathAssert assertThat(JsonVerifiable actual) {
        return new JsonPathAssert(actual);
    }

}
