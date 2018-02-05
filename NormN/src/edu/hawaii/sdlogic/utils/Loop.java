package edu.hawaii.sdlogic.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.eval.SDLogicEvaluation;
import edu.hawaii.sdlogic.initializer.Initializer;
import edu.hawaii.sdlogic.operant.OperantResource;
import edu.hawaii.utils.Canvas;

public class Loop {
	/**
	 * oneTurn execution
	 *   core loop of this simulator
	 * @param satisfiedActors
	 */
	public static void oneTurn(Set<Actor> satisfiedActors, int turn) {
		for(Actor actor: Env.actorList) {
			actor.setPerformance(0);
		}

		// @SuppressWarnings("unused")
		double totalOutput = 0;
		int totalActors = 0;

		// clear share value
		if(Env.shareRate > 0) {
			for(Actor actor: Env.actorList) {
				for(int i = 0; i < Env.roles; i++) {
					OperantResource ort = actor.getOperantResource(Env.roleNames[i]);
					ort.setShare(0.0d);
				}
			}
		}

		for(Actor actor: Env.actorList) {
			if(actor.getPerformance() == 0) {
				Env.output.calculateAll(actor);

				{
					int x = actor.getX() - Env.mapWidth / 2;;
					int y = actor.getY() - Env.mapHeight / 2;

					if(x * x + y * y > 100) {
						totalActors++;
						for(int k = 0; k < Env.roles; k++) {
							totalOutput += actor.getOperantResource(Env.roleNames[k]).getOutput();
						}

					}
				}

				Env.eval.evaluate(actor);
			}

			if(Env.macroFlag3) {
				int[] counts = new int[Env.roleNames.length];
				boolean found = false;

				for(int x1 = -1; x1 <= 1 && !found; x1++) {
					for(int y1 = -1; y1 <= 1 && !found; y1++) {
						if(x1 !=0 || y1 != 0) {
							int x2 = (actor.getX() + x1 + Env.mapWidth) % Env.mapWidth;
							int y2 = (actor.getY() + y1 + Env.mapHeight) % Env.mapHeight;
							Actor neighbor = Env.map[x2][y2];
							if(neighbor != null) {
								for(int j = 0; j < Env.roleNames.length; j++) {
									OperantResource ort = neighbor.getOperantResource(Env.roleNames[j]);
									if(ort.getEffort() > 0.5) {
										counts[j]++;
										if(counts[j] > 4) {
											double effort = actor.getOperantResource(Env.roleNames[j]).getEffort();
											if(effort < 0.5) {
												actor.getOperantResource(Env.roleNames[j]).setEffort(effort + 1);

												double sum = 0;
												double[] efforts = new double[Env.roleNames.length];

												for(int k = 0; k < Env.roleNames.length; k++) {
													efforts[k] = actor.getOperantResource(Env.roleNames[k]).getEffort();
													sum+= efforts[k];
												}

												for(int k = 0; k < Env.roleNames.length; k++) {
													actor.getOperantResource(Env.roleNames[k]).setEffort(efforts[k] / sum);
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

		// add share value
		if(Env.shareRate > 0) {
			for(Actor actor: Env.actorList) {
				for(int i = 0; i < Env.roles; i++) {
					OperantResource ort = actor.getOperantResource(Env.roleNames[i]);
					double share = ort.getShare();
					ort.addOutput(share);
					ort.addOutput0(share);
				}
			}
		}

		if(Env.eval instanceof SDLogicEvaluation) {
			Env.exchange.exchange(satisfiedActors);
		}

		int[] population = new int[Env.roleNames.length + 1];
		double[][] outcome = new double[Env.roleNames.length + 1][Env.roles];

		for(Actor actor: Env.actorList) {
			boolean found = false;
			for(int i = 0; i < Env.roleNames.length; i++) {
				OperantResource ort = actor.getOperantResource(Env.roleNames[i]);
				if(ort.getEffort() > 0.66) {
					population[i]++;
					found = true;
					for(int j = 0; j < Env.roles; j++) {
						OperantResource ort2 = actor.getOperantResource(Env.roleNames[j]);
						outcome[i][j] += ort2.getOutput0();
					}
					break;
				}
			}
			if(!found) {
				population[Env.roleNames.length]++;
				for(int j = 0; j < Env.roles; j++) {
					OperantResource ort2 = actor.getOperantResource(Env.roleNames[j]);
					outcome[Env.roleNames.length][j] += ort2.getOutput0();
				}
			}
		}

		if((turn + 1) % Env.printInterval == 0) {
			if(Env.printStatistics) {
				// System.out.println("average of output: " + totalOutput / Env.actorList.size() / Env.types);
				System.out.printf("%d ", Env.actorList.size());
				for(int i = 0; i < Env.roleNames.length + 1; i++) {
					System.out.printf("%d ", population[i]);
					for(int j = 0; j < Env.roles; j++) {
						if(population[i] != 0) {
							System.out.printf("%8.6f ", outcome[i][j] / population[i]);
						} else {
							System.out.print("0.0 ");
						}
					}
				}
				// System.out.printf("%8.6f%n", totalOutput / Env.actorList.size() / Env.types);
				if(totalActors > 0) {
					System.out.printf("%8.6f ", totalOutput / totalActors / Env.roles);
				} else {
					System.out.printf("%8.6f ", 0.0d);
				}
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
		for(int turn = 0; turn < Env.periods; turn++) {
			// if the actor list becomes empty before the end of the loop, return false
			if(Env.actorList.isEmpty()) {
				if(turn < Env.periods) {
					return false;
				} else {
					return true;
				}
			}

			if((turn + 1) % Env.printInterval == 0) {
				if(Env.printCollaborationCountFlag || Env.printSkillLevelsFlag || Env.printEntropyFlag
						|| Env.printStatistics || Env.printExchangeLinksFlag) {
					System.out.print((turn + 1) + " ");
				}
			}

			// Click mouse if you want to pend loop execution.
			int x = Canvas.getPointedX();
			int y = Canvas.getPointedY();

			if(x != prevX || y != prevY) {

				if(Env.drawRelation) {
					Env.draw.drawRelation(x, y);
				}

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

			oneTurn(satisfiedActors, turn);

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

			if((turn + 1) % Env.printInterval == 0) {

				if(Env.printSkillLevelsFlag) {
					Print.printSkillLevels();
				}

				if(Env.printExchangeLinksFlag) {
					Print.printExchangeLinks();
				}

				if(Env.printEntropyFlag) {
					Env.entropy.printMaxEntropy();
					Env.entropy.friendEntropy();
					Env.entropy.entropy();
				}

				if(Env.printCollaborationCountFlag) {
					Print.printCollaborationCount();
				}

				if(Env.printCollaborationCountFlag || Env.printSkillLevelsFlag || Env.printEntropyFlag
						|| Env.printStatistics || Env.printExchangeLinksFlag) {
					System.out.println();
				}
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
		for(int i = 0; i < Env.repeats; i++) {
			Initializer.metaInit();
			Env.initializer.init();
			Env.draw.draw();

			while(!Loop.loop()) {
				Env.initializer.init();
			}

			fin();
		}
	}
}
