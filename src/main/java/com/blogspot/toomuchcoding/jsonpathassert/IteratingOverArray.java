package com.blogspot.toomuchcoding.jsonpathassert;

public interface IteratingOverArray {
	/**
	 * True if is in progress of iterating over nameless array
	 */
	boolean isIteratingOverNamelessArray();

	/**
	 * True if is in progress of iterating over an array
	 */
	boolean isIteratingOverArray();

	/**
	 * True if current element is a particular value on which concrete assertion will take place
	 */
	boolean isAssertingAValueInArray();

}