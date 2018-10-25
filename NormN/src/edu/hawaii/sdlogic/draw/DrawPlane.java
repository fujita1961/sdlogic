package edu.hawaii.sdlogic.draw;

import static edu.hawaii.sdlogic.Env.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.Term;
import edu.hawaii.utils.Canvas;

/**
 * draw plane for actor map and collaboration links
 * @author fujita
 *
 */
public class DrawPlane implements Draw {
	/**
	 * initializer method
	 */
	@Override
	public void init() {
		initColor();
		// displayWidth = mapWidth * cellWidth + displayMargin * 2;
		displayWidth = mapWidth * cellWidth * 2 + displayMargin * 3;
		displayHeight = mapHeight * cellHeight + displayMargin * 2;
		Canvas.setTitle(Env.titleBar);
		Canvas.show(displayWidth, displayHeight);
		Canvas.disableAutoRepaint();
		if(Env.animationGIFFileName != null) {
			String fileName = Env.animationGIFFileName;
			if(!fileName.endsWith(".gif")) {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("_yyyyMMdd_HHmmss");
				fileName = fileName + sdf.format(cal.getTime()) + ".gif";
			}

			Canvas.prepareAnimationGIF(fileName);
		}
	}

	/**
	 * color table
	 */
	protected int[][] colors0 = {
			{ 0, 0, 255 },
			{ 255, 0, 0 },
			{ 0, 255, 255 },
			{ 255, 255, 0 },
			{ 255, 0, 255 },
			{ 255, 255, 255 }
	};

	private int[] exchangeColor = { 0, 255, 0 };

	protected int[][] colors;

	protected void initColor() {
		colors = new int[colors0.length + 1][];
		int exchangeIndex = 0;
		for(int i = 0; i < Env.roleNames.length; i++) {
			if(Env.roleNames[i] == Term.EXCHANGING) {
				exchangeIndex = i;
				break;
			}
		}

		if(exchangeIndex >= colors0.length) {
			exchangeIndex = colors0.length - 1;
		}

		for(int i = 0; i < colors0.length; i++) {
			if(i < exchangeIndex) {
				colors[i] = colors0[i];
			} else if(i == exchangeIndex) {
				colors[i] = exchangeColor;
				colors[i + 1] = colors0[i];
			} else {
				colors[i + 1] = colors0[i];
			}
		}
	}

	/**
	 * select proper color for actor
	 * @param actor
	 * @param rgb
	 */
	protected void selectColor(Actor actor, int[] rgb) {
		int red = 0;
		int green = 0;
		int blue = 0;

		for(int i = 0; i < Env.roleNames.length; i++) {
			String name = Env.roleNames[i];
			double effort = actor.getOperantResource(name).getEffort();
			red += (int)(effort * colors[i % colors.length][0]);
			green += (int)(effort * colors[i % colors.length][1]);
			blue += (int)(effort * colors[i % colors.length][2]);
		}

		if(red < 0) red = 0;
		if(blue < 0) blue = 0;
		if(green < 0) green = 0;
		if(red > 255) red = 255;
		if(blue > 255) blue = 255;
		if(green > 255) green = 255;

		rgb[0] = red;
		rgb[1] = green;
		rgb[2] = blue;
	}

	/**
	 * draw actor map
	 * @param left left margin
	 * @param top top margin
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param rgb color
	 */
	protected void drawMap(int left, int top, int x, int y, int[] rgb) {
		Canvas.setColor(rgb[0], rgb[1], rgb[2]);
		Canvas.fillRect(left + x * cellWidth + displayMargin,
				top + y * cellHeight + displayMargin, cellWidth, cellHeight);
	}

