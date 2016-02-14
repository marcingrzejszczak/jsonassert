package com.blogspot.toomuchcoding.jsonpathassert;

public interface ReadyToCheck {
	/**
	 * True if the whole JSON Path got built
	 */
	boolean isReadyToCheck();

	/**
	 * Perform the assertion of the object against JSON Path
	 */
	void check();
}

