package edu.hawaii.sdlogic.output;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.Term;
import edu.hawaii.sdlogic.operant.OperantResource;

/**
 * CooperativeCalculationOutput class
 *
 * @author fujita
 *
 */
public class CooperativeCalculateOutput implements CalculateOutput {
	public CooperativeCalculateOutput() {
	}

	protected double calculateCollaboration(Actor partner, OperantResource collaboOtr, double cooperativeActorExchange,
			double[] outputs, double[] cooperates) {
		double value = 0;

		OperantResource partnerCollaboOtr = partner.getOperantResource(Term.COLLABORATING);

		// calculate collaborating effect
		for(int k = 0; k < Env.roles + Env.stockRoles; k++) {
			OperantResource partnerOtr = partner.getOperantResource(Env.roleNames[k]);
			if(collaboOtr != null) {
				cooperates[k] = partnerOtr.getEffort() * partnerOtr.getSkill()
						* collaboOtr.getSkill() * partnerCollaboOtr.getSkill();
			} else {
				cooperates[k] = partnerOtr.getEffort() * partnerOtr.getSkill();
			}
			// outputs[k] *= (1 + cooperate / Env.friends);
			// outputs0[k] is not equal to outputs[k]
			value += outputs[k] * (1 + cooperates[k] / Env.friends);
		}

		// collaboration for exchange
		OperantResource partnerExchangeOtr = partner.getOperantResource(Term.EXCHANGING);
		double cooperativeExchange;
		if(collaboOtr != null) {
			cooperativeExchange =  partnerExchangeOtr.getEffort() * partnerExchangeOtr.getSkill()
					* collaboOtr.getSkill() * partnerCollaboOtr.getSkill();
		} else {
			cooperativeExchange =  partnerExchangeOtr.getEffort() * partnerExchangeOtr.getSkill();
		}
		double actorExchangeOutput = cooperativeActorExchange * (1 + cooperativeExchange / Env.friends);

		value += actorExchangeOutput;

		return value;
	}

