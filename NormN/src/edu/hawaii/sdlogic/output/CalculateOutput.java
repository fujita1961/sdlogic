package edu.hawaii.sdlogic.output;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.operant.OperantResource;

/**
 * CalculateOutput class
 * @author fujita
 *
 */
public interface CalculateOutput {
	public double calculate(Actor actor, OperantResource otr);
	public double calculate(Actor actor);
}
