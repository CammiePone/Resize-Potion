package com.camellias.resizer.common.potions;

public class AttribOperationHelper {

	/**
	 * Plainly adds the value (or subtracts for negative values)
	 */
	public static final int ABSOLUTE_ADD = 0;
	
	/**
	 * Scales the value, using 1 doesn't change the value, using 0 
	 * nullifies it, using 2 doubles it.
	 */
	public static final int MULTIPLICATIVE = 1;
	
	/**
	 * Scales the value in percentual. 1 adds 100%, 0 doesn't 
	 * change the value, -1 nullifies it (-100%), 0.5 halves
	 */
	public static final int PERCENTUAL_ADDITION = 2;
	
}
