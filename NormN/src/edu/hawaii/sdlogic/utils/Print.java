package edu.hawaii.sdlogic.utils;

import java.util.HashMap;
import java.util.Map;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.operant.OperantResource;

public class Print {
	/**
	 * analyze skill levels for each role
	 */
	public static void printSkillLevels() {
		double[][] total = new double[Env.roleNames.length][Env.roleNames.length];
		int[] count = new int[Env.roleNames.length];

		for(Actor actor: Env.actorList) {
			int role = actor.getActorType();
			count[role]++;
			for(int i = 0; i < Env.roleNames.length; i++) {
				OperantResource ort = actor.getOperantResource(Env.roleNames[i]);
				total[role][i] += ort.getSkill();
			}
		}

		for(int i = 0; i < Env.roleNames.length; i++) {
			for(int j = 0; j < Env.roleNames.length; j++) {
				System.out.printf("%6.4f ", total[i][j] / count[i]);
			}
		}
	}

	/**
	 * analyze exchange links
	 */
	public static void printExchangeLinks() {
		int roles = Env.roles + Env.stockRoles;

		int[][] total = new int[Env.roleNames.length][roles];
		int[] count = new int[Env.roleNames.length];

		for(Actor actor: Env.actorList) {
			int role = actor.getActorType();
			count[role]++;
			for(int i = 0; i < roles; i++) {
				int links = actor.getExchangers()[i].size();
				total[role][i] += links;
			}
		}

		for(int i = 0; i < Env.roleNames.length; i++) {
			System.out.printf("%d ", count[i]);
			for(int j = 0; j < roles; j++) {
				System.out.printf("%d ", total[i][j]);
			}
		}
	}

	/**
	 * count collaboration friend numbers
	 */
	public static void printCollaborationCount() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int max = 1;

		for(Actor actor: Env.actorList) {
			int num = actor.getReverseFriends().size();
			int newNum;
			Integer obj = map.get(num);
			if(obj == null) {
				newNum = 1;
			} else {
				newNum = obj + 1;
			}
			if(num > max) {
				max = num;
			}
			map.put(num, newNum);
		}

		for(int i = 1; i <= max; i++) {
			Integer obj = map.get(i);
			int num;
			if(obj == null) {
				num = 0;
			} else {
				num = obj;
			}
			System.out.print(num + " ");
		}
	}

	public static void printStatistics() {
		int roles = Env.roles + Env.stockRoles;

		double totalOutput = 0;
		double totalOutput1 = 0;
		double totalValue = 0;
		int totalActors = 0;

		int[] population = new int[Env.roleNames.length + 1];
		double[][] outcome = new double[Env.roleNames.length + 1][roles];

		for(Actor actor: Env.actorList) {
			boolean found = false;
			for(int i = 0; i < Env.roleNames.length; i++) {
				OperantResource ort = actor.getOperantResource(Env.roleNames[i]);
				if(ort.getEffort() > 0.66) {
					population[i]++;
					found = true;
					for(int j = 0; j < roles; j++) {
						OperantResource ort2 = actor.getOperantResource(Env.roleNames[j]);
						outcome[i][j] += ort2.getOutput0();
					}
					break;
				}
			}
			if(!found) {
				population[Env.roleNames.length]++;
				for(int j = 0; j < roles; j++) {
					OperantResource ort2 = actor.getOperantResource(Env.roleNames[j]);
					outcome[Env.roleNames.length][j] += ort2.getOutput0();
				}
			}

			{
				int x = actor.getX() - Env.mapWidth / 2;;
				int y = actor.getY() - Env.mapHeight / 2;

				if(x * x + y * y > 100) {
					totalActors++;
					for(int k = 0; k < Env.roles; k++) {
						totalOutput += actor.getOperantResource(Env.roleNames[k]).getOutput0();
						totalOutput1 += actor.getOperantResource(Env.roleNames[k]).getOutput();
					}

					totalValue += actor.getOperantResource(Env.roleNames[Env.roles + Env.stockRoles]).getOutput();
				}
			}
		}

		// System.out.println("average of output: " + totalOutput / Env.actorList.size() / Env.types);
		System.out.printf("%d ", Env.actorList.size());
		for(int i = 0; i < Env.roleNames.length + 1; i++) {
			System.out.printf("%d ", population[i]);
			for(int j = 0; j < roles; j++) {
				if(population[i] != 0) {
					System.out.printf("%8.6f ", outcome[i][j] / population[i]);
				} else {
					System.out.print("0.0 ");
				}
			}
		}
		for(int j = 0; j < roles; j++) {
			int pop = 0;
			double out = 0;
			for(int i = 0; i < Env.roleNames.length + 1; i++) {
				pop += population[i];
				out += outcome[i][j];
			}

			if(pop != 0) {
				System.out.printf("%8.6f ", out / pop);
			} else {
				System.out.print("0.0 ");
			}
		}
		// System.out.printf("%8.6f%n", totalOutput / Env.actorList.size() / Env.types);
		if(totalActors > 0) {
			System.out.printf("%8.6f ", totalOutput / totalActors / Env.roles);
			System.out.printf("%8.6f ", totalOutput1 / totalActors / Env.roles);
			System.out.printf("%8.6f ", totalValue / totalActors);
		} else {
			System.out.printf("%8.6f %8.6f %8.6f ", 0.0d, 0.0d, 0.0d);
		}
	}

	public static void printActor(int x, int y) {
		int xx = (x - Env.displayMargin) / Env.cellWidth;
		int yy = (y - Env.displayMargin) / Env.cellHeight;
		if(xx < 0 || xx >= Env.mapWidth || yy < 0 || yy >= Env.mapHeight) {
			return;
		}
		Actor actor = Env.map[xx][yy];
		if(actor == null) {
			System.out.println("No Actor");
		} else {
			for(int i = 0; i < Env.roleNames.length; i++) {
				OperantResource ort = actor.getOperantResource(Env.roleNames[i]);
				double effort = ort.getEffort();
				double skill = ort.getSkill();

				System.out.printf("(%6.4f %6.4f) ", skill, effort);
			}
			System.out.println();
		}
	}
}
