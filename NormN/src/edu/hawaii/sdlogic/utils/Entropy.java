package edu.hawaii.sdlogic.utils;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.draw.DrawPlane3;
import edu.hawaii.sdlogic.operant.OperantResource;
import edu.hawaii.utils.Canvas;

/**
 * Entropy class
 * utitity functions for calculating entropy
 * @author fujita
 *
 */
public class Entropy {
	public double primitiveEntropy(int x, int y, int range, boolean self) {
		int empty = 0;
		int[] typeWorkers = new int[Env.typeNames.length];

		for(int i = -range; i <= range; i++) {
			int xx = (x + i + Env.mapWidth) % Env.mapWidth;
			for(int j = -range; j <= range; j++) {
				if(!self && i == 0 && j == 0) continue;

				int yy = (y + j + Env.mapHeight) % Env.mapHeight;

				Actor actor = Env.map[xx][yy];
				if(actor == null) {
					empty++;
				} else {
					double max = 0.0;
					int maxIndex = -1;

					for(int k = 0; k < Env.typeNames.length; k++) {
						OperantResource otr = actor.getOperantResource(Env.typeNames[k]);
						double value = otr.getSkill() * otr.getEffort();

						if(value > max) {
							max = value;
							maxIndex = k;
						}
					}

					typeWorkers[maxIndex]++;
				}
			}
		}

		int width = 2 * range + 1;
		double width2 = width * width;

		double entropy = 0;

		if(empty != 0) {
			double pempty = (double)empty / width2;
			entropy -= pempty * Math.log(pempty);
		}

		for(int i = 0; i < Env.typeNames.length; i++) {
			if(typeWorkers[i] != 0) {
				double p = (double)typeWorkers[i] / width2;
				entropy -= p * Math.log(p);
			}
		}

		return entropy;
	}

	public double primitiveEntropy(Actor self) {
		int empty = 0;
		int[] typeWorkers = new int[Env.typeNames.length];

		for(int i = 0; i < Env.friends; i++) {
			Actor actor = self.getFriend(i);
			if(actor == null) {
				empty++;
			} else {
				double max = 0.0;
				int maxIndex = -1;

				for(int k = 0; k < Env.typeNames.length; k++) {
					OperantResource otr = actor.getOperantResource(Env.typeNames[k]);
					double value = otr.getSkill() * otr.getEffort();

					if(value > max) {
						max = value;
						maxIndex = k;
					}
				}

				typeWorkers[maxIndex]++;
			}
		}

		double entropy = 0;

		if(empty != 0) {
			double pempty = (double)empty / Env.friends;
			entropy -= pempty * Math.log(pempty);
		}

		for(int i = 0; i < Env.typeNames.length; i++) {
			if(typeWorkers[i] != 0) {
				double p = (double)typeWorkers[i] / Env.friends;
				entropy -= p * Math.log(p);
			}
		}

		return entropy;
	}

	public double entropy() {
		boolean draw = false;
		int left = Env.displayMargin * 2 + Env.cellWidth * Env.mapWidth;

		if(Env.draw instanceof DrawPlane3) {
			draw = true;
		}

		int range = 1;

		double sum = 0;

		for(int x = 0; x < Env.mapWidth; x++) {
			for(int y = 0; y < Env.mapHeight; y++) {
				double entropy = primitiveEntropy(x, y, range, true);

				if(draw) {
					int level = (int)(entropy * 100);
					if(level > 255) level = 255;
					Canvas.setColor(level, level, level);
					Canvas.fillRect(left + x * Env.cellWidth + Env.displayMargin, y * Env.cellHeight + Env.displayMargin, Env.cellWidth, Env.cellHeight);
				}

				sum += entropy;
			}
		}

		if(draw) {
			Canvas.enableAutoRepaint();
		}

		double val = sum / Math.log(2);

		System.out.print(val / (Env.mapWidth * Env.mapHeight));

		return val;
	}

	public double friendEntropy() {
		double sum = 0;

		for(Actor actor: Env.actorList) {
			double entropy = primitiveEntropy(actor);

			sum += entropy;
		}

		double val = sum / Math.log(2);

		System.out.print(val / (Env.mapWidth * Env.mapHeight) + " ");

		return val;
	}

	public double maxEntropy2(int actors, int types, int[] count, double[] p, int depth, double prob) {
		if(depth == actors) {
			double log2 = Math.log(2);
			double entropy = 0;
			for(int i = 0; i < types; i++) {
				if(count[i] != 0) {
					double pp = ((double)count[i]) / (double) actors;
					entropy -=  pp * Math.log(pp) / log2;
				}
			}

			return entropy * prob;
		}

		double total = 0;

		for(int i = 0; i < types; i++) {
			count[i]++;
			total += maxEntropy2(actors, types, count, p, depth + 1, prob * p[i]);
			count[i]--;
		}

		return total;
	}

	public double maxEntropy(int actors, int types, double emptyP) {
		int[] count = new int[types];
		double[] p = new double[types];

		p[0] = emptyP;
		for(int i = 1; i < types; i++) {
			p[i] = (1 - emptyP) / (types - 1);
		}

		return maxEntropy2(actors, types, count, p, 0, 1.0);
	}

	public void printMaxEntropy() {
		int actors = Env.actorList.size();
		int types = Env.types + 2;
		double emptyP = 1 - ((double)actors) / (Env.mapWidth * Env.mapHeight);

		System.out.print(actors + " " + maxEntropy(9, types, emptyP) + " ");
	}

	public static void main(String[] args) {
		Entropy entropy = new Entropy();

		System.out.println(entropy.maxEntropy(9, 4, 1 - 39499.0/40000));
	}
}
