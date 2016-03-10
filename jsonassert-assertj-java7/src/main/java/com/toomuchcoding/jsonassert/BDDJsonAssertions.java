package com.toomuchcoding.jsonassert;

import org.assertj.core.api.BDDAssertions;

import com.jayway.jsonpath.DocumentContext;

/**
 * Entry point for {@link DocumentContext} {@link BDDAssertions}
 *
 * @author Marcin Grzejszczak
 *
 * @since 0.2.0
 */
public class BDDJsonAssertions extends BDDAssertions {

    public static JsonPathAssert then(DocumentContext actual) {
        return new JsonPathAssert(actual);
    }

    public static JsonPathAssert then(JsonVerifiable actual) {
        return new JsonPathAssert(actual);
    }

}
