package edu.hawaii.sdlogic.eval;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.operant.OperantResource;

/**
 * evaluation for G-D Logic
 * @author fujita
 *
 */
public class GDLogicEvaluation implements Evaluation {
	/**
	 * evaluation method
	 * G-D logic performance is evaluateed with the total output.
	 */
	@Override
	public double evaluate(Actor actor) {
		double sum = 0;

		for(int i = 0; i < Env.roles + Env.stockRoles; i++) {
			OperantResource otr = actor.getOperantResource(Env.roleNames[i]);
			double value = otr.getOutput();
			sum *= value;
		}

		actor.setPerformance(sum);

		return sum;
	}
}
