package com.blogspot.toomuchcoding.jsonpathassert;

/**
 * Contract to match a parsed JSON via JSON Path
 *
 * @author Marcin Grzejszczak
 */
public interface JsonVerifiable extends ReadyToCheck, IteratingOverArray {

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
	 * When you want to compare values of a particular field in a named array
	 */
	JsonVerifiable arrayField(Object value);

	/**
	 * When you want to compare values of a field in a nameless array
	 */
	JsonVerifiable arrayField();

	/**
	 * When in JSON path you iterate over a nameless array
	 */
	JsonVerifiable array();

	/**
	 * When in JSON path you iterate over arrays and need to skip iteration
	 *
	 * TODO: Think of removing this
	 */
	JsonVerifiable iterationPassingArray();

	//TODO: All below should return ReadyToCheck
	/**
	 * Equality comparison with String
	 */
	JsonVerifiable isEqualTo(String value);

	/**
	 * Equality comparison with any object
	 */
	JsonVerifiable isEqualTo(Object value);

	/**
	 * Equality comparison with a Number
	 */
	JsonVerifiable isEqualTo(Number value);

	/**
	 * Equality comparison to null
	 */
	JsonVerifiable isNull();

	/**
	 * Regex matching
	 */
	JsonVerifiable matches(String value);

	/**
	 * Equality comparison with a Boolean
	 */
	JsonVerifiable isEqualTo(Boolean value);

	/**
	 * Call this to to perform assertion against an array of primitives
	 */
	JsonVerifiable value();

	/**
	 * Returns current JSON Path expression
	 */
	String jsonPath();
}
