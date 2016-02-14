package com.blogspot.toomuchcoding.jsonassert;

/**
 * Contract to match a parsed JSON via JSON Path
 *
 * @author Marcin Grzejszczak
 */
public interface JsonVerifiable extends IteratingOverArray {

	/**
	 * Assertion of a field inside an array. Use it only for assertion and not traversing
	 */
	JsonVerifiable contains(Object value);

	/**
	 * Field assertion. Adds a JSON Path entry for the given field.
	 */
	JsonVerifiable field(Object value);

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
	 * Regex matching
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
}
