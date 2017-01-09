/**
 * 
 */
package com.dalonedrow.rpg.base.constants;

/**
 * @author 588648
 *
 */
public enum Die {
	/** D2. */
	D2(2),
	/** D3. */
	D3(3),
	/** D4. */
	D4(4),
	/** D6. */
	D6(6),
	/** D8. */
	D8(8),
	/** D10. */
	D10(10),
	/** D12. */
	D12(12),
	/** D20. */
	D20(20);
	/**
	 * Creates a new instance of {@link Die}.
	 * @param val the number of faces
	 */
	Die(final int val) {
		faces = val;
	}
	/** the number of faces. */
	private final int faces;
	/**
	 * Gets the number of faces.
	 * @return {@link int}
	 */
	public int getFaces() {
		return faces;
	}	
}
