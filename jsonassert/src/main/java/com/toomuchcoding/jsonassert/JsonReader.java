package com.toomuchcoding.jsonassert;

/**
 * Contract to read the value from a JSON basing on it.
 *
 * @author Marcin Grzejszczak
 *
 * @since 0.4.0
 */
public interface JsonReader {

	/**
	 * Returns the value from the JSON, based on the created JSON Path. If the result is an
	 * JSON Array and has a single value then that value is returned. If that's an array with
	 * greater number of results then that array is returned.
	 */
	<T> T read(Class<T> clazz);
}