	/**
	 * draw collaboration links
	 * @param actor
	 * @param left left margin
	 * @param top top margin
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param rgb color
	 */
	protected void drawLinks(Actor actor, int left, int top, int x, int y, int[] rgb) {
		int cellWidthHalf = cellWidth / 2;
		int cellHeightHalf = cellHeight / 2;
		int heightBias = cellHeightHalf + displayMargin;

		for(int i = 0; i < Env.friends; i++) {
			Actor friend = actor.getFriend(i);
			if(friend != null) {
				int fx = friend.getX();
				int fy = friend.getY();

				if(Math.abs(fx - x) <= collaborativeRange && Math.abs(fy - y) <= collaborativeRange) {
					int x0 = left + x * cellWidth + cellWidthHalf;
					int y0 = y * cellHeight + heightBias;
					int x2 = left + fx * cellWidth + cellWidthHalf;
					int y2 = fy * cellHeight + heightBias;
					int x1 = (x0 + x2 * 4) / 5;
					int y1 = (y0 + y2 * 4) / 5;
					Canvas.setColor(rgb[0], rgb[1], rgb[2]);
					Canvas.drawLine(x0, y0, x1, y1);
					Canvas.setColor(200, 200, 0);
					Canvas.drawLine(x1, y1, x2, y2);
				}
			}
		}
	}

	/**
	 * draw tight collaboration links
	 * @param actor
	 * @param left left margin
	 * @param top top margin
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param rgb color
	 */
	protected void drawTightLinks(Actor actor, int left, int top, int x, int y, int[] rgb) {
		int cellWidthHalf = cellWidth / 2;
		int cellHeightHalf = cellHeight / 2;
		int heightBias = cellHeightHalf + displayMargin;

		for(int i = 0; i < Env.friends; i++) {
			Actor friend = actor.getFriend(i);
			if(friend != null) {
				int fx = friend.getX();
				int fy = friend.getY();

				if(friend.getReverseFriends().size() > 50) {
					// && actor.getReverseFriends().contains(friend)) {
					// && Math.abs(fx - x) <= collaborativeRange && Math.abs(fy - y) <= collaborativeRange)

					int x0 = left + x * cellWidth + cellWidthHalf;
					int y0 = y * cellHeight + heightBias;
					int x2 = left + fx * cellWidth + cellWidthHalf;
					int y2 = fy * cellHeight + heightBias;
					int x1 = (x0 + x2 * 4) / 5;
					int y1 = (y0 + y2 * 4) / 5;
					Canvas.setColor(rgb[0], rgb[1], rgb[2]);
					Canvas.drawLine(x0, y0, x1, y1);
					Canvas.setColor(200, 200, 0);
					Canvas.drawLine(x1, y1, x2, y2);
				}
			}
		}
	}

	private void drawActor(Actor actor, int left, int top, int distance, int max) {
		int half = max / 2 + 1;

		int blue = distance * 255 / max;
		int red = (max - distance) * 255 / max;
		int green = 0;

		if(distance < half) {
			red = (half - distance) * 255 / half;
			green = distance * 255 / half;
			blue = 0;
		} else {
			red = 0;
			green = (max - distance) * 255 / (max - half);
			blue = (distance - half) * 255 / (max - half);
		}

		Canvas.setColor(red, green, blue);
		Canvas.fillRect(left + actor.getX() * cellWidth + displayMargin,
				top + actor.getY() * cellHeight + displayMargin, cellWidth, cellHeight);
	}

	protected int leftRelation;
	protected int topRelation;

