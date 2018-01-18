package edu.hawaii.sdlogic.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.Term;
import edu.hawaii.sdlogic.eval.SDLogicEvaluation;
import edu.hawaii.sdlogic.initializer.Initializer;
import edu.hawaii.sdlogic.operant.OperantResource;
import edu.hawaii.sdlogic.output.CooperativeCalculateOutput;
import edu.hawaii.utils.Canvas;

public class Loop {
	/**
	 * oneTurn execution
	 *   core loop of this simulator
	 * @param satisfiedActors
	 */
	public static void oneTurn(Set<Actor> satisfiedActors) {
		for(Actor actor: Env.actorList) {
			actor.setPerformance(0);
		}

		OperantResource[] otrs = new OperantResource[Env.types];
		double[] outputs = new double[Env.types];
		double[] outputs0 = new double[Env.types];
		double[] cooperates = new double[Env.types];

		// @SuppressWarnings("unused")
		double totalOutput = 0;
		int totalActors = 0;

		for(Actor actor: Env.actorList) {
			if(actor.getPerformance() == 0) {
				for(int j = 0; j < Env.types; j++) {
					Env.outputs[j].calculate(actor);
				}

				if(Env.outputs[0] instanceof CooperativeCalculateOutput) {

					OperantResource collaboOtr = actor.getOperantResource(Term.COLLABORATING);

					for(int j = 0; j < Env.types; j++) {
						otrs[j] = actor.getOperantResource(Env.typeNames[j]);
						outputs[j] = otrs[j].getOutput();
						outputs0[j] = outputs[j];
					}

					OperantResource actorExchangeOtr = actor.getOperantResource(Term.EXCHANGING);
					double cooperativeActorExchange =  actorExchangeOtr.getEffort() * actorExchangeOtr.getSkill();

					for(int j = 0; j < Env.friends; j++) {
						int x0;
						int y0;

						Actor partner = null;

						while(true) {
							x0 = Env.rand.nextInt(Env.collaborativeRange * 2 + 1) - Env.collaborativeRange;
							y0 = Env.rand.nextInt(Env.collaborativeRange * 2 + 1) - Env.collaborativeRange;
							if(x0 != 0 || y0 != 0) {
								partner = Env.map[(actor.getX() + x0 + Env.mapWidth) % Env.mapWidth][(actor.getY() + y0 + Env.mapHeight) % Env.mapHeight];
								if(partner == null) break;
								boolean cont = false;
								if(Env.friendFlag) {
									for(int k = 0; k < Env.friends; k++) {
										if(partner == actor.getFriend(k)) {
											cont = true;
											break;
										}
									}
								} else {
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
							OperantResource partnerCollaboOtr = partner.getOperantResource(Term.COLLABORATING);

							for(int k = 0; k < Env.types; k++) {
								OperantResource partnerOtr = partner.getOperantResource(Env.typeNames[k]);
								if(collaboOtr != null) {
									cooperates[k] = partnerOtr.getEffort() * partnerOtr.getSkill()
											* collaboOtr.getSkill() * partnerCollaboOtr.getSkill();
								} else {
									cooperates[k] = partnerOtr.getEffort() * partnerOtr.getSkill();
								}
								// outputs[k] *= (1 + cooperate / Env.friends);
								// outputs0[k] is not equal to outputs[k]
								value += outputs0[k] * (1 + cooperates[k] / Env.friends);
							}

							// exchange
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
							// partner == null
							if(Env.macroFlag1) {
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

						if(Env.friendFlag && value < actor.getFriendValue(j)) {
							/*
							&& Math.abs(actor.getFriend(j).getX() - actor.getX()) <= Env.collaborativeRange
							&& Math.abs(actor.getFriend(j).getY() - actor.getY()) <= Env.collaborativeRange) {
							*/
							partner = actor.getFriend(j);
							value = 0;

							OperantResource partnerCollaboOtr = partner.getOperantResource(Term.COLLABORATING);

							for(int k = 0; k < Env.types; k++) {
								OperantResource partnerOtr = partner.getOperantResource(Env.typeNames[k]);

								double cooperate;

								if(collaboOtr != null) {
									cooperate = partnerOtr.getEffort() * partnerOtr.getSkill() *
											collaboOtr.getSkill() * partnerCollaboOtr.getSkill();
								} else {
									cooperate = partnerOtr.getEffort() * partnerOtr.getSkill();
								}

								outputs[k] *= (1 + cooperate / Env.friends);
								// outputs[k] = otrs[k].getOutput() * (1 + cooperate / Env.friends);
								value += outputs0[k] * (1 + cooperate / Env.friends);
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
							for(int k = 0; k < Env.types; k++) {
								outputs[k] *= (1 + cooperates[k] / Env.friends);
							}
						}

						actor.setFriend(j, partner);
						actor.setFriendValue(j, value);
					}

					for(int k = 0; k < Env.types; k++) {
						double output = outputs[k];
						if(output < otrs[k].getOutput())
							System.out.println("Strange");
						output *= (1 + Env.rand.nextGaussian() * Env.sigmaOutput);
						if(output < 0) output = 0;
						otrs[k].setOutput(output);
						otrs[k].setOutput0(output);
					}
				}

				/*
				for(int k = 0; k < Env.types; k++) {
					totalOutput += actor.getOperantResource(Env.typeNames[k]).getOutput();
				}
				*/

				{
					int x = actor.getX() - Env.mapWidth / 2;;
					int y = actor.getY() - Env.mapHeight / 2;

					if(x * x + y * y > 100) {
						totalActors++;
						for(int k = 0; k < Env.types; k++) {
							totalOutput += actor.getOperantResource(Env.typeNames[k]).getOutput();
						}

					}
				}

				Env.eval.evaluate(actor);
			}

			if(Env.macroFlag3) {
				int[] counts = new int[Env.typeNames.length];
				boolean found = false;

				for(int x1 = -1; x1 <= 1 && !found; x1++) {
					for(int y1 = -1; y1 <= 1 && !found; y1++) {
						if(x1 !=0 || y1 != 0) {
							int x2 = (actor.getX() + x1 + Env.mapWidth) % Env.mapWidth;
							int y2 = (actor.getY() + y1 + Env.mapHeight) % Env.mapHeight;
							Actor neighbor = Env.map[x2][y2];
							if(neighbor != null) {
								for(int j = 0; j < Env.typeNames.length; j++) {
									OperantResource ort = neighbor.getOperantResource(Env.typeNames[j]);
									if(ort.getEffort() > 0.5) {
										counts[j]++;
										if(counts[j] > 4) {
											double effort = actor.getOperantResource(Env.typeNames[j]).getEffort();
											if(effort < 0.5) {
												actor.getOperantResource(Env.typeNames[j]).setEffort(effort + 1);

												double sum = 0;
												double[] efforts = new double[Env.typeNames.length];

												for(int k = 0; k < Env.typeNames.length; k++) {
													efforts[k] = actor.getOperantResource(Env.typeNames[k]).getEffort();
													sum+= efforts[k];
												}

												for(int k = 0; k < Env.typeNames.length; k++) {
													actor.getOperantResource(Env.typeNames[k]).setEffort(efforts[k] / sum);
												}
											}

											found = true;
											break;
										}
									}
								}
							}

						}
					}
				}
			}
		}

		if(Env.eval instanceof SDLogicEvaluation) {
			Env.exchange.exchange(satisfiedActors);
		}

		int[] population = new int[Env.typeNames.length + 1];
		double[][] outcome = new double[Env.typeNames.length + 1][Env.types];

		for(Actor actor: Env.actorList) {
			boolean found = false;
			for(int i = 0; i < Env.typeNames.length; i++) {
				OperantResource ort = actor.getOperantResource(Env.typeNames[i]);
				if(ort.getEffort() > 0.66) {
					population[i]++;
					found = true;
					for(int j = 0; j < Env.types; j++) {
						OperantResource ort2 = actor.getOperantResource(Env.typeNames[j]);
						outcome[i][j] += ort2.getOutput0();
					}
					break;
				}
			}
			if(!found) {
				population[Env.typeNames.length]++;
				for(int j = 0; j < Env.types; j++) {
					OperantResource ort2 = actor.getOperantResource(Env.typeNames[j]);
					outcome[Env.typeNames.length][j] += ort2.getOutput0();
				}
			}
		}

		if(Env.printStatistics) {
			// System.out.println("average of output: " + totalOutput / Env.actorList.size() / Env.types);
			System.out.printf("%d ", Env.actorList.size());
			for(int i = 0; i < Env.typeNames.length + 1; i++) {
				System.out.printf("%d ", population[i]);
				for(int j = 0; j < Env.types; j++) {
					if(population[i] != 0) {
						System.out.printf("%8.6f ", outcome[i][j] / population[i]);
					} else {
						System.out.print("0.0 ");
					}
				}
			}
			// System.out.printf("%8.6f%n", totalOutput / Env.actorList.size() / Env.types);
			if(totalActors > 0) {
				System.out.printf("%8.6f ", totalOutput / totalActors / Env.types);
			} else {
				System.out.printf("%8.6f ", 0.0d);
			}
		}
	}

	/**
	 * main loop method
	 * @return false if all actors die at the middle stage of the loop
	 */
	public static boolean loop() {
		int prevX = Canvas.getPointedX();
		int prevY = Canvas.getPointedY();

		// turn is the number of the loop
		for(int turn = 0; turn < Env.loops; turn++) {
			// if the actor list becomes empty before the end of the loop, return false
			if(Env.actorList.isEmpty()) {
				if(turn < Env.loops) {
					return false;
				} else {
					return true;
				}
			}

			// Click mouse if you want to pend loop execution.
			int x = Canvas.getPointedX();
			int y = Canvas.getPointedY();

			if(x != prevX || y != prevY) {
				// wait for click
				Canvas.waitForPoint();
				x = Canvas.getPointedX();
				y = Canvas.getPointedY();

				prevX = x;
				prevY = y;
			}

			double min = 0;

			// satisfied actors must be defined outside of the oneTurn.
			Set<Actor> satisfiedActors = new HashSet<Actor>();

			oneTurn(satisfiedActors);

			if(Env.changePopulation && turn > 0) {
				// change population mode

				List<Actor> removeActors = new ArrayList<Actor>();
				List<Actor> addActors = new ArrayList<Actor>();
				for(Actor actor: Env.actorList) {
					if(satisfiedActors.contains(actor)) {
						int age = actor.incrementAge();
						int birth = 5;

						if(age > actor.getLifeSpan() && Env.actorList.size() > Env.actors * 5) {
							// if an actor reaches the life span, the actor die.

							Env.map[actor.getX()][actor.getY()] = null;
							removeActors.add(actor);
						} else if(age % birth == 0) {
							// if an actor does not reach the life span, it generates a child every 5 years.

							Actor newActor = Actor.getInstance();
							newActor.init();

							// the first age is given by a random value less than 5 years.
							newActor.setAge(Env.rand.nextInt(birth));

							boolean found = false;
							for(int j = 0; j < 5; j++) {
								int x0 = Env.rand.nextInt(Env.mapWidth);
								int y0 = Env.rand.nextInt(Env.mapHeight);

								if(Env.map[x0][y0] == null) {
									Env.map[x0][y0] = newActor;
									addActors.add(newActor);
									newActor.setXY(x0, y0);
									found = true;
									break;
								}
							}

							// if empty space is found, a new child imitate his parent.
							if(found) {
								newActor.imitate(actor);
							} else {
								Actor.reclaim(newActor);
							}
						}
					} else {
						// actor.regenerate();
						Env.map[actor.getX()][actor.getY()] = null;
						removeActors.add(actor);
					}
				}

				// remove actors
				for(Actor actor: removeActors) {
					Env.actorList.remove(actor);
					Actor.reclaim(actor);
				}

				// add new actors.
				for(Actor actor: addActors) {
					if(Env.actorList.size() < Env.mapWidth * Env.mapHeight * 0.5) {
						Env.actorList.add(actor);
					} else {
						Env.map[actor.getX()][actor.getY()] = null;
						Actor.reclaim(actor);
					}
				}
			} else if(Env.survive) {
				// survival mode

				for(Actor actor: Env.actorList) {
					if(satisfiedActors.contains(actor)) {

					} else {
						actor.regenerate();
					}
				}
			} else {
				// ratio death mode

				int counter = 0;
				int minCounter = (int)(Env.actors * Env.deathRate);
				// double sum = 0;

				for(Actor actor: Env.actorList) {
					double value = actor.getPerformance();
					// sum += value;
					if(counter < minCounter) {
						counter++;
						if(value > min) {
							min = value;
						}
					} else if(min > value) {
						min = value;
					}
				}

				// System.out.println(i + ", " + sum / Env.actorList.size() + ", " + min);

				// Actor[] deathList = new Actor[minCounter];
				counter = 0;

				for(Actor actor: Env.actorList) {
					//if(actor.getPerformance() <= min || actor.getPerformance() < Env.liveCondition * 0.8) {
					// if(actor.getPerformance() < Env.liveCondition * 1.0) {
					if(actor.getPerformance() <= min) {
						if(counter < minCounter) {
							// deathList[counter++] = actor;
						actor.regenerate();;
						}
					}
				}
			}

			if(Env.printCollaborationCountFlag) {
				Print.printCollaborationCount();
			}

			if(Env.printSkillLevelsFlag) {
				Print.printSkillLevels();
			}

			if(Env.printEntropyFlag) {
				Env.entropy.printMaxEntropy();
				Env.entropy.friendEntropy();
				Env.entropy.entropy();
			}

			if(Env.printCollaborationCountFlag || Env.printSkillLevelsFlag || Env.printEntropyFlag
					|| Env.printStatistics) {
				System.out.println();
			}

			if(turn % Env.drawInterval == 0) {
				Env.draw.draw();
			}

			if(turn % Env.animationGIFInterval == 0) {
				Canvas.writeAnimationGIF();
			}
		}

		Env.draw.draw();

		return true;
	}

	public static void fin() {
		if(Env.animationGIFFileName != null) {
			Canvas.finishAnimationGIF();
		}
	}

	public static void mainLoop() {
		Initializer.metaInit();
		Env.initializer.init();
		Env.draw.draw();

		while(!Loop.loop()) {
			Env.initializer.init();
		}

		fin();
	}
}
