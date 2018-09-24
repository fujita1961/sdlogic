package edu.hawaii.sdlogic.utils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.operant.OperantResource;

public class Print {
	public static PrintStream out = System.out;

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
				out.printf("%6.4f ", total[i][j] / count[i]);
			}
		}
	}

	/**
	 * analyze exchange links
	 */
	public static void printExchangeLinks() {
		int roles = Env.roles + Env.storeRoles;

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
			out.printf("%d ", count[i]);
			for(int j = 0; j < roles; j++) {
				out.printf("%d ", total[i][j]);
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
			out.print(num + " ");
		}
	}

	public static void printCenterOfGravity() {
		long sumX = 0;
		long sumY = 0;
		long sumX2 = 0;
		long sumY2 = 0;

		for(Actor actor: Env.actorList) {
			int x = actor.getX();
			int y = actor.getY();

			sumX += x;
			sumY += y;
			sumX2 += x * x;
			sumY2 += y * y;
		}

		double averageX = (double)sumX / (double)Env.actorList.size();
		double averageY = (double)sumY / (double)Env.actorList.size();
		// double sigmaX = Math.sqrt((double)sumX2 / (double)Env.actorList.size() - averageX * averageX);
		// double sigmaY = Math.sqrt((double)sumY2 / (double)Env.actorList.size() - averageY * averageY);

		double sumD = 0;
		double sumD2 = 0;
		double[] sumDistance = new double[Env.roleNames.length + 1];
		double[] sumDistance2 = new double[Env.roleNames.length + 1];
		int[] size = new int[Env.roleNames.length + 1];

		for(Actor actor: Env.actorList) {
			int x = actor.getX();
			int y = actor.getY();

			double d2 = (x - averageX) * (x - averageX) + (y - averageY) * (y - averageY);
			double d = Math.sqrt(d2);

			sumD += d;
			sumD2 += d2;

			boolean found = false;
			for(int i = 0; i < Env.roleNames.length; i++) {
				OperantResource ort = actor.getOperantResource(Env.roleNames[i]);
				if(ort.getEffort() > 0.5) {
					sumDistance[i] += d;
					sumDistance2[i] += d2;
					size[i]++;
					found = true;
					break;
				}
			}
			if(!found) {
				sumDistance[Env.roleNames.length] += d;
				sumDistance2[Env.roleNames.length] += d2;
				size[Env.roleNames.length]++;
			}
		}

		double averageD = sumD / Env.actorList.size();
		double sigmaD = Math.sqrt(sumD2 / Env.actorList.size() - averageD * averageD);
		out.printf("%d %7.3f %7.3f %7.3f %7.3f ", Env.actorList.size(), averageX, averageY, averageD, sigmaD);

		for(int i = 0; i < Env.roleNames.length + 1; i++) {
			double averageDistance = (double)sumDistance[i] / size[i];
			double sigmaDistance = Math.sqrt((double)sumDistance2[i] / size[i] - averageDistance * averageDistance);

			out.printf("%d %7.3f %7.3f ", size[i], averageDistance, sigmaDistance);
		}
	}

	public static void printStatistics() {
		int roles = Env.roles + Env.storeRoles;

		double totalOutput = 0;
		double totalOutput1 = 0;
		double totalValue = 0;
		int totalActors = 0;

		int[] population = new int[Env.roleNames.length + 1];
		double[][] outcome0 = new double[Env.roleNames.length + 1][roles];
		double[][] outcome1 = new double[Env.roleNames.length + 1][roles];

		for(Actor actor: Env.actorList) {
			boolean found = false;
			for(int i = 0; i < Env.roleNames.length; i++) {
				OperantResource ort = actor.getOperantResource(Env.roleNames[i]);
				if(ort.getEffort() > 0.5) {
					population[i]++;
					found = true;
					for(int j = 0; j < roles; j++) {
						OperantResource ort2 = actor.getOperantResource(Env.roleNames[j]);
						outcome0[i][j] += ort2.getOutput0();
						outcome1[i][j] += ort2.getOutput();
					}
					break;
				}
			}
			if(!found) {
				population[Env.roleNames.length]++;
				for(int j = 0; j < roles; j++) {
					OperantResource ort2 = actor.getOperantResource(Env.roleNames[j]);
					outcome0[Env.roleNames.length][j] += ort2.getOutput0();
					outcome1[Env.roleNames.length][j] += ort2.getOutput();
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

					totalValue += actor.getOperantResource(Env.roleNames[Env.roles + Env.storeRoles]).getOutput();
				}
			}
		}

		// out.println("average of output: " + totalOutput / Env.actorList.size() / Env.types);
		out.printf("%d ", Env.actorList.size());
		for(int i = 0; i < Env.roleNames.length + 1; i++) {
			out.printf("%d ", population[i]);
			for(int j = 0; j < roles; j++) {
				if(population[i] != 0) {
					out.printf("%8.6f ", outcome0[i][j] / population[i]);
					out.printf("%8.6f ", outcome1[i][j] / population[i]);
				} else {
					out.print("0.0 0.0 ");
				}
			}
		}
		for(int j = 0; j < roles; j++) {
			int pop = 0;
			double outcome = 0;
			for(int i = 0; i < Env.roleNames.length + 1; i++) {
				pop += population[i];
				outcome += outcome0[i][j];
			}

			if(pop != 0) {
				out.printf("%8.6f ", outcome / pop);
			} else {
				out.print("0.0 ");
			}
		}
		// out.printf("%8.6f%n", totalOutput / Env.actorList.size() / Env.types);
		if(totalActors > 0) {
			out.printf("%8.6f ", totalOutput / totalActors / Env.roles);
			out.printf("%8.6f ", totalOutput1 / totalActors / Env.roles);
			out.printf("%8.6f ", totalValue / totalActors);
		} else {
			out.printf("%8.6f %8.6f %8.6f ", 0.0d, 0.0d, 0.0d);
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
			out.println("No Actor");
		} else {
			for(int i = 0; i < Env.roleNames.length; i++) {
				OperantResource ort = actor.getOperantResource(Env.roleNames[i]);
				double effort = ort.getEffort();
				double skill = ort.getSkill();

				out.printf("(%6.4f %6.4f) ", skill, effort);
			}
			out.println();
		}
	}

	private static void printHiererchy(Map<Actor, Set<Actor>>friendMap, boolean head) {
		Actor top = null;
		int max = 0;

		Set<Actor> keys = new HashSet<Actor>();
		keys.addAll(friendMap.keySet());

		if(head) {
			out.print("(" + (keys.size() + 1) + " ");
		}

		for(Actor actor: keys) {
			int size = friendMap.get(actor).size();
			if(size > max) {
				max = size;
				top = actor;
			}
		}

		if(max == 0) {
			if(!head) {
				for(int i = 0; i < keys.size(); i++) {
					out.print("(1 ) ");
				}
			}
			if(head) {
				out.print(") ");
			}
			return;
		}

		Map<Actor, Set<Actor>> newFriendMap = new HashMap<Actor, Set<Actor>>();

		Set<Actor> friends = friendMap.get(top);
		for(Actor actor: friends) {
			HashSet<Actor> set = new HashSet<Actor>();
			set.addAll(friendMap.get(actor));
			set.retainAll(friends);
			set.remove(top);
			newFriendMap.put(actor, set);
		}

		printHiererchy(newFriendMap, true);

		for(Actor actor: keys) {
			if(actor == top || friends.contains(actor)) {
				friendMap.remove(actor);
			} else {
				Set<Actor> set = friendMap.get(actor);
				set.removeAll(friends);
				set.remove(top);
			}
		}

		if(friendMap.size() > 0) {
			printHiererchy(friendMap, false);
		}

		if(head) {
			out.print(") ");
		}
	}

	public static void printStructure() {
		List<Actor> aList = new ArrayList<Actor>();

		aList.addAll(Env.actorList);

		/*
		for(Actor actor: Env.actorList) {
			aList.add(actor);
		}
		*/

		aList.sort(new Comparator<Actor>() {

			@Override
			public int compare(Actor arg0, Actor arg1) {
				return arg1.getReverseFriends().size() - arg0.getReverseFriends().size();
			}
		});

		Actor a = aList.get(0);

		Map<Actor, Set<Actor>> friendMap = new HashMap<Actor, Set<Actor>>();
		Set<Actor> friends = a.getReverseFriends();

		for(Actor friend: friends) {
			Set<Actor> set = new HashSet<Actor>();
			set.addAll(friend.getReverseFriends());
			set.retainAll(friends);
			friendMap.put(friend, set);
		}

		printHiererchy(friendMap, true);

		/*
		friendList.sort(new Comparator<Set<Actor>>() {

			@Override
			public int compare(Set<Actor> arg0, Set<Actor> arg1) {
				return arg1.size() - arg0.size();
			}
		});

		for(Set<Actor> friend: friendList) {
			out.print(friend.size() + " ");
		}
		*/

		/*
		Set<Actor> total = new HashSet<Actor>();
		int sum = 0;

		for(Actor actor: aList) {
			int size = actor.getReverseFriends().size();
			if(size < 100) {
				break;
			}
			total.addAll(actor.getReverseFriends());
			sum += size;
			out.print(sum + "-" + total.size() + " ");
		}
		*/
	}

	public static void printReward() {
		for(int i = 0; i < Env.rewardTable.length; i++) {
			out.printf("%6.4f ", Env.rewardTable[i]);
		}
	}
}