	/**
	 * draw relation
	 * @param leftRelation left margin
	 * @param topRelation top margin
	 */
	public void drawRelation(int x, int y) {
		int max = 10;

		int xx = (x - Env.displayMargin) / Env.cellWidth;
		int yy = (y - Env.displayMargin) / Env.cellHeight;

		if(xx >= Env.map.length || yy >= Env.map[xx].length || xx < 0 || yy < 0) {
			return;
		}
		Actor origin = Env.map[xx][yy];
		if(origin == null) {
			return;
		}

		HashMap<Actor, HashSet<Actor>> map = new HashMap<Actor, HashSet<Actor>>();
		for(Actor actor: Env.actorList) {
			LinkedList<Actor>[] exchangers = actor.getExchangers();
			for(int i = 0; i < roles; i++) {
				for(Actor exchanger: exchangers[i]) {
					HashSet<Actor> set = map.get(exchanger);
					if(set == null) {
						set = new HashSet<Actor>();
						map.put(exchanger, set);
					}
					set.add(actor);
				}
			}
		}

		HashSet<Actor> now = new HashSet<Actor>();
		HashSet<Actor> next = new HashSet<Actor>();
		HashSet<Actor> done = new HashSet<Actor>();

		now.add(origin);
		done.add(origin);
		drawActor(origin, leftRelation, topRelation, 0, max);

		System.out.print(Env.actorList.size() + ": ");;

		for(int i = 1; i < max; i++) {
			System.out.print(now.size() + " ");
			for(Actor actor: now) {
				for(int j = 0; j < Env.friends; j++) {
					Actor friend = actor.getFriend(j);
					if(friend != null && !done.contains(friend)) {
						done.add(friend);
						next.add(friend);
						drawActor(friend, leftRelation, topRelation, i, max);
					}
				}

				for(Actor friend: actor.getReverseFriends()) {
					if(!done.contains(friend)) {
						done.add(friend);
						next.add(friend);
						drawActor(friend, leftRelation, topRelation, i, max);
					}
				}

				for(int j = 0; j < roles; j++) {
					for(Actor friend: actor.getExchangers()[j]) {
						if(!done.contains(friend)) {
							done.add(friend);
							next.add(friend);
							drawActor(friend, leftRelation, topRelation, i, max);
						}
					}
				}

				HashSet<Actor> actors = map.get(actor);
				if(actors != null && !actors.isEmpty()) {
					for(Actor friend: actors) {
						if(!done.contains(friend)) {
							done.add(friend);
							next.add(friend);
							drawActor(friend, leftRelation, topRelation, i, max);
						}
					}
				}
			}

			HashSet<Actor> tmp = now;
			now = next;
			next = tmp;
			next.clear();
		}

		System.out.println();

		Canvas.forceRepaint();
	}

	/**
	 * draw relation density
	 * @param leftRelation left margin
	 * @param topRelation top margin
	 */
	public void drawRelationDensity(int left, int top) {
		HashMap<Actor, HashSet<Actor>> map = new HashMap<Actor, HashSet<Actor>>();
		int roles = Env.roles + Env.storeRoles;

		for(Actor actor: Env.actorList) {
			LinkedList<Actor>[] exchangers = actor.getExchangers();
			for(int i = 0; i < roles; i++) {
				for(Actor exchanger: exchangers[i]) {
					HashSet<Actor> set = map.get(exchanger);
					if(set == null) {
						set = new HashSet<Actor>();
						map.put(exchanger, set);
					}
					set.add(actor);
				}
			}
		}

		HashSet<Actor> now = new HashSet<Actor>();
		HashSet<Actor> next = new HashSet<Actor>();
		HashSet<Actor> done = new HashSet<Actor>();
		int max = 3;

		for(Actor origin: Env.actorList) {
			now.clear();
			done.clear();
			next.clear();
			now.add(origin);
			done.add(origin);

			for(int i = 1; i < max; i++) {
				for(Actor actor: now) {
					for(int j = 0; j < Env.friends; j++) {
						Actor friend = actor.getFriend(j);
						if(friend != null && !done.contains(friend)) {
							done.add(friend);
							next.add(friend);
						}
					}

					for(Actor friend: actor.getReverseFriends()) {
						if(!done.contains(friend)) {
							done.add(friend);
							next.add(friend);
						}
					}

					for(int j = 0; j < roles; j++) {
						for(Actor friend: actor.getExchangers()[j]) {
							if(!done.contains(friend)) {
								done.add(friend);
								next.add(friend);
							}
						}
					}

					HashSet<Actor> actors = map.get(actor);
					if(actors != null && !actors.isEmpty()) {
						for(Actor friend: actors) {
							if(!done.contains(friend)) {
								done.add(friend);
								next.add(friend);
							}
						}
					}
				}

				HashSet<Actor> tmp = now;
				now = next;
				next = tmp;
				next.clear();
			}

			int num = done.size();

			if(num > 127) num = 127;
			Canvas.setColor((127 - num) * 2, 0, num * 2);
			Canvas.fillRect(left + origin.getX() * cellWidth + displayMargin,
					top + origin.getY() * cellHeight + displayMargin, cellWidth, cellHeight);
		}
	}

