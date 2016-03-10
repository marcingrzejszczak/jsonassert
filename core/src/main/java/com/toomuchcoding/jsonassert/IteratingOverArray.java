package com.toomuchcoding.jsonassert;

/**
 * Helper interface describing the process of current iteration
 *
 * @author Marcin Grzejszczak
 *
 * @since 0.1.0
 */
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