package edu.hawaii.sdlogic.initializer;

import java.util.ArrayList;
import java.util.Random;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;

public class CenterSquareInitializer extends Initializer {
	/**
	 * square size
	 */
	private int centerWidth;

	/**
	 * Constructor
	 * @param centerWidth
	 */
	public CenterSquareInitializer(int centerWidth) {
		this.centerWidth = centerWidth;
	}

	public CenterSquareInitializer() {
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
				int x = Env.rand.nextInt(centerWidth) + Env.mapWidth / 2;
				int y = Env.rand.nextInt(centerWidth) + Env.mapHeight / 2;

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
