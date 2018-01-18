package edu.hawaii.sdlogic.draw;

import static edu.hawaii.sdlogic.Env.*;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
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
		// displayWidth = mapWidth * cellWidth + displayMargin * 2;
		displayWidth = mapWidth * cellWidth * 2 + displayMargin * 3;
		displayHeight = mapHeight * cellHeight + displayMargin * 2;
		Canvas.setTitle(Env.titleBar);
		Canvas.show(displayWidth, displayHeight);
		Canvas.disableAutoRepaint();
		if(Env.animationGIFFileName != null) {
			Canvas.prepareAnimationGIF(Env.animationGIFFileName);
		}
	}

	/**
	 * color table
	 */
	protected int[][] colors = {
			{ 0, 0, 255 },
			{ 255, 0, 0 },
			{ 0, 255, 0 },
			{ 0, 255, 255 },
			{ 255, 255, 0 },
			{ 255, 0, 255 },
			{ 255, 255, 255 }
	};

	/**
	 * select proper color for actor
	 * @param actor
	 * @param rgb
	 */
	protected void selectColor(Actor actor, int[] rgb) {
		int red = 0;
		int green = 0;
		int blue = 0;

		for(int i = 0; i < Env.typeNames.length; i++) {
			String name = Env.typeNames[i];
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

		for(int i = 0; i < Env.types; i++) {
			if(actor.getExchangers()[i].size() > 3) {
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