	public void calculateAll(Actor actor) {
		int roles = Env.roles + Env.stockRoles;

		OperantResource[] otrs = new OperantResource[roles];
		double[] outputs = new double[roles];
		double[] outputs0 = new double[roles];
		double[] cooperates0 = new double[roles];
		double[] cooperates1 = new double[roles];

		// calcuate
		for(int i = 0; i < roles; i++) {
			otrs[i] = actor.getOperantResource(Env.roleNames[i]);
			outputs[i] = calculate(actor, otrs[i]);
			outputs0[i] = outputs[i];
		}

		OperantResource collaboOtr = actor.getOperantResource(Term.COLLABORATING);
		OperantResource actorExchangeOtr = actor.getOperantResource(Term.EXCHANGING);

		double cooperativeActorExchange =  actorExchangeOtr.getEffort() * actorExchangeOtr.getSkill();

		for(int j = 0; j < Env.friends; j++) {
			int x0;
			int y0;

			Actor partner = null;

			// find a collaborative friend randomly
			while(true) {
				x0 = Env.rand.nextInt(Env.collaborativeRange * 2 + 1) - Env.collaborativeRange;
				y0 = Env.rand.nextInt(Env.collaborativeRange * 2 + 1) - Env.collaborativeRange;
				if(x0 != 0 || y0 != 0) {
					partner = Env.map[(actor.getX() + x0 + Env.mapWidth) % Env.mapWidth][(actor.getY() + y0 + Env.mapHeight) % Env.mapHeight];

					// unfortunately, partner cell is empty
					if(partner == null) break;

					boolean cont = false;
					if(Env.friendFlag) {
						// check if the actor is already registered
						for(int k = 0; k < Env.friends; k++) {
							if(partner == actor.getFriend(k)) {
								cont = true;
								break;
							}
						}
					} else {
						// check if the actor is already registered at this time
						for(int k = 0; k < j - 1; k++) {
							if(partner == actor.getFriend(k)) {
								cont = true;
								break;
							}
						}
					}
					if(!cont) {
						break;
					}
				}
			};

			// cooperative performance with partner

			double value = 0;

			if(partner != null) {
				value = calculateCollaboration(partner, collaboOtr, cooperativeActorExchange, outputs0, cooperates0);
			} else {
				// partner == null

				if(Env.macroFlag1) {
					// try to move to the empty space
					int neighbours = 0;
					int neighboursPartner = 0;
					for(int x1 = -1; x1 <= 1; x1++) {
						for(int y1 = -1; y1 <= 1; y1++) {
							if(x1 !=0 || y1 != 0) {
								int x2 = (actor.getX() + x0 + x1 + Env.mapWidth) % Env.mapWidth;
								int y2 = (actor.getY() + y0 + y1 + Env.mapHeight) % Env.mapHeight;
								if(Env.map[x2][y2] != null) {
									neighboursPartner++;
								}

								int x3 = (actor.getX() + x1 + Env.mapWidth) % Env.mapWidth;
								int y3 = (actor.getY() + y1 + Env.mapHeight) % Env.mapHeight;
								if(Env.map[x3][y3] != null) {
									neighbours++;
								}

							}
						}
					}

					if(neighboursPartner > neighbours) {
						int x00 = (actor.getX() + x0 + Env.mapWidth) % Env.mapWidth;
						int y00 = (actor.getY() + y0 + Env.mapWidth) % Env.mapWidth;
						Env.map[actor.getX()][actor.getY()] = null;
						Env.map[x00][y00] = actor;
						actor.setX(x00);
						actor.setY(y00);
					}
				} else if(Env.macroFlag2){
					// try to move to the empty space with entropy criteria
					double entropyValue = Env.entropy.primitiveEntropy(actor.getX(), actor.getY(), 1, false);
					int x2 = (actor.getX() + x0 + Env.mapWidth) % Env.mapWidth;
					int y2 = (actor.getY() + y0 + Env.mapHeight) % Env.mapHeight;
					double partnerEntropy = Env.entropy.primitiveEntropy(x2, y2, 1, false);

					if(partnerEntropy < entropyValue) {
						// System.out.println("entropy = " + entropy +" " + partnerEntropy);
						Env.map[actor.getX()][actor.getY()] = null;
						Env.map[x2][y2] = actor;
						actor.setX(x2);
						actor.setY(y2);
					}
				}
			}

			if(Env.friendFlag) {
				Actor friend = actor.getFriend(j);
				if(friend != null && Env.rand.nextDouble() < 0.1) {
					// friend of friend
					Actor friend2 = friend.getFriend(Env.rand.nextInt(Env.friends));

					for(int k = 0; k < Env.friends; k++) {
						if(friend2 == actor.getFriend(k)) {
							friend2 = null;
							break;
						}
					}

					if(friend2 != null && friend2 != actor && friend2 != friend) {
						double value2 = calculateCollaboration(friend2, collaboOtr, cooperativeActorExchange, outputs0, cooperates1);

						if(value2 > value) {
							// replace
							for(int k = 0; k < roles; k++) {
								cooperates0[k] = cooperates1[k];
							}
							value = value2;
							partner = friend2;
						}
					}
				}

				if(value < actor.getFriendValue(j)) {
					// the previous friend is better than the actor newly found.

					/*
					&& Math.abs(actor.getFriend(j).getX() - actor.getX()) <= Env.collaborativeRange
					&& Math.abs(actor.getFriend(j).getY() - actor.getY()) <= Env.collaborativeRange) {
					*/
					partner = actor.getFriend(j);
					value = 0;

					OperantResource partnerCollaboOtr = partner.getOperantResource(Term.COLLABORATING);

					for(int k = 0; k < roles; k++) {
						OperantResource partnerOtr = partner.getOperantResource(Env.roleNames[k]);

						double cooperate;

						if(collaboOtr != null) {
							cooperate = partnerOtr.getEffort() * partnerOtr.getSkill() *
									collaboOtr.getSkill() * partnerCollaboOtr.getSkill();
						} else {
							cooperate = partnerOtr.getEffort() * partnerOtr.getSkill();
						}

						outputs[k] *= (1 + cooperate / Env.friends);

						// outputs[k] = otrs[k].getOutput() * (1 + cooperate / Env.friends);
						value += outputs0[k] * (1 + (1 - Env.shareRate) * cooperate / Env.friends);

						if(Env.shareRate > 0) {
							double share = outputs0[k] * Env.shareRate * cooperate / Env.friends;
							OperantResource otr = partner.getOperantResource(Env.roleNames[k]);
							otr.addShare(share);
						}
					}

					OperantResource partnerExchangeOtr = partner.getOperantResource(Term.EXCHANGING);
					double cooperativeExchange;
					if(collaboOtr != null) {
						cooperativeExchange =  partnerExchangeOtr.getEffort() * partnerExchangeOtr.getSkill()
								* collaboOtr.getSkill() * partnerCollaboOtr.getSkill();
					} else {
						cooperativeExchange =  partnerExchangeOtr.getEffort() * partnerExchangeOtr.getSkill();
					}
					double actorExchangeOutput = cooperativeActorExchange * (1 + cooperativeExchange / Env.friends);

					value += actorExchangeOutput;
				} else {
					for(int k = 0; k < roles; k++) {
						outputs[k] *= (1 + (1 - Env.shareRate) * cooperates0[k] / Env.friends);
						if(Env.shareRate > 0 && partner != null) {
							double share = outputs0[k] * Env.shareRate * cooperates0[k] / Env.friends;
							OperantResource otr = partner.getOperantResource(Env.roleNames[k]);
							otr.addShare(share);
						}
					}
				}
			} else {
				for(int k = 0; k < roles; k++) {
					outputs[k] *= (1 + (1 - Env.shareRate) * cooperates0[k] / Env.friends);
					if(Env.shareRate > 0 && partner != null) {
						double share = outputs0[k] * Env.shareRate * cooperates0[k] / Env.friends;
						OperantResource otr = partner.getOperantResource(Env.roleNames[k]);
						otr.addShare(share);
					}
				}
			}

			actor.setFriend(j, partner);
			actor.setFriendValue(j, value);
		}

		// add fluctuation to the output
		for(int k = 0; k < roles; k++) {
			double output = outputs[k];
			/*
			if(output < otrs[k].getOutput())
				System.out.println("Strange");
			*/
			output *= (1 + Env.rand.nextGaussian() * Env.sigmaOutput);
			if(output < 0) output = 0;
			if(Env.enableStoring || Env.enableStoring2) {
				otrs[k].addOutput(output);
				otrs[k].addOutput0(output);
			} else {
				otrs[k].setOutput(output);
				otrs[k].setOutput0(output);
			}
		}
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

		// otr.setOutput(output);

		return output;
	}
}
