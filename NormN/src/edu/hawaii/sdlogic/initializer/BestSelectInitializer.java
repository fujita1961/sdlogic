package edu.hawaii.sdlogic.initializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.exchange.Exchange;
import edu.hawaii.sdlogic.operant.OperantResource;
import edu.hawaii.sdlogic.utils.Loop;

public class BestSelectInitializer extends Initializer {
	private int actors;
	private int pools;
	private int children;
	private double sigma;

	public BestSelectInitializer(int actors, int pools, int children, double sigma) {
		this.actors = actors;
		this.pools = pools;
		this.children = children;
		this.sigma = sigma;
	}

	@Override
	public void init() {
		initTypes();
		initFertilityAndOutputFunction();

		Env.rand = new Random();
		Env.map = new Actor[Env.mapWidth][Env.mapHeight];
		Env.actorList = new ArrayList<Actor>();

		double envSigmaOutput = Env.sigmaOutput;
		double envLiveCondition = Env.liveCondition;
		Exchange envExchange = Env.exchange;
		boolean envFriendFlag = Env.friendFlag;
		int envWindowSize = Env.windowSize;

		Env.sigmaOutput = 0;
		Env.friendFlag = false;
		Env.windowSize = 2;

		Actor[][] actorArray = new Actor[pools][actors];
		Actor[][] candidates = new Actor[pools * children][actors];
		Map<Integer, Actor> actorMap = new HashMap<Integer, Actor>();

		class Population implements Comparable<Population> {
			Actor[] actors;
			int satisfiedActors;

			Population() {
			}

			@Override
			public int compareTo(Population o) {
				return -(this.satisfiedActors - o.satisfiedActors);
			}
		}

		Population[] pops = new Population[pools * children];

		for(int i = 0; i < pops.length; i++) {
			pops[i] = new Population();
		}

		for(int i = 0; i < pools * children; i++) {
			for(int j = 0; j < actors; j++) {
				Actor actor = Actor.getInstance();
				actor.init();
				candidates[i][j] = actor;
			}
		}

		// Env.exchange = new NoExchange();
		Env.liveCondition = 1.0;
		Set<Actor> satisfiedActors = new HashSet<Actor>();
		// double condition = 0;

		for(int i = 0; i < pools; i++) {
			actorMap.clear();
			satisfiedActors.clear();
			Env.actorList.clear();

			for(int xx = 0; xx < Env.mapWidth; xx++) {
				for(int yy = 0; yy < Env.mapHeight; yy++) {
					Env.map[xx][yy] = null;
				}
			}

			for(int j = 0; j < actors; j++) {
				Actor actor = Actor.getInstance();
				actor.init();

				while(true) {
					int x = Env.mapWidth / 2 + (int)(Env.rand.nextGaussian() * sigma);
					int y = Env.mapHeight / 2 + (int)(Env.rand.nextGaussian() * sigma);

					Integer index = y * Env.mapWidth + x;
					if(actorMap.get(index) == null) {
						actorMap.put(index, actor);
						actorArray[i][j] = actor;
						actor.setX(x);
						actor.setY(y);
						break;
					}

					// System.out.println(x + ", " + y);
				}
				Env.actorList.add(actor);
				Env.map[actor.getX()][actor.getY()] = actor;
			}

			Loop.oneTurn(satisfiedActors, -1);

			// Env.draw.draw();

			//if(satisfiedActors.size() > actors / 20) {
			if(satisfiedActors.size() > 0) {
				System.out.println(satisfiedActors.size());
			// Canvas.waitForPoint();

			// double min = Double.MAX_VALUE;
				/*
				for(int j = 0; j < actors; j++) {
					double performance = actorArray[i][j].getPerformance();
					// condition += performance;
					// System.out.println(performance);

					// if(performance < min) {
					//	min = performance;
					//}

				}
				*/
			} else {
				i--;
			}
		}

		// Env.liveCondition = max * 0.9;
		// Env.liveCondition = condition / pools / actors;
		// Env.liveCondition = 1.0;

		// Env.exchange = envExchange;

		// int best = 0;

		for(int turn = 0; turn < 10000; turn++) {
			for(int i = 0; i < pools; i++) {
				Env.actorList.clear();
				satisfiedActors.clear();
				for(int j = 0; j < actors; j++) {
					int x0 = actorArray[i][j].getX();
					int y0 = actorArray[i][j].getY();
					cloneActor(actorArray[i][j], candidates[i  * children][j]);
					Env.map[x0][y0] = candidates[i * children][j];
					Env.actorList.add(candidates[i * children][j]);
				}

				Loop.oneTurn(satisfiedActors, turn);

				pops[i].actors = candidates[i * children];
				pops[i].satisfiedActors = satisfiedActors.size();

				for(int k = 1; k < children; k++) {
					Env.actorList.clear();
					satisfiedActors.clear();
					for(int xx = 0; xx < Env.mapWidth; xx++) {
						for(int yy = 0; yy < Env.mapHeight; yy++) {
							Env.map[xx][yy] = null;
						}
					}

					for(int j = 0; j < actors; j++) {
						int x0 = actorArray[i][j].getX();
						int y0 = actorArray[i][j].getY();
						candidates[i * children + k][j].setXY(x0, y0);
						Env.map[x0][y0] = candidates[i * children + k][j];
					}

					for(int j = 0; j < actors; j++) {
						Actor actor = candidates[i * children + k][j];

						if(actorArray[i][j].getPerformance() < Env.liveCondition) {
							actor.imitate(actorArray[i][j], Env.mapWidth / 2, Env.mapHeight / 2);
						} else {
							cloneActor(actorArray[i][j], actor);
						}
						Env.actorList.add(actor);
						// Env.map[actor.getX()][actor.getY()] = actor;
					}

					Loop.oneTurn(satisfiedActors, turn);

					pops[i + k * pools].actors = candidates[i * children + k];
					pops[i + k * pools].satisfiedActors = satisfiedActors.size();

					// Env.draw.draw();
				}
			}
			Arrays.sort(pops);

			int success = pops[0].satisfiedActors;

			if(success > actors * 0.8) {
				if(Env.liveCondition > 1.01) {
					Env.actorList.clear();
					for(int xx = 0; xx < Env.mapWidth; xx++) {
						for(int yy = 0; yy < Env.mapHeight; yy++) {
							Env.map[xx][yy] = null;
						}
					}

					for(int j = 0; j < actors; j++) {
						Actor actor0 = actorArray[0][j];
						cloneActor(pops[0].actors[j], actor0);
						Env.actorList.add(actor0);
						Env.map[actor0.getX()][actor0.getY()] = actor0;
						actor0.setAge(Env.rand.nextInt(80));

						System.out.println(printActor(actor0));
					}

					break;
				} else {
					Env.actorList.clear();
					for(int xx = 0; xx < Env.mapWidth; xx++) {
						for(int yy = 0; yy < Env.mapHeight; yy++) {
							Env.map[xx][yy] = null;
						}
					}

					for(int j = 0; j < actors; j++) {
						Actor actor0 = actorArray[0][j];
						cloneActor(pops[0].actors[j], actor0);
						Env.actorList.add(actor0);
						Env.map[actor0.getX()][actor0.getY()] = actor0;
						actor0.setAge(Env.rand.nextInt(80));
					}

					Env.draw.draw();

					Env.liveCondition += 0.1;
					// best = 0;
				}
			} else {
				// if(success > best) {
					for(int i = 0; i < pools; i++) {
						for(int j = 0; j < actors; j++) {
							cloneActor(pops[i].actors[j], actorArray[i][j]);
						}
					}
				// }
			}

			System.out.print(Env.liveCondition + ", ");

			for(int i = 0; i < pools; i++) {
				System.out.print(pops[i].satisfiedActors + " ");
			}
			System.out.println();
		}

		Env.actorList.clear();
		for(int xx = 0; xx < Env.mapWidth; xx++) {
			for(int yy = 0; yy < Env.mapHeight; yy++) {
				Env.map[xx][yy] = null;
			}
		}

		for(int j = 0; j < actors; j++) {
			Actor actor0 = actorArray[0][j];
			cloneActor(pops[0].actors[j], actor0);
			Env.actorList.add(actor0);
			Env.map[actor0.getX()][actor0.getY()] = actor0;
			actor0.setAge(Env.rand.nextInt(80));
		}

		Env.draw.draw();

		Env.sigmaOutput = envSigmaOutput;
		Env.liveCondition = envLiveCondition;
		Env.exchange = envExchange;
		Env.friendFlag = envFriendFlag;
		Env.windowSize = envWindowSize;
	}

	private static void cloneActor(Actor from, Actor to) {
		to.setX(from.getX());
		to.setY(from.getY());
		to.setAge(from.getAge());
		to.setLifeSpan(from.getLifeSpan());
		to.setPerformance(from.getPerformance());
		for(int i = 0; i < Env.roleNames.length; i++) {
			String ortName = Env.roleNames[i];

			OperantResource fromOrt = from.getOperantResource(ortName);
			OperantResource toOrt = to.getOperantResource(ortName);

			toOrt.setSkill(fromOrt.getSkill());
			toOrt.setEffort(fromOrt.getEffort());
		}
	}

	private static String printActor(Actor actor) {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("%d %d ", actor.getX(), actor.getY()));
		for(int i = 0; i < Env.roleNames.length; i++) {
			String ortName = Env.roleNames[i];
			OperantResource ort = actor.getOperantResource(ortName);

			sb.append(String.format("%f %f ", ort.getSkill(), ort.getEffort()));
		}

		return sb.toString();
	}
}
