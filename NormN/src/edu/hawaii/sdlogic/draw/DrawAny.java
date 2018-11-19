package edu.hawaii.sdlogic.draw;

import static edu.hawaii.sdlogic.Env.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.utils.Canvas2;

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
		canvas = new Canvas2(Env.drawVisible);
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

		if(Env.drawLiveCondition) {
			number++;
		}

		displayWidth = (mapWidth * cellWidth + displayMargin) * number + displayMargin;
		displayHeight = mapHeight * cellHeight + displayMargin * 2;
		canvas.setTitle(Env.titleBar);
		canvas.show(displayWidth, displayHeight);
		canvas.disableAutoRepaint();
		if(Env.animationGIFFileName != null) {
			String fileName = Env.animationGIFFileName;
			if(!fileName.endsWith(".gif")) {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("_yyyyMMdd_HHmmss");
				fileName = fileName + sdf.format(cal.getTime()) + ".gif";
			}

			canvas.prepareAnimationGIF(fileName);
		}
	}


	/**
	 * draw method
	 */
	@Override
	public void draw() {
		canvas.setColor(255, 255, 255);
		canvas.fillRect(0, 0, displayWidth, displayHeight);

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

			if(Env.drawLiveCondition) {
				drawLiveCondition(actor, left, 0, x, y);
				left +=  mapWidth * cellWidth + displayMargin;
			}
		}

		if(Env.drawRelationDensity) {
			drawRelationDensity(left, 0);
		}

		canvas.forceRepaint();
	}
}
