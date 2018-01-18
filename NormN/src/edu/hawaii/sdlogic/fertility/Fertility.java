package edu.hawaii.sdlogic.fertility;

import java.util.HashMap;

/**
 * Capacity class represents a calculating function of place's capacity.
 * @author fujita
 *
 */
public abstract class Fertility {
	/**
	 * Map from name to class
	 */
	public static HashMap<String, Fertility> classList
		= new HashMap<String, Fertility>();

	/**
	 * find class from name
	 * @param name
	 * @return class
	 */
	public static Fertility get(String name) {
		return classList.get(name);
	}

	/**
	 * set class to classList
	 * @param name
	 * @param cls is the OperantResource class
	 */
	public static void setCapacity(String name, Fertility capacity) {
		classList.put(name, capacity);
	}

	/**
	 * a calculating function of the capacity.
	 * @param place
	 * @return capacity value
	 */
	public abstract double calculate(int x, int y);
}
