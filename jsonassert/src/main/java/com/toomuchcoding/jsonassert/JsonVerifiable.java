package com.toomuchcoding.jsonassert;

/**
 * Contract to match a parsed JSON via JSON Path
 *
 * @author Marcin Grzejszczak
 *
 * @since 0.1.0
 */
public interface JsonVerifiable extends IteratingOverArray, JsonReader {

    /**
     * Assertion of a field inside an array. Use it only for assertion and not traversing
     */
    JsonVerifiable contains(Object value);

    /**
     * Field assertion. Adds a JSON Path entry for the given field.
     */
    JsonVerifiable field(Object value);

    /**
     * Field assertions. Traverses through the list of fields and
     * adds a JSON Path entry for each one.
     */
    JsonVerifiable field(String... fields);

    /**
     * When you want to assert values in a array with a given name
     */
    JsonVerifiable array(Object value);

    /**
     * When you want to compare values of a field in a nameless array
     */
    JsonVerifiable arrayField();

    /**
     * When in JSON path you iterate over a nameless array
     */
    JsonVerifiable array();

    /**
     * Provides the {@link JsonVerifiable} for the {@code index} element of the array
     */
    JsonVerifiable elementWithIndex(int index);

    /**
     * 0Equality comparison with String
     *
     * @throws IllegalStateException - if JSON Path is not matched for the parsed JSON
     */
    JsonVerifiable isEqualTo(String value) throws IllegalStateException;

    /**
     * Equality comparison with any object
     *
     * @throws IllegalStateException - if JSON Path is not matched for the parsed JSON
     */
    JsonVerifiable isEqualTo(Object value) throws IllegalStateException;

    /**
     * Equality comparison with a Number
     *
     * @throws IllegalStateException - if JSON Path is not matched for the parsed JSON
     */
    JsonVerifiable isEqualTo(Number value) throws IllegalStateException;

    /**
     * Equality comparison to null
     *
     * @throws IllegalStateException - if JSON Path is not matched for the parsed JSON
     */
    JsonVerifiable isNull() throws IllegalStateException;

    /**
     * Regex matching for strings
     *
     * @throws IllegalStateException - if JSON Path is not matched for the parsed JSON
     */
    JsonVerifiable matches(String value) throws IllegalStateException;

    /**
     * Equality comparison with a Boolean
     *
     * @throws IllegalStateException - if JSON Path is not matched for the parsed JSON
     */
    JsonVerifiable isEqualTo(Boolean value) throws IllegalStateException;

    /**
     * Checks if the array is empty
     *
     * @throws IllegalStateException - if the result of the JSON Path contains any values
     */
    JsonVerifiable isEmpty() throws IllegalStateException;

    /**
     * Syntactic sugar for checking against an array of primitives
     */
    JsonVerifiable value();

    /**
     * Calling this method will setup the fluent interface to ignore any JSON Path verification
     */
    JsonVerifiable withoutThrowingException();

    /**
     * Returns current JSON Path expression
     */
    String jsonPath();

    /**
     * Checks if the parsed document matches given JSON Path
     */
    void matchesJsonPath(String jsonPath);

    /**
     * Checks if the array is of a given size. Example of usage:
     *
     * <pre>{@code
     * String json =  '''{ "some_list" : ["name1", "name2"] }'''
     * assertThat(json).array("some_list").hasSize(2)
     * }</pre>
     *
     */
    JsonVerifiable hasSize(int size);

}
