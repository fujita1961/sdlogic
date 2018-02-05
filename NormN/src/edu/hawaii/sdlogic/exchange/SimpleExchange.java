package edu.hawaii.sdlogic.exchange;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.Term;
import edu.hawaii.sdlogic.operant.OperantResource;
import edu.hawaii.sdlogic.output.CooperativeCalculateOutput;

/**
 * basic exchange class
 * @author fujita
 *
 */
public class SimpleExchange implements Exchange {
	protected boolean memorized = false;

	public SimpleExchange() {
		memorized = false;
	}

	/**
	 * actors are categorized into satisfied and poor actors.
	 * @param satisfiedActors
	 * @param poorActors
	 * @param extras
	 */
	protected void categorizeActors(Set<Actor> satisfiedActors, Set<Actor> poorActors, List<Actor>[] extras) {
		double[] outputs = new double[Env.roles];
		double liveCondition = Env.liveCondition * Env.roles;

		for(Actor actor: Env.actorList) {
			double total = 0;

			boolean satisfy = true;
			for(int k = 0; k < Env.roles; k++) {
				outputs[k] = actor.getOperantResource(Env.roleNames[k]).getOutput();
				total += outputs[k];
				if(outputs[k] < Env.liveCondition) {
					satisfy = false;
				}
			}

			if(total > liveCondition) {
				if(satisfy) {
					// complete satisfaction
					satisfiedActors.add(actor);
				} else {
					// partial satisfaction
					poorActors.add(actor);

					for(int k = 0; k < Env.roles; k++) {
						if(outputs[k] >= Env.liveCondition) {
							extras[k].add(actor);
						}
					}
				}
			} else {
				// complete poor
				poorActors.add(actor);
			}
		}
	}

	/**
	 * capability calculation with collaboration effects
	 * @param actor
	 * @return capability
	 */
	protected double calculateExchangeCapability(Actor actor) {
		OperantResource otr = actor.getOperantResource(Term.EXCHANGING);

		// capability is a multiplication of effort and skil.
		double capability = otr.getEffort() * otr.getSkill();
		OperantResource collaboOtr = actor.getOperantResource(Term.COLLABORATING);

		if(Env.output instanceof CooperativeCalculateOutput) {
			for(int i = 0; i < Env.friends; i++) {
				Actor friend = actor.getFriend(i);
				if(friend != null) {
					OperantResource friendOtr = friend.getOperantResource(Term.EXCHANGING);
					double friendCapability = friendOtr.getEffort() * friendOtr.getSkill();
					OperantResource partnerCollaboOtr = friend.getOperantResource(Term.COLLABORATING);

					if(collaboOtr != null) {
						friendCapability *= collaboOtr.getSkill() * partnerCollaboOtr.getSkill();
					}

					// capability is multiplied by friend's collaboration factor.
					capability *= (1 + friendCapability / Env.friends);
				}
			}
		}

		return capability;
	}

