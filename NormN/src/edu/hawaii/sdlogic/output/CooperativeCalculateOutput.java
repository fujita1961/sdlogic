package edu.hawaii.sdlogic.output;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.operant.OperantResource;

/**
 * CooperativeCalculationOutput class
 *
 * @author fujita
 *
 */
public class CooperativeCalculateOutput implements CalculateOutput {
	private String otrName;

	public CooperativeCalculateOutput(String otrName) {
		this.otrName = otrName;
	}

	public double calculate(Actor actor) {
		return calculate(actor, actor.getOperantResource(otrName));
	}

	/**
	 * calculate method
	 * This method does not involves a calculation of the effect of collaboration.
	 * Howeber, after calling this method, the collaboration effect is mulltiplied.
	 */
	public double calculate(Actor actor, OperantResource otr) {
		double effort = otr.getEffort();
		double capability = otr.getSkill();
		double field = Env.fertilityMaps[otr.getType()][(int)actor.getX()][(int)actor.getY()];
		double output = effort * capability * Env.outputRate * field;

		otr.setOutput(output);

		return output;
	}
}