	/**
	 * draw exchange links
	 * @param actor
	 * @param left left margin
	 * @param top top margin
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param rgb color
	 */
	protected void drawExchange(Actor actor, int left, int top, int x, int y, int[] rgb) {
		int cellWidthHalf = cellWidth / 2;
		int cellHeightHalf = cellHeight / 2;
		int heightBias = cellHeightHalf + displayMargin;
		int roles = Env.roles + Env.storeRoles;

		for(int i = 0; i < roles; i++) {
			if(actor.getExchangers()[i].size() > 6) {
				for(Actor friend: actor.getExchangers()[i]) {
					if(friend != null) {
						int fx = friend.getX();
						int fy = friend.getY();

						int x0 = left + x * cellWidth + cellWidthHalf;
						int y0 = y * cellHeight + heightBias;
						int x2 = left + fx * cellWidth + cellWidthHalf;
						int y2 = fy * cellHeight + heightBias;
						// int x1 = (x0 + x2 * 4) / 5;
						// int y1 = (y0 + y2 * 4) / 5;
						Canvas.setColor(rgb[0], rgb[1], rgb[2]);
						Canvas.drawLine(x0, y0, x2, y2);
						// Canvas.setColor(200, 200, 0);
						// Canvas.drawLine(x1, y1, x2, y2);
					}
				}
			}
		}
	}

	/**
	 * draw exchange links
	 * @param actor
	 * @param left left margin
	 * @param top top margin
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param rgb color
	 */
	protected void drawExchangeLocation(Actor actor, int left, int top, int x, int y, int[] rgb) {
		int cellWidthHalf = cellWidth / 2;
		int cellHeightHalf = cellHeight / 2;
		int heightBias = cellHeightHalf + displayMargin;
		int roles = Env.roles + Env.storeRoles;

		for(int i = 0; i < roles; i++) {
			int[][] locations = actor.getExchangingLocations();

			if(locations != null) {
				int lx = locations[i][0];
				int ly = locations[i][1];

				if(lx >=0 && ly >= 0) {

					if(i == 0) {
						Canvas.setColor(0, 0, 255);
					} else if(i == 1) {
						Canvas.setColor(255, 0, 0);
					} else {
						Canvas.setColor(0, 255, 0);
					}

					int x0 = left + lx * cellWidth + cellWidthHalf;
					int y0 = ly * cellHeight + heightBias;

					Canvas.drawRect(x0, y0, cellWidth, cellHeight);
				}
			}
		}
	}

	/**
	 * draw Live Condition
	 * @param actor
	 * @param left left margin
	 * @param top top margin
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	protected void drawLiveCondition(Actor actor, int left, int top, int x, int y) {
		double live = actor.getLiveCondition();
		int red = 0;
		int green = 0;
		int blue = 0;

		if(live < 1.0) {
			red = 255;
		} else if(live < 1.5) {
			red = (int)((1.5 - live) * 2 * 255);
			green = (int)((live - 1.0) * 2 * 255);
		} else if(live < 2.0){
			green = (int)((2.0 - live) * 2 * 255);
			blue = (int)((live - 1.5) * 2 * 255);
		} else {
			blue = 255;
		}

		Canvas.setColor(red, green, blue);
		Canvas.fillRect(left + x * cellWidth + displayMargin,
				top + y * cellHeight + displayMargin, cellWidth, cellHeight);
	}

	/**
	 * draw method
	 */
	@Override
	public void draw() {
		Canvas.setColor(255, 255, 255);
		Canvas.fillRect(0, 0, displayWidth, displayHeight);

		int left = mapWidth * cellWidth + displayMargin * 2;

		for(Actor actor: Env.actorList) {
			int x = actor.getX();
			int y = actor.getY();

			int[] rgb = new int[3];

			selectColor(actor,rgb);

			// draw actor map
			drawMap(0, 0, x, y, rgb);

			// draw collaboration links
			drawLinks(actor, left, 0, x, y, rgb);
		}

		Canvas.forceRepaint();
	}
}
