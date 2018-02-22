package edu.hawaii.sdlogic.fertility;

/**
 * Capacity class represents a calculating function of place's capacity.
 * @author fujita
 *
 */
public abstract class Fertility {
	/**
	 * a calculating function of the capacity.
	 * @param place
	 * @return capacity value
	 */
	public abstract double calculate(int x, int y);
}
