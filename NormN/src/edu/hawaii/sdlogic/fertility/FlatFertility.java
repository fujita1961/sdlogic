package edu.hawaii.sdlogic.fertility;

/**
 * Flat-Rate Capacity class
 * @author fujita
 *
 */
public class FlatFertility extends Fertility {
	private static FlatFertility flatCapacity = new FlatFertility();

	protected FlatFertility() {
	}

	/**
	 * Capacity value is always 2.
	 * @return 2
	 */
	@Override
	public double calculate(int x, int y) {
		return 2;
	}

	/**
	 * sattic method for getting the singleton instance.
	 * @return flatCapacity instance;
	 */
	public static FlatFertility get() {
		return flatCapacity;
	}
}
