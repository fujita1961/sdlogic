package edu.hawaii.sdlogic.fertility;

import edu.hawaii.sdlogic.Env;

/**
 * HoleFertility class
 * Fertility level is higher at the center circle.
 * @author fujita
 *
 */
public class HoleFertility extends Fertility {
	// private static Map<Double, LinearCapacity> linearCapacities = new HashMap<Double, LinearCapacity>();

	/**
	 * radious of outside of the center circle
	 */
	private double radiusOut = 10.0;

	/**
	 * radious of inside of the center circle
	 */
	private double radiusIn = 5.0;

	/**
	 * fertility level within the center circle
	 */
	private double peak = 4.0;

	/**
	 * fertility level outside of the center circle
	 */
	private double field = 2.0;

	static {
//		linearCapacities.put(1.0, new LinearCapacity(1.0));
//		Capacity.setCapacity(Term.FISH, fishCapacity);
	}

	protected HoleFertility() {
		this(4.0, 2.0,10.0, 5.0);
	}

	/**
	 * Construtor ofr HoleFertility class
	 * @param peak fertility level inside the center circle
	 * @param field fertility level outside of the center circle
	 * @param radiusOut radius of the outside of the circle
	 * @param radiusIn radius of the inside of the circle
	 */
	protected HoleFertility(double peak, double field, double radiusOut, double radiusIn) {
		this.peak = peak;
		this.field = field;
		this.radiusOut = radiusOut;
		this.radiusIn = radiusIn;
	}

	@Override
	public double calculate(int x, int y) {
		double x0 = Env.mapWidth / 2.0;
		double y0 = Env.mapHeight / 2.0;
		double d = Math.sqrt((x - x0) * (x - x0) + (y - y0) * (y - y0));

		if(d < radiusOut) {
			if(d < radiusIn) {
				return peak;
			} else {
				// sin curve between radiusIn and radiusOut
				double theta = Math.PI * (d - radiusIn) / (radiusOut - radiusIn);
				return (peak - field) / 2 * Math.cos(theta) + (peak + field) / 2;
			}
		} else {
			return field;
		}
	}

	public static HoleFertility get(double peak, double field, double radiusOut, double radiusIn) {
		return new HoleFertility(peak, field, radiusOut, radiusIn);
	}
}
