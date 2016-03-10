package com.toomuchcoding.jsonassert;

import com.jayway.jsonpath.DocumentContext;
import org.assertj.core.api.Assertions;

/**
 * Entry point for {@link DocumentContext} {@link Assertions}
 *
 * @author Marcin Grzejszczak
 *
 * @since 0.2.0
 */
public class JsonAssertions extends Assertions {

    public static JsonPathAssert assertThat(DocumentContext actual) {
        return new JsonPathAssert(actual);
    }

    public static JsonPathAssert assertThat(JsonVerifiable actual) {
        return new JsonPathAssert(actual);
    }

}
