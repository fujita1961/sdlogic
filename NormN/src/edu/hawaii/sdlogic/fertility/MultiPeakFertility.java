package edu.hawaii.sdlogic.fertility;

import edu.hawaii.sdlogic.Env;

/**
 * HoleFertility class
 * Fertility level is higher at the center circle.
 * @author fujita
 *
 */
public class MultiPeakFertility extends Fertility {
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
	private double peak = 1.5;

	/**
	 * fertility level outside of the center circle
	 */
	private double field = 1.0;

	/**
	 * fertility level of lower peaks
	 */
	private double lowerPeak = 1.1;

	/**
	 * radious of lower peaks.
	 */
	private double lowerRadius = 5.0;

	protected double[][] peaks;

	protected int num = 9;

	static {
//		linearCapacities.put(1.0, new LinearCapacity(1.0));
//		Capacity.setCapacity(Term.FISH, fishCapacity);
	}

	public MultiPeakFertility() {
		this(3.0, 1.0,10.0, 5.0, 2.0, 10.0);
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
	public MultiPeakFertility(double peak, double field, double radiusOut, double radiusIn,
			double lowerPeak, double lowerRadius) {
		this.peak = peak;
		this.field = field;
		this.radiusOut = radiusOut;
		this.radiusIn = radiusIn;
		this.radiusIn = radiusIn;
		this.lowerPeak = lowerPeak;
		this.lowerRadius = lowerRadius;

		peaks = new double[num * num - 1][2];

		double centerX = Env.mapWidth / 2.0;
		double centerY = Env.mapHeight / 2.0;

		for(int i = 0; i < num; i++) {
			for(int j = 0; j < num; j++) {
				if(i == 0 && j == 0) continue;
				double x = i * Env.mapWidth / (double)num + centerX;
				if(x > Env.mapWidth) x -= Env.mapWidth;
				double y = j * Env.mapHeight / (double)num + centerY;
				if(y > Env.mapHeight) y -= Env.mapHeight;

				peaks[i * num + j - 1][0] = x;
				peaks[i * num + j - 1][1] = y;
			}
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
		double x0 = Env.mapWidth / 2.0;
		double y0 = Env.mapHeight / 2.0;
		double d = Math.sqrt((x - x0) * (x - x0) + (y - y0) * (y - y0));

		double value = 0;

		if(d < radiusOut) {
			if(d < radiusIn) {
				value = peak;
			} else {
				// sin curve between radiusIn and radiusOut
				double theta = Math.PI * (d - radiusIn) / (radiusOut - radiusIn);
				value = (peak - field) / 2 * Math.cos(theta) + (peak + field) / 2;
			}
		} else {
			value = field;
		}

		double halfPeak = lowerPeak / 2;

		for(int i = 0; i < peaks.length; i++) {
			double distance = distance(peaks[i][0], x, peaks[i][1], y);

			if(distance < lowerRadius) {
				value += halfPeak * Math.cos(Math.PI * distance / lowerRadius) + halfPeak;
			}
		}

		return value;
	}
}
