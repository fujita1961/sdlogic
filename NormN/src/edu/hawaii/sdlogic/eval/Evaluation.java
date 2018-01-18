package edu.hawaii.sdlogic.eval;

import edu.hawaii.sdlogic.Actor;

/**
 * evaluation class
 * @author fujita
 *
 */
public interface Evaluation {
	/**
	 * evaluate actor's performance
	 * @param actor the focal actor
	 * @return performance
	 */
	public double evaluate(Actor actor);
}
