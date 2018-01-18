package edu.hawaii.sdlogic.exchange;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.Term;

/**
 * basic exchange class
 * @author fujita
 *
 */
public class MemoryExchange extends SimpleExchange {
	/**
	 * exchange output method
	 */
	@Override
	public void exchange(Set<Actor> satisfiedActors) {
		// actors who have extra amount of resources
		@SuppressWarnings("unchecked")
		List<Actor>[] extras = new ArrayList[Env.types];

		for(int k = 0; k < Env.types; k++) {
			extras[k] = new ArrayList<Actor>();
		}

		Set<Actor> originalSatisfiedActors = new HashSet<Actor>();

		Set<Actor> poorActors = new HashSet<Actor>();

		categorizeActors(satisfiedActors, poorActors, extras);

		// copy of satisfied actor list
		originalSatisfiedActors.addAll(satisfiedActors);

		// System.out.println(Env.actorList.size());
		// System.out.println(poorActors.size());

		double[] outputs = new double[Env.types];
		double liveCondition = Env.liveCondition * Env.types;

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

		double[] partnerOutputs = new double[Env.types];
		int[] exchangerIndex = new int[Env.types];

		for(Actor actor: Env.actorList) {
			if(Env.learnFlag) {
				inc.clear();
				dec.clear();
			}

			if(satisfiedActors.contains(actor)) {
				// actor satisfies the living condition

				if(originalSatisfiedActors.contains(actor)) {
					if(Env.learnFlag) {
						for(int k = 0; k < Env.types; k++) {
							inc.add(Env.typeNames[k]);
						}
						dec.add(Term.EXCHANGING);
						actor.update(inc, dec);
					}

					// need not exchange
				}
			} else if(poorActors.contains(actor)) {
				// actor does not satisfy the living condition

				for(int k = 0; k < Env.types; k++) {
					outputs[k] = actor.getOperantResource(Env.typeNames[k]).getOutput();
				}

				// the maximum distance for searching
				double searchLimit = Env.searchLimit;

				// exchange capability with collaboration
				double exchangeCapability = calculateExchangeCapability(actor);

				for(int i = 0; i < Env.types; i++) {
					exchangerIndex[i] = 0;
				}

				// search exchanging partners
				for(int i = 0; i < Env.searchIteration; i++) {

					// find the type with the minimum output
					double min = outputs[0];

					// type index for the minimum output
					int minIndex = 0;

					for(int k = 1; k < Env.types; k++) {
						if(outputs[k] < min) {
							min = outputs[k];
							minIndex = k;
						}
					}

					// if no partner exists, exit the loop
					if(extras[minIndex].isEmpty()) break;

					Actor partner;
					LinkedList<Actor> exchangers = actor.getExchangers()[minIndex];

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

					if(partner == null) {
						// pick up a candidate of exchanging parner.
						int index = Env.rand.nextInt(extras[minIndex].size());
						partner = extras[minIndex].get(index);
						if(!exchangers.contains(partner)) {
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

						for(int k = 0; k < Env.types; k++) {
							partnerOutputs[k] = partner.getOperantResource(Env.typeNames[k]).getOutput();
							sum += partnerOutputs[k];
						}

						// partner must have enough resources.
						if(sum > liveCondition) {
							for(int k = 0; k < Env.types; k++) {
								if(partnerOutputs[k] >= Env.liveCondition) {
									if(Env.learnFlag) {
										incPartner.add(Env.typeNames[k]);
									}
									extras[k].remove(partner);
								} else {
									if(Env.learnFlag) {
										decPartner.add(Env.typeNames[k]);
									}
								}

								// set the partner's output just to the living condition.
								partner.getOperantResource(Env.typeNames[k]).setOutput(Env.liveCondition);
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
							for(int k = 0; k < Env.types; k++) {
								outputs[k] += partnerOutputs[k] - Env.liveCondition;
								if(outputs[k] < Env.liveCondition) {
									satisfy = false;
								}
							}

							if(satisfy) {
								// focal actor reaches at the satisfing condition
								poorActors.remove(actor);
								satisfiedActors.add(actor);

								for(int k = 0; k < Env.types; k++) {
									if(extras[k].contains(actor)) {
										extras[k].remove(actor);
										if(Env.learnFlag) {
											inc.add(Env.typeNames[k]);
										}
									} else {
										if(Env.learnFlag) {
											dec.add(Env.typeNames[k]);
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

				for(int i = 0; i < Env.types; i++) {
					LinkedList<Actor> exchangers = actor.getExchangers()[i];
					int diff = exchangers.size() - exchangerIndex[i];
					if(diff > 0) {
						for(int j = 0; j < diff; j++) {
							exchangers.removeLast();
						}
					}
				}

				// put back to the output of operant resources
				for(int k = 0; k < Env.types; k++) {
					actor.getOperantResource(Env.typeNames[k]).setOutput(outputs[k]);
				}

				Env.eval.evaluate(actor);
			} else {
				// This part is not used in the current version.
				// Code must not enter this part.

				System.err.println("This code must not be used in the current version!");

				for(int k = 0; k < Env.types; k++) {
					outputs[k] = actor.getOperantResource(Env.typeNames[k]).getOutput();
				}

				double searchLimit = Env.searchLimit;

				double effort = calculateExchangeCapability(actor);

				for(int i = 0; i < Env.searchIteration; i++) {
					double min = outputs[0];
					int minIndex = 0;

					for(int k = 1; k < Env.types; k++) {
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

						for(int k = 0; k < Env.types; k++) {
							partnerOutputs[k] = partner.getOperantResource(Env.typeNames[k]).getOutput();

							if(partnerOutputs[k] + outputs[k] < Env.liveCondition * 2) {
								satisfy = false;
								break;
							}
						}

						if(satisfy) {
							for(int k = 0; k < Env.types; k++) {
								if(partnerOutputs[k] >= Env.liveCondition) {
									extras[k].remove(partner);
								}

								if(outputs[k] >= Env.liveCondition) {
									extras[k].remove(actor);
								}

								actor.getOperantResource(Env.typeNames[k]).setOutput((outputs[k] + partnerOutputs[k]) / 2);
								partner.getOperantResource(Env.typeNames[k]).setOutput((outputs[k] + partnerOutputs[k]) / 2);
							}

							Env.eval.evaluate(actor);
							Env.eval.evaluate(partner);
							satisfiedActors.add(actor);
							satisfiedActors.add(partner);


							if(Env.learnFlag) {
								incPartner.clear();
								decPartner.clear();

								for(int k = 0; k < Env.types; k++) {
									if(partnerOutputs[k] >= Env.liveCondition) {
										incPartner.add(Env.typeNames[k]);
									} else {
										decPartner.add(Env.typeNames[k]);
									}

									if(outputs[k] >= Env.liveCondition) {
										inc.add(Env.typeNames[k]);
									} else {
										dec.add(Env.typeNames[k]);
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
