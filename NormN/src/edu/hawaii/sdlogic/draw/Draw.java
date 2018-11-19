package edu.hawaii.sdlogic.draw;

import edu.hawaii.utils.Canvas2;

/**
 * drawing panel interface
 * @author fujita
 *
 */
public interface Draw {
	/**
	 * initializer method
	 */
	void init();

	/**
	 * draw method
	 */
	void draw();

	void drawRelation(int x, int y);

	Canvas2 getCanvas();
}
