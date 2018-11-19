package edu.hawaii.sdlogic.draw;

import static edu.hawaii.sdlogic.Env.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.utils.Canvas2;

/**
 * draw plane for entropy
 * @author fujita
 *
 */
public class DrawPlane5 extends DrawPlane {
	/**
	 * initializer method
	 */
	@Override
	public void init() {
		canvas = new Canvas2(Env.drawVisible);
		initColor();
		// displayWidth = mapWidth * cellWidth + displayMargin * 2;
		displayWidth = mapWidth * cellWidth * 2 + displayMargin * 3;
		// displayWidth = mapWidth * cellWidth * 3 + displayMargin * 4;
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

		leftRelation = mapWidth * cellWidth + displayMargin * 2;
		topRelation = 0;
	}

	/**
	 * draw method
	 */
	@Override
	public void draw() {
		canvas.setColor(255, 255, 255);
		canvas.fillRect(0, 0, displayWidth, displayHeight);

		int left1 = mapWidth * cellWidth + displayMargin * 2;
		// int left2 = mapWidth * cellWidth * 2 + displayMargin * 3;

		for(Actor actor: Env.actorList) {
			int x = actor.getX();
			int y = actor.getY();

			int[] rgb = new int[3];

			selectColor(actor,rgb);

			// draw actor map
			drawMap(0, 0, x, y, rgb);

			// draw collaboration links
			// drawLinks(actor, left1, 0, x, y, rgb);

			drawExchange(actor, left1, 0, x, y, rgb);
		}

		canvas.forceRepaint();
	}
}
