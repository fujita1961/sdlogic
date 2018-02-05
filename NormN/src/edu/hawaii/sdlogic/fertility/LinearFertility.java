package edu.hawaii.sdlogic.fertility;

import edu.hawaii.sdlogic.Env;

/**
 * LinearFertiity class
 * @author fujita
 *
 */
public class LinearFertility extends Fertility {
	// private static Map<Double, LinearCapacity> linearCapacities = new HashMap<Double, LinearCapacity>();

	/**
	 * angle of linear function
	 */
	private double angle = 1.0;

	/**
	 * peak position relative to the mapWidth.
	 */
	private double peak = 0.25;

	static {
//		linearCapacities.put(1.0, new LinearCapacity(1.0));
//		Capacity.setCapacity(Term.FISH, fishCapacity);
	}

	protected LinearFertility() {
		this(1.0, 0.25);
	}

	protected LinearFertility(double a, double b) {
		angle = a;
		peak = b;
	}

	@Override
	public double calculate(int x, int y) {
		double x0 = ((double)((x + Env.mapWidth * (1 - peak)) % Env.mapWidth)) * 2 /  Env.mapWidth;
		double x1 = Math.abs(x0 - 1);
		return (x1 - 0.5) * angle * 1 + 1;
	}

	public static LinearFertility get(double a, double b) {
		return new LinearFertility(a, b);
	}
}