	/**
	 * exchange output method
	 */
	@Override
	public void exchange(Set<Actor> satisfiedActors) {
		// actors who have extra amount of resources
		@SuppressWarnings("unchecked")
		List<Actor>[] extras = new ArrayList[Env.roles];

		for(int k = 0; k < Env.roles; k++) {
			extras[k] = new ArrayList<Actor>();
		}

		Set<Actor> originalSatisfiedActors = new HashSet<Actor>();

		Set<Actor> poorActors = new HashSet<Actor>();

		categorizeActors(satisfiedActors, poorActors, extras);

		// copy of satisfied actor list
		originalSatisfiedActors.addAll(satisfiedActors);

		// System.out.println(Env.actorList.size());
		// System.out.println(poorActors.size());

		double[] outputs = new double[Env.roles];

		// List variables for learning
		List<String> inc = null;
		List<String> dec = null;
		List<String> incPartner = null;
		List<String> decPartner = null;

		if(Env.learnFlag) {
			// initialization
			inc = new ArrayList<String>();
			dec = new ArrayList<String>();
			incPartner = new ArrayList<String>();
			decPartner = new ArrayList<String>();
		}

		double[] partnerOutputs = new double[Env.roles];
		int[] exchangerIndex = null;
		if(memorized) {
			exchangerIndex = new int[Env.roles];
		}

		int size = Env.actorList.size();
		int bias = Env.rand.nextInt(size);

		for(int ii = 0; ii < size; ii++) {
			Actor actor = Env.actorList.get((ii + bias) % size);

			if(Env.learnFlag) {
				inc.clear();
				dec.clear();
			}

			if(satisfiedActors.contains(actor)) {
				// actor satisfies the living condition

				if(originalSatisfiedActors.contains(actor)) {
					if(Env.learnFlag) {
						for(int k = 0; k < Env.roles; k++) {
							inc.add(Env.roleNames[k]);
						}
						dec.add(Term.EXCHANGING);
						actor.update(inc, dec);
					}

					// need not exchange
				}
			} else if(poorActors.contains(actor)) {
				// actor does not satisfy the living condition

				for(int k = 0; k < Env.roles; k++) {
					outputs[k] = actor.getOperantResource(Env.roleNames[k]).getOutput();
				}

				// the maximum distance for searching
				double searchLimit = Env.searchLimit;

				// exchange capability with collaboration
				double exchangeCapability = calculateExchangeCapability(actor);

				if(memorized) {
					for(int i = 0; i < Env.roles; i++) {
						exchangerIndex[i] = 0;
						// tentative;
						// actor.getExchangers()[i].clear();
					}
				}

				// search exchanging partners
				for(int i = 0; i < Env.searchIteration; i++) {

					// find the type with the minimum output
					double min = outputs[0];

					// type index for the minimum output
					int minIndex = 0;

					for(int k = 1; k < Env.roles; k++) {
						if(outputs[k] < min) {
							min = outputs[k];
							minIndex = k;
						}
					}

					// if no partner exists, exit the loop
					if(extras[minIndex].isEmpty()) break;

					// pick up a candidate of exchanging parner.
					Actor partner = null;
					LinkedList<Actor> exchangers = null;
					if(memorized) {
						exchangers = actor.getExchangers()[minIndex];

						try {
							partner = exchangers.get(exchangerIndex[minIndex]);
							if(extras[minIndex].contains(partner)) {
								exchangerIndex[minIndex]++;
							} else {
								exchangers.remove(partner);
								partner = null;
							}
						} catch (IndexOutOfBoundsException e) {
							partner = null;
						}
					}

					if(partner == null) {
						// pick up a candidate of exchanging parner.
						int index = Env.rand.nextInt(extras[minIndex].size());
						partner = extras[minIndex].get(index);
						if(memorized && !exchangers.contains(partner)) {
							exchangers.addFirst(partner);
							exchangerIndex[minIndex]++;
						}
					}

					double distance = actor.distance(partner);

					// distance must be less than the search limit.
					if(distance < searchLimit * exchangeCapability) {
						searchLimit -= distance / exchangeCapability;

						if(Env.learnFlag) {
							incPartner.clear();
							decPartner.clear();
						}

						double sum = 0;
						double minus = 0;

						for(int k = 0; k < Env.roles; k++) {
							partnerOutputs[k] = partner.getOperantResource(Env.roleNames[k]).getOutput();
							if(Env.exchangeRate > 1.0) {
								double diff = partnerOutputs[k] - Env.liveCondition;
								if(diff < 0) {
									sum += diff * actor.getExchangeRate();
									minus += diff * actor.getExchangeRate();
								} else {
									sum += diff;
								}
							} else {
								sum += partnerOutputs[k] - Env.liveCondition;
							}
						}

						// partner must have enough resources.
						if(sum > 0) {
							double minus0 = minus;
							for(int k = 0; k < Env.roles; k++) {
								double plus = partnerOutputs[k] - Env.liveCondition;
								if(plus > 0) {
									if(Env.learnFlag) {
										incPartner.add(Env.roleNames[k]);
									}
									extras[k].remove(partner);
								} else {
									if(Env.learnFlag) {
										decPartner.add(Env.roleNames[k]);
									}
								}

								if(Env.exchangeRate > 1.0) {
									if(plus > 0) {
										if(minus0 + plus >= 0) {
											partner.getOperantResource(Env.roleNames[k]).addOutput(minus0);
											minus0 = 0;
										} else {
											partner.getOperantResource(Env.roleNames[k]).setOutput(Env.liveCondition);
											minus0 += plus;
										}
									} else {
										partner.getOperantResource(Env.roleNames[k]).setOutput(Env.liveCondition);
									}
								} else {
									// set the partner's output just to the living condition.
									partner.getOperantResource(Env.roleNames[k]).setOutput(Env.liveCondition);
								}
							}

							// partner actrr becomes to satisfy the living condition.
							satisfiedActors.add(partner);

							Env.eval.evaluate(partner);

							if(Env.learnFlag) {
								decPartner.add(Term.EXCHANGING);
								partner.update(incPartner, decPartner);
							}

							// add exchanging margin to the actor's output
							boolean satisfy = true;
							for(int k = 0; k < Env.roles; k++) {
								double plus = partnerOutputs[k] - Env.liveCondition;
								if(Env.exchangeRate > 1.0) {
									if(plus > 0) {
										if(minus + plus >= 0) {
											outputs[k] -= minus;
											minus = 0;
										} else {
											outputs[k] += plus;
											minus += plus;
										}
									} else {
										outputs[k] += plus;
									}
								} else {
									outputs[k] += plus;
								}
								if(outputs[k] < Env.liveCondition) {
									satisfy = false;
								}
							}

							if(satisfy) {
								// focal actor reaches at the satisfing condition
								poorActors.remove(actor);
								satisfiedActors.add(actor);

								for(int k = 0; k < Env.roles; k++) {
									if(extras[k].contains(actor)) {
										extras[k].remove(actor);
										if(Env.learnFlag) {
											inc.add(Env.roleNames[k]);
										}
									} else {
										if(Env.learnFlag) {
											dec.add(Env.roleNames[k]);
										}
									}
								}

								if(Env.learnFlag) {
									inc.add(Term.EXCHANGING);
									actor.update(inc, dec);
								}

								break;
							}
						}
					} else {
						break;
					}
				}

				if(memorized) {
					for(int i = 0; i < Env.roles; i++) {
						LinkedList<Actor> exchangers = actor.getExchangers()[i];
						int diff = exchangers.size() - exchangerIndex[i];
						if(diff > 0) {
							for(int j = 0; j < diff; j++) {
								exchangers.removeLast();
							}
						}
					}
				}

				// put back to the output of operant resources
				for(int k = 0; k < Env.roles; k++) {
					actor.getOperantResource(Env.roleNames[k]).setOutput(outputs[k]);
				}

				Env.eval.evaluate(actor);
			} else {
				// This part is not used in the current version.
				// Code must not enter this part.

				System.err.println("This code must not be used in the current version!");

				for(int k = 0; k < Env.roles; k++) {
					outputs[k] = actor.getOperantResource(Env.roleNames[k]).getOutput();
				}

				double searchLimit = Env.searchLimit;

				double effort = calculateExchangeCapability(actor);

				for(int i = 0; i < Env.searchIteration; i++) {
					double min = outputs[0];
					int minIndex = 0;

					for(int k = 1; k < Env.roles; k++) {
						if(outputs[k] < min) {
							min = outputs[k];
							minIndex = k;
						}
					}

					if(extras[minIndex].isEmpty()) break;

					int index = Env.rand.nextInt(extras[minIndex].size());
					Actor partner = extras[minIndex].get(index);

					double distance = actor.distance(partner);

					if(distance < searchLimit * effort) {
						searchLimit -= distance / effort;

						boolean satisfy = true;

						for(int k = 0; k < Env.roles; k++) {
							partnerOutputs[k] = partner.getOperantResource(Env.roleNames[k]).getOutput();

							if(partnerOutputs[k] + outputs[k] < Env.liveCondition * 2) {
								satisfy = false;
								break;
							}
						}

						if(satisfy) {
							for(int k = 0; k < Env.roles; k++) {
								if(partnerOutputs[k] >= Env.liveCondition) {
									extras[k].remove(partner);
								}

								if(outputs[k] >= Env.liveCondition) {
									extras[k].remove(actor);
								}

								actor.getOperantResource(Env.roleNames[k]).setOutput((outputs[k] + partnerOutputs[k]) / 2);
								partner.getOperantResource(Env.roleNames[k]).setOutput((outputs[k] + partnerOutputs[k]) / 2);
							}

							Env.eval.evaluate(actor);
							Env.eval.evaluate(partner);
							satisfiedActors.add(actor);
							satisfiedActors.add(partner);


							if(Env.learnFlag) {
								incPartner.clear();
								decPartner.clear();

								for(int k = 0; k < Env.roles; k++) {
									if(partnerOutputs[k] >= Env.liveCondition) {
										incPartner.add(Env.roleNames[k]);
									} else {
										decPartner.add(Env.roleNames[k]);
									}

									if(outputs[k] >= Env.liveCondition) {
										inc.add(Env.roleNames[k]);
									} else {
										dec.add(Env.roleNames[k]);
									}
								}

								inc.add(Term.EXCHANGING);
								decPartner.add(Term.EXCHANGING);

								actor.update(inc, dec);
								partner.update(incPartner, decPartner);
							}

							break;
						}
					} else {
						break;
					}
				}
			}
		}
	}
}
