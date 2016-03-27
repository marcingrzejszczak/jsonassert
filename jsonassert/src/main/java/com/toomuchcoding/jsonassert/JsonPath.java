package com.toomuchcoding.jsonassert;

/**
 * Builder of JSON Paths.
 *
 * @author Marcin Grzejszczak
 * @since 0.3.0
 *
 * @see JsonVerifiable
 * @see JsonAssertion
 */
public class JsonPath {

    /**
     * Returns a builder of {@link JsonVerifiable} with which you can build your
     * JSON Path. Once finished just call {@link JsonVerifiable#jsonPath()} to get
     * JSON Path as String.
     */
    public static JsonVerifiable builder() {
        return JsonAssertion.assertThat("").withoutThrowingException();
    }

}
