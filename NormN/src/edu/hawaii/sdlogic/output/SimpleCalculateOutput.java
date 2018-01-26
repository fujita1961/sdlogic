package edu.hawaii.sdlogic.output;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.operant.OperantResource;

/**
 * SimpleCalculateOutput
 * @author fujita
 *
 */
public class SimpleCalculateOutput implements CalculateOutput {
	public SimpleCalculateOutput() {
	}

	public void calculateAll(Actor actor) {
		for(int i = 0; i < Env.types; i++) {
			OperantResource otr = actor.getOperantResource(Env.typeNames[i]);
			calculate(actor, otr);
		}
	}

	/**
	 * calculate method
	 */
	public double calculate(Actor actor, OperantResource otr) {
		double effort = otr.getEffort();
		double capability = otr.getSkill();
		double field = Env.fertilityMaps[otr.getType()][(int)actor.getX()][(int)actor.getY()];
		double output = effort * capability * Env.outputRate * field;
		output = output * (1 + Env.rand.nextGaussian() * Env.sigmaOutput);
		if(output < 0) output = 0;

		otr.setOutput(output);

		return output;
	}
}
