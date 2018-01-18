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
		double[][] total = new double[Env.typeNames.length][Env.typeNames.length];
		int[] count = new int[Env.typeNames.length];

		for(Actor actor: Env.actorList) {
			int role = actor.getActorType();
			count[role]++;
			for(int i = 0; i < Env.typeNames.length; i++) {
				OperantResource ort = actor.getOperantResource(Env.typeNames[i]);
				total[role][i] += ort.getSkill();
			}
		}

		for(int i = 0; i < Env.typeNames.length; i++) {
			for(int j = 0; j < Env.typeNames.length; j++) {
				System.out.printf("%4.2f ", total[i][j] / count[i]);
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
}
