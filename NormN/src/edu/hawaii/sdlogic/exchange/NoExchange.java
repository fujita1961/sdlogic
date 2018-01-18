package edu.hawaii.sdlogic.exchange;

import java.util.Set;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;

/**
 * no exchange is permitted among actors
 * @author fujita
 *
 */
public class NoExchange implements Exchange {
	protected void categorizeActors(Set<Actor> satisfiedActors) {
		// pick up actors who does not satisfy the condition.
		for(Actor actor: Env.actorList) {
			boolean satisfy = true;
			for(int k = 0; k < Env.types; k++) {
				double output = actor.getOperantResource(Env.typeNames[k]).getOutput();
				if(output < Env.liveCondition) {
					satisfy = false;
				}
			}

			if(satisfy) {
				satisfiedActors.add(actor);
			}
		}
	}

	@Override
	public void exchange(Set<Actor> satisfiedActors) {
		categorizeActors(satisfiedActors);
	}
}
