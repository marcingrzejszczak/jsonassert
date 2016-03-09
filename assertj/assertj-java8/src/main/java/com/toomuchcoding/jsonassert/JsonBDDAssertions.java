package com.toomuchcoding.jsonassert;

import com.jayway.jsonpath.DocumentContext;
import org.assertj.core.api.BDDAssertions;

/**
 * Entry point for DocumentContext BDDAssertions
 *
 * @author Marcin Grzejszczak
 */
public class JsonBDDAssertions extends BDDAssertions {

    public static JsonPathAssert then(DocumentContext actual) {
        return new JsonPathAssert(actual);
    }

    public static JsonPathAssert then(JsonVerifiable actual) {
        return new JsonPathAssert(actual);
    }

}
