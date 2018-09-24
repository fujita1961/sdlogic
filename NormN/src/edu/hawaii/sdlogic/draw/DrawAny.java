package edu.hawaii.sdlogic.draw;

import static edu.hawaii.sdlogic.Env.*;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.utils.Canvas;

/**
 * draw any map
 * @author fujita
 *
 */
public class DrawAny extends DrawPlane {
	/**
	 * initializer method
	 */
	@Override
	public void init() {
		initColor();

		int number = 0;

		if(Env.drawMap) {
			number++;
		}

		if(Env.drawLinks) {
			number++;
		}

		if(Env.drawTightLinks) {
			number++;
		}

		if(Env.drawRelation) {
			leftRelation = (mapWidth * cellWidth + displayMargin) * number + displayMargin;
			number++;
		}

		if(Env.drawExchange) {
			number++;
		}

		if(Env.drawRelationDensity) {
			number++;
		}

		if(Env.drawExchangeLocation) {
			number++;
		}

		displayWidth = (mapWidth * cellWidth + displayMargin) * number + displayMargin;
		displayHeight = mapHeight * cellHeight + displayMargin * 2;
		Canvas.setTitle(Env.titleBar);
		Canvas.show(displayWidth, displayHeight, Env.drawVisible);
		Canvas.disableAutoRepaint();
		if(Env.animationGIFFileName != null) {
			Canvas.prepareAnimationGIF(Env.animationGIFFileName);
		}
	}


	/**
	 * draw method
	 */
	@Override
	public void draw() {
		Canvas.setColor(255, 255, 255);
		Canvas.fillRect(0, 0, displayWidth, displayHeight);

		int left = 0;

		for(Actor actor: Env.actorList) {
			left = displayMargin;

			int x = actor.getX();
			int y = actor.getY();

			int[] rgb = new int[3];

			selectColor(actor,rgb);

			// draw actor map
			if(Env.drawMap) {
				drawMap(left, 0, x, y, rgb);
				left +=  mapWidth * cellWidth + displayMargin;
			}

			if(Env.drawLinks) {
				drawLinks(actor, left, 0, x, y, rgb);
				left +=  mapWidth * cellWidth + displayMargin;
			}

			if(Env.drawTightLinks) {
				drawTightLinks(actor, left, 0, x, y, rgb);
				left +=  mapWidth * cellWidth + displayMargin;
			}

			if(Env.drawRelation) {
				left +=  mapWidth * cellWidth + displayMargin;
			}

			if(Env.drawExchange) {
				drawExchange(actor, left, 0, x, y, rgb);
				left +=  mapWidth * cellWidth + displayMargin;
			}

			if(Env.drawExchangeLocation) {
				drawExchangeLocation(actor, left, 0, x, y, rgb);
				left +=  mapWidth * cellWidth + displayMargin;
			}
		}

		if(Env.drawRelationDensity) {
			drawRelationDensity(left, 0);
		}

		Canvas.forceRepaint();
	}
}
