package com.toomuchcoding.jsonassert;

import com.jayway.jsonpath.DocumentContext;
import org.assertj.core.api.Assertions;

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
