package edu.hawaii.sdlogic.draw;

import static edu.hawaii.sdlogic.Env.*;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.utils.Canvas;

/**
 * draw plane for entropy
 * @author fujita
 *
 */
public class DrawPlane3 extends DrawPlane {
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
