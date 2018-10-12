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
	protected boolean localized = false;

	public SimpleExchange() {
		memorized = false;
		localized = false;
	}

	private void checkSatisfy(Actor actor) {
		if(Env.DEBUG) {
			for(int i = 0; i < Env.roles; i++) {
				OperantResource otr = actor.getOperantResource(Env.roleNames[i]);
				double outcome = otr.getOutput();
				if(outcome < Env.liveCondition - Env.EPSILON) {
					System.err.println(outcome);;
					throw new RuntimeException("Satisfy check error");
				}
			}
		}
	}

	/**
	 * actors are categorized into satisfied and poor actors.
	 * @param satisfiedActors
	 * @param poorActors
	 * @param extras
	 */
	protected void categorizeActors(Set<Actor> satisfiedActors, Set<Actor> poorActors, List<Actor>[] extras) {
		int roles = Env.roles + Env.storeRoles;

		double[] outputs = new double[roles];
		double liveCondition = Env.liveCondition * roles;

		for(Actor actor: Env.actorList) {
			// double total = 0;

			boolean satisfy = true;
			double sum = 0;
			for(int k = 0; k < Env.roles; k++) {
				outputs[k] = actor.getOperantResource(Env.roleNames[k]).getOutput();
				// total += outputs[k];
				if(outputs[k] < Env.liveCondition) {
					satisfy = false;
				}

				if(Env.exchangeRate > 1.0) {
					double diff = outputs[k] - Env.liveCondition;
					if(diff < 0) {
						sum += diff * Env.exchangeRate;
					} else {
						sum += diff;
					}
				} else {
					sum += outputs[k] - Env.liveCondition;
				}
			}

			// if(total > liveCondition) {
				if(satisfy) {
					// complete satisfaction
					satisfiedActors.add(actor);
					checkSatisfy(actor);
				} else {
					// partial satisfaction
					poorActors.add(actor);

					if(sum > 0) {
						// sum must be more than 0 when the actor is registerd in extras list.

						for(int k = 0; k < Env.roles; k++) {
							if(outputs[k] >= Env.liveCondition) {
								extras[k].add(actor);
							}
						}

						for(int k = 0; k < Env.storeRoles; k++) {
							double value = actor.getOperantResource(Env.roleNames[Env.roles + k]).getOutput();
							// 0.1 is tentative
							if(value > 0.1) {
							extras[Env.roles + k].add(actor);
							}
						}
					}
				}

				/*
				for(int k = 0; k < Env.valueRoles; k++) {
					// 0.1 is tamporal value setting.
					if(outputs[Env.roles + k] >= 0.1) {
						extras[Env.roles + k].add(actor);
					}
				}
				*/
			/*
			} else {
				// complete poor
				poorActors.add(actor);
			}
			*/
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
					capability *= (1 + friendCapability * Env.collaborationFactor / Env.friends);
				}
			}
		}

		return capability;
	}

	public void exchangeSurplus(Actor actor, Set<Actor> satisfiedActors, Set<Actor> poorActors,
			int[] exchangerIndex, List<Actor>[] extras,
			int iterationStart,	double exchangeRate, double searchLimit) {
		// exchange capability with collaboration
		double exchangeCapability = calculateExchangeCapability(actor);

		int roles = Env.roles + Env.storeRoles;

		if(memorized) {
			for(int i = 0; i < Env.storeRoles; i++) {
				exchangerIndex[Env.roles + i] = 0;
				// tentative;
				// actor.getExchangers()[i].clear();
			}
		}

		double[] extraVolume = new double[roles];

		// how many resources are overflowed over the live condition.
		int extraCount = 0;
		// int surplusVolume = 0;
		// int stockVolume = 0;

		for(int j = 0; j < Env.roles; j++) {
			OperantResource ownOtr = actor.getOperantResource(Env.roleNames[j]);
			double ownOutcome = ownOtr.getOutput();
			double diff = ownOutcome - Env.liveCondition;
			if(diff > 0) {
				extraVolume[j] = diff;
				extraCount++;
				// surplusVolume += diff;
			} else {
				extraVolume[j] = 0;
			}
		}

		for(int j = 0; j < Env.storeRoles; j++) {
			int jj = Env.roles + j;
			OperantResource ownOtr = actor.getOperantResource(Env.roleNames[jj]);
			double ownOutcome = ownOtr.getOutput();

			if(ownOutcome < -Env.EPSILON)
				System.out.println("Strange at 166");

			extraVolume[jj] = ownOutcome;
			// stockVolume += ownOutcome;
		}

		for(int i = iterationStart; extraCount != 0 && i < Env.searchIteration; i++) {
			int originalIndex = -1;
			double max = 0;
			for(int k = 0; k < Env.roles; k++) {
				double diff0;
				if(Env.enableStoring2) {
					diff0 = extraVolume[k] - extraVolume[Env.roles + k] * Env.storeRate;
				} else {
					diff0 = extraVolume[k] - extraVolume[Env.roles + k];
				}
				if(diff0 > max) {
					max = diff0;
					originalIndex = k;
				}
			}

			if(originalIndex == -1) break;

			/*
			// original volumme for maximizing value
			double payoffVolume = (surplusVolume - stockVolume) / (1 + exchangeRate);

			if(payoffVolume <= 0) {
				break;
			}
			*/

			int stockIndex = Env.roles + originalIndex;

			if(extras[stockIndex].isEmpty()) {
				break;
			}

			Actor partner = null;
			LinkedList<Actor> exchangers = null;
			if(memorized) {
				exchangers = actor.getExchangers()[stockIndex];
				try {
					partner = exchangers.get(exchangerIndex[stockIndex]);
					if(extras[stockIndex].contains(partner)) {
						exchangerIndex[stockIndex]++;
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
				int index = Env.rand.nextInt(extras[stockIndex].size());
				partner = extras[stockIndex].get(index);
				if(memorized && !exchangers.contains(partner)) {
					exchangers.addFirst(partner);
					exchangerIndex[stockIndex]++;
				}
			}

			double distance = actor.distance(partner);

			// distance must be less than the search limit.
			if(distance < searchLimit * exchangeCapability) {
				searchLimit -= distance / exchangeCapability;

				OperantResource partnerStockOtr = partner.getOperantResource(Env.roleNames[stockIndex]);
				double partnerStockVolume = partnerStockOtr.getOutput();

				if(partnerStockVolume < -Env.EPSILON) {
					System.out.println("Strange at 256");
				}

				boolean endFlag = false;

				int satisfiedCount = 0;
				for(int j = 0; j < Env.roles; j++) {
					OperantResource partnerOtr = partner.getOperantResource(Env.roleNames[j]);
					double partnerOriginalVolume = partnerOtr.getOutput();

					// original amount that partner wants (negative value)
					double diff = partnerOriginalVolume - Env.liveCondition;
					if(diff < 0) {
						if(!Env.enableStoring2) {
							// original amount that actor can pay
							double payoff = extraVolume[j];

							// original amount that partner can pay
							double volume = partnerStockVolume / exchangeRate;

							if(payoff > -diff) {
								if(-diff > volume) {
									endFlag = true;
								} else {
									volume = -diff;
									satisfiedCount++;
								}
							} else {
								if(payoff > volume) {
									endFlag = true;
								} else {
									volume = payoff;
									extraCount--;
								}
							}
							partnerStockVolume -= volume * exchangeRate;

							if(partnerStockVolume < -Env.EPSILON) {
								System.out.println("Strange at 294");
							}

							partnerOtr.addOutput(volume);
							extraVolume[stockIndex] += volume * exchangeRate;

							if(extraVolume[stockIndex] < -Env.EPSILON)
								System.out.println("Strange at 278");

							extraVolume[j] -= volume;
							if(extraVolume[j] < -Env.EPSILON)
								System.out.println("Strange at 295");

							if(endFlag) {
								extras[stockIndex].remove(partner);
								break;
							}
						} else {
							// original amount that actor wants
							double payoff = 0;

							if(j == originalIndex) {
								// calculate payoff point
								payoff = (extraVolume[originalIndex] - extraVolume[stockIndex] * Env.storeRate) / (1 + exchangeRate);
							} else {
								// original maximum amount that actor can pay
								payoff = extraVolume[j] - extraVolume[Env.roles + j] * Env.storeRate;
								// maximum original amount that actor wants
								double payoff0 = (extraVolume[originalIndex] - extraVolume[stockIndex] * Env.storeRate) / exchangeRate;
								if(payoff > payoff0) {
									payoff = payoff0;
								}
							}

							if(payoff < 0) {
								payoff = 0;
							}

							// trading volume
							double volume = partnerStockVolume * Env.storeRate / exchangeRate;

							if(volume < -Env.EPSILON) {
								System.out.println("Strange at 332");
							}

							if(payoff > -diff) {
								if(-diff > volume) {
									endFlag = true;
								} else {
									volume = -diff;
									satisfiedCount++;
								}
							} else {
								if(payoff > volume) {
									endFlag = true;
								} else {
									volume = payoff;
									extraCount--;
								}
							}
							partnerStockVolume -= volume / Env.storeRate * exchangeRate;

							if(partnerStockVolume < -Env.EPSILON) {
								System.out.println("Strange at 349");
							}

							partnerOtr.addOutput(volume);
							extraVolume[stockIndex] += volume / Env.storeRate * exchangeRate;
							if(extraVolume[stockIndex] < -Env.EPSILON)
								System.out.println("Strange at 327");
							extraVolume[j] -= volume;
							if(extraVolume[j] < -Env.EPSILON)
								System.out.println("Strange at 341");


							if(endFlag) {
								extras[stockIndex].remove(partner);
								break;
							}
						}
					} else {
						satisfiedCount++;
					}
				}

				partnerStockOtr.setOutput(partnerStockVolume);

				if(satisfiedCount == Env.roles) {
					satisfiedActors.add(partner);
					checkSatisfy(partner);
					poorActors.remove(partner);
					for(int k = 0; k < roles; k++) {
						extras[k].remove(partner);
					}
				}

				// no more extra amount of resources
				if(extraCount == 0) {
					break;
				}
			} else {
				break;
			}
		}

		for(int j = 0; j < Env.roles; j++) {
			if(extraVolume[j] < -Env.EPSILON)
				System.out.println("Volume is negative!");
			OperantResource ownOtr = actor.getOperantResource(Env.roleNames[j]);
			ownOtr.setOutput(extraVolume[j] + Env.liveCondition);
		}

		for(int j = 0; j < Env.storeRoles; j++) {
			if(extraVolume[Env.roles + j] < -Env.EPSILON)
				System.out.println("Stock Volume is negative!");
			OperantResource ownOtr = actor.getOperantResource(Env.roleNames[Env.roles + j]);
			ownOtr.setOutput(extraVolume[Env.roles + j]);
		}
	}

	/**
	 * exchange output method
	 */
	@Override
	public void exchange(Set<Actor> satisfiedActors) {
		int roles = Env.roles + Env.storeRoles;

		// actors who have extra amount of resources
		@SuppressWarnings("unchecked")
		List<Actor>[] extras = new ArrayList[roles];

		for(int k = 0; k < roles; k++) {
			extras[k] = new ArrayList<Actor>();
		}

		Set<Actor> originalSatisfiedActors = new HashSet<Actor>();

		Set<Actor> poorActors = new HashSet<Actor>();

		categorizeActors(satisfiedActors, poorActors, extras);

		/*
		for(int i = 0; i < roles; i++) {
			System.out.print(extras[i].size() + " ");
		}
		System.out.println();
		*/

		// copy of satisfied actor list
		originalSatisfiedActors.addAll(satisfiedActors);

		// System.out.println(Env.actorList.size());
		// System.out.println(poorActors.size());

		double[] outputs = new double[roles];

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
			exchangerIndex = new int[roles];
		}

		int size = Env.actorList.size();
		int bias = Env.rand.nextInt(size);

		for(int ii = 0; ii < size; ii++) {
			Actor actor = Env.actorList.get((ii + bias) % size);

			if(Env.learnFlag) {
				inc.clear();
				dec.clear();
			}

			double exchangeRate = Env.exchangeRate;

			OperantResource exchangeOtr = actor.getOperantResource(Term.EXCHANGING);
			exchangeRate *= (1 + exchangeOtr.getEffort() * exchangeOtr.getSkill());

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
					originalSatisfiedActors.remove(actor);
					// need not exchange
				}

				// for value added role
				if(Env.storeRoles > 0) {
					// the maximum distance for searching
					double searchLimit = Env.searchLimit;

					exchangeSurplus(actor, satisfiedActors, poorActors, exchangerIndex, extras,
							0,	exchangeRate, searchLimit);

				}
			} else if(poorActors.contains(actor)) {
				// actor does not satisfy the living condition

				for(int k = 0; k < roles; k++) {
					outputs[k] = actor.getOperantResource(Env.roleNames[k]).getOutput();
				}

				// the maximum distance for searching
				double searchLimit = Env.searchLimit;

				// exchange capability with collaboration
				double exchangeCapability = calculateExchangeCapability(actor);

				if(memorized) {
					for(int i = 0; i < roles; i++) {
						exchangerIndex[i] = 0;
						// tentative;
						// actor.getExchangers()[i].clear();
					}
				}

				// search exchanging partners

				int currentIteration = 0;

				// type index for the minimum output
				int minIndex = -1;
				int lastMinIndex = -1;

				int lastX = -1;
				int lastY = -1;
				int currentX = -1;
				int currentY = -1;

				for(currentIteration = 0; currentIteration < Env.searchIteration; currentIteration++) {
					// pick up a candidate of exchanging parner.
					Actor partner = null;
					LinkedList<Actor> exchangers = null;

					boolean contFlag = false;
					boolean breakFlag = false;

					double total = 0;
					for(int k = 0; k < Env.roles; k++) {
						total += outputs[k];
					}

					// If Env.stockRoles > 0 and total does not satisfy the living conditions,
					if(total < Env.liveCondition * Env.roles) {
						for(int k = 0; k < Env.storeRoles && !satisfiedActors.isEmpty(); k++) {

							int stockIndex = Env.roles + k;

							if(outputs[stockIndex] > 0) {
								if(memorized) {
									exchangers = actor.getExchangers()[stockIndex];

									try {
										partner = exchangers.get(exchangerIndex[stockIndex]);
										if(satisfiedActors.contains(partner)) {
											exchangerIndex[stockIndex]++;
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
									while(true) {
										int index = Env.rand.nextInt(Env.actorList.size());
										partner = Env.actorList.get(index);
										if(satisfiedActors.contains(partner)) {
										break;
										}
									}
									if(memorized && !exchangers.contains(partner)) {
										exchangers.addFirst(partner);
										exchangerIndex[stockIndex]++;
									}
								}

								double distance = actor.distance(partner);

								// distance must be less than the search limit.
								if(distance < searchLimit * exchangeCapability) {
									searchLimit -= distance / exchangeCapability;

									for(int j = 0; j < Env.roles; j++) {
										OperantResource partnerOtr = partner.getOperantResource(Env.roleNames[j]);
										double partnerOriginalVolume = partnerOtr.getOutput();

										// diff must be positive or zero
										double diff = partnerOriginalVolume - Env.liveCondition;

										if(diff > 0) {
											if(!Env.enableStoring2) {
												if(outputs[stockIndex] * exchangeRate > diff) {
													outputs[stockIndex] -= diff / exchangeRate;
													outputs[j] += diff;
													partnerOtr.setOutput(Env.liveCondition);
													OperantResource valueOtr = partner.getOperantResource(Env.roleNames[stockIndex]);
													valueOtr.addOutput(diff / exchangeRate);
												} else {
													outputs[j] += outputs[stockIndex] * exchangeRate;
													partnerOtr.addOutput(-outputs[stockIndex] * exchangeRate);
													OperantResource valueOtr = partner.getOperantResource(Env.roleNames[stockIndex]);
													valueOtr.addOutput(outputs[stockIndex]);
													outputs[stockIndex] = 0;
													extras[stockIndex].remove(actor);
													break;
												}
											} else {
												// Env.enableExchanging2 == true

												OperantResource partnerStockOtr = partner.getOperantResource(Env.roleNames[stockIndex]);

												// trading stock volume
												double payoff = (diff - partnerStockOtr.getOutput() * Env.storeRate) * exchangeRate / (1 + exchangeRate);

												if(outputs[stockIndex] > payoff / Env.storeRate) {
													outputs[stockIndex] -= payoff / Env.storeRate;
													outputs[j] += payoff / exchangeRate;
													partnerOtr.addOutput(- payoff / exchangeRate);
													partnerStockOtr.addOutput(payoff / Env.storeRate);
												} else {
													outputs[j] += outputs[stockIndex] * Env.storeRate / exchangeRate;
													partnerOtr.addOutput(-outputs[stockIndex] * Env.storeRate / exchangeRate);
													partnerStockOtr.addOutput(outputs[stockIndex]);
													outputs[stockIndex] = 0;
													extras[stockIndex].remove(actor);
													break;
												}
											}
										}
									}
								} else {
									breakFlag = true;
									break;
								}

								contFlag = true;;
								currentIteration++;
							}
						}

						if(breakFlag) {
							break;
						} else if(contFlag) {
							continue;
						}
					}

					// System.out.println(currentIteration + ", " + minIndex + ", " + total);

					if(localized && total < Env.liveCondition * (1.0 / Env.exchangeRate + (Env.roles - 1))
							&& minIndex >= 0) {
						// continue to use the same minIndex
					} else {

						// find the type with the minimum output
						double min = outputs[0];
						minIndex = 0;


						for(int k = 1; k < Env.roles; k++) {
							if(outputs[k] < min) {
								min = outputs[k];
								minIndex = k;
							}
						}
					}

					// if no partner exists, exit the loop
					if(extras[minIndex].isEmpty()) break;

					partner = null;

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

					if(localized) {
						int locX = -1;
						int locY = -1;

						if(minIndex != lastMinIndex) {
							int[][] locations = actor.getExchangingLocations();
							locX = locations[minIndex][0];
							locY = locations[minIndex][1];
						} else {
							locX = lastX;
							locY = lastY;
						}

						if(locX >= 0 && locY >= 0) {

							// need to change
							int repeat = 10;

							for(int j = 0; j < repeat; j++) {
								locX = locX + (int)(Env.windowSize * Env.rand.nextGaussian());
								locY = locY + (int)(Env.windowSize * Env.rand.nextGaussian());

								locX = (locX + Env.mapWidth) % Env.mapWidth;
								locY = (locY + Env.mapHeight) % Env.mapHeight;

								partner = Env.map[locX][locY];
								if(partner != null && partner != actor && extras[minIndex].contains(partner)) {
									currentX = locX;
									currentY = locY;
									break;
								} else {
									partner = null;
								}
							}
						} else {
							// no location memory
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

						if(localized) {
							currentX = partner.getX();
							currentY = partner.getY();

							int locations[][] = actor.getExchangingLocations();

							// if no location is memorized, store the current location
							// if(locations[minIndex][0] < 0 && locations[minIndex][1] < 0) {
								locations[minIndex][0] = currentX;
								locations[minIndex][1] = currentY;
							// }
						}
					}

					lastMinIndex = minIndex;

					double distance;

					if(localized) {
						if(lastX >= 0 && lastY >= 0) {
							// torus distance
							int x = currentX - lastX;
							int y = currentY - lastY;
							if(x < 0) x = -x;
							if(x > Env.mapWidth - x) {
								x = Env.mapWidth - x;
							}
							if(y < 0) y = -y;
							if(y > Env.mapHeight - y) {
								y = Env.mapHeight - y;
							}

							distance = Math.sqrt(x * x + y * y);
						} else {
							distance = actor.distance(partner);
						}
						lastX = currentX;
						lastY = currentY;
					} else {
						distance = actor.distance(partner);
					}

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
									sum += diff * exchangeRate;
									minus += diff * exchangeRate;
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
											if(Env.DEBUG) {
												if(partner.getOperantResource(Env.roleNames[k]).getOutput() < Env.liveCondition - Env.EPSILON) {
													System.err.printf("minus0 = %f, plus = %f%n", minus0, plus);
													System.err.println("Strange at 721");
												}
											}
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

							// partner actor becomes to satisfy the living condition.
							satisfiedActors.add(partner);
							checkSatisfy(partner);

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
								if(Env.DEBUG) {
									for(int k = 0; k < roles; k++) {
										actor.getOperantResource(Env.roleNames[k]).setOutput(outputs[k]);
									}
								}

								poorActors.remove(actor);
								satisfiedActors.add(actor);
								checkSatisfy(actor);

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

								// special loop control
								// after satisfaction, the actor can obtain value added resources.
								currentIteration--;
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
				for(int k = 0; k < roles; k++) {
					actor.getOperantResource(Env.roleNames[k]).setOutput(outputs[k]);
				}

				if(satisfiedActors.contains(actor) && Env.storeRoles > 0) {

					exchangeSurplus(actor, satisfiedActors, poorActors, exchangerIndex, extras,
							currentIteration, exchangeRate, searchLimit);

				}

				Env.eval.evaluate(actor);
			} else {
				// This part is not used in the current version.
				// Code must not enter this part.

				System.err.println("This code must not be used in the current version!");

				/*
				for(int k = 0; k < roles; k++) {
					outputs[k] = actor.getOperantResource(Env.roleNames[k]).getOutput();
				}

				double searchLimit = Env.searchLimit;

				double effort = calculateExchangeCapability(actor);

				for(int i = 0; i < Env.searchIteration; i++) {
					double min = outputs[0];
					int minIndex = 0;

					for(int k = 1; k < roles; k++) {
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

						for(int k = 0; k < roles; k++) {
							partnerOutputs[k] = partner.getOperantResource(Env.roleNames[k]).getOutput();

							if(partnerOutputs[k] + outputs[k] < Env.liveCondition * 2) {
								satisfy = false;
								break;
							}
						}

						if(satisfy) {
							for(int k = 0; k < roles; k++) {
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

								for(int k = 0; k < roles; k++) {
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
				*/
			}
		}
	}
}
