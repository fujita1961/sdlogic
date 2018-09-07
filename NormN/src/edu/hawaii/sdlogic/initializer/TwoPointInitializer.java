package edu.hawaii.sdlogic.initializer;

import java.util.ArrayList;
import java.util.Random;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.exchange.MemoryExchange;
import edu.hawaii.sdlogic.exchange.NoExchange;
import edu.hawaii.sdlogic.exchange.SimpleExchange;
import edu.hawaii.sdlogic.fertility.Fertility;
import edu.hawaii.sdlogic.fertility.TwoPointFertility;
import edu.hawaii.sdlogic.output.CooperativeCalculateOutput;
import edu.hawaii.sdlogic.output.SimpleCalculateOutput;

public class TwoPointInitializer extends Initializer {
	/**
	 * circle radius
	 */
	private int radius;

	/**
	 * Constructor
	 * @param radius
	 */
	public TwoPointInitializer(int radius) {
		this.radius = radius;
	}

	public TwoPointInitializer() {
		this(5);
	}

	private void initPoint(int cx, int cy) {
		for(int i = 0; i < Env.actors; i++) {
			Actor actor = Actor.getInstance();
			actor.init();

			// Only one actor can reside on one location.
			while(true) {
				int x = ((int)(Env.rand.nextGaussian() * radius) + cx + Env.mapWidth) % Env.mapWidth;
				int y = ((int)(Env.rand.nextGaussian() * radius) + cy + Env.mapHeight) % Env.mapHeight;

				if(Env.map[x][y] == null) {
					Env.map[x][y] = actor;
					Env.actorList.add(actor);
					actor.setXY(x, y);
					break;
				}
			}
		}
	}

	/**
	 * common method for initializing fertility maps and output functions.
	 */
	protected static void initFertilityAndOutputFunction() {
		int roles = Env.roles + Env.storeRoles;

		if(Env.fertilityMaps == null) {
			Env.fertilityMaps = new double[roles][Env.mapWidth][Env.mapHeight];
		}

		if(Env.fertilityClasses == null) {
			Env.fertilityClasses = new Fertility[roles];

			Fertility fertilityRight = new TwoPointFertility(3, 1, 10, 5, true);
			Fertility fertilityLeft = new TwoPointFertility(3, 1, 10, 5, false);

			for(int i = 0; i < roles; i++) {
				if(i % 2 == 0) {
					Env.fertilityClasses[i] = fertilityLeft;
				} else {
					Env.fertilityClasses[i] = fertilityRight;
				}

				/*
				if(Env.fertilityClassName == null) {
					Env.fertilityClasses[i] = FlatFertility.get();
				} else if(Env.fertilityClassName.equals("HoleFertility")) {
					Env.fertilityClasses[i] = HoleFertility.get(3.0, 1.0, 10.0, 5.0);
				} else if(Env.fertilityClassName.equals("LinearFertility")) {
					Env.fertilityClasses[i] = LinearFertility.get(0.6, 0.5 / roles + ((double)i) / roles);
				} else {
					Env.fertilityClasses[i] = FlatFertility.get();
				}
				*/
			}

			for(int i = 0; i < roles; i++) {
				for(int x = 0; x < Env.mapWidth; x++) {
					for(int y = 0; y < Env.mapHeight; y++) {
						Env.fertilityMaps[i][x][y] = Env.fertilityClasses[i].calculate(x, y);
						/*
						if(i == 0) {
							System.out.print(Env.fertilityMaps[i][x][y] + " ");
						}
						*/
					}
					/*
					if(i == 0) {
						System.out.println();
					}
					*/
				}
			}
		}

		if(Env.output == null) {
			if(Env.outputClassName == null) {
				Env.output = new SimpleCalculateOutput();
			} else if(Env.outputClassName.equals("CooperativeCalculateOutput")) {
				Env.output = new CooperativeCalculateOutput();
			} else {
				Env.output = new SimpleCalculateOutput();
			}
		}

		if(Env.exchangeClassName == null) {
			Env.exchange = new SimpleExchange();
		} else if(Env.exchangeClassName.equals("NoExchange")) {
			Env.exchange = new NoExchange();
		} else if(Env.exchangeClassName.equals("MemoryExchange")) {
			Env.exchange = new MemoryExchange();
		} else {
			Env.exchange = new SimpleExchange();
		}
	}

	/**
	 * init method
	 */
	@Override
	public void init() {
		Env.rand = new Random();
		Env.map = new Actor[Env.mapWidth][Env.mapHeight];
		Env.actorList = new ArrayList<Actor>();

		initTypes();

		/*
		initPoint(Env.mapWidth / 4, Env.mapHeight / 2);
		initPoint(Env.mapWidth * 3 / 4, Env.mapHeight / 2);
		*/
		initPoint(Env.mapWidth / 2 - 50, Env.mapHeight / 2);
		initPoint(Env.mapWidth / 2 + 50, Env.mapHeight / 2);

		initFertilityAndOutputFunction();
	}
}