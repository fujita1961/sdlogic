package edu.hawaii.sdlogic.fertility;

/**
 * Flat-Rate Capacity class
 * @author fujita
 *
 */
public class FlatFertility extends Fertility {
	public static FlatFertility flatCapacity = new FlatFertility();

	public FlatFertility() {
	}

	/**
	 * Capacity value is always 1.
	 * @return 1
	 */
	@Override
	public double calculate(int x, int y) {
		return 1;
	}
}
