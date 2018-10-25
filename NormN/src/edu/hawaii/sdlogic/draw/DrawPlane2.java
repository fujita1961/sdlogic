package edu.hawaii.sdlogic.draw;

import static edu.hawaii.sdlogic.Env.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.utils.Canvas;

/**
 * draw actor map
 * @author fujita
 *
 */
public class DrawPlane2 extends DrawPlane {
	/**
	 * initializer method
	 */
	@Override
	public void init() {
		initColor();
		displayWidth = mapWidth * cellWidth + displayMargin * 2;
		// displayWidth = mapWidth * cellWidth * 2 + displayMargin * 3;
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
	 * draw method
	 */
	@Override
	public void draw() {
		Canvas.setColor(255, 255, 255);
		Canvas.fillRect(0, 0, displayWidth, displayHeight);

		// int left = mapWidth * cellWidth + displayMargin * 2;

		for(Actor actor: Env.actorList) {
			int x = actor.getX();
			int y = actor.getY();

			int[] rgb = new int[3];

			selectColor(actor,rgb);

			// draw actor map
			drawMap(0, 0, x, y, rgb);

			// draw collaboration links
			// drawLinks(actor, left, 0, x, y, rgb);
		}

		Canvas.forceRepaint();
	}
}
