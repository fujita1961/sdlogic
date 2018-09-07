package edu.hawaii.sdlogic.fertility;

import edu.hawaii.sdlogic.Env;

/**
 * HoleFertility class
 * Fertility level is higher at the center circle.
 * @author fujita
 *
 */
public class TwoPointFertility extends Fertility {
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
	private double peak = 3.0;

	/**
	 * fertility level outside of the center circle
	 */
	private double field = 1.0;

	protected double[][] peaks;

	protected int num = 2;

	protected boolean right = true;

	static {
//		linearCapacities.put(1.0, new LinearCapacity(1.0));
//		Capacity.setCapacity(Term.FISH, fishCapacity);
	}

	public TwoPointFertility() {
		this(3.0, 1.0,10.0, 5.0, true);
	}

	/**
	 * Construtor ofr HoleFertility class
	 * @param peak fertility level inside the center circle
	 * @param field fertility level outside of the center circle
	 * @param radiusOut radius of the outside of the circle
	 * @param radiusIn radius of the inside of the circle
	 * @param lowerPeak fertility level outside of the lower peaks
	 * @param lowerRadius radius of the lower peaks
	 */
	public TwoPointFertility(double peak, double field, double radiusOut, double radiusIn, boolean right) {
		this.peak = peak;
		this.field = field;
		this.radiusOut = radiusOut;
		this.radiusIn = radiusIn;
		this.radiusIn = radiusIn;
		this.right = right;

		peaks = new double[num][2];

		double biasX = Env.mapWidth / (2 * num);

		for(int i = 0; i < num; i++) {
			// double x = i * Env.mapWidth / (double)num + biasX;
			double x = Env.mapWidth / 2 + (i * 2 - 1) * 50;
			if(x > Env.mapWidth) x -= Env.mapWidth;
			double y = Env.mapHeight / 2;

			peaks[i][0] = x;
			peaks[i][1] = y;
		}
	}

	/**
	 * 1-dimensional distance of tolus plane
	 *
	 * @param x0
	 * @param x1
	 * @return distance
	 */
	private double diff(double x0, double x1) {
		double diffx0 = x0 - x1;
		double diffx1 = x1 + Env.mapWidth - x0;
		return diffx0 > diffx1? diffx1: diffx0;
	}

	/**
	 * distance for tolus plane between actors
	 *
	 * @param actor
	 * @return distance
	 */
	public double distance(double x0, double x1, double y0, double y1) {
		double diffx;
		double diffy;

		if(x0 > x1) {
			diffx = diff(x0, x1);
		} else {
			diffx = diff(x1, x0);
		}

		if(y0 > y1) {
			diffy = diff(y0, y1);
		} else {
			diffy = diff(y1, y0);
		}

		double distance = Math.sqrt(diffx * diffx + diffy * diffy);

		return distance;
	}

	@Override
	public double calculate(int x, int y) {
		double value = field;

		if(right && x < Env.mapWidth / 2) {
			return value;
		} else if(!right && x > Env.mapWidth / 2) {
			return value;
		}

		for(int i = 0; i < peaks.length; i++) {
			double distance = distance(peaks[i][0], x, peaks[i][1], y);

			if(distance < radiusOut) {
				if(distance < radiusIn) {
					value += peak;
				} else {
					double theta = Math.PI * (distance - radiusIn) / (radiusOut - radiusIn);
					value += peak / 2 * Math.cos(theta) + peak / 2;
				}
			}
		}

		return value;
	}
}
