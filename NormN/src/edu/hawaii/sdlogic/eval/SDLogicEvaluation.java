package edu.hawaii.sdlogic.eval;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.operant.OperantResource;

/**
 * evaluation for S-D Logic
 * @author fujita
 *
 */
public class SDLogicEvaluation implements Evaluation {
	/**
	 * evaluation method
	 * S-D logic performance is evaluated with minimum output
	 */
	@Override
	public double evaluate(Actor actor) {
		double min = Double.MAX_VALUE;

		for(int i = 0; i < Env.types; i++) {
			OperantResource otr = actor.getOperantResource(Env.typeNames[i]);
			double value = otr.getOutput();
			if(value < min) {
				min = value;
			}
		}

		actor.setPerformance(min);

		return min;
	}
}
