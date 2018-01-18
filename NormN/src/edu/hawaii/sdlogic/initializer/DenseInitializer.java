package edu.hawaii.sdlogic.initializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;

public class DenseInitializer extends Initializer {
	/**
	 * square width for dense area
	 */
	private int width;

	/**
	 * Constructor
	 * @param width
	 */
	public DenseInitializer(int width) {
		this.width = width;
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

		double effort = 0.9;
		List<Actor> actors = new ArrayList<Actor>();
		Set<Actor> friends = new HashSet<Actor>();

		for(int type = 0; type < Env.types; type++) {
			actors.clear();

			for(int i = 0; i < width * width; i++) {
				// fisherman
				Actor actor = Actor.getInstance();
				actor.init();
				actors.add(actor);

				int x = Env.mapWidth / 2 + i % width + type * width * 2;
				int y = Env.mapHeight / 2 + i / width + type * width * 2;

				Env.map[x][y] = actor;
				Env.actorList.add(actor);

				actor.setXY(x, y);

				for(int j = 0; j < Env.types; j++) {
					if(type == j) {
						actor.getOperantResource(Env.typeNames[j]).setSkill(0.9);
						actor.getOperantResource(Env.typeNames[j]).setEffort(effort);
					} else {
						actor.getOperantResource(Env.typeNames[j]).setSkill(0.5);
						actor.getOperantResource(Env.typeNames[j]).setEffort((1 - effort) / (Env.types));
					}
				}
				// Exchange skill
				actor.getOperantResource(Env.typeNames[Env.types]).setSkill(0.9);
				actor.getOperantResource(Env.typeNames[Env.types]).setEffort((1 - effort) / (Env.types));

				// Collaboration skill
				actor.getOperantResource(Env.typeNames[Env.types + 1]).setSkill(0.9);
				actor.getOperantResource(Env.typeNames[Env.types * 1]).setEffort(0);
			}

			for(Actor actor: actors) {
				friends.clear();
				friends.add(actor);
				for(int i = 0; i < Env.friends; i++) {
					while(true) {
						Actor partner = actors.get(Env.rand.nextInt(actors.size()));
						if(!friends.contains(partner)) {
							actor.setFriend(i, partner);
							friends.add(partner);
							break;
						}
					}
				}
			}
		}

		initFertilityAndOutputFunction();
	}
}
