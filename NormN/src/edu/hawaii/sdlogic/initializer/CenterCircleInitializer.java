package edu.hawaii.sdlogic.initializer;

import java.util.ArrayList;
import java.util.Random;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;

public class CenterCircleInitializer extends Initializer {
	/**
	 * circle radius
	 */
	private int radius;

	/**
	 * Constructor
	 * @param radius
	 */
	public CenterCircleInitializer(int radius) {
		this.radius = radius;
	}

	public CenterCircleInitializer() {
		this(5);
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

		for(int i = 0; i < Env.actors; i++) {
			Actor actor = Actor.getInstance();
			actor.init();

			// Only one actor can reside on one location.
			while(true) {
				int x = (int)(Env.rand.nextGaussian() * radius + Env.mapWidth / 2);
				int y = (int)(Env.rand.nextGaussian() * radius + Env.mapHeight / 2);

				if(Env.map[x][y] == null) {
					Env.map[x][y] = actor;
					Env.actorList.add(actor);
					actor.setXY(x, y);
					break;
				}
			}
		}

		initFertilityAndOutputFunction();
	}
}