/**
 *
 */
package com.dalonedrow.pooled;

/**
 * @author Donald
 */
public interface PoolableObject {
	/** Initializes the object. */
	void init();
	/** Returns the object to the pool. */
	void returnToPool();
}
